package com.startupsdigidojo.usersandteams.startup.application.event;

import lombok.Getter;
import lombok.Setter;

public abstract class StartupEvent {
    @Getter
    @Setter
    protected String type;

    @Getter
    @Setter
    protected Object payload;
}
