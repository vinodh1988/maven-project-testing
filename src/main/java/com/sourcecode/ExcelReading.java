package com.sourcecode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReading {
   public static void createDataFile(String excelPath,String outputPath) throws IOException {
    	   String [] defaultFields=
    		   {
    				  "feild1Default",
    				  "feild2Default",
    				  "feild3Default",
    				  "feild4Default",
    				  "feild5Default",
    				  "feild6Default",
    				  "feild7Default",
    				  "feild8Default",
    				  "feild9Default",
    				  "feild10Default",
    				  "feild11Default",
    				  "feild12Default",
    		   };
    	
    	   //file path to save the output file
    	   try (
    			    
    	FileInputStream fis = new FileInputStream(excelPath); //to read excel file
    	BufferedWriter bw = new BufferedWriter(new FileWriter( new File(outputPath)));	//to write on to text file   
    			    
    	   ){
    		 
    		   Workbook workbook = new XSSFWorkbook(fis);
    	       Sheet sheet = workbook.getSheetAt(0); //collection of rows
    	       boolean firstRow = true;
    	       /*
    	        * for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
    	        *   Row row = sheet.getRow(i); //get each row
    	        *   double number1 = row.getCell(0).getNumericCellValue();
    	        * }
    	        */
    	       for(Row row : sheet) {
    	    	 if(firstRow) {
    	    		 firstRow = false; //skip header row
    	    		 continue;
    	    	 }
    	    	 double number1 = row.getCell(0).getNumericCellValue();
    	    	 String field2 = row.getCell(1).getStringCellValue();
    	    	 String field3 = row.getCell(2).getStringCellValue();
    	    	 double number2 = row.getCell(3).getNumericCellValue();
    	    	 String field5 = row.getCell(4).getStringCellValue();
    	    	 defaultFields[3] = number1+"";
    	    	 defaultFields[7] = field2;
    	    	 defaultFields[8] = field3;
    	    	 defaultFields[9] = number2+"";
    	    	 defaultFields[11] = field5;
    	    	 StringBuilder sb = new StringBuilder("");// Mutable version of String
    	         for(String field : defaultFields) {
		        	 sb.append(field).append("^");
		         }
    	         
    	    	 bw.write(sb.toString()); //Writing to the output file
    	    	 bw.newLine();                          //it is important to close the BufferedWriter
    	       }
    	       
    		   
    		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 throw e;
		}
    	   catch(IOException e) {
    		   e.printStackTrace();
    		   throw e;
    	   }
    	   // index 3,7,8,9,and 11 are dynamic fields (output file)
    	   
   }
}
