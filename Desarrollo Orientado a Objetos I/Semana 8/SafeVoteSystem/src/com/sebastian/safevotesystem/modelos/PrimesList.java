package com.sebastian.safevotesystem.modelos;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrimesList extends ArrayList<Integer> {

    private final Lock lock = new ReentrantLock();

    public boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean add(Integer num) {
        lock.lock();
        try {
            if (num == null || !isPrime(num)) {
                throw new IllegalArgumentException("Solo se pueden añadir números primos válidos.");
            }
            return super.add(num);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Integer remove(int id) {
        lock.lock();
        try {
            return super.remove(id);
        } finally {
            lock.unlock();
        }
    }

    public int getPrimesCount() {
        lock.lock();
        try {
            return size();
        } finally {
            lock.unlock();
        }
    }
}