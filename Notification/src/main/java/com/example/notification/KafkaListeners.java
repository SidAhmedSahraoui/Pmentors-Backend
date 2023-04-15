package com.example.notification;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(
            topics = "notificationTopic",
            groupId = "groupId",
            containerFactory = "")
    void listener(String data){
        System.out.println("finally " + data + "  \uD83E\uDD79");
    }
}
