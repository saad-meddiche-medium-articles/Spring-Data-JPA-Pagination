package com.saadMeddiche.spring_data_jpa_pagination.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@Component
@ConditionalOnProperty(name = "database.indexes.clean", havingValue = "true", matchIfMissing = false)
public class DatabaseIndexCleaner {

    private final DataSource dataSource;

    public DatabaseIndexCleaner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void removeIndexes() {
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {

            statement.execute("DROP INDEX IF EXISTS idx_person_fullname_trgm");
            log.info("idx_person_fullname_trgm index dropped");

            statement.execute("DROP EXTENSION IF EXISTS pg_trgm");
            log.info("pg_trgm extension dropped");

            log.info("Index cleanup complete");

        } catch (SQLException e) {
            log.warn("Could not clean up indexes: {}", e.getMessage());
        }
    }
}
