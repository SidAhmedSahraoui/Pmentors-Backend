package com.example.notification;

import com.example.notification.config.MessageRequest;
import com.example.notification.models.Notification;
import com.example.notification.models.NotificationRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaListeners {

    private final NotificationRepository notificationRepository;



    @KafkaListener(topics = "myTopic", groupId = "groupId", containerGroup = "messageFactory")
    void listener(MessageRequest data) {
        Notification notification =
                Notification.builder()
                        .notificationId(null)
                        .clientEmail(data.getClientEmail())
                        .providerEmail(data.getProviderEmail())
                        .clientPhone(data.getClientPhone())
                        .providerPhone(data.getProviderPhone())
                        .appointmentDate(data.getAppointmentDate())
                        .startsAt(data.getStartsAt())
                        .time(data.getTime())
                        .date(data.getDate())
                        .build();
        Twilio.init("AC6cae1612a8df038d4ae48d68a8e3e466",
                "874ab8ff6c877c0415576b6d59b41549");
        Message.creator(new PhoneNumber("+213655649000"),
                new PhoneNumber("+16202979623"), "Hello Sid Ahmed, you have a new appointment 📞").create();
        notificationRepository.save(notification);
        System.out.println("Listener received: " + data + " ");
    }

}