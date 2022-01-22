package org.utcn.socialapp.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
public class EditDTO {
    private Long id;
    private String selected;
    private String change;
    public boolean requiredAnyMatchNull(){
        return Stream.of(id,selected).anyMatch(Objects::isNull);
    }
}
