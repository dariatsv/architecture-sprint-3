package ru.yandex.practicum.smarthome.devicemanagement.kafka;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.yandex.practicum.smarthome.devicemanagement.model.DeviceStatusMessage;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, DeviceStatusMessage> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, DeviceStatusMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    public static final String DEVICE_COMMANDS_TOPIC = "device_commands";
    public static final String DEVICE_STATUSES_TOPIC = "device_statuses";

    @Bean
    public NewTopic deviceCommandsTopic() {
        return new NewTopic(DEVICE_COMMANDS_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic deviceStatusesTopic() {
        return new NewTopic(DEVICE_STATUSES_TOPIC, 1, (short) 1);
    }
}