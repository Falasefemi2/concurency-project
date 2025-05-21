package com.prsctice.weatherdashboard;

import java.util.concurrent.StructuredTaskScope;

public class DashboardFetcher {

    // Demo to fetch weather
    private static String fetchWeather() throws InterruptedException {
        Thread.sleep(1000);
        return "🌦️ Weather: 27°C, Sunny";
    }

    // Demo to fetch news
    private static String fetchNews() throws InterruptedException {
        Thread.sleep(1500);
        return "📰 News: Java 21 Released!";
    }

    // Demo to fecth stock market data
    private static String fetchStocks() throws InterruptedException {
        Thread.sleep(1200);
        return "📈 Stock: JavaCorp +4.5%";
    }

    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var weatherScope = scope.fork(() -> fetchWeather());
            var newsScope = scope.fork(() -> fetchNews());
            var stockScope = scope.fork(() -> fetchStocks());

            scope.join(); // wait for all to complete
            scope.throwIfFailed(); // cancel if one failed

            String dashboard = """
                    DASHBOARD LOADED:
                    ---------------------
                    %s
                    %s
                    %s
                    """.formatted(weatherScope.get(), newsScope.get(), stockScope.get());

            System.out.println(dashboard);
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("Total Time: " + time + "ms");
    }
}
