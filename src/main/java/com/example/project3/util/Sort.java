package com.example.project3.util;

import com.example.project3.sourcefiles.Account;
import com.example.project3.sourcefiles.AccountDatabase;

public class Sort {
    /**
     * Sorts the list of accounts based on the specified key.
     * 'B' - Sort by Branch (County, then City)
     * 'H' - Sort by Holder (Name, then Account Number)
     * 'T' - Sort by Type (Type, then Account Number)
     *
     * @param list the list of accounts to sort
     * @param key  the sorting criteria
     */
    public static void account(AccountDatabase list, char key) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                boolean swap = false;

                switch (key) {
                    case 'B': // Sort by Branch (County, then City)
                        String county1 = list.get(min).getAccountNumber().getBranch().getCounty();
                        String city1 = list.get(min).getAccountNumber().getBranch().name();
                        String county2 = list.get(j).getAccountNumber().getBranch().getCounty();
                        String city2 = list.get(j).getAccountNumber().getBranch().name();

                        if (county1.compareTo(county2) > 0 ||
                                (county1.equals(county2) && city1.compareTo(city2) > 0)) {
                            swap = true;
                        }
                        break;

                    case 'H': // Sort by Holder (Name, then Account Number)
                        int holderComparison = list.get(min).getHolder().compareTo(list.get(j).getHolder());
                        if (holderComparison > 0 ||
                                (holderComparison == 0 &&
                                        list.get(min).getAccountNumber().compareTo(list.get(j).getAccountNumber()) > 0)) {
                            swap = true;
                        }
                        break;

                    case 'T': // Sort by Type (Type, then Account Number)
                        String type1 = list.get(min).getAccountNumber().getType().getCode();
                        String type2 = list.get(j).getAccountNumber().getType().getCode();

                        if (type1.compareTo(type2) > 0 ||
                                (type1.equals(type2) &&
                                        list.get(min).getAccountNumber().compareTo(list.get(j).getAccountNumber()) > 0)) {
                            swap = true;
                        }
                        break;
                }

                if (swap) {
                    min = j;
                }
            }

            if (min != i) {
                Account temp = list.get(i);
                list.set(i, list.get(min));
                list.set(min, temp);
            }
        }
    }
}
