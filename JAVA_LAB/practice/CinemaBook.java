package practice;

interface Ticket{
    void bookticket();
    void cancelticket();
    void showseats();
}

final class Cinema implements Ticket{
    String name;
    int seatnumber,price;
    boolean isbooked=false;

    Cinema(String name,int seatnumber,int price){
        this.name=name;
        this.seatnumber=seatnumber;
        this.price=price;
    }

    public void bookticket(){
        if(!isbooked){
            isbooked=true;
            System.out.println("Ticket booked successfully for "+name);
        }else{
            System.out.println("Sorry, the seat is already booked for "+name);
        }
    }

    public void cancelticket(){
        if(isbooked){
            isbooked=false;
            System.out.println("Ticket cancelled successfully for "+name);
        }else{
            System.out.println("No ticket to cancel for "+name);
        }
    }

    public void showseats(){
        if(isbooked){
            System.out.println("Seat is booked for "+name);
        }else{
            System.out.println("Seat is available for "+name);
        }
    }
}

public class CinemaBook{
    public static void main(String[] args){
        Cinema a[]=new Cinema[3];
        a[0]=new Cinema("Movie A",1,100);
        a[1]=new Cinema("Movie B",2,150);
        a[2]=new Cinema("Movie C",3,200);
        int t=0;
        for(int i=0;i<3;i++){
            a[i].showseats();
        }
        for(int i=0;i<3;i++){
            a[i].bookticket();
            t+=a[i].price;
        }
        System.out.println("Total amount: "+t);
    }
}
