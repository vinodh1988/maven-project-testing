package com.sourcecode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlExpressConnection {
    public static void main(String[] args) {
    	// Load the SQL Server JDBC driver
        String url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=javatesting;encrypt=true;trustServerCertificate=true;"; // Adjust the URL as needed
        String user = "sa"; // or your SQL login
        String password = "password";
        Connection connection = null;
        try  {
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
        	connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful!");
            Statement statement = connection.createStatement();	
            statement.execute
            ("CREATE TABLE firstab (sno INT PRIMARY KEY, name NVARCHAR(50))");
            System.out.println("Table created successfully ");
        } catch (SQLException e) {
            System.out.println("Connection failed:");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}