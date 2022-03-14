package com.meli.cbt.paymentapi.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Validated
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "spring.r2dbc.properties")
public class DatabaseProperties {

    @NotBlank
    private String host;
    @NotNull
    private int port;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String database;
}
