package com.sourcecode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddInteractively {
    public static void main(String[] args) {
	  Connection connection = ConnectionProvider.getConnection();
	  Scanner scanner = new Scanner(System.in);
	  try {
		  PreparedStatement ps= 
				  connection.prepareStatement("INSERT INTO firstab (sno, name) VALUES (?, ?)");
		  while(true) {
			 System.out.println("Enter sno (or type '-1' to quit): ");
			 int sno = Integer.parseInt(scanner.next());
			 if(sno == -1) {
				 break; // Exit the loop if user types -1
			 }
			 System.out.println("Enter name: ");
			 String name = scanner.next();
			 ps.setInt(1, sno);
			 ps.setString(2, name);
			 ps.executeUpdate();
		  }
	  }
	  catch(SQLException e) {
		  e.printStackTrace();
	  }
    }
}
