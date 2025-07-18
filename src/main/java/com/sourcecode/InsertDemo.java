package com.sourcecode;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertDemo {
   public static void main(String[] args) {
	  Connection connection = ConnectionProvider.getConnection();
	  if(connection != null) {
		  try {
			  String insertQuery = "INSERT INTO firstab (sno, name) VALUES (1, 'John Doe')";
			  Statement statement = connection.createStatement();
			  statement.executeUpdate(insertQuery);
			  System.out.println("Data inserted successfully into firstab table.");
		  }
		  catch (SQLException e) {
			  System.err.println("Error executing insert query: " + e.getMessage());
		  } finally {
			  try {
				  connection.close();
			  } catch (SQLException e) {
				  System.err.println("Error closing connection: " + e.getMessage());
			  }
		  }
   }
}}
