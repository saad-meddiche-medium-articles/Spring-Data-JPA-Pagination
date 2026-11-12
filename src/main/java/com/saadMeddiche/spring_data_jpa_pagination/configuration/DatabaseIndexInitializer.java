package com.saadMeddiche.spring_data_jpa_pagination.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@Component
@ConditionalOnProperty(name = "database.indexes.enabled", havingValue = "true", matchIfMissing = false)
public class DatabaseIndexInitializer {

    private final DataSource dataSource;

    public DatabaseIndexInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void createIndexes() {
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {

            statement.execute("CREATE EXTENSION IF NOT EXISTS pg_trgm");
            log.info("pg_trgm extension ready");

            statement.execute("""
                    CREATE INDEX IF NOT EXISTS idx_person_fullname_trgm
                    ON person USING gin (lower(first_name || ' ' || last_name) gin_trgm_ops)
                    """);
            log.info("idx_person_fullname_trgm index created/verified");

            statement.execute("ANALYZE person");
            log.info("person table analyzed for index usage");

        } catch (SQLException e) {
            log.warn("Could not create fullname trigram index: {}", e.getMessage());
        }
    }
}
