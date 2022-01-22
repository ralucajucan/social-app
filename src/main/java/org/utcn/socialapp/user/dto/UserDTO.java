package org.utcn.socialapp.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.utcn.socialapp.user.Role;
import org.utcn.socialapp.user.User;

@Getter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private Role role;
    private String createdAt;
    private String updatedAt;
    private boolean enabled;
    private boolean locked;
    private String firstName;
    private String lastName;
    private String birthDate;

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getAudit().getCreatedOn().toString();
        this.updatedAt = user.getAudit().getUpdatedOn().toString();
        this.enabled = user.isEnabled();
        this.locked = user.isLocked();
        this.firstName = user.getProfile().getFirstName();
        this.lastName = user.getProfile().getLastName();
        this.birthDate = user.getProfile().getBirthDate().toString();
    }
}
