package com.employeePayRoll;
import java.time.LocalDate;

public class Employee {
	public String name;
	public int id;
	public double salary;
	public LocalDate start;
	public Employee(int id, String name, double salary) {
		this.name = name;
		this.id = id;
		this.salary = salary;
	}
	public Employee(int id, String name, double salary, LocalDate start) {
		this(id, name, salary);
		this.start = start;
	}
	@Override
	public String toString() {
		return "id = " + id + ", name = " + name + ", salary = " + salary;
	}
	
}

