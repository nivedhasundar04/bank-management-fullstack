package com.example.project3.sourcefiles;

/**
 * Represents a Money Market savings account with higher interest rates,
 * loyalty benefits, and additional withdrawal fees.
 * Interest: 3.5% annually (0.035/12 per month), plus an extra 0.25% if balance >= $5000.
 * Fee: $25 if balance is below $2000, plus $10 for each withdrawal beyond 3.
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public class MoneyMarket extends Savings {
    private int withdrawalCount;

    /**
     * Constructs a Money Market account.
     * @param number  The individual account number.
     * @param holder  The profile of the account holder.
     * @param balance The initial balance of the account.
     */
    public MoneyMarket(AccountNumber number, Profile holder, double balance) {
        super(number, holder, balance);
        this.withdrawalCount = 0;
        if (balance >= 5000) {
            this.isLoyal = true;
        }
    }

    /**
     * Calculates the monthly interest earned on the account balance.
     * @return The interest amount for the current balance.
     */
    @Override
    public double interest() {
        double rate = 0.035;
        if (isLoyal) {
            rate += 0.0025;
        }
        return balance * (rate / 12);
    }

    /**
     * Determines the monthly fee for the Money Market account.
     * @return $25 if balance is below $2000, plus $10 for each withdrawal beyond three.
     */
    @Override
    public double fee() {
        double fee = (balance >= 2000) ? 0 : 25;
        if (withdrawalCount > 3) {
            fee += 10;
        }
        return fee;
    }

    /**
     * Withdraws a specified amount from the account.
     * Increments the withdrawal count and deducts loyalty status if balance drops below $5000.
     * @param amount The amount to withdraw.
     */
    @Override
    public void withdraw(double amount) {
        super.withdraw(amount);
        withdrawalCount++;
        if (balance < 5000) {
            isLoyal = false;
        }

        if(withdrawalCount > 3){
            balance -= 10;
        }
    }

    /**
     * Compares this Money Market account with another based on the account holder's profile.
     * @param obj The object to compare with.
     * @return true if both accounts belong to the same holder, otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MoneyMarket other)) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Returns a string representation of the Money Market account,
     * including the number of withdrawals.
     * @return A formatted string containing account details.
     */
    @Override
    public String toString() {
        return super.toString() + String.format(" Withdrawal[%d]", withdrawalCount);
    }

    /**
     * Retrieves amount of withdrawals for account.
     * @return withdrawal count.
     */
    public int getWithdrawalCount() {
        return withdrawalCount;
    }

}
