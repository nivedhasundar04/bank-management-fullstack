package com.example.project3;

import com.example.project3.sourcefiles.*;
import com.example.project3.util.Date;
import com.example.project3.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Controller {
    private AccountDatabase db = new AccountDatabase();
    @FXML
    private TextField firstNameField, lastNameField, initialDepositField, accountNumberField, depositWithdrawAmt,
            closeAcctFName, closeAcctLName;

    @FXML
    private DatePicker dobPicker, closingDatePicker, dateOpenPicker, closingAcctsDOB;

    @FXML
    private ComboBox<String> branchComboBox, cdTermComboBox;

    @FXML
    private RadioButton checkingRadio, collegeCheckingRadio, savingsRadio, moneyMarketRadio, cdRadio,
                        nbRadio, newarkRadio, camdenRadio;

    @FXML
    private CheckBox loyalCustomerCheckBox;

    @FXML
    private Button openButton, clearButton, depositButton, withdrawButton, closeButton,
    loadActivitiesButton, loadAccountsButton;

    @FXML
    private ToggleGroup accountType;

    /**
     * Sets up the controller by adding listeners and filling the combo boxes.
     */
    @FXML
    public void initialize(){
        accountType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                if(newValue == checkingRadio){
                    checkingSelected();
                } else if(newValue == collegeCheckingRadio){
                    collegeSelected();
                } else if(newValue == savingsRadio){
                    savingsSelected();
                } else if(newValue == moneyMarketRadio){
                    moneyMarketSelected();
                } else if(newValue == cdRadio){
                    cdSelected();
                }
            }
        });

        branchComboBox.getItems().addAll(
                Branch.BRIDGEWATER.name(),
                Branch.WARREN.name(),
                Branch.PISCATAWAY.name(),
                Branch.EDISON.name(),
                Branch.PRINCETON.name()
        );
        cdTermComboBox.getItems().addAll("3","6","9", "12", "24");



    }

    /**
     * Correctly displays UI when a Checking account is chosen.
     */
    @FXML
    protected void checkingSelected(){
        disableCampus();
        loyalCustomerCheckBox.setDisable(true);
        cdTermComboBox.setDisable(true);
        dateOpenPicker.setDisable(true);

    }

    /**
     * Correctly displays UI when a Savings account is chosen.
     */
    @FXML
    public void savingsSelected(){
        disableCampus();
        loyalCustomerCheckBox.setDisable(false);
        cdTermComboBox.setDisable(true);
        dateOpenPicker.setDisable(true);

    }

    /**
     * Correctly displays UI when a college checking account is chosen.
     */
    @FXML
    public void collegeSelected(){
        enableCampus();
        loyalCustomerCheckBox.setDisable(true);
        cdTermComboBox.setDisable(true);
        dateOpenPicker.setDisable(true);

    }

    /**
     * Correctly displays UI when a Money Market account is chosen.
     */
    @FXML
    public void moneyMarketSelected(){
        disableCampus();
        loyalCustomerCheckBox.setDisable(false);
        cdTermComboBox.setDisable(true);
        dateOpenPicker.setDisable(true);

    }

    /**
     * Correctly displays UI when a Certificate Deposit account is chosen.
     */
    @FXML
    public void cdSelected(){
        disableCampus();
        loyalCustomerCheckBox.setDisable(true);
        cdTermComboBox.setDisable(false);
        dateOpenPicker.setDisable(false);

    }

    /**
     * Disables the campus radio buttons.
     */
    @FXML
    private void disableCampus(){
        nbRadio.setDisable(true);
        newarkRadio.setDisable(true);
        camdenRadio.setDisable(true);
    }

    /**
     * Enables the campus radio buttons.
     */
    @FXML
    public void enableCampus(){
        nbRadio.setDisable(false);
        newarkRadio.setDisable(false);
        camdenRadio.setDisable(false);

    }

    /**
     * Creates a new account using the provided input and chosen options.
     */
    @FXML
    public void openAccount() {
        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            if (firstName.length() > 20 || lastName.length() > 20) {
                showAlert("Error", "First and last name cannot exceed 20 characters.");
                return;
            }

            boolean isLoyal = false;
            Date dob = convertToDate(dobPicker);
            Date dateOpen = convertToDate(dateOpenPicker);

            double initialBalance = Double.parseDouble(initialDepositField.getText());
            if(initialBalance <= 0){
                showAlert("Error", "Initial balance cannot be 0 or negative.");
                return;
            }
            String branchStr = branchComboBox.getValue();
            Branch branch = parseBranch(branchStr);

            if (firstName.isEmpty() || lastName.isEmpty() || dob == null || branch == null) {
                showAlert("Error", "Please fill all required fields.");
                return;
            }

            if (!dob.isValid()) {
                showAlert("Error", "DOB invalid " + dob + " not a valid calendar date!");
                return;
            }

            if(!isAgeAtLeast18(dob)){
                showAlert("Error", "Must be at least 18 to open account.");
                return;
            }

            Profile profile = new Profile(firstName, lastName, dob);
            AccountType acctType = getSelectedAccountType();

            if (acctType == null) {
                showAlert("Error", "Please select an account type.");
                return;
            }

            int cdTerm = 0;
            if (acctType == AccountType.CD) {
                if (cdTermComboBox.getValue() != null) {
                    cdTerm = Integer.parseInt(cdTermComboBox.getValue());
                } else {
                    showAlert("Error", "Please select a term for the CD.");
                    return;
                }
            }

            // checking for duplicate accounts before creating account
            if (isDuplicateAccount(profile, acctType, cdTerm)) {
                showAlert("Error", firstName + " " + lastName + " already has a " + acctType + " account.");
                return;
            }

            AccountNumber accountNumber = new AccountNumber(branch, acctType);
            Account newAccount = null;

            if (checkingRadio.isSelected()) {
                newAccount = new Checking(accountNumber, profile, initialBalance);

            } else if (collegeCheckingRadio.isSelected()) {
                if(!isEligibleForCollege(dob)){
                    showAlert("Error", "Not eligible for college checking. Must be 24 years or younger.");
                    return;
                }
                Campus campus = null;
                if (nbRadio.isSelected()) {
                    campus = Campus.NEW_BRUNSWICK;
                } else if (newarkRadio.isSelected()) {
                    campus = Campus.NEWARK;
                } else if (camdenRadio.isSelected()) {
                    campus = Campus.CAMDEN;
                } else {
                    showAlert("Error", "Please select a campus.");
                    return;
                }
                newAccount = new CollegeChecking(accountNumber, profile, initialBalance, campus);

            } else if (savingsRadio.isSelected()) {
                isLoyal = loyalCustomerCheckBox.isSelected();
                Savings savingsAccount = new Savings(accountNumber, profile, initialBalance);
                savingsAccount.setLoyal(isLoyal);
                newAccount = savingsAccount;

            } else if (moneyMarketRadio.isSelected()) {
                if(initialBalance < 2000){
                    showAlert("Error", "Minimum of $2,000 required to open a Money Market Account.");
                    return;
                }
                isLoyal = loyalCustomerCheckBox.isSelected();
                MoneyMarket moneyMarketAccount = new MoneyMarket(accountNumber, profile, initialBalance);
                moneyMarketAccount.setLoyal(isLoyal);
                newAccount = moneyMarketAccount;

            } else if (cdRadio.isSelected()) {
                if (dateOpen == null || cdTermComboBox.getValue() == null) {
                    showAlert("Error", "Please select a date and term for the CD.");
                    return;
                }

                if(initialBalance < 1000){
                    showAlert("Error", "Minimum of $1,000 required to open a Certificate Deposit account.");
                    return;
                }

                int term = Integer.parseInt(cdTermComboBox.getValue());

                if (!dateOpen.isValid()) {
                    showAlert("Error", "Invalid open date.");
                    return;
                }

                newAccount = new CertificateDeposit(accountNumber, profile, initialBalance, term, dateOpen);
            }

            if (newAccount != null) {
                db.add(newAccount);
                showAlert("Success", "Account opened successfully!");
                clearOpenFields();
            } else {
                showAlert("Error", "Failed to create account.");
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid amount format.");
        }
    }


    /**
     * Handles a deposit for the account matching the given number.
     */
    @FXML
    public void processDeposit() {
        try {
            String acctNum = accountNumberField.getText().trim();

            if (acctNum.isEmpty()) {
                showAlert("Error", "Please enter an account number.");
                return;
            }

            double amount = Double.parseDouble(depositWithdrawAmt.getText().trim());

            if (amount <= 0) {
                showAlert("Error", "Deposit amount cannot be 0 or negative.");
                return;
            }

            // Find account in the database
            Account account = null;
            for (Account acct : db) {
                if (acct.getAccountNumber().toString().equals(acctNum)) {
                    account = acct;
                    break;
                }
            }

            if (account == null) {
                showAlert("Error", "Account not found.");
                return;
            }

            // Perform deposit
            boolean success = db.deposit(acctNum, amount);
            if(!success){
                showAlert("Error", "Deposit of $" + amount + " unsuccessful.");
                return;
            }
            showAlert("Success", "Deposit of $" + amount + " completed successfully.");

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid deposit amount.");
        }
        clear2ndTabFields();
    }


    /**
     * Handles a withdrawal for the account matching the given number.
     */
    @FXML
    public void processWithdraw() {
        try {
            String acctNum = accountNumberField.getText().trim();

            if (acctNum.isEmpty()) {
                showAlert("Error", "Please enter an account number.");
                return;
            }

            double amount = Double.parseDouble(depositWithdrawAmt.getText().trim());

            if (amount <= 0) {
                showAlert("Error", "Withdrawal cannot be 0 or negative.");
                return;
            }

            // Find account in the database
            Account account = null;
            for (Account acct : db) {
                if (acct.getAccountNumber().toString().equals(acctNum)) {
                    account = acct;
                    break;
                }
            }

            if (account == null) {
                showAlert("Error", "Account not found.");
                return;
            }

            // Perform deposit
            boolean success = db.withdraw(acctNum, amount);
            if (!success) {
                showAlert("Error", "Insufficient funds.");
                return;
            }
            if(account.getAccountNumber().getType() == AccountType.MONEY_MARKET && account.getBalance() < 2000){
                showAlert("Success", acctNum + " Balance below $2,000 -- " + "Withdrawal of $" + amount + " completed successfully.");
            } else{
                showAlert("Success", "Withdrawal of $" + amount + " completed successfully.");
            }



        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid withdrawal amount.");
        }
        clear2ndTabFields();
    }

    /**
     * Closes the account with the given number and removes it from the database.
     */
    @FXML
    public void closeAccount() {
        try {
            // Get the account number from the input field
            String acctNum = accountNumberField.getText().trim();
            Date dateClosed = convertToDate(closingDatePicker);

            if (acctNum.isEmpty()) {
                showAlert("Error", "Please enter an account number.");
                return;
            }

            // Find the account in the database
            Account accountToClose = null;
            for (Account acct : db) {
                if (acct.getAccountNumber().toString().equals(acctNum)) {
                    accountToClose = acct;
                    break;
                }
            }

            if (accountToClose == null) {
                showAlert("Error", "Account does not exist.");
                return;
            }

            if(findAccountByNumber(accountToClose.getAccountNumber())){
                db.remove(accountToClose);
                showAlert("Success", "Account closed successfully on: " + dateClosed);
            } else{
                showAlert("Error", "Account does not exist.");
            }


        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
        }
        clear2ndTabFields();
    }

    /**
     * Closes all accounts matching the holder's info and archives them.
     */
    @FXML
    public void closeAllAccounts() {
        String firstName = closeAcctFName.getText().trim();
        String lastName = closeAcctLName.getText().trim();
        Date dob = convertToDate(closingAcctsDOB);
        Date closingDate = convertToDate(closingDatePicker);

        if (firstName.isEmpty() || lastName.isEmpty() || dob == null) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Parse and validate the date
        try {
            if (!dob.isValid()) {
                showAlert("Error", "Invalid date format. Use MM/DD/YYYY.");
                return;
            }
        } catch (Exception e) {
            showAlert("Error", "Invalid date format. Use MM/DD/YYYY.");
            return;
        }

        Profile profileHolder = new Profile(firstName, lastName, dob);
        // Use current date for closing
        boolean closedAny = false;
        StringBuilder result = new StringBuilder();

        // Iterate over the accounts and remove matching ones
        for (int i = 0; i < db.size(); ) {
            Account acct = db.get(i);

            if (acct.getHolder().equals(profileHolder)) {
                // Print details before closing
                result.append("Closed: ").append(acct).append("\n");

                // Remove and archive the account
                db.remove(acct, closingDate);

                closedAny = true;
            } else {
                i++;  // Only increment when no account is removed
            }
        }

        // Display the result
        if (closedAny) {
            showTextArea("Closed Accounts", result.toString());
        } else {
            showAlert("Information", "No accounts found for " + firstName + " " + lastName + " " + dob);
        }
        clear2ndTabFields();
    }


    /**
     * Opens a FileChooser to load accounts from a file.
     */
    @FXML
    private void loadAccountsFromFile() {
        File file = openFileChooser("Select Accounts File");
        if (file != null) {
            try {
                db.loadAccounts(file);
                showAlert("Success", "Accounts loaded successfully from: " + file.getName());
            } catch (IOException e) {
                showAlert("Error", "Failed to load accounts: " + e.getMessage());
            }
        }
    }

    /**
     * Opens a FileChooser to load activities from a file.
     */
    @FXML
    private void loadActivitiesFromFile() {
        File file = openFileChooser("Select Activities File");
        if (file != null) {
            try {
                StringBuilder result = new StringBuilder();

                // Process activities and accumulate the output
                List<String> activities = db.processActivities(file);  // Assuming it returns List<String>

                // Append the list as a string
                for (String activity : activities) {
                    result.append(activity).append("\n");
                }  // This should call the toString() method of your List class

                // Display the results in a TextArea
                if (!activities.isEmpty()) {
                    showTextArea("Activity Results", result.toString());
                } else {
                    showAlert("Information", "No activities processed.");
                }

            } catch (Exception e) {
                showAlert("Error", "Failed to process activities: " + e.getMessage());
            }
        }
    }

    /**
     * Shows accounts sorted by branch in a text area.
     */
    @FXML
    public void printByBranch() {
        String result = db.printByBranch();
        if (result.isEmpty()) {
            showAlert("Information", "No accounts found.");
        } else {
            showTextArea("Accounts by Branch", result);
        }
    }

    /**
     * Shows accounts sorted by type in a text area.
     */
    @FXML
    public void printByType() {
        String result = db.printByType();
        if (result.isEmpty()) {
            showAlert("Information", "No accounts found.");
        } else {
            showTextArea("Accounts by Type", result);
        }
    }

    /**
     * Shows accounts sorted by holder in a text area.
     */
    @FXML
    public void printByHolder() {
        String result = db.printByHolder();
        if (result.isEmpty()) {
            showAlert("Information", "No accounts found.");
        } else {
            showTextArea("Accounts by Holder", result);
        }
    }

    /**
     * Shows archived accounts in a text area.
     */
    @FXML
    public void printArchive() {
        String result = db.printArchive();
        if (result.isEmpty()) {
            showAlert("Information", "No accounts in archive.");
        } else {
            showTextArea("Archived Accounts", result);
        }
    }

    /**
     * Shows account statements in a text area.
     */
    @FXML
    public void printAccountStatements(){
        String result = db.printStatements();
        if (result.isEmpty()) {
            showAlert("Information", "No account statements available.");
        } else {
            showTextArea("Account Statements", result);
        }


    }

    /**
     * Converts a LocalDate from the date picker into our custom Date format.
     */
    private Date convertToDate(DatePicker datePicker) {
        try {
            if (datePicker.getValue() == null) {
                return null; // Handle case where no date is selected
            }
            LocalDate localDate = datePicker.getValue();
            // Assuming your custom Date class accepts date in the "MM/DD/YYYY" format
            return new Date(localDate.getMonthValue() + "/" + localDate.getDayOfMonth() + "/" + localDate.getYear());
        } catch (DateTimeParseException e) {
            showAlert("Error", "Invalid date format. Please select a valid date.");
            return null;
        }
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
     * Looks for duplicate accounts in the database based on profile, account type,
     * and CD term if needed.
     * @param profile The account holder's profile.
     * @param acctType The type of account.
     * @param cdTerm The CD term (only matters for CD accounts).
     * @return true if a duplicate is found; false if not.
     */
    private boolean isDuplicateAccount(Profile profile, AccountType acctType, int cdTerm) {
        for (Account acc : db) {  // Iterate over the db collection
            if (acc.getHolder().equals(profile) && acc.getAccountNumber().getType() == acctType) {

                // Handle CD accounts separately by term
                if (acctType == AccountType.CD && acc instanceof CertificateDeposit) {
                    CertificateDeposit existingCD = (CertificateDeposit) acc;
                    if (existingCD.getTerm() == cdTerm) {  // Check for matching CD term
                        return true;
                    }
                } else {
                    return true;  // Duplicate found
                }
            }
        }
        return false;  // No duplicates found
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
     * Gets the account type that's been picked in the UI.
     * @return the selected AccountType, or null if none is chosen.
     */
    private AccountType getSelectedAccountType() {
        if (checkingRadio.isSelected()) {
            return AccountType.CHECKING;
        } else if (collegeCheckingRadio.isSelected()) {
            return AccountType.COLLEGE_CHECKING;
        } else if (savingsRadio.isSelected()) {
            return AccountType.SAVINGS;
        } else if (moneyMarketRadio.isSelected()) {
            return AccountType.MONEY_MARKET;
        } else if (cdRadio.isSelected()) {
            return AccountType.CD;
        }
        return null;  // Return null if no account type is selected
    }


    /**
     * Opens a FileChooser and returns the selected file.
     * @param title The title of the FileChooser dialog
     * @return The selected file or null if no file was selected
     */
    private File openFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.csv")
        );

        Stage stage = (Stage) loadAccountsButton.getScene().getWindow();
        return fileChooser.showOpenDialog(stage);
    }

    /**
     * Displays an alert message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Shows some text in a text area inside an alert.
     * @param title The title of the alert.
     * @param content The text to show.
     */
    private void showTextArea(String title, String content) {
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    /**
     * Searches for account in database using account number
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


    @FXML
    /**
     * Clears all input fields.
     */
    private void clearOpenFields() {
        firstNameField.clear();
        lastNameField.clear();
        initialDepositField.clear();
        dobPicker.setValue(null);
        dateOpenPicker.setValue(null);
        branchComboBox.getSelectionModel().clearSelection();
        cdTermComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Clears out all the fields used for opening an account.
     */
    private void clear2ndTabFields() {
        accountNumberField.clear();
        closeAcctFName.clear();
        closeAcctLName.clear();
        depositWithdrawAmt.clear();
        closingAcctsDOB.setValue(null);
        closingDatePicker.setValue(null);
    }



}