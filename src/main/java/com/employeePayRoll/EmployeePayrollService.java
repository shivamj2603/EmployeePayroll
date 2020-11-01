package com.employeePayRoll;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
public class EmployeePayrollService {
	static EmployeePayrollDBService employeePayrollDBService;
	static Scanner consoleInput = new Scanner(System.in);
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	};
	private List<Employee> employeeList;
	public EmployeePayrollService() {
		employeePayrollDBService = EmployeePayrollDBService.getInstance();
	}
	public EmployeePayrollService(List<Employee> list) {
		this.employeeList = list;
	}
	public static void main(String[] args) {
		ArrayList<Employee> list = new ArrayList<Employee>();
		EmployeePayrollService eService = new EmployeePayrollService(list);
		eService.readEmployeePayrollData(IOService.FILE_IO);
		eService.writeData(IOService.CONSOLE_IO);
	}
	/**
	 * Usecase 4
	 * Write data to a file
	 * @param ioService
	 */
	public void writeData(IOService ioService) {
		if (ioService.equals(IOService.CONSOLE_IO))
			System.out.println("Writting data of employee to console: " + employeeList);
		else if (ioService.equals(IOService.FILE_IO)) {
			new EmployeeFileService().writeData(employeeList);
		}
	}
	public List<Employee> readEmployeePayrollData(IOService ioService) {
		List<Employee> list = new ArrayList<>();
		if (ioService.equals(IOService.CONSOLE_IO)) {
			System.out.println("Enter the employee id");
			int id = consoleInput.nextInt();
			consoleInput.nextLine();
			System.out.println("Enter the employee name");
			String name = consoleInput.nextLine();
			System.out.println("Enter the employee salary");
			double salary = consoleInput.nextDouble();
			employeeList.add(new Employee(id, name, salary));
		} else if (ioService.equals(IOService.FILE_IO)) {
			list = new EmployeeFileService().readData();
			System.out.println("Reading data from file" + list);
		}
		//Reading Data from the Database
		else if(ioService.equals(IOService.DB_IO)) {
			try {
				list = employeePayrollDBService.readData();
			} catch (DatabaseException exception) {
				System.out.println(exception);
			}
			System.out.println("Reading data from database" + list);
			this.employeeList = list;
		}
		return list;
	}
	//Update Employee Records
	public void updateEmployeeSalary(String name, double salary) throws DatabaseException {
		int result = 0;
		try {
			result = employeePayrollDBService.updateEmployeeData(name,salary);
		} catch (SQLException exception) {
			System.out.println(exception);
		}
		if(result == 0) return;
		Employee employee = this.getEmployee(name);
		if(employee != null) {
			employee.salary = salary;
		}
	}
	//Get the updated record of the employee
	private Employee getEmployee(String name) {
		Employee employee = this.employeeList.stream()
				    .filter(employeeData -> employeeData.name.equals(name))
				    .findFirst()
				    .orElse(null);
		return employee;
	}
	//check whether the updated record matches the record of database
	public boolean checkEmployeeDataSync(String name) {
		List<Employee> employees = null;
		try {
			employees = employeePayrollDBService.getEmployeeData(name);
		}
		catch (DatabaseException exception) {
			System.out.println(exception);
		}
		return employees.get(0).equals(getEmployee(name));
	}
	//get Employees for a given date range
	public List<Employee> getEmployeeByDate(LocalDate start, LocalDate end) throws DatabaseException {
		return employeePayrollDBService.readDataForGivenDateRange(start, end);
	}
	public Map<String, Double> getEmployeeAverageByGender() throws DatabaseException{
		return employeePayrollDBService.getEmployeesByFunction("AVG");
	}
	public Map<String, Double> getEmployeeSumByGender() throws DatabaseException{
		return employeePayrollDBService.getEmployeesByFunction("SUM");
	}
	public Map<String, Double> getEmployeeMaximumSalaryByGender() throws DatabaseException{
		return employeePayrollDBService.getEmployeesByFunction("MAX");
	}
	public Map<String, Double> getEmployeeMinimumSalaryByGender() throws DatabaseException{
		return employeePayrollDBService.getEmployeesByFunction("MIN");
	}
	public Map<String, Double> getEmployeeCountByGender() throws DatabaseException{
		return employeePayrollDBService.getEmployeesByFunction("COUNT");
	}
	public void printData(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO)) {
			new EmployeeFileService().printData();
		}
	}
	/**
	 * Usecase 6
	 * Print number of entries
	 * @param ioService
	 * @return
	 */
	public long countEntries(IOService ioService) {
		long entries = 0;
		if (ioService.equals(IOService.FILE_IO)) {
			entries = new EmployeeFileService().countEntries();
		}
		System.out.println("No of Entries in File: " + entries);
		return entries;
	}
}