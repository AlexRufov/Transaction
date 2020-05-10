package com.company.transactionIsolationLevel;

import java.io.IOException;
import java.sql.*;

public class PhantomRead {
    static String connectionURL = "jdbc:mysql://localhost:3306/mysql?useSSL=false";
    static String name = "root";
    static String password = "qwer";
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        try (Connection connection = DriverManager.getConnection(connectionURL, name, password);
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            ResultSet resultSet = statement.executeQuery("select count(*) from books");
            while (resultSet.next()){
                System.out.println(resultSet.getInt(1));
            }
            new OtherTransaction().start();
            Thread.sleep(2000);
            ResultSet resultSet2 = statement.executeQuery("select count(*) from books");
            while (resultSet2.next()){
                System.out.println(resultSet2.getInt(1));
            }

        }
    }

    static class OtherTransaction extends Thread{
        @Override
        public void run() {
            try (Connection connection = DriverManager.getConnection(connectionURL, name, password);
                 Statement statement = connection.createStatement()) {
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                statement.executeUpdate("insert into books (name) values ('Other book')");
                connection.commit();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
