package com.example.project3.sourcefiles;

/**
 * Enum class for campuses.
 * Campus codes: 1 = New Brunswick, 2 = Newark, 3 = Camden.
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public enum Campus {
    NEW_BRUNSWICK("1"),
    NEWARK("2"),
    CAMDEN("3");


    private final String code;

    /**
     * Constructs a Campus enum with the given code.
     * @param code the unique code representing the campus
     */
    Campus(String code) {
        this.code = code;
    }

    /**
     * Retrieves the code associated with a given campus.
     * @return campus code
     */
    public String getCode() {
        return code;
    }

    /**
     * Converts a campus code to the corresponding Campus enum.
     * @param code the string representation of the campus code
     * @return the corresponding Campus enum
     * @throws IllegalArgumentException if the code does not match any campus
     */
    public static Campus fromCode(String code) {
        for (Campus c : Campus.values()) {
            if (c.code.equals(code))
                return c;
        }
        return null;
    }

    /**
     * Returns a string representation of the campus.
     * @return the name of the campus
     */
    @Override
    public String toString() {
        switch (this) {
            case NEW_BRUNSWICK: return "New Brunswick";
            case NEWARK: return "Newark";
            case CAMDEN: return "Camden";
            default: return "";
        }
    }
}
