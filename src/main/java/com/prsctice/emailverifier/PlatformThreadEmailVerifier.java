package com.prsctice.emailverifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PlatformThreadEmailVerifier {

    public static void verifyEmail(String email) {
        try {
            Thread.sleep(100);
            System.out.println("Verified: " + email);
        } catch (InterruptedException e) {
            System.out.println("Error Verifying: " + email);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(100);
        List<String> emails = Files.readAllLines(Path.of("src/email.txt"));

        long start = System.currentTimeMillis();

        for (String email : emails) {
            service.submit(() -> verifyEmail(email));
        }

        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
        long end = System.currentTimeMillis();
        System.out.println("Finished verifying: " + emails.size() + " emails in " + (end - start) + "ms");
    }
}
