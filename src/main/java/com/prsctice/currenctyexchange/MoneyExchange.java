package com.prsctice.currenctyexchange;

import java.util.concurrent.StructuredTaskScope;

public class MoneyExchange {

    public static String fetchUSDTONGN(double amount) throws InterruptedException {
        Thread.sleep(1000);
        double rate = 1480.50;
        double total = amount * rate;
        return String.format("USD → NGN: ₦%.2f (Amount: $%.2f = ₦%.2f)", rate, amount, total);
    }

    public static String fetchEURToNGN(double amount) throws InterruptedException {
        Thread.sleep(1000);
        double rate = 1625.00;
        double total = amount * rate;
        return String.format("EUR → NGN: ₦%.2f (Amount: €%.2f = ₦%.2f)", rate, amount, total);
    }

    public static String fetchGBPToNGN(double amount) throws InterruptedException {
        Thread.sleep(1000);
        double rate = 1895.20;
        double total = amount * rate;
        return String.format("GBP → NGN: ₦%.2f (Amount: £%.2f = ₦%.2f)", rate, amount, total);
    }

    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var usdToNgn = scope.fork(() -> fetchUSDTONGN(5000));
            var eurToNgn = scope.fork(() -> fetchEURToNGN(5000));
            var gbpToNgn = scope.fork(() -> fetchGBPToNGN(5000));

            scope.join();
            scope.throwIfFailed();

            System.out.println("\n Exchange Rates (NGN)");
            System.out.println("----------------------");
            System.out.println(usdToNgn.get());
            System.out.println(eurToNgn.get());
            System.out.println(gbpToNgn.get());

            long end = System.currentTimeMillis();
            System.out.println("\n All rates fetched in " + (end - start) + "ms");
        }
    }
}
