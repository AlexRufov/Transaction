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
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR (30) NOT NULL, PRIMARY KEY (id))");
			statement.executeUpdate("insert into books (name) values ('Dark Tower')");
			statement.executeUpdate("insert into books (name) values ('Lord of the rings')");
			statement.executeUpdate("insert into books (name) values ('Harry Potter')");

			Statement statement1 = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = statement1.executeQuery("select * from books");
			if (resultSet.next())
				System.out.println(resultSet.getString("name"));
			if (resultSet.next())
				System.out.println(resultSet.getString("name"));
			if (resultSet.previous())
				System.out.println(resultSet.getString("name"));
			if (resultSet.relative(2))
				System.out.println(resultSet.getString("name"));
			if (resultSet.relative(-2))
				System.out.println(resultSet.getString("name"));
			if (resultSet.absolute(2))
				System.out.println(resultSet.getString("name"));
			if (resultSet.first())
				System.out.println(resultSet.getString("name"));
			if (resultSet.last())
				System.out.println(resultSet.getString("name"));

			System.out.println("_____________________");


			Statement statement2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet resultSet2 = statement2.executeQuery("select * from books");
			while (resultSet2.next()){
				System.out.println(resultSet2.getInt("id"));
				System.out.println(resultSet2.getString("name"));
			}

			System.out.println("________________________");

			resultSet2.last();
			resultSet2.updateString("name", "Fantastic beasts");
			resultSet2.updateRow();

			resultSet2.moveToInsertRow();
			resultSet2.updateString("name", "Hobbit");
			resultSet2.insertRow();

			resultSet2.absolute(2);
			resultSet2.deleteRow();

			resultSet2.beforeFirst();
			while (resultSet2.next()){
				System.out.println(resultSet2.getInt("id"));
				System.out.println(resultSet2.getString("name"));
			}
   		 }
    }
}
