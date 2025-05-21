package com.prsctice.smsbulk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SMSBulk {

    public static void sendSms(String number) {
        try {
            Thread.sleep(500);
            System.out.println("SMS sent to " + number);
        } catch (InterruptedException e) {
            System.out.println("Failed to send SMS: " + number);
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> phones = Files.readAllLines(Path.of("src/phones.txt"));

        long start = System.currentTimeMillis();

        try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
            for (String phone : phones) {
                service.submit(() -> {
                    sendSms(phone);
                });
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Finished verifying: " + phones.size() + " emails in " + (end - start) + "ms");

    }
}
