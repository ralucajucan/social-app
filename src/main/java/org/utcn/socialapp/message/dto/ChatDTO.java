package org.utcn.socialapp.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Log
public class ChatDTO {
    private Long sender;
    private Long receiver;
    private int page = 0;

    public boolean requiredMatchNull() {
        return Stream.of(sender, receiver)
                     .anyMatch(Objects::isNull);
    }

    public boolean pageIsNegative() {
        return page < 0;
    }
}
