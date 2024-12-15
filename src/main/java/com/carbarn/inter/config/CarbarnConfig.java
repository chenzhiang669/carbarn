package com.carbarn.inter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class CarbarnConfig {
    @Value("${carbarn.local.static.dir}")
    private String staticDir;

    @Value("${carbarn.local.cars.pictures.dir}")
    private String picturesDir;
}
