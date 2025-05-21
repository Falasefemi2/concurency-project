package com.prsctice.webcrawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class WebCrawler {

    // List of sample URLS to crawl  
    private static List<String> generateUrls(int count) {
        return List.of(
                "https://example.com",
                "https://httpstat.us/200?sleep=1000",
                "https://httpstat.us/500",
                "https://jsonplaceholder.typicode.com/posts/1",
                "https://jsonplaceholder.typicode.com/posts/2",
                "https://jsonplaceholder.typicode.com/posts/3",
                "https://jsonplaceholder.typicode.com/posts/4",
                "https://jsonplaceholder.typicode.com/posts/5",
                "https://jsonplaceholder.typicode.com/posts/6",
                "https://jsonplaceholder.typicode.com/posts/7",
                "https://jsonplaceholder.typicode.com/posts/8",
                "https://jsonplaceholder.typicode.com/posts/9",
                "https://jsonplaceholder.typicode.com/posts/10"
        );
    }

    // Sample method to fetch content from a URL
    private static String fetchUrl(String urlString) {
        try {
            URL url = URI.create(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line);
                }
                return "Success: " + urlString;
            }
        } catch (Exception e) {
            return "Failed: " + urlString + " | Reason: " + e.getMessage();
        }
    }

    // Method 1: Using Platform Threads with Fixed Thread Pool
    public static void crawlWithPlatformThreads(List<String> urls) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Future<String>> results = new ArrayList<>();

        Instant start = Instant.now();
        for (String url : urls) {
            results.add(executor.submit(() -> fetchUrl(url)));
        }
        for (Future<String> future : results) {
            try {
                System.out.println(future.get());
            } catch (Exception ignored) {
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        Instant end = Instant.now();

        System.out.println("\n Time with Platform Threads: " + Duration.between(start, end).toMillis() + "ms");
    }

    // Method 2: Using Virtual Threads
    public static void crawlWithVirtualThreads(List<String> urls) throws InterruptedException {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        List<Future<String>> results = new ArrayList<>();

        Instant start = Instant.now();
        for (String url : urls) {
            results.add(executor.submit(() -> fetchUrl(url)));
        }

        for (Future<String> future : results) {
            try {
                System.out.println(future.get());
            } catch (Exception ignored) {
            }
        }

        executor.close();
        Instant end = Instant.now();

        System.out.println("\n Time with Virtual Threads: " + Duration.between(start, end).toMillis() + "ms");
    }

    public static void main(String[] args) throws InterruptedException {
        List<String> urls = generateUrls(1000);

        System.out.println("\n===== Crawling with Platform Threads =====");
        crawlWithPlatformThreads(urls);

        System.out.println("\n===== Crawling with Virtual Threads =====");
        crawlWithVirtualThreads(urls);

    }
}
