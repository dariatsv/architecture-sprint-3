package ru.yandex.practicum.smarthome.devicemanagement.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.devicemanagement.model.DeviceStatusMessage;

@Component
public class DeviceStatusProducer {

    private final KafkaTemplate<String, DeviceStatusMessage> kafkaTemplate;

    public DeviceStatusProducer(KafkaTemplate<String, DeviceStatusMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, DeviceStatusMessage message) {
        kafkaTemplate.send(topic, message);
    }
}