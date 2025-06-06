package com.example.project3.sourcefiles;

/**
 * A regular checking account.
 * Interest = 1.5% annual => monthly = balance * (0.015 / 12)
 * Fee = $15 unless balance >= $1000, then $0
 */
public class Checking extends Account {

    /**
     * Constructs a Checking account with the specified account number,
     * account holder, and initial balance.
     * @param number  the unique account number
     * @param holder  the profile of the account holder
     * @param balance the initial balance of the account
     */
    public Checking(AccountNumber number, Profile holder, double balance) {
        super(number, holder, balance);
    }

    /**
     * Calculates and returns the monthly interest earned on the account balance.
     * @return the monthly interest amount
     */
    @Override
    public double interest() {
        // 1.5% annual => monthly
        double annualRate = 0.015;
        return balance * (annualRate / 12);
    }

    /**
     * Determines and returns the monthly maintenance fee for the account.
     * The fee is $15 unless the account balance is at least $1000, otherwise it is 0.
     * @return the monthly fee amount
     */
    @Override
    public double fee() {
        return (balance >= 1000) ? 0 : 15;
    }
}
