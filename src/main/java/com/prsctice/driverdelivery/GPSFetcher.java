package com.prsctice.driverdelivery;

public class GPSFetcher {

    public static String fetchLocation(Driver driver) throws InterruptedException {
        Thread.sleep((long) (Math.random()) * 2000); // stimulate delay

        // Genrate rando, latitude between -90 and 90
        double latitude = Math.round((Math.random() * 180 - 90) * 100.0) / 100.0;

        // Generate random longitude between -180 and 180
        double longitude = Math.round((Math.random() * 360 - 90) * 100.0) / 100.0;

        return "Driver " + driver.name() + "(ID: " + driver.id()
                + ") is at [Lat: " + latitude + ", Lon: " + longitude + "]";
    }
}
