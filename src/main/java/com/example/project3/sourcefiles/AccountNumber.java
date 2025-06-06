package com.example.project3.sourcefiles;
import java.util.Random;

/**
 * Represents an account number, which is associated with a branch and account type.
 * The account number is a 4-digit randomly generated number.
 * This class implements the Comparable interface to allow ordering of account numbers.
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public class AccountNumber implements Comparable<AccountNumber> {
    private static final int SEED = 9999;
    private static final Random random = new Random(SEED);
    private Branch branch;
    private AccountType type;
    private String number; // 4-digit string

    /**
     * Constructs an AccountNumber with a specified branch and account type.
     * A random 4 digit number is generated for this account.
     * @param branch the branch associated with the account.
     * @param type   the type of the account.
     */
    public AccountNumber(Branch branch, AccountType type) {
        this.branch = branch;
        this.type = type;
        this.number = generateNumber();
    }

    /**
     * Constructs an AccountNumber with a specified branch, account type, and a predefined serial number.
     * @param branch the branch associated with the account.
     * @param type   the type of the account.
     * @param serial the predefined serial number for the account.
     */
    public AccountNumber(Branch branch, AccountType type, String serial) {
        this.branch = branch;
        this.type = type;
        this.number = serial;
    }

    /**
     * Generates a 4 digit random number formatted as a string.
     * @return returns a randomly generated 4 digit string.
     */
    private String generateNumber() {
        int num = random.nextInt(SEED);
        return String.format("%04d", num);
    }

    /**
     * Retrieves the branch associated with this account number.
     * @return returns the branch of the account.
     */
    public Branch getBranch() { return branch; }

    /**
     * Retrieves the account type associated with this account number.
     * @return returns the account type.
     */
    public AccountType getType() { return type; }

    /**
     * Updates the account type for this account number.
     * @param newType the new account type to be assigned.
     */
    public void setType(AccountType newType) { this.type = newType; }

    /**
     * Compares this account number with another account number.
     * Comparison is based on the numerical value of the account number.
     * @param other the account number to compare against.
     * @return returns 0 if the numbers are equal, -1 if this account number is smaller, 1 if it is larger.
     */
    @Override
    public int compareTo(AccountNumber other) {
        return this.toString().compareTo(other.toString());
    }

    /**
     * Checks whether this account number is equal to another object.
     * Two account numbers are considered equal if their string representations match.
     * @param obj the object to compare against.
     * @return returns true if the account numbers are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AccountNumber)) return false;
        AccountNumber other = (AccountNumber) obj;
        return this.toString().equals(other.toString());
    }

    /**
     * Returns a string representation of the account number.
     * The string consists of the branch code, account type code, and the 4-digit account number.
     * @return returns a formatted string representing the full account number.
     */
    @Override
    public String toString() {
        return branch.getBranchCode() + type.getCode() + number;
    }
}
