package com.example.project3.sourcefiles;

/**
 * Represents different types of bank accounts, each associated with a unique code.
 * The available account types are CHECKING, SAVINGS, MONEY_MARKET, COLLEGE_CHECKING, and CD.
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public enum AccountType {
    CHECKING("01"),
    SAVINGS("02"),
    MONEY_MARKET("03"),
    COLLEGE_CHECKING("04"),
    CD("05");

    private final String code;
    /**
     * Constructs an AccountType enum constant with the specified code.
     * @param code the unique code representing the account type.
     */
    AccountType(String code) {
        this.code = code;
    }

    /**
     * Retrieves the unique code associated with this account type.
     * @return returns the account type code as a string.
     */
    public String getCode() { return code; }

    /**
     * Returns the AccountType corresponding to the provided code.
     * @param code the string code to convert to an AccountType.
     * @return the matching AccountType, or {@code null} if no match is found.
     */
    public static AccountType fromCode(String code) {
        for (AccountType at : AccountType.values()) {
            if (at.code.equals(code)){
                return at;
            }
        }
        return null;
    }
}
