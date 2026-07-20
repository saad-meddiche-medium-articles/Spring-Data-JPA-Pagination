package com.saadMeddiche.spring_data_jpa_pagination.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter @Setter
@ConfigurationProperties("person-seeder")
public class PersonSeederConfig {

    private long personCount = 1_000_000;

    private int productPerPerson = 2;

    private int chunkSize = 25_000;

    private int threadCount = Runtime.getRuntime().availableProcessors();

    private boolean enabled = true;

}