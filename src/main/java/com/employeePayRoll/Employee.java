package com.employeePayRoll;

public class Employee {
	public String name;
	public int id;
	public double salary;
	public Employee(int id, String name, double salary) {
		super();
		this.name = name;
		this.id = id;
		this.salary = salary;
	}
	@Override
	public String toString() {
		return "name = " + name + ", id = " + id + ", salary = " + salary;
	}
	
}

