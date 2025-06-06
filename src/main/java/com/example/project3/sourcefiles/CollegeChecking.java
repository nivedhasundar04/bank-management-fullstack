package com.example.project3.sourcefiles;

/**
 * Represents a College Checking account, which is a type of Checking account
 * associated with a specific university campus.
 * College Checking accounts do not incur a monthly fee.
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public class CollegeChecking extends Checking {
    private Campus campus;

    /**
     * Constructs a College Checking account with the specified account number,
     * account holder, initial balance, and associated campus.
     * @param number  the unique account number
     * @param holder  the profile of the account holder
     * @param balance the initial balance of the account
     * @param campus  the associated campus of the account
     */
    public CollegeChecking(AccountNumber number, Profile holder, double balance, Campus campus) {
        super(number, holder, balance);
        this.campus = campus;
    }

    /**
     * Retrieves and returns the given campus.
     * @return campus.
     */
    public Campus getCampus() {
        return campus;
    }

    /**
     * Returns the monthly fee for a College Checking account.
     * Since this account type does not have a monthly fee, it always returns 0.
     * @return the monthly fee, which is always 0
     */
    @Override
    public double fee() {
        return 0;
    }

    /**
     * Returns a string representation of the College Checking account,
     * including the campus information.
     * @return a formatted string containing account details and campus
     */
    @Override
    public String toString() {
        return super.toString() + " Campus[" + campus + "]";
    }
}
