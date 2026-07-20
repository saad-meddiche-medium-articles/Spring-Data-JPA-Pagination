package com.saadMeddiche.spring_data_jpa_pagination.seeders;

import com.saadMeddiche.spring_data_jpa_pagination.configuration.PersonSeederConfig;
import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PersonWatcher implements Runnable {

    private final PersonPool pool;
    private final PersonSeederConfig config;
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public void run() {

        EntityManager em = entityManagerFactory.createEntityManager();
        int chunkSize = config.getChunkSize();
        long count = 0;

        try {
            em.getTransaction().begin();

            while (true) {
                Person person = pool.consume();
                if (pool.isSentinel(person)) break;

                em.persist(person);
                count++;

                if (count % chunkSize == 0) {
                    em.flush();
                    em.getTransaction().commit();
                    em.clear();
                    em.getTransaction().begin();
                    log.info("Watcher inserted {} people", count);
                }
            }

            em.flush();
            em.getTransaction().commit();
            log.info("Watcher completed: {} people", count);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Watcher interrupted after {} people", count);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            log.error("Watcher failed after {} people", count, e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }

    }

}