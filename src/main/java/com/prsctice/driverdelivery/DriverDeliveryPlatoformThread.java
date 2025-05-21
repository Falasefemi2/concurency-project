package com.prsctice.driverdelivery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class DriverDeliveryPlatoformThread {

    public static void main(String[] args) throws Exception {
        List<Driver> drivers = DriverList.getDrivers();
        long start = System.currentTimeMillis();

        ExecutorService service = Executors.newFixedThreadPool(10);
        List<Future<String>> futures = new ArrayList<>();

        for (Driver driver : drivers) {
            Future<String> future = service.submit(() -> GPSFetcher.fetchLocation(driver));
            futures.add(future);
        }

        System.out.println("\n Driver Location");
        for (Future<String> future : futures) {
            try {
                System.out.println(future.get());
            } catch (ExecutionException e) {
                System.out.println("Error fetching location: " + e.getCause().getMessage());
            }
        }

        service.shutdown();
        if (!service.awaitTermination(30, TimeUnit.SECONDS)) {
            service.shutdownNow();
        }

        long end = System.currentTimeMillis();
        System.out.println("\n All locations fetched in " + (end - start) + " ms");
    }
}
