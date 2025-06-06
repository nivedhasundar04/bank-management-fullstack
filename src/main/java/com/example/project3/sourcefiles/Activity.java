package com.example.project3.sourcefiles;

import com.example.project3.util.Date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents an account activity such as a deposit or withdrawal.
 * Each activity includes a transaction type, amount, date, branch location, and whether it was conducted at an ATM.
 * This class implements the comparable interface for sorting by date.
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public class Activity implements Comparable<Activity> {
    private Date date;
    private Branch location;
    private char type;   // 'D' or 'W'
    private double amount;
    private boolean atm; // true if ATM transaction

    /**
     * Constructs an Activity with given type, amount, and branch location.
     * Date is set to today's date.
     */
    public Activity(char type, double amount, Branch location) {
        this.type = type;
        this.amount = amount;
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        this.date = new Date(now.format(formatter));
        this.atm = false;
        this.location = location;
    }

    /**
     * Compares this activity with another activity based on the transaction date.
     * @param other the other activity to compare to
     * @return a negative integer, zero, or a positive integer if this activity's date is before, the same as,
     * or after the other activity's date
     */
    @Override
    public int compareTo(Activity other) {
        return this.date.compareTo(other.date);
    }

    /**
     * Returns a formatted string representation of this activity.
     * The format includes the date, branch location, transaction type, amount,
     * and whether it was an ATM transaction.
     * @return a string representation of the transaction
     */
    @Override
    public String toString() {
        if(type == 'W'){
            return "\t" + date + "::" + location + "::withdrawal::"+ " $" + String.format("%.2f", amount) + (atm ? " (ATM)" : "");
        } else{
            return "\t" + date + "::" + location + "::deposit::"+ " $" + String.format("%.2f", amount) + (atm ? " (ATM)" : "");
        }

    }
}
