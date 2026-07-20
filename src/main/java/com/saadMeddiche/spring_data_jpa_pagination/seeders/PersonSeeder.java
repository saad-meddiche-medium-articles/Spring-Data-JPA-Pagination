package com.saadMeddiche.spring_data_jpa_pagination.seeders;

import com.saadMeddiche.spring_data_jpa_pagination.configuration.PersonSeederConfig;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component @Slf4j
@RequiredArgsConstructor
@ConditionalOnBooleanProperty(prefix = "person-seeder", value = "enabled")
public class PersonSeeder implements CommandLineRunner {

    private final PersonSeederConfig config;
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public void run(String... args) {
        log.info("Starting seeding with {} watchers...", config.getThreadCount());
        long start = System.currentTimeMillis();
        mainProcess();
        long end = System.currentTimeMillis();
        log.info("Finished seeding in {}ms", (end - start));
    }

    private void mainProcess() {

        int watcherCount = config.getThreadCount();
        int poolCapacity = watcherCount * config.getChunkSize();

        PersonPool pool = new PersonPool(poolCapacity);
        PersonGenerator generator = new PersonGenerator(pool, config);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            executor.submit(generator);

            for (int i = 0; i < watcherCount; i++) {
                executor.submit(new PersonWatcher(pool, config, entityManagerFactory));
            }

        }

    }

}