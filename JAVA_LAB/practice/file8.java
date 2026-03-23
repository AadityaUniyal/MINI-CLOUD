

public class file8 {
    int id;
    String name;
    double salary;

    file8(int id,String name,double salary){
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    void display(){
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Salary: " + salary);
    }
}

class EmployeeManagementSystem {
    public static void main(String[] args) {
        file8 emp1 = new file8(1, "Alice", 50000);
        file8 emp2 = new file8(2, "Bob", 60000);
        
        emp1.display();
        System.out.println();
        emp2.display();
    }
}