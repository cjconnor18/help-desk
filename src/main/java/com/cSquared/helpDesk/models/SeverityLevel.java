package com.cSquared.helpDesk.models;

public enum SeverityLevel {

    LOW ("Low"),
    MEDIUM ("Medium"),
    HIGH ("High"),
    CRITICAL("Critical");

    private final String displayName;

    SeverityLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
