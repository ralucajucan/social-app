package org.utcn.socialapp.message.dto;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.utcn.socialapp.message.attachment.Attachment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private String id;
    private String name;
    private String type = null;
    private long size;
    private byte[] file;

    public FileDTO(Attachment attachment) {
        this.id = attachment.getId();
        this.name = attachment.getName();
        this.type = attachment.getType();
        this.size = attachment.getSize();
        this.file = attachment.getFile();
    }

    public FileDTO(GridFSFile gridFSFile, GridFsOperations operations) {
        this.id = gridFSFile.getObjectId().toHexString();
        this.name = gridFSFile.getFilename();
        this.type = gridFSFile.getMetadata() != null ?
                gridFSFile.getMetadata().get("_contentType").toString() : null;
        this.size = gridFSFile.getLength();
        try {
            this.file = operations.getResource(gridFSFile).getInputStream().readAllBytes();
        } catch (Exception e) {
            this.file = e.getMessage().getBytes();
        }
    }

    public FileDTO(GridFSFile gridFSFile) {
        this.id = gridFSFile.getObjectId().toHexString();
        this.name = gridFSFile.getFilename();
        this.type = gridFSFile.getMetadata() != null ?
                gridFSFile.getMetadata().get("_contentType").toString() : null;
        this.size = gridFSFile.getLength();
    }
}
