/*Write a java program to calculate the area of a rectangle, a square and a circle. Create an abstract class 'Shape' with three abstract methods namely rectangleArea() taking two parameters, squareArea() and circleArea() taking one parameter each.  */
package practice;

public abstract class file11 {
    public  abstract double rectangleArea(double length, double width);
    public abstract double squareArea(double side);
    public abstract double circleArea(double radius);
}

class Area extends file11{

    @Override
    public double rectangleArea(double length, double width) {
        return length * width;
    }

    @Override
    public double squareArea(double side) {
        return side * side;
    }

    @Override
    public double circleArea(double radius) {
        return Math.PI * radius * radius;
    }

}

public class Main {
    public static void main(String[] args) {
        Area area = new Area();
        
        double rectArea = area.rectangleArea(5, 3);
        double squareArea = area.squareArea(4);
        double circleArea = area.circleArea(2);
        
        System.out.println("Area of Rectangle: " + rectArea);
        System.out.println("Area of Square: " + squareArea);
        System.out.println("Area of Circle: " + circleArea);
    }
}
