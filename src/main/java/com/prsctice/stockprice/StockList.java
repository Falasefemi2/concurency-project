package com.prsctice.stockprice;

import java.util.List;

public class StockList {

    public static List<Stock> getStocks() {
        return List.of(
                new Stock("AAPL"),
                new Stock("TSLA"),
                new Stock("GOOG"),
                new Stock("MSFT"),
                new Stock("AMZN"),
                new Stock("NFLX"),
                new Stock("META"),
                new Stock("NVDA"),
                new Stock("ORCL"),
                new Stock("INTC")
        );
    }
}
