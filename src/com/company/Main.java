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

			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[] {"Table"});
			while (resultSet.next()){
				System.out.println(resultSet.getString(3));
			}
			System.out.println("________________");

			ResultSet resultSet1 = statement.executeQuery("select * from books");
			ResultSetMetaData resultSetMetaData = resultSet1.getMetaData();
			for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++){
				System.out.println(resultSetMetaData.getCatalogName(i));
				System.out.println(resultSetMetaData.getColumnType(i));
			}

   		 }
    }
}
