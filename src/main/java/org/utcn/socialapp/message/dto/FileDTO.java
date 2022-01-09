package org.utcn.socialapp.message.dto;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.utcn.socialapp.message.attachment.Attachment;

@Getter
@NoArgsConstructor
public class FileDTO extends EmptyFileDTO {
    private byte[] file;

    public FileDTO(String id, String name, String type, long size, byte[] file) {
        super(id, name, type, size);
        this.file = file;
    }

    public FileDTO(Attachment attachment) {
        super(attachment.getId(), attachment.getName(), attachment.getType(), attachment.getSize());
        this.file = attachment.getFile();
    }

    public FileDTO(GridFSFile gridFSFile, GridFsOperations operations) {
        super(gridFSFile.getObjectId().toHexString(),
                gridFSFile.getFilename(),
                gridFSFile.getMetadata() != null ?
                        gridFSFile.getMetadata().get("_contentType").toString() : null,
                gridFSFile.getLength());
        try {
            this.file = operations.getResource(gridFSFile).getInputStream().readAllBytes();
        } catch (Exception e) {
            this.file = e.getMessage().getBytes();
        }
    }
}
