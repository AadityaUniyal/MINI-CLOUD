package practice;

interface Shape{
    void draw();
}

interface twodshapes extends Shape{
    double area();
}

interface threedshapes extends Shape{
    double volume();
}

class Circle implements twodshapes{
    private final double radius;

    public Circle(double radius){
        this.radius=radius;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a circle");
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

class Sphere implements threedshapes{
    private final double radius;

    public Sphere(double radius){
        this.radius=radius;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a sphere");
    }

    @Override
    public double volume() {
        return (4.0/3.0) * Math.PI * Math.pow(radius, 3);
    }
}

public class pp3 {
    public static void main(String[] args) {
        Circle circle = new Circle(5);
        Sphere sphere = new Sphere(5);

        circle.draw();
        System.out.println("Area of the circle: " + circle.area());

        sphere.draw();
        System.out.println("Volume of the sphere: " + sphere.volume());
    }
    
}
