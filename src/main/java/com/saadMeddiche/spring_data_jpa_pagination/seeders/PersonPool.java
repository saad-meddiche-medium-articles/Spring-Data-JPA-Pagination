package com.saadMeddiche.spring_data_jpa_pagination.seeders;

import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PersonPool {

    private static final Person SENTINEL = new Person();

    private final BlockingQueue<Person> queue;

    public PersonPool(int capacity) {
        this.queue = new ArrayBlockingQueue<>(capacity);
    }

    public void produce(Person person) throws InterruptedException {
        queue.put(person);
    }

    public Person consume() throws InterruptedException {
        return queue.take();
    }

    public boolean isSentinel(Person person) {
        return person == SENTINEL;
    }

    public void finish(int watcherCount) {
        for (int i = 0; i < watcherCount; i++) {
            queue.add(SENTINEL);
        }
    }

}