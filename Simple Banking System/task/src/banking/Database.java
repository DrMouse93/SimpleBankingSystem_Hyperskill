package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;


public class Database {
    private final SQLiteDataSource dataSource = new SQLiteDataSource();
    private Connection connection;


    public Database(String url) {
        dataSource.setUrl("jdbc:sqlite:" + url);
    }

    public void connectionToDB() {
        try {
            connection = dataSource.getConnection();

            try (Statement statement = connection.createStatement();) {
                //statement.executeUpdate("DROP TABLE card;");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0);");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addCardToDB(Account account) {
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate("INSERT INTO card (number, pin) VALUES " +
                    "('" + account.getCardNumber() + "' , '" + account.getCardPIN() + "')");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Account findCard(String cardNumber, String cardPIN) {
        try (Statement statement = connection.createStatement();
                ResultSet foundCard = statement.executeQuery("SELECT * FROM card " +
                        "WHERE number LIKE '" + cardNumber + "' AND pin LIKE '" + cardPIN + "'")) {
            while (foundCard.next()) {
                int balance = foundCard.getInt("balance");
                return new Account(cardNumber, cardPIN, balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Account findCard(String cardNumber) {
        try (Statement statement = connection.createStatement();
             ResultSet foundCard = statement.executeQuery("SELECT * FROM card " +
                     "WHERE number = '" + cardNumber + "'")) {
            while (foundCard.next()) {
                int balance = foundCard.getInt("balance");
                return new Account(cardNumber, foundCard.getString("pin"), balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public boolean updateBalanceCard(Account account) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("UPDATE card " +
                    "SET balance = " + account.getBalance() +
                    " WHERE number = '" + account.getCardNumber() + "'");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean dropCard(Account account) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM card " +
                    "WHERE number = '" + account.getCardNumber() + "'");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean transfer(Account sender, Account receiver) {
        try {
            connection.setAutoCommit(false);

            updateBalanceCard(sender);
            updateBalanceCard(receiver);

            connection.commit();
            connection.setAutoCommit(true);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnectFromDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
