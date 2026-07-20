package com.saadMeddiche.spring_data_jpa_pagination.seeders;

import com.saadMeddiche.spring_data_jpa_pagination.configuration.PersonSeederConfig;
import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import com.saadMeddiche.spring_data_jpa_pagination.entities.Product;
import com.saadMeddiche.spring_data_jpa_pagination.utils.FastUUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
public class PersonGenerator implements Runnable {

    private final PersonPool pool;
    private final PersonSeederConfig config;

    @Override
    public void run() {

        Faker faker = new Faker();
        long personCount = config.getPersonCount();
        int productPerPerson = config.getProductPerPerson();
        int watcherCount = config.getThreadCount();

        try {

            for (long i = 0; i < personCount; i++) {

                if (Thread.currentThread().isInterrupted()) {
                    log.warn("Generator interrupted, finishing early ({} / {})", i, personCount);
                    return;
                }

                pool.produce(createPerson(faker, productPerPerson));

            }

            log.info("Generator finished: {} persons produced", personCount);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            pool.finish(watcherCount);
        }

    }

    private Person createPerson(Faker faker, int productPerPerson) {

        Person person = new Person();

        person.setFirstName(faker.name().firstName());
        person.setLastName(faker.name().lastName());
        person.setBirthDate(faker.timeAndDate().birthday());

        for (int i = 0; i < productPerPerson; i++)
            person.addProduct(createProduct(faker));

        return person;
    }

    private Product createProduct(Faker faker) {

        Product product = new Product();

        product.setName(faker.food().dish());
        product.setDescription(faker.lorem().paragraph(10));
        product.setSerial(FastUUID.randomUUID());
        product.setPrice(BigDecimal.valueOf(faker.number().randomDouble(2, 5, 100)));

        return product;

    }

}