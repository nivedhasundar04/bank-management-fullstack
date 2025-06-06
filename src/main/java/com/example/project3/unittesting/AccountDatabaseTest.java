package com.example.project3.unittesting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.project3.sourcefiles.*;
import com.example.project3.util.Date;

//import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;

public class AccountDatabaseTest {
    private AccountDatabase db;
    private MoneyMarket moneyMarketAccount;
    private Checking checkingAccount;
    private Savings savingsAccount;
    private Profile holder;

    @BeforeEach
    public void setUp() {
        db = new AccountDatabase();
        holder = new Profile("John", "Doe", new Date("01/01/1985"));

        // Create test accounts
        checkingAccount = new Checking(new AccountNumber(Branch.BRIDGEWATER, AccountType.CHECKING), holder, 1000);
        moneyMarketAccount = new MoneyMarket(new AccountNumber(Branch.WARREN, AccountType.MONEY_MARKET), holder, 4000); // Below loyalty threshold
        savingsAccount = new Savings(new AccountNumber(Branch.PRINCETON, AccountType.SAVINGS), holder, 2000);

        // Add accounts to database
        db.add(checkingAccount);
        db.add(moneyMarketAccount);
        db.add(savingsAccount);
    }

    @Test
    public void deposit() {
        // Test deposit to a checking account
        assertTrue(db.deposit(checkingAccount.getAccountNumber().toString(), 500));
        assertEquals(1500, checkingAccount.getBalance(), 0.01);

        // Test deposit to a money market account that makes it loyal
        assertTrue(db.deposit(moneyMarketAccount.getAccountNumber().toString(), 1500));
        assertEquals(5500, moneyMarketAccount.getBalance(), 0.01);
        //assertTrue(moneyMarketAccount.isLoyal()); // Should now be loyal

        // Test deposit to a savings account
        assertTrue(db.deposit(savingsAccount.getAccountNumber().toString(), 1000));
        assertEquals(3000, savingsAccount.getBalance(), 0.01);
    }

    @Test
    public void withdraw() {
        // Test valid withdrawal from checking
        assertTrue(db.withdraw(checkingAccount.getAccountNumber().toString(), 500));
        assertEquals(500, checkingAccount.getBalance(), 0.01);

        // Test insufficient funds withdrawal from savings
        assertFalse(db.withdraw(savingsAccount.getAccountNumber().toString(), 3000));
        assertEquals(2000, savingsAccount.getBalance(), 0.01); // Balance should remain unchanged

        // Test valid withdrawal from money market that drops loyalty
        assertTrue(db.withdraw(moneyMarketAccount.getAccountNumber().toString(), 1000));
        assertEquals(3000, moneyMarketAccount.getBalance(), 0.01);
        assertFalse(moneyMarketAccount.isLoyal()); // No longer loyal
    }
}
