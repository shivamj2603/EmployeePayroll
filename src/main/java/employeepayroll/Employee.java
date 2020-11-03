package employeepayroll;
import java.time.LocalDate;

public class Employee {
	public String name;
	public int id;
	public double salary;
	public LocalDate start;
	public String gender;
	public String department;
	public Employee(int id, String name, double salary) {
		this.name = name;
		this.id = id;
		this.salary = salary;
	}
	public Employee(int id, String name, double salary, LocalDate start) {
		this(id, name, salary);
		this.start = start;
	}
	public Employee(int id, String name, String gender, double salary, LocalDate start) {
		this(id, name, salary, start);
		this.gender = gender;
	}
	public Employee(int id, String name, String gender, double salary, LocalDate start, String department) {
		this(id, name, gender, salary, start);
		this.department = department;
	}
	@Override
	public String toString() {
		return "id = " + id + ", name = " + name + ", salary = " + salary;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (id != other.id)
			return false;
		if (Double.doubleToLongBits(salary) != Double.doubleToLongBits(other.salary))
			return false;
		return true;
	}	
}

