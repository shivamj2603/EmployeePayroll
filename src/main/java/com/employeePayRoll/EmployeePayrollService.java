package com.employeePayRoll;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
public class EmployeePayrollService {
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	};
	private List<Employee> employeeList;

	public EmployeePayrollService(List<Employee> list) {
		this.employeeList = list;
	}
	public static void main(String[] args) {
		ArrayList<Employee> list = new ArrayList<Employee>();
		EmployeePayrollService eService = new EmployeePayrollService(list);
		Scanner consoleInput = new Scanner(System.in);
		eService.readEmployeePayrollData(consoleInput);
		eService.writeData(IOService.CONSOLE_IO);
	}
	public void writeData(IOService ioService) {
		if (ioService.equals(IOService.CONSOLE_IO))
			System.out.println("Writting data of employee to console: " + employeeList);
		else if (ioService.equals(IOService.FILE_IO)) {
			new EmployeeFileService().writeData(employeeList);
		}
	}
	public void readEmployeePayrollData(Scanner consoleInput) {
		System.out.println("Enter the employee id");
		int id = consoleInput.nextInt();
		consoleInput.nextLine();
		System.out.println("Enter the employee name");
		String name = consoleInput.nextLine();
		System.out.println("Enter the employee salary");
		double salary = consoleInput.nextDouble();
		employeeList.add(new Employee(id, name, salary));
	}
	public long countEntries(IOService ioService) {
		long entries = 0;
		if (ioService.equals(IOService.FILE_IO)) {
			entries = new EmployeeFileService().countEntries();
		}
		return entries;
	}
	/**
	 * Usecase 5
	 * @param ioService
	 */
	public void printData(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO)) {
			new EmployeeFileService().printData();
		}
	}
}