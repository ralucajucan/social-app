package org.utcn.socialapp.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
public class PasswordDTO {
    public Long id;
    public String oldPassword;
    public String password;

    public boolean requiredAnyMatchNull(){
        return Stream.of(id,oldPassword,password).anyMatch(Objects::isNull);
    }
}
