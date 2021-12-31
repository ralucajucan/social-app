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
    private String receiver;

    public boolean requiredMatchNull() {
        return Stream.of(receiver)
                     .anyMatch(Objects::isNull)
                || Stream.of(receiver).anyMatch(s -> !StringUtils.hasLength(s));
    }
}
