# bank-management-fullstack
# RU Banking Management System Application
This full-stack application features a GUI and backend logic to simulate a comprehensive bank account management system. It enables users to manage account holders and perform core banking operations such as opening and closing accounts, as well as handling deposits and withdrawals across three distinct account types: checking, regular savings, and money market savings. When an account is created, a unique account number is generated using the branch code, account type, and a randomized identifier. The system includes a set of defined command inputs to execute each transaction, allowing for efficient and interactive management of banking operations.
### Key Features
- Open accounts (with validation based on age, deposit amount, and account type)
- Close accounts or move them to an archive
- Deposit or withdraw funds (with checks for minimum balances and downgrades)
- Print account summaries by various orderings (by holder, type, branch, etc.)
- Maintain separate structures for active and closed accounts
### Project Structure
```bash
ru-bank-project3/
├── src/
│   ├── account/           # Account, AccountNumber, AccountType, Branch, Archive, AccountDatabase
│   ├── user/              # Profile, Date
│   ├── ui/                # TransactionManager.java (main processor)
│   └── RunProject3.java   # Driver class
├── test/                  # Testbed main() methods for Date and Profile
├── spec/                  # Test specification document
├── doc/                   # Javadoc folder (generated)
├── gui/                   # GUI files for user interface (optional enhancement)
