package com.sourcecode;

import java.io.IOException;

public class FileCreationMain {
   public static void main(String[] args) {
	      String excelPath = "d:\\eclipse\\files\\metrics.xlsx";
		  String outputPath = "d:\\eclipse\\files\\datafile.ok";
		  try {
			  // Create the data file from the Excel file
			  ExcelReading.createDataFile(excelPath, outputPath);
		  } catch (IOException e) {
			  System.err.println("Error creating data file: " + e.getMessage());
			  System.exit(1);
		  }
		 
		  System.out.println("File created successfully at: " + outputPath);
	}
  }
	  

