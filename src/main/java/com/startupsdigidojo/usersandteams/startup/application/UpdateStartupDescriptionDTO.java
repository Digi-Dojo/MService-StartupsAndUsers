package com.startupsdigidojo.usersandteams.startup.application;

public class UpdateStartupDescriptionDTO {

    private String name;
    private String description;

    public UpdateStartupDescriptionDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
