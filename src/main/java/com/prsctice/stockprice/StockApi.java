package com.prsctice.stockprice;

import java.util.Random;

public class StockApi {

    public static String fetchPrices(Stock stock) throws InterruptedException {
        Thread.sleep(new Random().nextInt(1500)); // simulate delay
        return stock.symbol() + ": " + String.format("$%.2f", 300 + (new Random().nextDouble() * 700)); // price btw $300 - $700
    }
}
