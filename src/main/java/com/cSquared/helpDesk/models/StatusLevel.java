package com.cSquared.helpDesk.models;

public enum StatusLevel {
    UNASSIGNED("Unassigned"),
    ACTIVE("Active"),
    CLOSED("Closed");

    private final String displayName;

    StatusLevel (String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
