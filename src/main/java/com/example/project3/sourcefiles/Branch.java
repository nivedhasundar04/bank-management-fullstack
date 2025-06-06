package com.example.project3.sourcefiles;

/**
 * Represents different bank branches, each associated with a zip code,
 * a unique branch code, and a county.
 * This enum defines a fixed set of branch locations.
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public enum Branch {
    EDISON("08817", "100", "Middlesex"),
    BRIDGEWATER("08807", "200", "Somerset"),
    PRINCETON("08542", "300", "Mercer"),
    PISCATAWAY("08854", "400", "Middlesex"),
    WARREN("07057", "500", "Somerset");

    private final String zip;
    private final String branchCode;
    private final String county;

    /**
     * Constructs a Branch enum constant with the specified zip code, branch code, and county.
     * @param zip        the ZIP code of the branch location.
     * @param branchCode the unique branch code.
     * @param county     the county where the branch is located.
     */
    Branch(String zip, String branchCode, String county) {
        this.zip = zip;
        this.branchCode = branchCode;
        this.county = county;
    }

    /**
     * Retrieves the unique branch code associated with this branch.
     * @return returns the branch code as a string.
     */
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Retrieves the zip code of this branch.
     * @return returns the zip code as a string.
     */
    public String getZip() {
        return zip;
    }

    /**
     * Retrieves the county where this branch is located.
     * @return returns the county name as a string.
     */
    public String getCounty() {
        return county;
    }
}
