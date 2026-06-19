package com.saadMeddiche.spring_data_jpa_pagination.seeders;

import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import com.saadMeddiche.spring_data_jpa_pagination.repositories.PersonRepository;
import jakarta.persistence.EntityManager;
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

    private final static long COUNT = 1_000_000;
    private final static int CHUNK_SIZE = 25_000;

    private final Faker faker;
    private final EntityManager entityManager;
    private final PersonRepository personRepository;

    @Override
    public void run(String... args) {

        log.info("Starting seeding...");

        long start = System.currentTimeMillis();

        this.main_process();

        long end = System.currentTimeMillis();

        log.info("Finished seeding in {}ms", (end - start));

    }

    private void main_process() {

        // Haha it sounds weird
        List<Person> chunkOfPeople = new ArrayList<>(CHUNK_SIZE);

        for (int i = 0; i < COUNT; i++) {

            if(chunkOfPeople.size() == CHUNK_SIZE) {
                log.info("Inserting chunk#{}", i / CHUNK_SIZE );
                this.save_chunk_and_clear(chunkOfPeople);
            }

            Person person = this.createPerson();

            chunkOfPeople.add(person);

        }

        if(!chunkOfPeople.isEmpty()) {
            log.info("Inserting last chunk");
            this.save_chunk_and_clear(chunkOfPeople);
        }

    }

    private void save_chunk_and_clear(List<Person> chunkOfPeople) {
        this.personRepository.saveAll(chunkOfPeople);
        chunkOfPeople.clear();
        this.entityManager.clear();
    }

    private Person createPerson() {

        Person person = new Person();

        person.setFirstName(faker.name().firstName());
        person.setLastName(faker.name().lastName());
        person.setBirthDate(faker.timeAndDate().birthday());

        return person;

    }

}