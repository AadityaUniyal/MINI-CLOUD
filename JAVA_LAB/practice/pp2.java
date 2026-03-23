package practice;
/*Define an abstract class UniversityMember with:
Fields for name, age, and department.
A constructor to initialize these fields.
An abstract method getRole() that returns the member’s role in the university (e.g., “Student”, “Faculty”).
A concrete method printDetails() to output the member’s information.
Create two concrete classes: Student and Faculty, inheriting from UniversityMember:
Student should have additional fields for studentID and major, and implement the getRole() method.
Faculty should have additional fields for facultyID and specialization, and implement the getRole() method.
In the main method, create a list of UniversityMember objects that includes both Student and Faculty instances. Iterate over the list, calling printDetails() for each member. */
import java.util.ArrayList;
import java.util.List;

public class pp2 {
    public static void main(String[] args) {
        List<UniversityMember> members = new ArrayList<>();

        members.add(new Student("Alice", 20, "Computer Science", "S12345", "Software Engineering"));
        members.add(new Faculty("Dr. Brown", 45, "Physics", "F67890", "Quantum Mechanics"));

        for (UniversityMember member : members) {
            member.printDetails();
            System.out.println("Role: " + member.getRole());
            System.out.println();
        }
    }
}

abstract class UniversityMember {
    private String name;
    private int age;
    private String department;

    public UniversityMember(String name, int age, String department) {
        this.name = name;
        this.age = age;
        this.department = department;
    }

    public abstract String getRole();

    public void printDetails() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Department: " + department);
    }
}

class Student extends UniversityMember {
    private String studentID;
    private String major;

    public Student(String name, int age, String department, String studentID, String major) {
        super(name, age, department);
        this.studentID = studentID;
        this.major = major;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("Student ID: " + studentID);
        System.out.println("Major: " + major);
    }
}

class Faculty extends UniversityMember {
    private String facultyID;
    private String specialization;

    public Faculty(String name, int age, String department, String facultyID, String specialization) {
        super(name, age, department);
        this.facultyID = facultyID;
        this.specialization = specialization;
    }

    @Override
    public String getRole() {
        return "Faculty";
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("Faculty ID: " + facultyID);
        System.out.println("Specialization: " + specialization);
    }
}
