package com.github.blvxme.itrumtesttask;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Import(FixtureConfiguration.class)
public abstract class BasicIntegrationTest {
    @Container
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18.1-alpine").withReuse(true);

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected Fixture fixture;

    @DynamicPropertySource
    public static void configureDatasourceProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgres::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgres::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgres::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.driverClassName", postgres::getDriverClassName);
    }

    @BeforeAll
    public static void startPostgres() {
        postgres.start();
    }

    @AfterAll
    public static void stopPostgres() {
        postgres.stop();
    }
}
