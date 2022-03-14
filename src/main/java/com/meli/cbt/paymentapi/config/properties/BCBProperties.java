package com.meli.cbt.paymentapi.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "banco-central")
public class BCBProperties {

    @NotBlank
    private String url;
}



