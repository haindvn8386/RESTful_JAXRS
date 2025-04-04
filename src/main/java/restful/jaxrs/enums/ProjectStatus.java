package restful.jaxrs.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProjectStatus {
    PLANNING, IN_PROGRESS, COMPLETED;

    @JsonCreator
    public static ProjectStatus fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Project status cannot be null");
        }
        try {
            return ProjectStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid project status: " + value);
        }
    }
}

