package com.example.project3.sourcefiles;

import com.example.project3.util.Date;

/**
 * Represents an archive of closed accounts using a linked list structure.
 * Accounts are stored in a singly linked list, where new accounts are added to the front.
 * This class allows adding and printing archived accounts.
 * @author Yakelin Melendez-Gonzalez, Nivedha Sundar
 */
public class Archive {
    private AccountNode first;

    private class AccountNode {
        Account account;
        Date close;
        AccountNode next;

        /**
         * Constructs an AccountNode containing the specified account.
         * @param account the account to be stored in the node.
         * @param closeDate the date of closure
         */
        public AccountNode(Account account, Date closeDate) {
            this.account = account;
            this.close = closeDate;
            this.next = null;
        }
    }

    /**
     * Adds a closed account to the archive.
     * @param account the account to be archived
     * @param close the date of closure
     */
    public void add(Account account, Date close) {
        AccountNode newNode = new AccountNode(account, close);
        newNode.next = first;
        first = newNode;
    }

    /**
     * Returns a string representation of all archived accounts along with their closure dates.
     * @return a formatted string of archived accounts
     */
    public String print() {
        if (first == null) {
            return "No closed accounts.";
        }

        StringBuilder sb = new StringBuilder();
        AccountNode current = first;

        while (current != null) {
            sb.append(current.account)
                    .append(" Closed[")
                    .append(current.close)
                    .append("]\n");
            current = current.next;
        }

        return sb.toString().trim();  // Remove trailing newline
    }
}
