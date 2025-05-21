package com.prsctice.ticketbooking;

import java.util.List;
import java.util.Random;

public class ExternalServices {

    private static final Random random = new Random();

    /**
     * Simulates browsing venues with network latency
     */
    public static Venue browseVenues(List<Venue> venues) throws InterruptedException {
        simulateNetworkLatency(100, 500);
        return venues.get(random.nextInt(venues.size()));
    }

    /**
     * Checks ticket availability with service latency
     */
    public static int checkAvailability(Venue venue) throws InterruptedException {
        simulateNetworkLatency(200, 700);
        return venue.availableTickets;
    }

    /**
     * Simulates payment processing with potential failures
     */
    public static boolean processPayment(int userId) throws InterruptedException {
        simulateNetworkLatency(500, 2000);

        // 10% chance of payment failure
        return random.nextDouble() > 0.1;
    }

    /**
     * Books a ticket if available
     */
    public static String bookTicket(Venue venue, int userId) throws InterruptedException {
        simulateNetworkLatency(300, 800);

        boolean success = venue.bookTicket(userId);
        if (success) {
            return "TKT-" + venue.name.substring(0, 3).toUpperCase() + "-" + userId;
        }
        return null;
    }

    /**
     * Sends booking confirmation
     */
    public static void sendConfirmation(int userId, String ticketId, Venue venue) throws InterruptedException {
        simulateNetworkLatency(200, 600);
        // In a real application, this might send an email or SMS
    }

    /**
     * Simulates network latency between min and max milliseconds
     */
    private static void simulateNetworkLatency(int minMs, int maxMs) throws InterruptedException {
        Thread.sleep(minMs + random.nextInt(maxMs - minMs));
    }

}
