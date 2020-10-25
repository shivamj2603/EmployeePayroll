package com.employeePayRoll;
import java.util.*; 

public class EmployeePayrollService {
	private List<Employee> employeeList;
	public EmployeePayrollService() {
		employeeList = new ArrayList<Employee>(); 
	}
	public static void main(String[] args) {
		EmployeePayrollService eService = new EmployeePayrollService();
		Scanner consoleInput = new Scanner(System.in);
		eService.readEmployeePayrollData(consoleInput);
		eService.writeData();
	}
	private void writeData() {
		System.out.println("Writting data of employee to console: "+employeeList);
	}
	private void readEmployeePayrollData(Scanner consoleInput) {
		System.out.println("Enter the employee id");
		int id = consoleInput.nextInt();
		consoleInput.nextLine();
		System.out.println("Enter the employee name");
		String name = consoleInput.nextLine();
		System.out.println("Enter the employee salary");
		double salary = consoleInput.nextDouble();
		employeeList.add(new Employee(id,name,salary));
	}
}
