package com.employeePayRoll;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.time.LocalDate;
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
	public void givenDatabase_WhenUpdated_ShouldBeInSync() throws DatabaseException {
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
	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() throws DatabaseException {
		List<Employee> employees = new ArrayList<>();
		EmployeePayrollService eService = new EmployeePayrollService();
		eService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		LocalDate start = LocalDate.of(2018, 01, 01);
		LocalDate end = LocalDate.now();
		employees = eService.getEmployeeByDate(start, end);
		assertEquals(2, employees.size());
	}
	@Test
	public void givenEmployees_WhenRetrievedAverage_ShouldReturnTrue() throws DatabaseException {
		EmployeePayrollService eService = new EmployeePayrollService();
		eService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		assertTrue(eService.getEmployeeAverageByGender().get("M").equals(2000000.0));
	}
	@Test
	public void givenEmployees_WhenRetrievedMaximumSalaryByGender_ShouldReturnTrue() throws DatabaseException {
		EmployeePayrollService eService = new EmployeePayrollService();
		eService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		assertTrue(eService.getEmployeeMaximumSalaryByGender().get("M").equals(2000000.0));
	}
	@Test
	public void givenEmployees_WhenRetrievedMinimumSalaryByGender_ShouldReturnTrue() throws DatabaseException {
		EmployeePayrollService eService = new EmployeePayrollService();
		eService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		assertTrue(eService.getEmployeeMinimumSalaryByGender().get("M").equals(2000000.0));
	}
	@Test
	public void givenEmployees_WhenRetrievedSumByGender_ShouldReturnTrue() throws DatabaseException {
		EmployeePayrollService eService = new EmployeePayrollService();
		eService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		assertTrue(eService.getEmployeeSumByGender().get("M").equals(4000000.0));
	}
	@Test
	public void givenEmployees_WhenRetrievedCountByGender_ShouldReturnTrue() throws DatabaseException {
		EmployeePayrollService eService = new EmployeePayrollService();
		eService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		System.out.println(eService.getEmployeeAverageByGender().get("M"));
		assertTrue(eService.getEmployeeCountByGender().get("M").equals(2.0));
	}
}
