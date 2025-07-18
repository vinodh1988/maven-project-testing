package com.sourcecode;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadTableRecords {
   private static final Logger logger = LogManager.getLogger(ReadTableRecords.class);
   public static void main(String [] args) {
	   Connection connection = ConnectionProvider.getConnection();
		if (connection != null) {
			try {
			 String selectQuery = "SELECT * FROM firstab1";
			 Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(selectQuery);
			 //System.out.printf("%-10s %-20s%n", "SNO", "NAME");
			 System.out.println("\n-------------------------------");
			 while(resultSet.next()) {
				 int sno = resultSet.getInt("sno");
				 String name = resultSet.getString("name");
				 //System.out.printf("%-10d %-20s%n", sno, name);
				 logger.info("SNO: {}, NAME: {}", sno, name); // Log the retrieved data
			 }
			 }catch (SQLException e) {
					//System.err.println("Error executing select query: " + e.getMessage());
					logger.error("Error executing select query: {}", e.getMessage(), e);
				} 
			catch (Exception e) {
				//System.err.println("Unexpected error: " + e.getMessage());
				logger.error("Unexpected error: {}", e.getMessage(), e);
			}
			finally {
				try {
					connection.close();
				} catch (SQLException e) {
					//System.err.println("Error closing connection: " + e.getMessage());
					logger.error("Error closing connection: {}", e.getMessage(), e);
				}
			}
		}
   }
}
