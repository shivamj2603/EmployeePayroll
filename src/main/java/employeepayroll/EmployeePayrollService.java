package employeepayroll;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployeePayrollService {
	private static final Logger LOG = LogManager.getLogger(EmployeePayrollDBService.class); 
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
	public Employee getEmployee(String name) {
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
	//Add Employee to Payroll
	public void addEmployeeToPayrollAndDepartment(String name, String gender, double salary, LocalDate start, String Department) throws SQLException, DatabaseException {
		this.employeeList.add(employeePayrollDBService.addEmployeeToPayrollAndDepartment(name, gender, salary, start, Department));
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
			System.out.println("No of Entries in File: " + entries);
		}
		if(ioService.equals(IOService.DB_IO)) {
			entries= employeeList.size();
		}
		return entries;
	}
	public List<Employee> deleteEmployee(String name) throws DatabaseException {
		employeePayrollDBService.deleteEmployee(name);
		return readEmployeePayrollData(IOService.DB_IO);
	}
	public List<Employee> removeEmployeeFromPayroll(int id) throws DatabaseException {
		List<Employee> activeEmployees = null;
		activeEmployees = employeePayrollDBService.removeEmployeeFromCompany(id);
		return activeEmployees;
	}
	public void addEmployeesToPayroll(List<Employee> asList) {
		employeeList.forEach(employee -> {
			System.out.println("Employee Being added: "+employee.name);
			try {
				this.addEmployeeToPayrollAndDepartment(employee.name,employee.gender,employee.salary,employee.start,employee.department);
			} catch (SQLException | DatabaseException exception) {
				exception.printStackTrace();
			}
			System.out.println("Employee added: "+employee.name);
		});
		System.out.println(this.employeeList);
	}	
	public void addEmployeesToPayrollWithThreads(List<Employee> employeeDataList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
		employeeDataList.forEach(employee -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(employee.hashCode(), false);
				System.out.println("Employee Being Added: "+ Thread.currentThread().getName());
				try {
					this.addEmployeeToPayrollAndDepartment(employee.name,employee.gender,employee.salary,
					                                       employee.start,employee.department);
				} catch (SQLException | DatabaseException e) {
					e.printStackTrace();
				}
				employeeAdditionStatus.put(employee.hashCode(), true);
				System.out.println("Employee Added: "+ Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, employee.name);
			thread.start();
		});
		while(employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}
	}
	public void updatePayroll(Map<String, Double> salaryMap) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
		salaryMap.forEach((k,v) -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(k.hashCode(), false);
				LOG.info("Employee Being Added: "+ Thread.currentThread().getName());
				try {
					this.updatePayrollDB(k,v);
				} catch (DatabaseException | SQLException e) {
					e.printStackTrace();
				} 
				employeeAdditionStatus.put(k.hashCode(), true);
				LOG.info("Employee Added: "+ Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, k);
			thread.start();
		});
		while(employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException exception) {
				exception.printStackTrace();  
			}
		}
	}
	public void updatePayrollDB(String name, Double salary) throws DatabaseException, SQLException {
		int result = employeePayrollDBService.updateEmployeeData(name, salary);
		if (result == 0)
			return;
		Employee employee = this.getEmployee(name);
		if (employee != null)
			employee.salary = salary;
	}
	public boolean checkEmployeeListSync(List<String> nameList) throws DatabaseException {
		List<Boolean> resultList = new ArrayList<>();
		nameList.forEach(name -> {
			List<Employee> employeeList;
			try {
				employeeList = employeePayrollDBService.getEmployeeData(name);
				resultList.add(employeeList.get(0).equals(getEmployee(name)));
			} catch (DatabaseException e) {}
		});
		if(resultList.contains(false)){
			return false;
		}
		return true;
	}
	public void addEmployeeToPayroll(Employee employee) {
		employeeList.add(employee);
	}
	public void deleteEmployee(String name, IOService ioService) {
		if(ioService.equals(IOService.REST_IO)) {
			Employee employee = this.getEmployee(name);
			employeeList.remove(employee);
		}	
	}
}