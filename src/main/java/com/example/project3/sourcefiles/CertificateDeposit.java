package com.example.project3.sourcefiles;

import com.example.project3.util.Date;

import java.text.DecimalFormat;

/**
 * Represents a Certificate Deposit (CD) account.
 * A CD account has a fixed term (3, 6, 9, or 12 months) with no monthly fees.
 * The interest rate depends on the term length, and an early withdrawal incurs a penalty.
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public class CertificateDeposit extends Savings {
    private int term;  // in months
    private Date openDate;

    /**
     * Constructs a Certificate Deposit account with the specified parameters.
     * @param number   the account number
     * @param holder   the profile of the account holder
     * @param balance  the initial balance of the CD
     * @param term     the term length in months (3, 6, 9, or 12)
     * @param openDate the date the CD was opened
     */
    public CertificateDeposit(AccountNumber number, Profile holder, double balance, int term, Date openDate) {
        super(number, holder, balance);
        this.term = term;
        this.openDate = openDate;
    }

    /**
     * Returns the term length of the CD.
     * @return the term in months
     */
    public int getTerm() {
        return term;
    }

    /**
     * Returns the open date of the CD.
     * @return opening date
     */
    public Date getOpenDate() {
        return openDate;
    }


    /**
     * Calculates and returns the maturity date of the CD.
     * The maturity date is determined by adding the term length to the opening date.
     * @return the maturity date of the CD
     */
    public Date getMaturityDate() {
        int newYear = openDate.getYear();
        int newMonth = openDate.getMonth() + term;
        int newDay = openDate.getDay();

        // Adjust the year and month if months exceed 12
        while (newMonth > 12) {
            newMonth -= 12;
            newYear++;
        }

        // Get the maximum valid day for the new month and year
        int maxDaysInMonth = Date.DAYS_IN_MONTH[newMonth - 1];

        // Adjust for leap year if the new month is February
        if (newMonth == 2 && newYear % Date.QUADRENNIAL == 0 &&
                (newYear % Date.CENTENNIAL != 0 || newYear % Date.QUATERCENTENNIAL == 0)) {
            maxDaysInMonth = 29;
        }

        // If the original day is too large for the new month, adjust it
        if (newDay > maxDaysInMonth) {
            newDay = maxDaysInMonth;
        }

        return new Date(newMonth + "/" + newDay + "/" + newYear);
    }

    /**
     * Computes the monthly interest based on the term length.
     * @return the monthly interest earned on the CD
     */
    @Override
    public double interest() {
        double rate;
        switch (term) {
            case 3:  rate = 0.03;  break;
            case 6:  rate = 0.0325; break;
            case 9:  rate = 0.035;  break;
            case 12: rate = 0.04;   break;
            default: rate = 0.03;   break;
        }
        return balance * (rate / 12);
    }

    /**
     * Returns the monthly fee for a CD account, which is always $0.
     * @return the monthly fee (always 0)
     */
    @Override
    public double fee() {

        return 0;
    }

    /**
     * Returns a string representation of the Certificate Deposit account,
     * including the account details, term length, opening date, and maturity date.
     * @return a string containing the CD details
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return super.toString() + " Term[" + term + "] Date Opened[" + openDate + "] Maturity Date[" + getMaturityDate() + "]";
    }
}
