package com.example.project3.sourcefiles;

import com.example.project3.util.Date;
import com.example.project3.util.List;
import com.example.project3.util.Sort;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * AccountDatabase extends a generic List of Accounts.
 * Contains an Archive for closed accounts.
 * Provides methods for deposit, withdrawal, loading accounts, processing activities, and printing statements.
 *
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public class AccountDatabase extends List<Account> {
    private Archive archive;

    /**
     * Constructs an AccountDatabase instance and initializes an Archive for closed accounts.
     */
    public AccountDatabase() {
        super();
        archive = new Archive();
    }

    /**
     * Returns a string representation of archived accounts.
     * @return a formatted string of archived accounts
     */
    public String printArchive() {
        return archive.print();
    }

    /**
     * Print statements for all accounts.
     */
    public String printStatements() {
        if (this.isEmpty()) {
            return "No accounts available.";
        }

        StringBuilder statements = new StringBuilder();
        for (Account acct : this) {
            statements.append(acct.statement()).append("\n");
        }
        return statements.toString();
    }


    /**
     * Prints accounts sorted by account type and then by account number.
     */
    public String printByType() {
        if (this.isEmpty()) {
            return "No accounts available.";
        }

        Sort.account(this, 'T');  // Sort by Type
        StringBuilder sb = new StringBuilder();
        sb.append("*List of accounts ordered by account type and number.\n");

        String currentType = "";
        DecimalFormat df = new DecimalFormat("#,##0.00");

        for (Account account : this) {
            updateLoyaltyStatus(account.getHolder());

            String typeCode = account.getAccountNumber().getType().getCode();
            String typeName = switch (typeCode) {
                case "01" -> "CHECKING";
                case "02" -> "SAVINGS";
                case "03" -> "MONEY_MARKET";
                case "04" -> "COLLEGE_CHECKING";
                case "05" -> "CERTIFICATE_DEPOSIT";
                default -> "UNKNOWN";
            };

            if (!typeName.equals(currentType)) {
                if (!currentType.isEmpty()) {
                    sb.append("\n");
                }
                sb.append("Account Type: ").append(typeName).append("\n");
                currentType = typeName;
            }

            String loyaltyTag = (account instanceof Savings savingsAcc && savingsAcc.isLoyal) ? " [LOYAL]" : "";

            if (account instanceof MoneyMarket mmAccount) {
                sb.append(String.format("Account#[%s] Holder[%s] Balance[$%s] Branch[%s]%s Withdrawal[%d]\n",
                        mmAccount.getAccountNumber(), mmAccount.getHolder(), df.format(mmAccount.getBalance()),
                        mmAccount.getAccountNumber().getBranch().name(),
                        mmAccount.isLoyal() ? " [LOYAL]" : "",
                        mmAccount.getWithdrawalCount()));
            } else if (account instanceof CertificateDeposit cdAccount) {
                sb.append(String.format("Account#[%s] Holder[%s] Balance[$%s] Branch[%s] Term[%d] Date opened[%s] Maturity date[%s]\n",
                        cdAccount.getAccountNumber(), cdAccount.getHolder(), df.format(cdAccount.getBalance()),
                        cdAccount.getAccountNumber().getBranch().name(), cdAccount.getTerm(),
                        cdAccount.getOpenDate(), cdAccount.getMaturityDate()));
            } else if (account instanceof CollegeChecking campusAcc) {
                sb.append(String.format("Account#[%s] Holder[%s] Balance[$%s] Branch[%s] Campus[%s]\n",
                        campusAcc.getAccountNumber(), campusAcc.getHolder(), df.format(campusAcc.getBalance()),
                        campusAcc.getAccountNumber().getBranch().name(),
                        campusAcc.getCampus().name()));
            } else {
                sb.append(String.format("Account#[%s] Holder[%s] Balance[$%s] Branch[%s]%s\n",
                        account.getAccountNumber(), account.getHolder(), df.format(account.getBalance()),
                        account.getAccountNumber().getBranch().name(),
                        loyaltyTag));
            }
        }

        sb.append("*end of list.");
        return sb.toString();
    }



    /**
     * Prints accounts sorted by branch location (county and city).
     */
    public String printByBranch() {
        if (this.isEmpty()) {
            return "No accounts available.";
        }

        Sort.account(this, 'B');

        StringBuilder sb = new StringBuilder();
        sb.append("*List of accounts ordered by branch location (county, city).\n");

        String currentCounty = "";
        String currentCity = "";
        DecimalFormat df = new DecimalFormat("#,##0.00");

        for (Account account : this) {
            updateLoyaltyStatus(account.getHolder());

            String county = account.getAccountNumber().getBranch().getCounty();
            String city = account.getAccountNumber().getBranch().name();

            if (!county.equals(currentCounty)) {
                if (!currentCounty.isEmpty()) {
                    sb.append("\n");
                }
                sb.append("County: ").append(county).append("\n");
                currentCounty = county;
                currentCity = city;
            }

            String loyaltyTag = (account instanceof Savings savingsAcc && savingsAcc.isLoyal) ? " [LOYAL]" : "";

            if (account instanceof MoneyMarket mmAccount) {
                sb.append(String.format("Account#[%s] Holder[%s] Balance[$%s] Branch[%s]%s Withdrawal[%d]\n",
                        mmAccount.getAccountNumber(), mmAccount.getHolder(), df.format(mmAccount.getBalance()),
                        mmAccount.getAccountNumber().getBranch().name(),
                        mmAccount.isLoyal() ? " [LOYAL]" : "",
                        mmAccount.getWithdrawalCount()));
            } else if (account instanceof CertificateDeposit cdAccount) {
                sb.append(String.format("Account#[%s] Holder[%s] Balance[$%s] Branch[%s] Term[%d] Date opened[%s] Maturity date[%s]\n",
                        cdAccount.getAccountNumber(), cdAccount.getHolder(), df.format(cdAccount.getBalance()),
                        cdAccount.getAccountNumber().getBranch().name(), cdAccount.getTerm(),
                        cdAccount.getOpenDate(), cdAccount.getMaturityDate()));
            } else if (account instanceof CollegeChecking campusAcc) {
                sb.append(String.format("Account#[%s] Holder[%s] Balance[$%s] Branch[%s] Campus[%s]\n",
                        campusAcc.getAccountNumber(), campusAcc.getHolder(), df.format(campusAcc.getBalance()),
                        campusAcc.getAccountNumber().getBranch().name(),
                        campusAcc.getCampus().name()));
            } else {
                sb.append(String.format("Account#[%s] Holder[%s] Balance[$%s] Branch[%s]%s\n",
                        account.getAccountNumber(), account.getHolder(), df.format(account.getBalance()),
                        account.getAccountNumber().getBranch().name(),
                        loyaltyTag));
            }
        }

        sb.append("*end of list.");
        return sb.toString();
    }


    /**
     * Prints accounts sorted by account holder and then by account number.
     * @return a string containing the formatted list of accounts by holder.
     */
    public String printByHolder() {
        if (this.isEmpty()) {
            return "No accounts available.";
        }

        Sort.account(this, 'H'); // Sorting by Holder

        StringBuilder sb = new StringBuilder();
        sb.append("*List of accounts ordered by account holder and number.\n");

        DecimalFormat df = new DecimalFormat("#,##0.00");

        for (Account account : this) {
            updateLoyaltyStatus(account.getHolder()); // Ensure loyalty status is updated

            String loyaltyTag = (account instanceof Savings savingsAcc && savingsAcc.isLoyal) ? " [LOYAL]" : "";

            if (account instanceof MoneyMarket mmAccount) {
                sb.append(String.format("Account#[%s] Holder[%s] Balance[$%s] Branch[%s]%s Withdrawal[%d]\n",
                        mmAccount.getAccountNumber(), mmAccount.getHolder(), df.format(mmAccount.getBalance()),
                        mmAccount.getAccountNumber().getBranch().name(),
                        mmAccount.isLoyal() ? " [LOYAL]" : "",
                        mmAccount.getWithdrawalCount()));
            }
            else if (account instanceof CertificateDeposit cdAccount) {
                sb.append(String.format("Account#[%s] Holder[%s] Balance[$%s] Branch[%s] Term[%d] Date opened[%s] Maturity date[%s]\n",
                        cdAccount.getAccountNumber(), cdAccount.getHolder(), df.format(cdAccount.getBalance()),
                        cdAccount.getAccountNumber().getBranch().name(), cdAccount.getTerm(),
                        cdAccount.getOpenDate(), cdAccount.getMaturityDate()));
            }
            else if (account instanceof CollegeChecking campusAcc) {
                sb.append(String.format("Account#[%s] Holder[%s] Balance[$%s] Branch[%s] Campus[%s]\n",
                        campusAcc.getAccountNumber(), campusAcc.getHolder(), df.format(campusAcc.getBalance()),
                        campusAcc.getAccountNumber().getBranch().name(),
                        campusAcc.getCampus().name()));
            }
            else {
                sb.append(String.format("Account#[%s] Holder[%s] Balance[$%s] Branch[%s]%s\n",
                        account.getAccountNumber(), account.getHolder(), df.format(account.getBalance()),
                        account.getAccountNumber().getBranch().name(),
                        loyaltyTag));
            }
        }

        sb.append("*end of list.");
        return sb.toString();
    }


    /**
     * Loads accounts from a file.
     * @param file the given file to load accounts from
     */
    public void loadAccounts(File file) throws IOException {
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            StringTokenizer tokens = new StringTokenizer(line, ",");
            if (tokens.countTokens() < 6) {
                continue;
            }

            String type = tokens.nextToken().trim().toLowerCase();
            String branch = tokens.nextToken().trim().toUpperCase();
            String fname = tokens.nextToken().trim();
            String lname = tokens.nextToken().trim();
            String dobStr = tokens.nextToken().trim();
            String depositStr = tokens.nextToken().trim();

            double deposit;
            try {
                deposit = Double.parseDouble(depositStr);
            } catch (NumberFormatException e) {
                continue;
            }

            Date dob = new Date(dobStr);
            if (!dob.isValid()) {
                continue;
            }

            Profile profile = new Profile(fname, lname, dob);
            Account newAccount = null;

            try {
                Branch branchEnum = Branch.valueOf(branch);

                switch (type) {
                    case "checking":
                        newAccount = new Checking(new AccountNumber(branchEnum, AccountType.CHECKING), profile, deposit);
                        break;
                    case "savings":
                        newAccount = new Savings(new AccountNumber(branchEnum, AccountType.SAVINGS), profile, deposit);
                        break;
                    case "moneymarket":
                        newAccount = new MoneyMarket(new AccountNumber(branchEnum, AccountType.MONEY_MARKET), profile, deposit);
                        break;
                    case "college":
                        if (!tokens.hasMoreTokens()) {
                            continue;
                        }
                        String campusCode = tokens.nextToken().trim();
                        Campus campus = Campus.fromCode(campusCode);
                        if (campus == null) {
                            continue;
                        }
                        newAccount = new CollegeChecking(new AccountNumber(branchEnum, AccountType.COLLEGE_CHECKING), profile, deposit, campus);
                        break;
                    case "certificate":
                        if (tokens.countTokens() < 2) {
                            continue;
                        }
                        int term = Integer.parseInt(tokens.nextToken().trim());
                        String openDateStr = tokens.nextToken().trim();
                        Date openDate = new Date(openDateStr);
                        if (!openDate.isValid()) {
                            continue;
                        }
                        newAccount = new CertificateDeposit(new AccountNumber(branchEnum, AccountType.CD), profile, deposit, term, openDate);
                        break;
                    default:
                        continue;
                }
            } catch (IllegalArgumentException e) {
                continue;
            }

            if (newAccount != null) {
                this.add(newAccount);
            }

            for (Account acc : this) {
                updateLoyaltyStatus(acc.getHolder());
            }

        }
        sc.close();
    }

    /**
     * Processes the deposit and withdraw activities from a file.
     * @param file The given file to process activities from
     */
    public List<String> processActivities(File file) {
        List<String> messages = new List<>();

        try {
            Scanner fileScanner = new Scanner(file);
            messages.add("Processing \"" + file.getName() + "\"...");

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                StringTokenizer tokens = new StringTokenizer(line, ",");
                if (tokens.countTokens() < 5) {
                    messages.add("Invalid transaction format: " + line);
                    continue;
                }

                String type = tokens.nextToken().trim();
                String accountNum = tokens.nextToken().trim();
                String date = tokens.nextToken().trim();
                String location = tokens.nextToken().trim().toUpperCase();
                String amountStr = tokens.nextToken().trim();

                double amount;
                try {
                    amount = Double.parseDouble(amountStr);
                } catch (NumberFormatException e) {
                    messages.add("Invalid amount format: " + amountStr);
                    continue;
                }

                boolean success = false;
                if (type.equals("D")) {
                    success = deposit(accountNum, amount);
                } else if (type.equals("W")) {
                    success = withdraw(accountNum, amount);
                }

                if (success) {
                    messages.add(String.format("%s::%s::%s[ATM]::%s::$%,.2f",
                            accountNum, date, location, type.equals("D") ? "deposit" : "withdrawal", amount));
                } else {
                    messages.add("Transaction failed for account: " + accountNum);
                }
            }

            fileScanner.close();
            messages.add("Account activities in \"" + file.getName() + "\" processed.");
        } catch (IOException e) {
            messages.add("Error processing file: " + e.getMessage());
        }

        return messages;
    }


    /**
     * Updates the loyalty status of a given account holder based on account ownership and balance.
     * @param holder the account holder whose loyalty status needs to be updated
     */
    public void updateLoyaltyStatus(Profile holder) {
        boolean hasRegularChecking = false;
        boolean hasMoneyMarketWithEnoughBalance = false;

        // Check if the holder has a regular checking account or a qualifying Money Market account
        for (Account acc : this) {
            if (acc instanceof Checking && acc.getHolder().equals(holder)) {
                hasRegularChecking = true;
            }
            if (acc instanceof MoneyMarket && acc.getHolder().equals(holder) && acc.getBalance() >= 5000) {
                hasMoneyMarketWithEnoughBalance = true;
            }
        }

        // Update only Savings and Money Market accounts
        for (Account acc : this) {
            if (acc instanceof Savings && !(acc instanceof CertificateDeposit) && acc.getHolder().equals(holder)) {
                ((Savings) acc).setLoyal(hasRegularChecking);
            }
            if (acc instanceof MoneyMarket && acc.getHolder().equals(holder)) {
                ((MoneyMarket) acc).setLoyal(hasMoneyMarketWithEnoughBalance);
            }
        }
    }





    /**
     * Deposits to an account given its 9 digit account number string.
     * @param accountNumStr the account number as a string
     * @param amount the deposit amount
     * @return true if successful, false otherwise.
     */
    public boolean deposit(String accountNumStr, double amount) {
        AccountNumber num = buildAccountNumberFromString(accountNumStr);
        if (num == null) {
            return false;
        }
        for (Account acct : this) {
            if (acct.getAccountNumber().equals(num)) {
                acct.deposit(amount);
                return true;
            }
        }
        return false;
    }

    /**
     * Withdraws from an account given its 9 digit account number string.
     * @param accountNumStr the account number as a string
     * @param amount the withdrawal amount
     * @return true if successful, false otherwise.
     */
    public boolean withdraw(String accountNumStr, double amount) {
        AccountNumber num = buildAccountNumberFromString(accountNumStr);
        if (num == null)
            return false;
        for (Account acct : this) {
            if (acct.getAccountNumber().equals(num)) {
                if (acct.getBalance() < amount)
                    return false;
                acct.withdraw(amount);
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to build an AccountNumber from a 9 digit string.
     */
    AccountNumber buildAccountNumberFromString(String s) {
        if (s.length() != 9)
            return null;
        String branchCode = s.substring(0, 3);
        String typeCode = s.substring(3, 5);
        String serial = s.substring(5);
        Branch branch = null;
        for (Branch b : Branch.values()) {
            if (b.getBranchCode().equals(branchCode)) {
                branch = b;
                break;
            }
        }
        if (branch == null)
            return null;
        AccountType type = AccountType.fromCode(typeCode);
        if (type == null)
            return null;
        try {
            Integer.parseInt(serial);
        } catch (NumberFormatException e) {
            return null;
        }
        return new AccountNumber(branch, type, serial);
    }

    /**
     * Removes an account from the database and archives it.
     * @param acct the account to remove
     * @param closingDate the date when the account is closed
     */
    public void remove(Account acct, Date closingDate) {
        int index = -1;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).equals(acct)) {
                index = i;
                break;
            }
        }
        if (index == -1)
            return;
        acct.setBalance(0);
        archive.add(acct, closingDate);
        this.set(index, this.get(this.size() - 1));
        this.remove(this.get(this.size() - 1));
        updateLoyaltyStatus(acct.getHolder());
    }
}
