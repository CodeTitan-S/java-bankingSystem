package features;

import utils.InputHelper;

public class FDCalculator {

    public static void launch() {
        System.out.println("\n=== Fixed Deposit Calculator ===");
        while (true) {
            double principal = InputHelper.getPositiveDouble("Principal amount: ₹");
            double rate = InputHelper.getPositiveDouble("Annual interest rate (%): ");
            int years = InputHelper.getInt("Duration (years): ");
            if (years <= 0) {
                System.out.println("Duration must be positive.");
                continue;
            }
            double maturity = principal * Math.pow(1 + rate / 100, years);
            double interest = maturity - principal;
            System.out.printf("Principal:        ₹%.2f%n", principal);
            System.out.printf("Rate:             %.2f%% per annum%n", rate);
            System.out.printf("Duration:         %d years%n", years);
            System.out.printf("Interest Earned:  ₹%.2f%n", interest);
            System.out.printf("Maturity Amount:  ₹%.2f%n", maturity);

            String again = InputHelper.getString("Calculate another? (yes/no): ");
            if (!again.equalsIgnoreCase("yes")) {
                break;
            }
        }
    }
}