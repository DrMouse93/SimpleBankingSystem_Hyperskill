package banking;

import java.util.Arrays;
import java.util.Random;

public class Account {

    private final StringBuilder cardNumber = new StringBuilder();
    private final StringBuilder cardPIN = new StringBuilder();

    private int balance = 0;

    public int sum;

    public Account() {}

    public Account(String cardNumber, String cardPin, int balance) {
        this.cardNumber.append(cardNumber);
        this.cardPIN.append(cardPin);
        this.balance = balance;
    }

    public void createNewAccount() {
        Random random = new Random();

        //generated cardNumber
        cardNumber.append(400000);
        for (int i = 0; i < 9; i++) {
            cardNumber.append(random.nextInt(10));
        }

        //generated checksum
        int checksum = calculationChecksum(cardNumber.toString());
        cardNumber.append(checksum);

        //generated cardPIN
        for (int i = 0; i < 4; i++) {
            cardPIN.append(random.nextInt(10));
        }
    }

    public int calculationChecksum(String cardNumber) {
        int[] numbers = cardNumber.chars().map(Character::getNumericValue)
                .toArray();

        for (int i = 0; i < numbers.length; i = i + 2) {
            numbers[i] = numbers[i] * 2;
        }

        numbers = Arrays.stream(numbers).map(x -> x > 9 ? x - 9 : x).toArray();

        sum = Arrays.stream(numbers).sum();

        return sum % 10 == 0 ? 0 : Math.abs(sum % 10 - 10);
    }

    public String getCardNumber() {
        return cardNumber.toString();
    }

    public String getCardPIN() {
        return cardPIN.toString();
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
