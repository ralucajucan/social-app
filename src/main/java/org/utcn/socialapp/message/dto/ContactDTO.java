package org.utcn.socialapp.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ContactDTO {
    private String email;
    private String name;
    private boolean online = false;
    private Long newMessages = -1L;

    public ContactDTO(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public ContactDTO(String email, String name, boolean online, Long newMessages) {
        this.email = email;
        this.name = name;
        this.online = online;
        this.newMessages = newMessages;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setNewMessages(Long newMessages) {
        this.newMessages = newMessages;
    }
}
