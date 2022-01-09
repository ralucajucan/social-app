package org.utcn.socialapp.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SendDTO {
    private String text;
    private String attachmentIds;
    private String user;
}
