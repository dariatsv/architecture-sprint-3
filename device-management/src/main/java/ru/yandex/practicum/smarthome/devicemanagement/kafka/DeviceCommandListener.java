package ru.yandex.practicum.smarthome.devicemanagement.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.devicemanagement.model.DeviceCommand;
import ru.yandex.practicum.smarthome.devicemanagement.service.DeviceService;

@Component
public class DeviceCommandListener {

    private final DeviceService deviceService;

    public DeviceCommandListener(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @KafkaListener(topics = "device_commands")
    public void listenDeviceCommands(ConsumerRecord<String, DeviceCommand> commandRecord) {
        DeviceCommand deviceCommand = commandRecord.value();
        deviceService.processDeviceCommand(deviceCommand);
    }
}