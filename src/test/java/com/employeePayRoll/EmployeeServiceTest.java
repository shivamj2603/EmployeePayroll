package com.employeePayRoll;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
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
	/**
	 * Usecase7 
	 * Test case to check for reading the data from file and adding the data to list
	 */
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
}
