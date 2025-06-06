package com.example.project3.sourcefiles;

import com.example.project3.util.Date;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.StringTokenizer;


/**
 * Manages transactions including opening &amp; closing accounts as well as deposits &amp; withdrawals.
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public class TransactionManager {
    private AccountDatabase db;

    /**
     * Constructor that initializes a new account database and loads the accounts.txt file.
     */
    public TransactionManager() {
        db = new AccountDatabase();
        loadAccountsFromFile("accounts.txt");
    }

    /**
     * Runs the transaction manager &amp; processes user commands.
     */
    public void run() {
        System.out.println("Transaction Manager is running.");
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();

            if (!line.isEmpty()) {
                line = line.replaceAll("\\s+", " ").trim();  // Normalize spaces (replace multiple spaces with one)
                if (!processCommand(line)) {
                    break;
                }
            }
        }

        sc.close();
        System.out.println("Transaction Manager is terminated.");
    }

    /**
     * Processes a given command from user input.
     * @param line The command line input.
     * @return false if the command is to quit otherwise returns true.
     */
    private boolean processCommand(String line) {
        StringTokenizer st = new StringTokenizer(line, " ");
        String cmd = st.nextToken();
        switch (cmd) {
            case "O":
                processOpen(st);
                break;
            case "C":
                processClose(st);
                break;
            case "D":
                processDeposit(st);
                break;
            case "W":
                processWithdraw(st);
                break;
            case "A":
                db.processActivities(new File("activities.txt"));
                break;
            case "PA":
                System.out.println("*List of accounts closed in the archive.");
                db.printArchive();
                break;
            case "PS":
                System.out.println("*Account statements by account holder.");
                db.printStatements();
                break;
            case "PB":
                db.printByBranch();
                break;
            case "PH":
                db.printByHolder();
                break;
            case "PT":
                db.printByType();
                break;
            case "P":
                System.out.println("P command is deprecated!");
                break;
            case "Q":
                return false;
            default:
                System.out.println("Invalid command!");
                break;
        }
        return true;
    }

    /**
     * Loads accounts from a given file into the database.
     * @param filename the file to load accounts from
     */
    private void loadAccountsFromFile(String filename) {
        File file = new File(filename);
        try {
            db.loadAccounts(file);
            System.out.println("Accounts in \"" + filename + "\" loaded to the database.");
            //System.out.println("Loaded Accounts:");
            //db.printStatements();
        } catch (IOException e) {
            System.out.println("Error loading accounts file: " + e.getMessage());
        }
    }


    /**
     * Processes the request to open an account.
     * @param st Tokenized input that contains account details.
     */
    public void processOpen(StringTokenizer st) {
        if (st.countTokens() < 6) {
            System.out.println("Missing data tokens for opening an account.");
            return;
        }

        // Read input tokens
        String typeStr = st.nextToken().toLowerCase();
        String branchStr = st.nextToken().toUpperCase(); // Ensure branch code is case-insensitive
        String fName = st.nextToken();
        String lName = st.nextToken();
        String dobStr = st.nextToken();
        String depositStr = st.nextToken();

        // Create profile using util.Profile and util.Date
        Date dob = new Date(dobStr);
        if (!dob.isValid()) {
            System.out.println("DOB invalid: " + dobStr + " not a valid calendar date!");
            return;
        }
        if (isFutureDate(dob)) {
            System.out.println("DOB invalid: " + dobStr + " cannot be today or a future day.");
            return;
        }

        if (!isAgeAtLeast18(dob)) {
            System.out.println("Not eligible to open: " + dobStr + " under 18.");
            return;
        }

        double deposit = 0;
        try {
            deposit = Double.parseDouble(depositStr);
        } catch (NumberFormatException e) {
            System.out.println("For input string: " + "\"" + depositStr + "\"" + " - not a valid amount.");
            return;
        }

        if (deposit <= 0) {
            System.out.println("Initial deposit cannot be 0 or negative.");
            return;
        }

        Profile profile = new Profile(fName, lName, dob);
        AccountType acctType = null;
        Campus campus = null;
        int term = 0;
        Date openDate = null;

        // Step 1: Determine the account type BEFORE creating an account object
        switch (typeStr) {
            case "checking":
                acctType = AccountType.CHECKING;
                break;

            case "savings":
                acctType = AccountType.SAVINGS;
                break;

            case "moneymarket":
                acctType = AccountType.MONEY_MARKET;
                if (deposit < 2000) {
                    System.out.println("Minimum of $2,000 to open a Money Market account.");
                    return;
                }
                break;

            case "college":
                if (st.countTokens() < 1) {
                    System.out.println("Missing data tokens for opening an account.");
                    return;
                }
                String campusCode = st.nextToken();
                campus = Campus.fromCode(campusCode);
                if (campus == null) {
                    System.out.println("Invalid campus code: " + campusCode);
                    return;
                }
                if (!isEligibleForCollege(dob)) {
                    System.out.println("Not eligible to open: " + dobStr + " over 24.");
                    return;
                }
                acctType = AccountType.COLLEGE_CHECKING;
                break;

            case "certificate":
                if (st.countTokens() < 2) {
                    System.out.println("Missing data tokens for opening an account.");
                    return;
                }
                try {
                    term = Integer.parseInt(st.nextToken());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid term for Certificate Deposit.");
                    return;
                }
                if (!(term == 3 || term == 6 || term == 9 || term == 12)) {
                    System.out.println(term + " is not a valid term.");
                    return;
                }
                String openDateStr = st.nextToken();
                openDate = new Date(openDateStr);
                if (!openDate.isValid()) {
                    System.out.println("Invalid open date: " + openDateStr);
                    return;
                }
                if (deposit < 1000) {
                    System.out.println("Minimum of $1,000 to open a Certificate Deposit account.");
                    return;
                }
                acctType = AccountType.CD;
                break;

            default:
                System.out.println(typeStr + " - invalid account type.");
                return;
        }

        Branch branch = parseBranch(branchStr);
        if (branch == null) {
            System.out.println(branchStr.toLowerCase() + " - invalid branch.");
            return;
        }

        //Checking if account already exists
        boolean isDuplicate = false;

        for (Account acc : db) {
            if (acc.getHolder().equals(profile) && acc.getAccountNumber().getType() == acctType) {
                // Special handling for Certificate Deposit (CD) accounts
                if (acctType == AccountType.CD) {
                    CertificateDeposit existingCD = (CertificateDeposit) acc;
                    if (existingCD.getTerm() == term) {  // Check if a CD with the same term exists
                        System.out.println(profile.getFname() + " already has a CD account with term " + term + " months.");
                        return;
                    }
                } else {
                    isDuplicate = true;
                    break;
                }
            }
        }

        if (isDuplicate) {
            System.out.println(profile.getFname() + " " + profile.getLname() + " already has a " + acctType + " account.");
            return;
        }



        //Creating accounts after verifying there are no duplicates
        AccountNumber accountNum = new AccountNumber(branch, acctType);
        Account newAccount = null;
        switch (acctType) {
            case CHECKING:
                newAccount = new Checking(accountNum, profile, deposit);
                break;

            case SAVINGS:
                newAccount = new Savings(accountNum, profile, deposit);
                break;

            case MONEY_MARKET:
                newAccount = new MoneyMarket(accountNum, profile, deposit);
                break;

            case COLLEGE_CHECKING:
                newAccount = new CollegeChecking(accountNum, profile, deposit, campus);
                break;

            case CD:
                newAccount = new CertificateDeposit(accountNum, profile, deposit, term, openDate);
                break;

            default:
                System.out.println("Unexpected error: account type not recognized.");
                return;
        }

        // Finally, add the account to the database
        db.add(newAccount);
        System.out.println(acctType.name() + " account " + newAccount.getAccountNumber() + " has been opened.");
    }


    /**
     * Processes the request to close an account.
     * @param st Tokenized input that contains account details.
     */
    private void processClose(StringTokenizer st) {
        if (!st.hasMoreTokens()) {
            System.out.println("Missing data for closing an account.");
            return;
        }

        String closingDateStr = st.nextToken();
        Date closingDate = new Date(closingDateStr);
        if (!closingDate.isValid()) {
            System.out.println("Invalid closing date.");
            return;
        }

        if (!st.hasMoreTokens()) {
            System.out.println("Missing data for closing an account.");
            return;
        }

        String token = st.nextToken();

        if (token.matches("\\d{9}")) {  // Case 1: Closing by account number
            AccountNumber acctNum = buildAccountNumberFromString(token);
            if (acctNum == null) {
                System.out.println("Invalid account number.");
                return;
            }

            Account acct = null;
            for (int i = 0; i < db.size(); i++) {
                if (db.get(i).getAccountNumber().equals(acctNum)) {
                    acct = db.get(i);
                    break;
                }
            }

            if (acct == null) {
                System.out.println("Account not found.");
                return;
            }

            System.out.println("Closing account " + acct.getAccountNumber());
            System.out.println("--interest earned: " + acct.fee());

            if(!findAccountByNumber(acct.getAccountNumber())){
                db.remove(acct);
                System.out.println(acct.getAccountNumber() + " has been closed.");
            } else{
                System.out.println(acctNum + " account does not exist.");
            }


        } else {
            String fname = token;

            if (!st.hasMoreTokens()) {
                System.out.println("Missing data for closing an account.");
                return;
            }
            String lname = st.nextToken();

            if (!st.hasMoreTokens()) {
                System.out.println("Missing data for closing an account.");
                return;
            }
            String dobStr = st.nextToken();
            Date dob = new Date(dobStr);
            if (!dob.isValid()) {
                System.out.println("Invalid DOB.");
                return;
            }

            Profile profileHolder = new Profile(fname, lname, dob);
            boolean closedAny = false;

            System.out.println("Closing accounts for " + profileHolder);

            for (int i = 0; i < db.size(); ) {
                Account acct = db.get(i);
                if (acct.getHolder().equals(profileHolder)) {
                    db.remove(acct, closingDate);  // Removing an account while iterating
                    closedAny = true;
                } else {
                    i++;  // Only increment when not removing to avoid skipping elements
                }
            }


            if (closedAny) {
                System.out.println("All accounts for " + profileHolder + " are closed and moved to archive.");
            } else {
                System.out.println(profileHolder + " does not have any accounts in the database.");
            }
        }
    }

    /**
     * Processes a deposit transaction.
     * @param st Tokenized input containing account number and deposit amount.
     */
    private void processDeposit(StringTokenizer st) {
        if (st.countTokens() < 2) {
            System.out.println("Missing data tokens for the deposit.");
            return;
        }

        String acctNumStr = st.nextToken();
        String amountStr = st.nextToken();
        double amount;

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            System.out.println("For input string: \"" + amountStr + "\" - not a valid amount.");
            return;
        }

        if (amount <= 0) {
            System.out.println(amount + " - deposit amount cannot be 0 or negative.");
            return;
        }


        DecimalFormat df = new DecimalFormat("#,##0.00");
        boolean success = db.deposit(acctNumStr, amount);
        if (success) {
            System.out.printf("$%s deposited to %s%n", df.format(amount), acctNumStr);
        } else {
            System.out.println(acctNumStr + " does not exist.");
        }
    }

    /**
     * Processes a withdrawal transaction.
     * @param st Tokenized input containing account number and withdrawal amount.
     */
    private void processWithdraw(StringTokenizer st) {
        if (st.countTokens() < 2) {
            System.out.println("Missing data tokens for the withdrawal.");
            return;
        }

        String acctNumStr = st.nextToken();
        String amountStr = st.nextToken();
        double amount;

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            System.out.println("For input string: \"" + amountStr + "\" - not a valid amount.");
            return;
        }

        if (amount <= 0) {
            System.out.println(amount + " withdrawal amount cannot be 0 or negative.");
            return;
        }

        AccountNumber acctNum = db.buildAccountNumberFromString(acctNumStr);
        if (acctNum == null) {
            System.out.println(acctNumStr + " does not exist.");
            return;
        }

        Account account = null;
        for (Account acct : db) {
            if (acct.getAccountNumber().equals(acctNum)) {
                account = acct;
                break;
            }
        }

        if (account == null) {
            System.out.println(acctNumStr + " does not exist.");
            return;
        }

        double currentBalance = account.getBalance();

        DecimalFormat df = new DecimalFormat("#,##0.00");
        if (currentBalance < amount) {
            System.out.printf("%s balance below $2,000 - withdrawing $%s - insufficient funds.%n", acctNumStr, df.format(amount));
            return;
        }

        // Check if balance will fall below $2,000 after withdrawal
        boolean isBelow2000 = (currentBalance - amount < 2000);

        // Perform withdrawal (no boolean return, so we just update balance)
        account.withdraw(amount);

        if (isBelow2000) {
            System.out.printf("%s balance below $2,000 - $%s withdrawn from %s%n", acctNumStr, df.format(amount), acctNumStr);
        } else {
            System.out.printf("$%s withdrawn from %s%n", df.format(amount), acctNumStr);
        }
    }

    /**
     * Parses a branch from a string input.
     * @param cityStr The branch city as a string.
     * @return returns the corresponding branch enum value or null if invalid.
     */
    private Branch parseBranch(String cityStr) {
        for (Branch b : Branch.values()) {
            if (b.name().equalsIgnoreCase(cityStr)) { // Compare against the branch city name
                return b;
            }
        }
        return null;
    }

    /**
     * Validation check to see if DOB is a future date.
     * @param dob Date of birth given to check
     * @return returns true if DOB is a future date, otherwise false.
     */
    public boolean isFutureDate(Date dob) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = LocalDate.of(dob.getYear(), dob.getMonth(), dob.getDay());
        return birthDate.isAfter(currentDate);
    }

    /**
     * Validation check to see if an individual is at least 18.
     * @param dob Date of birth given to check
     * @return returns true if individual is at least 18, otherwise false.
     */
    private boolean isAgeAtLeast18(Date dob) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = LocalDate.of(dob.getYear(), dob.getMonth(), dob.getDay());


        int age = currentDate.getYear() - birthDate.getYear();
        if(currentDate.getMonthValue() < birthDate.getMonthValue() ||
                (currentDate.getMonthValue() == birthDate.getMonthValue() && currentDate.getDayOfMonth() < birthDate.getDayOfMonth())){
            age --;
        }

        return age >= 18;
    }

    /**
     * Validation check to see if an individual is eligible for a college checking account.
     * @param dob Date of birth given to check
     * @return returns true if individual is at below 24, otherwise false.
     */
    private boolean isEligibleForCollege(Date dob) {
        int currentYear = java.time.Year.now().getValue();
        int birthYear = dob.getYear();
        int age = currentYear - birthYear;

        return age < 24;
    }



    /**
     * Builds an account number from a string representation
     * @param numStr Account number as a string
     * @return returns account number object if valid, otherwise null
     */
    private AccountNumber buildAccountNumberFromString(String numStr) {
        if (numStr.length() != 9)
            return null;
        String branchCode = numStr.substring(0, 3);
        String typeCode = numStr.substring(3, 5);
        String serial = numStr.substring(5);
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
     * Searches for account in database using account number
     *
     * @param acctNum account number to check for
     * @return returns account associated with account number if found, otherwise returns null
     */
    private boolean findAccountByNumber(AccountNumber acctNum) {
        for (int i = 0; i < db.size(); i++) {
            Account account = db.get(i);
            if (account != null && account.getAccountNumber().equals(acctNum)) {
                return true;
            }
        }
        return false;
    }

}
