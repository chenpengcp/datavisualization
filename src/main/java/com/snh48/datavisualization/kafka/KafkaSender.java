package com.snh48.datavisualization.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public String send(String json) {
        kafkaTemplate.send("test", json);
        return "success";
    }
}
