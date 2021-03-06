package org.utcn.socialapp.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
public class PasswordDTO {
    private Long id;
    private String oldPassword;
    private String password;

    public boolean requiredAnyMatchNull(){
        return Stream.of(id,oldPassword,password).anyMatch(Objects::isNull);
    }
}
