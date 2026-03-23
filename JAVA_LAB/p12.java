interface CreditCardPayment {
    void validatecard(String cardnumber);
    void processPayment(double amount);
}

interface WalletPayment {
    void checkbalance();
    void processpayment(double amount);
}

class DigitalPayment implements CreditCardPayment, WalletPayment {
    private String name;
    private double balance;

    DigitalPayment(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    @Override
    public void validatecard(String cardnumber) {
        if (cardnumber == null || cardnumber.length() < 12 || cardnumber.length() > 19 || !cardnumber.matches("\\d+")) {
            System.out.println("Invalid credit card number: " + cardnumber);
        } else {
            System.out.println("Credit card number " + cardnumber + " is valid.");
        }
    }

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing credit card payment of " + amount + " for " + name);
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Payment successful. New balance: " + balance);
        } else {
            System.out.println("Payment failed. Insufficient balance (" + balance + ").");
        }
    }

    @Override
    public void checkbalance() {
        System.out.println(name + " wallet balance: " + balance);
    }

    @Override
    public void processpayment(double amount) {
        System.out.println("Processing wallet payment of " + amount + " for " + name);
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Wallet payment successful. New balance: " + balance);
        } else {
            System.out.println("Wallet payment failed. Insufficient balance (" + balance + ").");
        }
    }

    public static void main(String[] args) {
        DigitalPayment dp = new DigitalPayment("Tester", 500.0);

        dp.checkbalance();
        dp.validatecard("4111111111111111");
        dp.processPayment(150.0);
        dp.checkbalance();

        dp.processpayment(200.0);
        dp.checkbalance();

        dp.processpayment(2000.0);
    }
}