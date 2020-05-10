package com.company;

import java.io.IOException;
import java.sql.*;

public class Transactions {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        String connectionURL = "jdbc:mysql://localhost:3306/mysql?useSSL=false";
        String name = "root";
        String password = "qwer";
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(connectionURL, name, password);
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            statement.execute("drop table IF EXISTS books");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR (30) NOT NULL, PRIMARY KEY (id))");
            statement.executeUpdate("insert into books (name) values ('Dark Tower')");
            //Savepoint savepoint = connection.setSavepoint();
            statement.executeUpdate("insert into books (name) values ('Lord of the rings')");
            statement.executeUpdate("insert into books (name) values ('Harry Potter')");

            //connection.rollback(savepoint);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
    }
}

