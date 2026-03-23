package practice;
/*Define an abstract class Vehicle with:
A field for name and speed.
A constructor to initialize these fields.
An abstract method move() that describes how the vehicle moves.
A concrete method displayInfo() to print the vehicle’s name and speed.
Create three concrete classes: Car, Boat, and Airplane, inheriting from Vehicle:
Car should override the move() method to indicate it moves by driving on roads.
Boat should override the move() method to indicate it moves by sailing on water.
Airplane should override the move() method to indicate it moves by flying in the air.
In your main method, instantiate each of the three vehicle types, and call their move() and displayInfo() methods.*/

public class pp1 {
    public static void main(String[] args) {
        Car car = new Car("Sedan", 120);
        Boat boat = new Boat("Yacht", 40);
        Airplane airplane = new Airplane("Jet", 600);

        car.displayInfo();
        car.move();

        boat.displayInfo();
        boat.move();

        airplane.displayInfo();
        airplane.move();
    }
}

abstract class Vehicle {
    private String name;
    private int speed;

    public Vehicle(String name, int speed) {
        this.name = name;
        this.speed = speed;
    }

    public abstract void move();

    public void displayInfo() {
        System.out.println("Vehicle: " + name + ", Speed: " + speed + " km/h");
    }
}

class Car extends Vehicle {
    public Car(String name, int speed) {
        super(name, speed);
    }

    @Override
    public void move() {
        System.out.println("The car drives on roads.");
    }
}

class Boat extends Vehicle {
    public Boat(String name, int speed) {
        super(name, speed);
    }

    @Override
    public void move() {
        System.out.println("The boat sails on water.");
    }
}

class Airplane extends Vehicle {
    public Airplane(String name, int speed) {
        super(name, speed);
    }

    @Override
    public void move() {
        System.out.println("The airplane flies in the air.");
    }
}
