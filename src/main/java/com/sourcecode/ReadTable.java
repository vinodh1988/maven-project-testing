package com.sourcecode;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReadTable {
    public static void main(String[] args) {
		Connection connection = ConnectionProvider.getConnection();
		if (connection != null) {
			try {
			 String selectQuery = "SELECT * FROM firstab";
			 Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(selectQuery);
			 System.out.printf("%-10s %-20s%n", "SNO", "NAME");
			 System.out.println("\n-------------------------------");
			 while(resultSet.next()) {
				 int sno = resultSet.getInt("sno");
				 String name = resultSet.getString("name");
				 System.out.printf("%-10d %-20s%n", sno, name);
			 }
			} catch (SQLException e) {
				System.err.println("Error executing select query: " + e.getMessage());
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					System.err.println("Error closing connection: " + e.getMessage());
				}
			}
		}
	}
}
