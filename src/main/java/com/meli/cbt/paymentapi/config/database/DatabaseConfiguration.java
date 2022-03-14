package com.meli.cbt.paymentapi.config.database;

import com.meli.cbt.paymentapi.config.properties.DatabaseProperties;
import io.r2dbc.spi.ConnectionFactory;
import lombok.AllArgsConstructor;
import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class DatabaseConfiguration {

    private DatabaseProperties properties;

    @Bean
    ConnectionFactory connectionFactory() {
        return new MariadbConnectionFactory(getDatabaseConfiguration());
    }

    private MariadbConnectionConfiguration getDatabaseConfiguration() {
        return MariadbConnectionConfiguration.builder()
                .host(properties.getHost())
                .port(properties.getPort())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .database(properties.getDatabase())
                .build();
    }




}
