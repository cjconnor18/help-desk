package com.cSquared.helpDesk.models;

public enum SeverityLevel {

    LOW ("Low"),
    MEDIUM ("Medium"),
    HIGH ("High"),
    VERYHIGH("Very High");

    private String displayName;

    SeverityLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
