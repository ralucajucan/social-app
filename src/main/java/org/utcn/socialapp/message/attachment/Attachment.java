package org.utcn.socialapp.message.attachment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// LESS THAN 15 MB
@Getter
@NoArgsConstructor
@Document()
public class Attachment {
    @Id
    public String id;
    private String name;
    private String type;
    private long size;
    private byte[] file;

    public Attachment(String name, String type, long size, byte[] file) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.file = file;
    }
}
