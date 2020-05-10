package com.company;

import javax.imageio.ImageIO;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

public class Main {
	static String connectionURL = "jdbc:mysql://localhost:3306/mysql?useSSL=false";
	static String name = "root";
	static String password = "qwer";

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
		ResultSet resultSet = getData();
		while (resultSet.next()){
			System.out.println(resultSet.getInt("id"));
			System.out.println(resultSet.getString("name"));
		}
		CachedRowSet cachedRowSet = (CachedRowSet) resultSet;
		cachedRowSet.setUrl(connectionURL);
		cachedRowSet.setUsername(name);
		cachedRowSet.setPassword(password);
//		cachedRowSet.setCommand("select * from books where id = ?");
//		cachedRowSet.setInt(1,1);
//		cachedRowSet.setPageSize(20);
//		cachedRowSet.execute();
//		do {
//			while (cachedRowSet.next()){
//				System.out.println(cachedRowSet.getInt("id"));
//				System.out.println(cachedRowSet.getString("name"));
//			}
//		} while (cachedRowSet.nextPage());

		CachedRowSet cachedRowSet1 = (CachedRowSet) resultSet;
		cachedRowSet1.setTableName("books");
		cachedRowSet1.absolute(1);
		cachedRowSet1.deleteRow();
		cachedRowSet1.beforeFirst();
		while (cachedRowSet1.next()){
			System.out.println(cachedRowSet1.getInt("id"));
			System.out.println(cachedRowSet1.getString("name"));
		}
		cachedRowSet1.acceptChanges(DriverManager.getConnection(connectionURL, name, password));

    }

	static ResultSet getData() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		try(Connection connection = DriverManager.getConnection(connectionURL, name, password);
			Statement statement = connection.createStatement()) {
			statement.execute("drop table IF EXISTS books");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR (30) NOT NULL, PRIMARY KEY (id))");
			statement.executeUpdate("insert into books (name) values ('Dark Tower')");
			statement.executeUpdate("insert into books (name) values ('Lord of the rings')");
			statement.executeUpdate("insert into books (name) values ('Harry Potter')");

			RowSetFactory factory = RowSetProvider.newFactory();
			CachedRowSet cachedRowSet = factory.createCachedRowSet();

			Statement statement1 = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = statement1.executeQuery("select * from books");
			cachedRowSet.populate(resultSet);
			return cachedRowSet;
		}
	}
}


