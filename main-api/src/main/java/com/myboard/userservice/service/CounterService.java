package com.myboard.userservice.service;

import org.springframework.stereotype.Service;

@Service
public class CounterService {
    private int counter = 0;

    public synchronized int incrementCounter() {
        counter++;
        return counter;
    }

    public int getCounter() {
        return counter;
    }
}
