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
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR (30) NOT NULL, img LONGBLOB, PRIMARY KEY (id))");

			BufferedImage image = ImageIO.read(new File("Space.jpg"));
			Blob blob = connection.createBlob();
			OutputStream outputStream = blob.setBinaryStream(1);
			ImageIO.write(image, "jpg", outputStream);
			outputStream.close();



			PreparedStatement preparedStatement = connection.prepareStatement("insert into books (name, img) values (?,?)");
			preparedStatement.setString(1, "Space");
			preparedStatement.setBlob(2, blob);
			preparedStatement.execute();
			preparedStatement.close();

			ResultSet resultSet = statement.executeQuery("select * from books");
			while (resultSet.next()){
				Blob blob1 = resultSet.getBlob("img");
				BufferedImage image1 = ImageIO.read(blob1.getBinaryStream());
				File outPutFile = new File("saved.png");
				ImageIO.write(image1, "png", outPutFile);
			}
			resultSet.close();

   		 }
    }
}
