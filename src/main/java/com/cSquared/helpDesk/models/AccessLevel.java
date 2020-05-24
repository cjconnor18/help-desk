package com.cSquared.helpDesk.models;

public enum AccessLevel {

    USER ("User"),
    TECH ("Technician"),
    ADMIN ("Administrator");


    private final String displayName;

    AccessLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
