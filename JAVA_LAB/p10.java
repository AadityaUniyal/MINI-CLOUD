import java.util.*;

class Employee {
    private int empID;
    private String name;
    private double salary;
    private String department;
    private String designation;

    public Employee() {
        this.empID = 0;
        this.name = "";
        this.salary = 0.0;
        this.department = "";
        this.designation = "";
    }

    public Employee(int id, String n, double s, String dept, String desig) {
        this.empID = id;
        this.name = n;
        this.salary = s;
        this.department = dept;
        this.designation = desig;
    }

    public int getEmpID() {
        return empID;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setSalary(double s) {
        salary = s;
    }

    public void setDesignation(String desig) {
        designation = desig;
    }

    public void display() {
        System.out.printf("%d %s %15.2f %s %s \n", empID, name, salary, department, designation);
    }

    public double calculateBonus() {
        return salary * 0.10;
    }

    public double calculateTotalCompensation() {
        return salary + calculateBonus();
    }
}

class EmployeeManagementSystem {
    private ArrayList<Employee> employees;

    public EmployeeManagementSystem() {
        employees = new ArrayList<>();
    }

    public void addEmployee(Employee emp) {
        employees.add(emp);
    }

    public void removeEmployee(int empID) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmpID() == empID) {
                employees.remove(i);
                System.out.println("Employee removed successfully!");
                return;
            }
        }
        System.out.println("Employee not found!");
    }

    public Employee searchEmployee(int empID) {
        for (Employee emp : employees) {
            if (emp.getEmpID() == empID) {
                return emp;
            }
        }
        return null;
    }

    public void updateSalary(int empID, double newSalary) {
        Employee emp = searchEmployee(empID);
        if (emp != null) {
            emp.setSalary(newSalary);
            System.out.println("Salary updated successfully!");
        } else {
            System.out.println("Employee not found!");
        }
    }

    public void promoteEmployee(int empID, String newDesignation, double salaryIncrease) {
        Employee emp = searchEmployee(empID);
        if (emp != null) {
            emp.setDesignation(newDesignation);
            emp.setSalary(emp.getSalary() + salaryIncrease);
            System.out.println("Employee promoted successfully!");
        } else {
            System.out.println("Employee not found!");
        }
    }

    public double getTotalSalaryExpense() {
        double total = 0;
        for (Employee emp : employees) {
            total += emp.getSalary();
        }
        return total;
    }

    public double getAverageSalary() {
        if (employees.isEmpty())
            return 0;
        return getTotalSalaryExpense() / employees.size();
    }

    public void displayAllEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees in the system!");
            return;
        }
        System.out.println("\n" + String.format("%8s %20s %15s %18s %18s", "ID", "Name", "Salary", "Department", "Designation"));
        System.out.println("=" .repeat(79));

        for (Employee emp : employees) {
            emp.display();
        }
    }

    public void displayEmployeeDetails(int empID) {
        Employee emp = searchEmployee(empID);
        if (emp != null) {
            System.out.println("\nEmployee ID: " + emp.getEmpID());
            System.out.println("Name: " + emp.getName());
            System.out.println("Salary: " + emp.getSalary());
            System.out.println("Department: " + emp.getDepartment());
            System.out.println("Designation: " + emp.getDesignation());
            System.out.println("Bonus (10%): " + emp.calculateBonus());
            System.out.println("Total Compensation: " + emp.calculateTotalCompensation());
        } else {
            System.out.println("Employee not found!");
        }
    }

    public int getTotalEmployees() {
        return employees.size();
    }
}

public class p10 {
    public static void displayMenu() {
        System.out.println("1. Add Employee");
        System.out.println("2. Remove Employee");
        System.out.println("3. Search Employee");
        System.out.println("4. Update Salary");
        System.out.println("5. Promote Employee");
        System.out.println("6. Display All Employees");
        System.out.println("7. Display Employee Details");
        System.out.println("8. Total Salary Expense");
        System.out.println("9. Average Salary");
        System.out.println("10. Total Employees");
        System.out.println("11. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void main(String[] args) {
        EmployeeManagementSystem system = new EmployeeManagementSystem();
        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                int id;
                String name, dept, desig;
                double sal;

                System.out.print("Enter Employee ID: ");
                id = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter Employee Name: ");
                name = scanner.nextLine();

                System.out.print("Enter Salary: ");
                sal = scanner.nextDouble();
                scanner.nextLine();

                System.out.print("Enter Department: ");
                dept = scanner.nextLine();

                System.out.print("Enter Designation: ");
                desig = scanner.nextLine();

                Employee emp = new Employee(id, name, sal, dept, desig);
                system.addEmployee(emp);
                System.out.println("Employee added successfully!");
            }
             else if (choice == 2) {
                int id;
                System.out.print("Enter Employee ID to remove: ");
                id = scanner.nextInt();
                system.removeEmployee(id);
            } 
            else if (choice == 3) {
                int id;
                System.out.print("Enter Employee ID to search: ");
                id = scanner.nextInt();
                Employee emp = system.searchEmployee(id);
                if (emp != null) {
                    System.out.println("Employee found: " + emp.getName());
                } else {
                    System.out.println("Employee not found!");
                }
            } 
            else if (choice == 4) {
                int id;
                double newSal;
                System.out.print("Enter Employee ID: ");
                id = scanner.nextInt();
                System.out.print("Enter new salary: ");
                newSal = scanner.nextDouble();
                system.updateSalary(id, newSal);
            } 
            else if (choice == 5) {
                int id;
                String newDesig;
                double increase;
                System.out.print("Enter Employee ID: ");
                id = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter new designation: ");
                newDesig = scanner.nextLine();
                System.out.print("Enter salary increase: ");
                increase = scanner.nextDouble();
                system.promoteEmployee(id, newDesig, increase);
            } 
            else if (choice == 6) {
                system.displayAllEmployees();
            } 
            else if (choice == 7) {
                int id;
                System.out.print("Enter Employee ID: ");
                id = scanner.nextInt();
                system.displayEmployeeDetails(id);
            } 
            else if (choice == 8) {
                System.out.println("Total Salary Expense: " + system.getTotalSalaryExpense());
            }
             else if (choice == 9) {
                System.out.println("Average Salary: " + system.getAverageSalary());
            } 
            else if (choice == 10) {
                System.out.println("Total Employees: " + system.getTotalEmployees());
            } 
            else if (choice == 11) {
                System.out.println("Exiting the system. Thank you!");
                break;
            } 
            else 
                {
                System.out.println("Invalid choice! Please try again.");
            }
        }
        scanner.close();
    }
}