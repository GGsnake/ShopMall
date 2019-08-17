package com.superman.superman.event;

import com.superman.superman.req.UserRegiser;
import org.springframework.context.ApplicationEvent;

public class CreateUserEvent extends ApplicationEvent {
    private static final long serialVersionUID = 4385004033026265719L;

    private final UserRegiser regiser;

    public CreateUserEvent(Object source, UserRegiser regiser) {
        super(source);
        this.regiser = regiser;
    }

    public UserRegiser getRegiser() {
        return regiser;
    }
}
