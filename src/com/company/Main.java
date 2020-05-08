package com.company;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
		String connectionURL = "jdbc:mysql://localhost:3306/mysql?useSSL=false";
    	String name = "root";
		String password = "qwer";

		Class.forName("com.mysql.jdbc.Driver");
		try(Connection connection = DriverManager.getConnection(connectionURL, name, password);
		Statement statement = connection.createStatement()) {
			statement.execute("drop table Users");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS Users (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR (30) NOT NULL, password CHAR (30) NOT NULL, PRIMARY KEY (id))");
			statement.executeUpdate("INSERT INTO users (name, password) VALUES ('Alex', '1234')");
			statement.executeUpdate("INSERT INTO users SET name = 'Don', password = '4321'");

//			String userID = "1";
			String userID = "1' or 1 = '1";
//			ResultSet resultSet = statement.executeQuery("select * from users where id = '" + userID + "'");
//			while (resultSet.next()){
//				System.out.println("userName: " + resultSet.getString("name"));
//				System.out.println("userPassword: " + resultSet.getString("password"));
//			}

			PreparedStatement preparedStatement = connection.prepareStatement("select * from users where id = ?");
			preparedStatement.setString(1, userID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()){
				System.out.println("userName: " + resultSet.getString("name"));
				System.out.println("userPassword: " + resultSet.getString("password"));
			}
   		 }
    }
}
