package org.utcn.socialapp.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserPageDTO {
    private List<UserDTO> users;
    private int totalPages;
    private long totalElements;
}
