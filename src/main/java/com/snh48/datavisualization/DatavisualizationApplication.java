package com.snh48.datavisualization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DatavisualizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatavisualizationApplication.class, args);
    }

}
