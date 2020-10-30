package com.employeePayRoll;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class EmployeePayrollDBService {
	private Connection getConnection() throws DatabaseException {
		String jdbcurl = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String userName = "root";
		String password = "Shivam99@";
		Connection connection;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcurl, userName, password);
		}
		catch(Exception e) {
			throw new DatabaseException("Connection was unsuccessful");
		}
		return connection;
	}
	public List<Employee> readData() throws DatabaseException {
		String sql = "Select * from employee_payroll; ";
		List<Employee> employeeData = new ArrayList<>();
		try(Connection connection = this.getConnection();){
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getDouble("salary");
				LocalDate start = result.getDate("start").toLocalDate();
				employeeData.add(new Employee(id, name, salary, start));
			}
		}
			catch(DatabaseException exception) {
				System.out.println(exception);
			}
		catch(Exception exception) {
			throw new DatabaseException("Unable to execute query");	
		}
		return employeeData;
	}
	//Using Statement
	private int updateEmployeeUsingStatement(String name, double salary) throws DatabaseException {
		String sql = String.format("Update employee_payroll set salary = %.2f where name = '%s';",salary, name);
		try(Connection connection = this.getConnection()){
			Statement statement = connection.createStatement();
			return	statement.executeUpdate(sql);
		}
		catch(DatabaseException exception) {
			System.out.println(exception);
		}
		catch(Exception exception) {
			throw new DatabaseException("Unable to update");
		}
		return 0;
	}
	public List<Employee> getEmployeeData(String name) throws DatabaseException{
		List<Employee> employeeList = null;
		for(Employee employee : readData()) {
			if(employee.name.equals(name)) {
				employeeList.add(employee);
				break;
			}
		}
		return employeeList;
	}
	//Update records
	public int updateEmployeeData(String name, double salary) throws DatabaseException {
		return this.updateEmployeeUsingStatement(name, salary);
	}
}
