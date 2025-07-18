package com.sourcecode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
  private static Connection connection = null;
  
  static {
	  
    try {
	  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	  String url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=javatesting;encrypt=true;trustServerCertificate=true;";
	  String user = "sa"; // or your SQL login
	  String password = "password";
	  connection = DriverManager.getConnection(url, user, password);
	} catch (ClassNotFoundException e) {
	  System.err.println("SQL Server JDBC Driver not found.");
	  e.printStackTrace();
	} catch (SQLException e) {
	  System.err.println("Connection failed.");
	  e.printStackTrace();
	}
  }
  
  public static Connection getConnection() {
	return connection;
  }
}
