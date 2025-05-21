package com.prsctice.ticketbooking;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class Venue {

    String name;
    int availableTickets;
    Set<String> bookedTickets = Collections.synchronizedSet(new HashSet<>());

    public Venue(String name, int availableTickets) {
        this.name = name;
        this.availableTickets = availableTickets;
    }

    public synchronized boolean bookTicket(int userId) {
        if (availableTickets > 0) {
            availableTickets--;
            String ticketId = UUID.randomUUID().toString();
            bookedTickets.add(ticketId + "-" + userId);
            return true;
        }
        return false;
    }
}
