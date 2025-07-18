package com.sourcecode;

public class MainClass {
  public static void main(String[] args) {
	LogLevel logLevel = LogLevel.INFO;
	System.out.println("Current log level: " + logLevel);
	System.out.println("Log level ordinal: " + logLevel.ordinal());
	System.out.println("Log level name: " + logLevel.name());
  }
}
