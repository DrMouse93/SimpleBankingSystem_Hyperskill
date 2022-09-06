package banking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ActionWithAccount action = new ActionWithAccount(args[1]);
        action.getDatabase().connectionToDB();
        int enterNumber = 0;
        do {
            System.out.println("\n1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit\n");
            try {
                enterNumber = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                enterNumber = 99;
            }

            switch (enterNumber) {
                case 1:
                    action.createdAccount();
                    break;
                case 2:
                    Account account = action.logIntoAccount();
                    if (account == null) {
                        System.out.println("Wrong card number or PIN!");
                    } else {
                        System.out.println("You have successfully logged in!");

                        do {
                            System.out.println("\n1. Balance\n" +
                                    "2. Add income\n" +
                                    "3. Do transfer\n" +
                                    "4. Close account\n" +
                                    "5. Log out\n" +
                                    "0. Exit\n");
                            try {
                                enterNumber = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                enterNumber = 99;
                            }

                            switch (enterNumber) {
                                case 1:
                                    action.showBalance(account);
                                    break;
                                case 2:
                                    System.out.println("Enter income:");
                                    action.addIncome(account, scanner.nextLine());
                                    break;
                                case 3:
                                    System.out.println("Transfer");
                                    action.transfer(account, scanner);
                                    break;
                                case 4:
                                    action.closeAccount(account);
                                    System.out.println("The account has been closed!");
                                    break;
                                case 5:
                                    System.out.println("You have successfully logged out!");
                                    break;
                                case 0:
                                    System.out.println("Bye!");
                                    break;
                                default:
                                    System.out.println("Incorrect enter. Try again!");
                                    break;
                            }
                            if (enterNumber == 0 || enterNumber == 4 || enterNumber == 5) break;
                        } while (true);
                    }
                    break;
                case 0:
                    System.out.println("Bye!");
                    break;
                default:
                    System.out.println("Incorrect enter. Try again!");
                    break;
            }
        } while (enterNumber != 0);
        action.getDatabase().disconnectFromDB();
    }
}