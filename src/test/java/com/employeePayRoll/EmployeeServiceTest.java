package com.employeePayRoll;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class EmployeeServiceTest {
	@Test
	public void given3Employee_WhenWrittenToFile_ShouldMatchEmployeeEntries() {
		Employee[] arrayOfEmps = { new Employee(1, "Jeff Bezos", 100000), new Employee(2, "Bill Gates", 200000),
				new Employee(3, "Mark Zuckerberg", 300000) };
		EmployeePayrollService eService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		eService.writeData(EmployeePayrollService.IOService.FILE_IO);
		long result = eService.countEntries(EmployeePayrollService.IOService.FILE_IO);
		assertEquals(3, result);
	}
	@Test
	//print data
	public void given3Employee_WhenWrittenToFile_ShouldPrintEmployeeEntries() {
		Employee[] arrayOfEmps = { new Employee(1, "Jeff Bezos", 100000), new Employee(2, "Bill Gates", 200000),
				new Employee(3, "Mark Zuckerberg", 300000) };
		EmployeePayrollService eService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		eService.writeData(EmployeePayrollService.IOService.FILE_IO);
		eService.printData(EmployeePayrollService.IOService.FILE_IO);
		long result = eService.countEntries(EmployeePayrollService.IOService.FILE_IO);
		assertEquals(3, result);
	}
	@Test
	//count entries
	public void given3Employee_WhenWrittenToFile_ShouldReturnEmployeeEntries() {
		Employee[] arrayOfEmps = { new Employee(1, "Jeff Bezos", 100000), new Employee(2, "Bill Gates", 200000),
				new Employee(3, "Mark Zuckerberg", 300000) };
		EmployeePayrollService eService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		eService.writeData(EmployeePayrollService.IOService.FILE_IO);
		eService.printData(EmployeePayrollService.IOService.FILE_IO);
		long result = eService.countEntries(EmployeePayrollService.IOService.FILE_IO);
		assertEquals(3, result);
	}
	@Test
	public void given3Employee_WhenWrittenToFile_ShouldBeReadFromFile() {
		Employee[] arrayOfEmps = { new Employee(1, "Jeff Bezos", 100000), new Employee(2, "Bill Gates", 200000),
				new Employee(3, "Mark Zuckerberg", 300000) };
		EmployeePayrollService eService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		eService.writeData(EmployeePayrollService.IOService.FILE_IO);
		eService.readEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
		long result = eService.countEntries(EmployeePayrollService.IOService.FILE_IO);
		assertEquals(3, result);
	}
	@Test
	public void given3Employee_WhenRetrieved_ShouldMatchEmployeeCount() {
		List<Employee> employees = new ArrayList<>();
		EmployeePayrollService eService = new EmployeePayrollService(employees);
		employees = eService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		assertEquals(3, employees.size());
	}
	@Test
	public void givenDatabase_WhenUpdated_ShouldBeInSync() throws SQLException {
		List<Employee> employees = new ArrayList<>();
		EmployeePayrollService eService = new EmployeePayrollService(employees);
		employees = eService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		eService.updateEmployeeSalary("Terisa", 6000000);
		boolean result = eService.checkEmployeeDataSync("Terisa");
		assertTrue(result);
	}
	@Test
	public void givenEmployees_WhenRetrievedByName_ShouldReturnTrue() {
		List<Employee> employees = new ArrayList<>();
		EmployeePayrollService eService = new EmployeePayrollService();
		employees = eService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		assertTrue(eService.checkEmployeeDataSync("Terisa"));
	}
}
