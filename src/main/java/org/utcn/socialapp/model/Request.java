package org.utcn.socialapp.model;

import javax.persistence.*;

@Entity
@Table(name = "request")
public class Request {
    @EmbeddedId
    private RequestPK requestPK;

    @Enumerated(value = EnumType.STRING)
    private RequestStatus requestStatus;

}
