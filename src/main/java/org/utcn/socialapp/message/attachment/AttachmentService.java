package org.utcn.socialapp.message.attachment;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.message.dto.FileDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final GridFsTemplate template;
    private final AttachmentRepository attachmentRepository;

    private String addSmallFile(MultipartFile upload) throws IOException {
        Attachment attachment = new Attachment(
                upload.getOriginalFilename(),
                upload.getContentType(),
                upload.getSize(),
                upload.getBytes()
        );
        attachment = attachmentRepository.insert(attachment);
        return attachment.getId();
    }

    private String addBigFile(MultipartFile upload) throws IOException {
        DBObject metadata = new BasicDBObject();

        Object fileID = template.store(
                upload.getInputStream(),
                upload.getOriginalFilename(),
                upload.getContentType(),
                metadata);

        return "!" + fileID;
    }

    private Attachment getSmallFile(String id) throws BusinessException {
        return attachmentRepository.findById(id).orElseThrow(() -> new BusinessException(NOT_FOUND));
    }

    private FileDTO getBigFile(String id) {
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));
        if (gridFSFile == null || gridFSFile.getMetadata() == null) {
            return null;
        }
        return new FileDTO(gridFSFile, template);
    }

    private List<String> getIdList(String ids, Boolean isSmall) {
        if (isSmall) {
            return Arrays.stream(ids.split(","))
                    .filter(id -> !id.startsWith("!"))
                    .collect(Collectors.toList());
        } else {
            return Arrays.stream(ids.split(","))
                    .filter(id -> id.startsWith("!"))
                    .map(id -> id.substring(1))
                    .collect(Collectors.toList());
        }
    }

    public String addFile(MultipartFile upload) throws IOException {
        if (upload.getSize() < DataSize.ofMegabytes(15).toBytes()) {
            return this.addSmallFile(upload);
        }
        return this.addBigFile(upload);
    }

    public FileDTO getFile(String id) throws BusinessException {
        if (id.startsWith("!")) {
            return getBigFile(id.substring(1));
        } else {
            return new FileDTO(getSmallFile(id));
        }
    }

    public List<FileDTO> getFiles(String ids, boolean withLargeContent) {
        if (!StringUtils.hasLength(ids)) {
            return null;
        }
        List<FileDTO> fileList = attachmentRepository
                .findByMultipleIds(getIdList(ids, true).toArray(String[]::new))
                .stream()
                .map(FileDTO::new).collect(Collectors.toList());
        List<GridFSFile> gridFSFileList = new ArrayList<>();
        template.find(new Query(Criteria.where("_id").in(getIdList(ids, false)))).into(gridFSFileList);
        if (withLargeContent) {
            fileList.addAll(
                    gridFSFileList
                            .stream()
                            .map(gridFSFile -> new FileDTO(gridFSFile, template))
                            .collect(Collectors.toList())
            );
        } else {
            fileList.addAll(gridFSFileList.stream().map(FileDTO::new).collect(Collectors.toList()));
        }
        return fileList;
    }

    public void delete(String id) throws BusinessException {
        try {
            if (id.startsWith("!")) {
                template.delete(new Query(Criteria.where("_id").is(id.substring(1))));
            } else {
                attachmentRepository.deleteById(id);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

    public void deleteMultiple(String attachmentIds) throws BusinessException {
        try {
            List<String> smallIdList = this.getIdList(attachmentIds, true);
            List<String> bigIdList = this.getIdList(attachmentIds, false);
            if (smallIdList.size() != 0) {
                attachmentRepository.deleteAllById(smallIdList);
            }
            if (bigIdList.size() != 0){
                template.delete(new Query(Criteria.where("_id").in(bigIdList)));
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }
}
