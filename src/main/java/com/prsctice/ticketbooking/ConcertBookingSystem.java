package com.prsctice.ticketbooking;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcertBookingSystem {

    private static void runBookingSimulation(List<Venue> venues, int userCount, boolean useVirtualThreads) throws Exception {
        AtomicInteger successfultBookings = new AtomicInteger(0);
        AtomicInteger failedBookings = new AtomicInteger(0);

        Instant start = Instant.now();

        if (useVirtualThreads) {
            // using structuredscope (virtual threads)
            try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Void>()) {
                for (int i = 0; i < userCount; i++) {
                    int userId = i;
                    scope.fork(() -> {
                        processBooking(venues, userId, successfultBookings, failedBookings);
                        return null;
                    });
                }
                scope.join();
            }
        } else {
            // Using traditional ExecutorService with platform threads
            // Limiting thread pool size to avoid system resource exhaustion
            int threadPoolSize = Math.min(100, Runtime.getRuntime().availableProcessors() * 2);
            ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < userCount; i++) {
                int userId = i;
                futures.add(executor.submit(() -> {
                    processBooking(venues, userId, successfultBookings, failedBookings);
                    return null;
                }));
            }

            // Wait for all tasks to complete
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    System.err.println("Booking error: " + e.getCause().getMessage());
                }
            }
            executor.shutdown();
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);

        System.out.println("Booking simulation completed!!");
        System.out.println("Thread type: " + (useVirtualThreads ? "Virtual Threads" : "Platform Threads"));
        System.out.println("Total users: " + userCount);
        System.out.println("Successful bookings: " + successfultBookings.get());
        System.out.println("Failed bookings: " + failedBookings.get());
        System.out.println("Total execution time: " + duration.toMillis() + " ms");

        // Print venue status after bookings
        System.out.println("\nVenue Status After Booking:");
        venues.forEach(venue -> {
            System.out.println(venue.name + ": " + venue.bookedTickets.size() + " tickets booked, "
                    + venue.availableTickets + " tickets remaining");
        });
    }

    private static void processBooking(
            List<Venue> venues,
            int userId,
            AtomicInteger successfultBookings,
            AtomicInteger failedBookings) {

        try {
            // Step 1: Choose a random venue (simulates user browsing)
            Venue selectedVenue = ExternalServices.browseVenues(venues);

            // Step 2: Check ticket availability
            int availableTickets = ExternalServices.checkAvailability(selectedVenue);

            if (availableTickets > 0) {
                // Step 3: Process payment (this step might fail)
                boolean paymentSuccess = ExternalServices.processPayment(userId);

                if (paymentSuccess) {
                    // Step 4: Book the ticket
                    String ticketId = ExternalServices.bookTicket(selectedVenue, userId);

                    if (ticketId != null) {
                        // Step 5: Send confirmation
                        ExternalServices.sendConfirmation(userId, ticketId, selectedVenue);
                        successfultBookings.incrementAndGet();
                    } else {
                        failedBookings.incrementAndGet();
                    }
                } else {
                    failedBookings.incrementAndGet();
                }
            } else {
                failedBookings.incrementAndGet();
            }
        } catch (Exception e) {
            failedBookings.incrementAndGet();
            System.err.println("Error processing booking for user " + userId + ": " + e.getMessage());
        }
    }

    private static List<Venue> createVenues() {
        List<Venue> venues = new ArrayList<>();
        venues.add(new Venue("Madison Square Garden", 300));
        venues.add(new Venue("O2 Arena", 250));
        venues.add(new Venue("Hollywood Bowl", 200));
        venues.add(new Venue("Red Rocks Amphitheatre", 150));
        venues.add(new Venue("Royal Albert Hall", 175));
        return venues;
    }

    public static void main(String[] args) throws Exception {
        // Number of concurrent users trying to book tickets
        final int CONCURRENT_USERS = 1000;

        // Create concert venues with available tickets
        List<Venue> venues = createVenues();

        // Run booking simulation with virtual threads
        System.out.println("\n=== RUNNING WITH VIRTUAL THREADS ===");
        runBookingSimulation(venues, CONCURRENT_USERS, true);

        // Reset venues for fair comparison
        venues = createVenues();

        // Run booking simulation with platform threads
        System.out.println("\n=== RUNNING WITH PLATFORM THREADS ===");
        runBookingSimulation(venues, CONCURRENT_USERS, true);

    }
}
