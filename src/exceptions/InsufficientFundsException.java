package exceptions;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(double requested, double available) {
        super(String.format("Insufficient funds. Requested: ₹%.2f, Available: ₹%.2f",
                requested, available));
    }
}