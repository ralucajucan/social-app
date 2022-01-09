package org.utcn.socialapp.message.dto;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.utcn.socialapp.message.attachment.Attachment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmptyFileDTO {
    private String id;
    private String name;
    private String type = null;
    private long size;

    public EmptyFileDTO(Attachment attachment) {
        this.id = attachment.getId();
        this.name = attachment.getName();
        this.type = attachment.getType();
        this.size = attachment.getSize();
    }

    public EmptyFileDTO(GridFSFile gridFSFile) {
        this.id = gridFSFile.getObjectId().toHexString();
        this.name = gridFSFile.getFilename();
        this.type = gridFSFile.getMetadata() != null ?
                gridFSFile.getMetadata().get("_contentType").toString() : null;
        this.size = gridFSFile.getLength();
    }
}
