package com.prsctice.driverdelivery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

public class DriverLocationApp {

    public static void main(String[] args) throws Exception {
        List<Driver> drivers = DriverList.getDrivers();

        long start = System.currentTimeMillis();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<StructuredTaskScope.Subtask<String>> tasks = new ArrayList<>();

            for (Driver driver : drivers) {
                var task = scope.fork(() -> GPSFetcher.fetchLocation(driver));
                tasks.add(task);
            }

            scope.join();
            scope.throwIfFailed();

            System.out.println("\n Driver Location");
            for (var task : tasks) {
                System.out.println(task.get());
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("\n All locations fetched in " + (end - start) + " ms");
    }
}
