package com.snh48.datavisualization.kafka;


import com.alibaba.fastjson.JSON;
import com.snh48.datavisualization.dao.UrlDao;
import com.snh48.datavisualization.pojo.Record;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaReceiver.class);
    @Autowired
    private UrlDao urlDao;

    @KafkaListener(topics = {"test"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            LOG.info("成功接收：" + String.valueOf(message));
            Record re = JSON.parseObject(String.valueOf(message), Record.class);
            urlDao.insertRecord(re);
        }
    }
}
