package org.utcn.socialapp.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SendDTO {
    private String text;
    private Long receiver;
    private String receiverName;

    public boolean requiredMatchNull() {
        return Stream.of(receiver)
                     .anyMatch(Objects::isNull);
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
