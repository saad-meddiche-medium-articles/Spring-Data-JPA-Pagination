package com.saadMeddiche.spring_data_jpa_pagination.seeders;

import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import com.saadMeddiche.spring_data_jpa_pagination.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component @Slf4j
@RequiredArgsConstructor
public class PersonSeeder implements CommandLineRunner {

    private final static long count = 1_000_000;

    private final Faker faker;
    private final PersonRepository personRepository;

    @Override
    public void run(String... args) {

        log.info("Starting seeding...");

        long start = System.currentTimeMillis();

        List<Person> people = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            Person person = this.createPerson();

            people.add(person);

        }

        this.personRepository.saveAll(people);

        long end = System.currentTimeMillis();

        log.info("Finished seeding in {}ms", (end - start));

    }

    private Person createPerson() {

        Person person = new Person();

        person.setFirstName(faker.name().firstName());
        person.setLastName(faker.name().lastName());
        person.setBirthDate(faker.timeAndDate().birthday());

        return person;
    }

}