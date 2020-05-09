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

			CallableStatement callableStatement = connection.prepareCall("{call BooksCount(?)}");
			callableStatement.registerOutParameter(1, Types.INTEGER);
			callableStatement.execute();
			System.out.println(callableStatement.getInt(1));
			System.out.println("________________");

			CallableStatement callableStatement1 = connection.prepareCall("{call getBooks(?)}");
			callableStatement1.setInt(1, 1);
			if (callableStatement1.execute()){
				ResultSet resultSet = callableStatement1.getResultSet();
				while (resultSet.next()){
					System.out.println(resultSet.getInt("id"));
					System.out.println(resultSet.getString("name"));
				}
			}

			System.out.println("_______________________");

			CallableStatement callableStatement2 = connection.prepareCall("{call getCount()}");
			boolean hasResult = callableStatement2.execute();
			while (hasResult){
				ResultSet resultSet = callableStatement2.getResultSet();
				while (resultSet.next()){
					System.out.println(resultSet.getInt(1));
				}
				hasResult = callableStatement2.getMoreResults();
			}

   		 }
    }
}
