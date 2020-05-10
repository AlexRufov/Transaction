package com.company.transactionIsolationLevel;

import java.io.IOException;
import java.sql.*;

public class DirtyRead {
    static String connectionURL = "jdbc:mysql://localhost:3306/mysql?useSSL=false";
    static String name = "root";
    static String password = "qwer";
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        try (Connection connection = DriverManager.getConnection(connectionURL, name, password);
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            statement.execute("update books set name = 'other book' where id = 1");
            new OtherTransaction().start();
            Thread.sleep(2000);
            connection.rollback();
        }
    }

    static class OtherTransaction extends Thread{
        @Override
        public void run() {
            try (Connection connection = DriverManager.getConnection(connectionURL, name, password);
                 Statement statement = connection.createStatement()) {
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                ResultSet resultSet = statement.executeQuery("select * from books");
                while (resultSet.next()){
                    System.out.println(resultSet.getString("name"));
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}