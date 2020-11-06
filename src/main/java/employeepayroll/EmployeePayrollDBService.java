package employeepayroll;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeePayrollDBService {
	private PreparedStatement preparedStatement = null;
	private static EmployeePayrollDBService employeePayrollDBService;
	private static Connection connection = null;
	private EmployeePayrollDBService() {
	}
	//Singleton Object
	public static EmployeePayrollDBService getInstance() {
		if(employeePayrollDBService == null) {
			employeePayrollDBService = new EmployeePayrollDBService();
		}
		return employeePayrollDBService;
	}
	//Cached prepared Statement
	private void getPreparedStatement() throws SQLException, DatabaseException {
		this.getConnection();
		if(preparedStatement == null) {
			String sql = "Select * from employee_payroll where name = ?;";
		preparedStatement = connection.prepareStatement(sql);
		}
	}
	private Connection getConnection() throws DatabaseException {
		if(connection != null) {
			return connection;
		}
		String jdbcurl = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String userName = "root";
		String password = "Shivam99@";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcurl, userName, password);
			String sql = "Select * from employee_payroll where name = ?;";
			preparedStatement = connection.prepareStatement(sql);
		}
		catch(Exception e) {
			throw new DatabaseException("Connection was unsuccessful");
		}
		return connection;
	}
	//read all records
	public List<Employee> readData() throws DatabaseException {
		String sql = "Select * from employee_payroll inner join department on employee_payroll_service.id = department.employee_id; ";
		return this.getEmployeePayrollAndDeparmentData(sql);
	}
		private List<Employee> getEmployeePayrollAndDeparmentData(String sql) throws DatabaseException {
			List<Employee> employeePayrollList = new ArrayList<>();
			try (Connection connection = this.getConnection()) {
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql);
				while (resultSet.next()) {
					int id = resultSet.getInt("id");
					String name = resultSet.getString("name");
					String gender = resultSet.getString("gender");
					double salary = resultSet.getDouble("salary");
					LocalDate start = resultSet.getDate("start").toLocalDate();
					String department = resultSet.getString("department_name");
					employeePayrollList.add(new Employee(id, name, gender, salary, start,department));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return employeePayrollList;		
	}
	//Read records for a given date range
	public List<Employee> readDataForGivenDateRange(LocalDate start, LocalDate end) throws DatabaseException{
		String sql = String.format("Select * from employee_payroll inner join department on employee_payroll_service.id = department.employee_id where start between '%s' and '%s' ;", Date.valueOf(start), Date.valueOf(end));
	    return getEmployeeRecords(sql);
	}
	//get employee records by condition
	public Map<String,Double> getEmployeesByFunction(String function) throws  DatabaseException{
		Map<String,Double> employeeAverage = new HashMap<>();
		String sql = String.format("Select gender, %s(salary) from employee_payroll group by gender ; ",function) ;
		try {
			Statement statement = getConnection().createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				String gender = result.getString(1);
				Double salary = result.getDouble(2);
				employeeAverage.put(gender, salary);
			}
		}
		catch(SQLException exception) {
			throw new DatabaseException("Unable to calculate");
		}
		return employeeAverage;
	}
	//Read all records satisfying a given query
	private List<Employee> getEmployeeRecords(String sql) throws DatabaseException {
		List<Employee> employeeData = new ArrayList<>();
		try(Connection connection = this.getConnection();){
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			employeeData = getEmployeeData(result);
		}
			catch(DatabaseException exception) {
				System.out.println(exception);
			}
		catch(Exception exception) {
			throw new DatabaseException("Unable to execute query");	
		}
		return employeeData;
	}
	//Update salary
	private int updateEmployeeUsingPreparedStatement(String name, double salary) throws DatabaseException, SQLException {
		Connection connection = this.getConnection();
		String sql = "Update employee_payroll set salary = ? where name = ? ; " ; 
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		prepareStatement.setString(2, name);
		prepareStatement.setDouble(1, salary);
		return prepareStatement.executeUpdate();
	}
	//Get Employee Records for a given name
	public List<Employee> getEmployeeData(String name) throws DatabaseException{
		try {
			getPreparedStatement();
			preparedStatement.setString(1,  name);
			return getEmployeeData(preparedStatement.executeQuery());
		} catch (DatabaseException | SQLException e) {
			throw new DatabaseException("Unable to get results");
		}
	}
	//Update records
	public int updateEmployeeData(String name, double salary) throws DatabaseException, SQLException {
		return this.updateEmployeeUsingPreparedStatement(name, salary);
	}
	//Get the resultSet
	public List<Employee> getEmployeeData(ResultSet result) throws DatabaseException{
		List<Employee> employeeData = new ArrayList<Employee>();
		try {
			while(result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getDouble("salary");
				LocalDate start = result.getDate("start").toLocalDate();
				employeeData.add(new Employee(id, name, salary, start));
			}
		} catch (SQLException e) {
			throw new DatabaseException("Unable to get results");
		}
		return employeeData;
	}
	//Add new Employee to Payroll
	public Employee addEmployeeToPayrollAndDepartment(String name, String gender, double salary, LocalDate start, String department) throws DatabaseException, SQLException {
		int employeeId = -1;
		Connection connection = null;
		Employee employee = null;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format("INSERT INTO employee_payroll_service (name, gender, salary, start) "
					+ "VALUES ('%s','%s','%s','%s')", name, gender, salary, Date.valueOf(start));
			int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					employeeId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
			throw new DatabaseException("Unable to add new employee");
		}
		try (Statement statement = connection.createStatement()) {
			double deductions = salary * 0.2;
			double taxable_pay = salary - deductions;
			double tax = taxable_pay * 0.1;
			double netPay = salary - tax;
			String sql = String.format(
					"INSERT INTO payroll_details (employee_id, basic_pay, deductions, taxable_pay, tax, net_pay) "
							+ "VALUES ('%s','%s','%s','%s','%s','%s')",
					employeeId, salary, deductions, taxable_pay, tax, netPay);
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
			throw new DatabaseException("Unable to add payroll details of  employee");
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"INSERT INTO department (employee_id,department_name) "
							+ "VALUES ('%s','%s')",
					employeeId,department);
			int rowAffected = statement.executeUpdate(sql);
			if(rowAffected == 1) {
				employee = new Employee(employeeId, name, gender, salary,start, department);
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
			throw new DatabaseException("Unable to add department details of  employee");
		}
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return employee;
	}
	public List<Employee> removeEmployeeFromCompany(int id) throws DatabaseException {
		List<Employee> Employees = this.readData();
		Employees.forEach(employee -> {
			if (employee.id == id) {
				employee.is_active = false;
			}
		});
		return Employees;
	}
	//Delete employee from the payroll
	public void deleteEmployee(String name) throws DatabaseException {
		String sql = String.format("DELETE from employee_payroll where name = '%s';", name);
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException exception) {
			throw new DatabaseException("Unable to delete data");
		}
	}
}
