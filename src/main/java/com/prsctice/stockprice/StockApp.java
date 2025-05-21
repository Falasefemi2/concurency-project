package com.prsctice.stockprice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

public class StockApp {

    public static void main(String[] args) throws Exception {
        List<Stock> stocks = StockList.getStocks();

        long start = System.currentTimeMillis();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<StructuredTaskScope.Subtask<String>> tasks = new ArrayList<>();

            for (Stock stock : stocks) {
                var task = scope.fork(() -> StockApi.fetchPrices(stock));
                tasks.add(task);
            }

            scope.join();
            scope.throwIfFailed();

            System.out.println("\n Fetching stock prices......");
            for (var subtask : tasks) {
                System.out.println(subtask.get());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("\n All stock prices fetched in " + (end - start) + " ms");

    }
}
