package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
		String connectionURL = "jdbc:mysql://localhost:3306/mysql?useSSL=false";
    	String name = "root";
		String password = "qwer";

		Class.forName("com.mysql.jdbc.Driver");
		try(Connection connection = DriverManager.getConnection(connectionURL, name, password);
		Statement statement = connection.createStatement()) {
			statement.execute("drop table IF EXISTS books");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR (30) NOT NULL, dt DATE, PRIMARY KEY (id))");

//			PreparedStatement preparedStatement = connection.prepareStatement("insert into books (name, dt) VALUES ('today', ?)");
//			preparedStatement.setDate(1, new Date(1589020109190l));
//			preparedStatement.execute();
//			System.out.println(preparedStatement);

			statement.executeUpdate("insert into books (name, dt) VALUES ('today', '2020-05-09')");
			ResultSet resultSet = statement.executeQuery("select * from books");
			while (resultSet.next()){
				System.out.println(resultSet.getDate("dt"));
			}
   		 }
    }
}
