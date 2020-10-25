package com.employeePayRoll;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class EmployeeFileService {
	public static String PAYROLL_FILE_NAME = "payroll.txt";
	public void writeData(List<Employee> list) {
		StringBuffer employeeBuffer = new StringBuffer();
		list.forEach(employee -> {
			String empString = employee.toString().concat("\n");
			employeeBuffer.append(empString);
		});
		try {
			Files.write(Paths.get(PAYROLL_FILE_NAME), employeeBuffer.toString().getBytes());
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	public long countEntries() {
		long entries = 0;
		try {
			entries = Files.lines(new File(PAYROLL_FILE_NAME).toPath()).count();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return entries;
	}
	public void printData() {
		try {
			Files.lines(new File(PAYROLL_FILE_NAME).toPath()).forEach(System.out::println);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}

