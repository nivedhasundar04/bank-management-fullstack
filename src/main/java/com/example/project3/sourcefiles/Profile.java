package com.example.project3.sourcefiles;

import com.example.project3.util.Date;

/**
 * Represents a profile with a first name, last name, and date of birth.
 * This class implements the Comparable interface to allow ordering of profiles
 * based on last name, first name, and date of birth.
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public class Profile implements Comparable<Profile> {
    private String fname;
    private String lname;
    private Date dob;

    /**
     * Constructs a Profile with the specified first name, last name, and date of birth.
     *
     * @param fname the first name of the profile holder.
     * @param lname the last name of the profile holder.
     * @param dob   the date of birth of the profile holder.
     */
    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    public Date getDob() {
        return dob;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    /**
     * Compares this profile to another profile based on last name, first name,
     * and date of birth in that order.
     *
     * @param o the profile to compare against.
     * @return returns a negative integer, zero, or a positive integer if this profile is
     *         less than, equal to, or greater than the specified profile.
     */
    @Override
    public int compareTo(Profile o) {
        int cmp = this.lname.compareToIgnoreCase(o.lname);
        if (cmp < 0) return -1;
        if (cmp > 0) return 1;

        cmp = this.fname.compareToIgnoreCase(o.fname);
        if (cmp < 0) return -1;
        if (cmp > 0) return 1;

        cmp = this.dob.compareTo(o.dob);
        if (cmp < 0) return -1;
        if (cmp > 0) return 1;

        return 0;
    }

    /**
     * Checks whether this profile is equal to another object.
     * Two profiles are considered equal if they have the same first name, last name, and date of birth.
     * @param obj the object to compare against.
     * @return returns true if the profiles are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Profile other)) {
            return false;
        }
        return (this.fname.equalsIgnoreCase(other.fname)
                && this.lname.equalsIgnoreCase(other.lname)
                && this.dob.equals(other.dob));
    }

    /**
     * Returns a string representation of the profile, including the first name,
     * last name, and date of birth.
     * @return returns a formatted string representation of the profile.
     */
    @Override
    public String toString() {
        return fname + " " + lname + " " + dob;
    }

    /**
     * Main method to test the Profile class.
     * It creates and compares multiple Profile instances.
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Testbed main() for Profile class
        // Creating Date objects for testing
        Date d1 = new Date("1/15/1995");
        Date d2 = new Date("7/22/2000");
        Date d3 = new Date("5/10/1998");
        Date d4 = new Date("7/22/2001"); // Same DOB as d2 except for year
        Date d5 = new Date("4/25/1981");

        Profile p1 = new Profile("Robert", "Allen", d1);
        Profile p2 = new Profile("Amelia", "Stone", d2);
        Profile p3 = new Profile("Robert", "Smith", d3);
        Profile p4 = new Profile("Amelia", "Stone", d4); // Same name as p2, different DOB
        Profile p5 = new Profile("Barron", "Smith", d5); // Identical to p1
        Profile p6 = new Profile ("Barron", "Smith", d5);


    }
}
