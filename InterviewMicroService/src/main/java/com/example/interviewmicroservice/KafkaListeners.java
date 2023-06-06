package com.example.interviewmicroservice;

import com.example.interviewmicroservice.config.MessageConsume;
import com.example.interviewmicroservice.models.Provider;
import com.example.interviewmicroservice.repositories.ProviderRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaListeners {

    private final ProviderRepository providerRepository;
    @KafkaListener(topics = "topicAddProvider", groupId = "groupId1", containerGroup = "messageFactory")
    void listener1(MessageConsume data) {
        Provider provider =
                Provider.builder()
                        .providerId(data.getUserId())
                        .email(data.getEmail())
                        .username(data.getUsername())
                        .days(null)
                        .slots(null)
                        .appointments(null)
                        .build();

        providerRepository.save(provider);
        System.out.println("Listener received: " + data + " ");
    }

    @KafkaListener(topics = "topicUpdateProvider", groupId = "groupId2", containerGroup = "messageFactory")
    void listener2(MessageConsume data) {
        Provider provider =
                Provider.builder()
                        .providerId(data.getUserId())
                        .email(data.getEmail())
                        .username(data.getUsername())
                        .days(null)
                        .slots(null)
                        .appointments(null)
                        .build();

        providerRepository.save(provider);
        System.out.println("Listener received: " + data + " ");
    }
    @KafkaListener(topics = "topicDeleteProvider", groupId = "groupId3", containerGroup = "messageFactory")
    void listener3(MessageConsume data) {
        Provider provider = providerRepository.findByEmail(data.getEmail()).orElse(
                providerRepository.findByUsername(data.getUsername()).get()
        );

        providerRepository.delete(provider);
        System.out.println("Listener received: " + data + " ");
    }
}