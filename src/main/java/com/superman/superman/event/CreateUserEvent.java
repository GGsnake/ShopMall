package com.superman.superman.event;

import com.superman.superman.dto.UserCreateReq;
import com.superman.superman.model.User;
import org.springframework.context.ApplicationEvent;

public class CreateUserEvent extends ApplicationEvent {
    private static final long serialVersionUID = 4385004033026265719L;

    private final UserCreateReq user;

    public CreateUserEvent(Object source, UserCreateReq user) {
        super(source);
        this.user = user;
    }

    public UserCreateReq getUser() {
        return user;
    }
}
