package com.saadMeddiche.spring_data_jpa_pagination.seeders;

import com.saadMeddiche.spring_data_jpa_pagination.configuration.PersonSeederConfig;
import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import com.saadMeddiche.spring_data_jpa_pagination.entities.Product;
import com.saadMeddiche.spring_data_jpa_pagination.repositories.PersonRepository;
import com.saadMeddiche.spring_data_jpa_pagination.utils.FastUUID;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component @Slf4j
@RequiredArgsConstructor
@ConditionalOnBooleanProperty(prefix = "person-seeder", value = "enabled")
public class PersonSeeder implements CommandLineRunner {

    private final Faker faker;
    private final PersonSeederConfig config;
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
        List<Person> chunkOfPeople = new ArrayList<>(config.getChunkSize());

        for (long i = 0; i < config.getPersonCount(); i++) {

            if(chunkOfPeople.size() == config.getChunkSize()) {
                log.info("Inserting chunk#{}", i / config.getChunkSize() );
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

        for (int i = 0; i < config.getProductPerPerson(); i++)
            person.addProduct(this.createProduct());

        return person;

    }

    private Product createProduct() {

        Product product = new Product();

        product.setName(faker.food().dish());
        product.setDescription(faker.lorem().paragraph(10));
        product.setSerial(FastUUID.randomUUID());
        product.setPrice(BigDecimal.valueOf(faker.number().randomDouble(2, 5, 100)));

        return product;

    }

}