package com.example.project3.sourcefiles;
/**
 * Represents a savings account with interest, fees, and loyalty status.
 * Interest: 2.5% annually (0.025/12 per month), plus an additional 0.25% for loyal accounts.
 * Fee: $25 if balance is below $500; otherwise, no fee.
 *
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public class Savings extends Account {
    protected boolean isLoyal;

    /**
     * Constructs a Savings account.
     * @param number  Individual account number.
     * @param holder  The profile of the account holder.
     * @param balance The initial balance of the account.
     */
    public Savings(AccountNumber number, Profile holder, double balance) {
        super(number, holder, balance);
        this.isLoyal = false;
    }

    /**
     * Calculates the monthly interest earned on the account balance.
     * @return The interest amount for the current balance.
     */
    @Override
    public double interest() {
        double rate = 0.025;
        if (isLoyal) {
            rate += 0.0025;
        }
        return balance * (rate / 12);
    }

    /**
     * Determines the monthly fee for the savings account.
     * @return $25 if balance is below $500, otherwise $0.
     */
    @Override
    public double fee() {
        return (balance >= 500) ? 0 : 25;
    }

    /**
     * Sets loyalty status for an account.
     * @param loyal true if the account should be marked as loyal, otherwise false.
     */
    public void setLoyal(boolean loyal) {
        this.isLoyal = loyal;
    }

    /**
     * Returns loyalty status of an account as boolean.
     * @return true if the account is loyal, otherwise false.
     */
    public boolean isLoyal() {
        return isLoyal;
    }

    /**
     * Compares this savings account with another based on the account holder's profile.
     * @param obj The object to compare with.
     * @return true if both accounts belong to the same holder, otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Savings other)) {
            return false;
        }
        return this.getHolder().equals(other.getHolder()); // Compare based on Profile
    }


    /**
     * Returns a string representation of the savings account.
     * @return A formatted string containing account details.
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
