package com.example.project3.sourcefiles;

import com.example.project3.util.List;

import java.text.DecimalFormat;

/**
 * Represents an abstract bank account class with an account number, holder, and balance.
 * This class implements the Comparable interface to allow ordering of accounts
 * based on their account numbers.
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public abstract class Account implements Comparable<Account> {
    protected AccountNumber number;
    protected Profile holder;
    protected double balance;
    protected List<Activity> activities;

    /**
     * Constructs an Account with the specified account number, holder, and balance.
     * @param number  the account number associated with this account.
     * @param holder  the profile of the account holder.
     * @param balance the initial balance of the account.
     */
    public Account(AccountNumber number, Profile holder, double balance) {
        this.number = number;
        this.holder = holder;
        this.balance = balance;
        this.activities = new List<>();
    }

    /**
     * Calculates the interest earned on the account balance.
     * @return the amount of interest earned
     */
    public abstract double interest();

    /**
     * Calculates the monthly fee for maintaining the account.
     * @return the fee amount
     */
    public abstract double fee();

    /**
     * Generates an account statement as a string including transaction history,
     * interest earned, fees applied, and updated balance.
     * @return a string representing the account statement
     */
    public String statement() {
        StringBuilder statement = new StringBuilder();
        statement.append(printActivities());
        double interestEarned = interest();
        double monthlyFee = fee();
        statement.append(printInterestFee(interestEarned, monthlyFee));
        statement.append(updateBalance(interestEarned, monthlyFee));
        return statement.toString();
    }


    /**
     * Returns a string representation of the recorded activities of the account.
     * @return a string containing the activities
     */
    private String printActivities() {
        StringBuilder activitiesDetails = new StringBuilder();
        if (activities.isEmpty()) {
            activitiesDetails.append("No activities for ").append(holder.getFname()).append(" ").append(holder.getLname()).append("\n");
        } else {
            activitiesDetails.append(holder.getFname()).append(" ")
                    .append(holder.getLname()).append(" ")
                    .append(holder.getDob()).append("\n")
                    .append("[Account #] ").append(number).append("\n")
                    .append("[Activity]\n");
            for (Activity a : activities) {
                activitiesDetails.append(a).append("\n");
            }
        }
        return activitiesDetails.toString();
    }

    /**
     * Returns a string representing the interest earned and the monthly fee.
     * @param interest the interest earned
     * @param fee      the monthly fee
     * @return a string containing the interest and fee details
     */
    private String printInterestFee(double interest, double fee) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return "[interest] $" + df.format(interest) + " [Fee] $" + df.format(fee) + "\n";
    }

    /**
     * Returns a string representing the updated balance after applying interest and deducting fees.
     * @param interest the interest earned
     * @param fee      the monthly fee
     * @return a string containing the updated balance
     */
    private String updateBalance(double interest, double fee) {
        balance = balance + interest - fee;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return "[Balance] $" + df.format(balance) + "\n";
    }


    /**
     * Records an activity in the account's transaction history.
     * @param a the activity to record
     */
    public void addActivity(Activity a) {
        activities.add(a);
    }

    /**
     * Deposits a specified amount into the account and records the transaction.
     * @param amount the amount to deposit
     */
    public void deposit(double amount) {
        balance += amount;
        addActivity(new Activity('D', amount, number.getBranch()));
    }

    /**
     * Withdraws a specified amount from the account and records the transaction.
     * @param amount the amount to withdraw
     */
    public void withdraw(double amount) {
        balance -= amount;
        addActivity(new Activity('W', amount, number.getBranch()));
    }


    /**
     * Compares this account to another account based on account numbers.
     * @param other the other account to compare
     * @return a negative integer, zero, or a positive integer if this account
     *         is less than, equal to, or greater than the specified account
     */
    @Override
    public int compareTo(Account other) {
        return this.number.compareTo(other.number);
    }

    /**
     * Checks if this account is equal to another object.
     * Two accounts are considered equal if they have the same account holder and account type.
     * @param o the object to compare
     * @return true if the accounts are equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Account))
            return false;
        Account a = (Account) o;
        return this.holder.equals(a.holder) && this.number.getType() == a.number.getType();
    }

    /**
     * Returns a string representation of the account.
     * @return a formatted string containing account details
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return "Account#[" + number + "] Holder[" + holder + "] Balance[$" + df.format(balance) + "]";
    }

    /**
     * Gets the current balance of the account.
     * @return the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the account.
     * @param balance the new balance
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Gets the account number associated with this account.
     * @return the account number
     */
    public AccountNumber getAccountNumber() {
        return number;
    }

    /**
     * Gets the account holder's profile associated with this account.
     * @return the account holder's profile
     */
    public Profile getHolder() {
        return holder;
    }
}
