package banking;

import java.util.Scanner;

public class ActionWithAccount {

    private String url;
    private final Database database;

    public ActionWithAccount(String url) {
        this.url = url;
        database = new Database(url);
    }



    public void createdAccount() {
        Account account = new Account();
        account.createNewAccount();

        database.addCardToDB(account);

        System.out.println("Your card has been created\n" +
                "Your card number:");
        System.out.println(account.getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(account.getCardPIN());
    }

    public Account logIntoAccount() {
        Scanner scanner = new Scanner(System.in);
        String cardNumber;
        String cardPin;

        System.out.println("Enter your card number:");
        cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        cardPin = scanner.nextLine();

        return database.findCard(cardNumber, cardPin);
    }

    public void showBalance(Account account) {
        System.out.println("Balance: " + account.getBalance());
    }

    public void addIncome(Account account, String income) {
        try {
            int intIncome = Integer.parseInt(income);
            account.setBalance(account.getBalance() + intIncome);
            database.updateBalanceCard(account);
            System.out.println("Income was added!");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void closeAccount(Account account) {
        database.dropCard(account);
    }

    public void transfer(Account account, Scanner scanner) {
        System.out.println("Enter card number:");
        String cardNumberForTransfer = scanner.nextLine();
        Account recipientCard;

        if (account.getCardNumber().equals(cardNumberForTransfer)) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }

        if (account.calculationChecksum(cardNumberForTransfer
                .substring(0, cardNumberForTransfer.length() - 1)) != Character.getNumericValue(cardNumberForTransfer
                .charAt(cardNumberForTransfer.length() - 1))) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return;
        }

        recipientCard = database.findCard(cardNumberForTransfer);
        if (recipientCard == null) {
            System.out.println("Such a card does not exist");
            return;
        }

        System.out.println("Enter how much money you want to transfer:");
        try {
            int sumTransfer = Integer.parseInt(scanner.nextLine());
            if (account.getBalance() < sumTransfer) {
                System.out.println("Not enough money!");
                return;
            }

            account.setBalance(account.getBalance() - sumTransfer);
            recipientCard.setBalance(recipientCard.getBalance() + sumTransfer);

            boolean checkTransfer = database.transfer(account, recipientCard);

            if (!checkTransfer) {
                account.setBalance(account.getBalance() + sumTransfer);
                recipientCard.setBalance(recipientCard.getBalance() - sumTransfer);
            }

            System.out.println("Success!");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public Database getDatabase() {
        return database;
    }
}
