package com.company;

import java.io.IOException;
import java.sql.*;

public class SQLBatch {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        String connectionURL = "jdbc:mysql://localhost:3306/mysql?useSSL=false";
        String name = "root";
        String password = "qwer";
        Class.forName("com.mysql.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection(connectionURL, name, password);
            Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.addBatch("drop table IF EXISTS books");
            statement.addBatch("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR (30) NOT NULL, PRIMARY KEY (id))");
            statement.addBatch("insert into books (name) values ('Dark Tower')");
            statement.addBatch("insert into books (name) values ('Lord of the rings')");
            statement.addBatch("insert into books (name) values ('Harry Potter')");
            if (statement.executeBatch().length == 5){
                connection.commit();
            }
            else {
                connection.rollback();
            }
        }
    }
}
