package com.example.project3.util;

/**
 * Represents a date with a year, month, and day.
 * Provides methods for checking validity, leap years, and comparison operations.
 * This class implements the Comparable interface to allow ordering of dates.
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;

    public static final int[] DAYS_IN_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;

    /**
     * Constructs a Date object from a string in the format MM/DD/YYYY.
     * @param date the date string in MM/DD/YYYY format.
     */
    public Date(String date) {
        String[] dateParts = date.split("/");
        this.month = Integer.parseInt(dateParts[0]);
        this.day = Integer.parseInt(dateParts[1]);
        this.year = Integer.parseInt(dateParts[2]);
    }

    /**
     * Retrieves the year of this date.
     * @return the year as an integer.
     */
    public int getYear() {
        return year;
    }

    /**
     * Retrieves the month of this date.
     * @return the month as an integer (1-12).
     */
    public int getMonth() {
        return month;
    }

    /**
     * Retrieves the day of this date.
     * @return the day as an integer.
     */
    public int getDay() {
        return day;
    }

    /**
     * Determines whether the given year is a leap year.
     * @return true if the year is a leap year, false otherwise.
     */
    private boolean isLeapYear() {
        if (year % QUADRENNIAL == 0) {
            if (year % CENTENNIAL == 0) {
                return year % QUATERCENTENNIAL == 0;
            }
            return true;
        }
        return false;
    }

    /**
     * Checks whether this date is a valid calendar date.
     * The method validates the month, day, and ensures the year is within a reasonable range.
     * @return true if the date is valid, false otherwise.
     */
    public boolean isValid() {
        if (month < 1 || month > 12) {
            return false;
        }

        int daysInMonth = DAYS_IN_MONTH[month - 1];
        if (month == 2 && isLeapYear()) {
            daysInMonth = 29; // February has 29 days in a leap year
        }

        if (day < 1 || day > daysInMonth) {
            return false;
        }

        if (year < 1900) {
            return false;
        }

        return true;
    }

    /**
     * Compares this date with another date.
     * The comparison is based on year, then month, then day.
     * @param other the date to compare against.
     * @return a negative integer, zero, or a positive integer if this date is
     *         earlier than, equal to, or later than the specified date.
     */
    @Override
    public int compareTo(Date other) {
        if (this.year != other.year) {
            return this.year - other.year;
        }
        if (this.month != other.month) {
            return this.month - other.month;
        }
        return this.day - other.day;
    }

    /**
     * Checks whether this date is equal to another object.
     * Two dates are considered equal if they have the same year, month, and day.
     * @param obj the object to compare against.
     * @return true if the dates are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Date)) {
            return false;
        }
        Date other = (Date) obj;
        return (this.year == other.year && this.month == other.month && this.day == other.day);
    }

    /**
     * Returns a string representation of the date in MM/DD/YYYY format.
     * @return a formatted string representation of the date.
     */
    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

    /**
     * Main method to test the Date class.
     * It validates several date instances and prints the results.
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        Date d1 = new Date("1/17/900");
        Date d2 = new Date ("0/11/2001");
        Date d3 = new Date ("2/29/2018");
        Date d4 = new Date ("3/32/2003");
        Date d5 = new Date ("2/29/2000");
        Date d6 = new Date ("12/31/2001");


    }
}
