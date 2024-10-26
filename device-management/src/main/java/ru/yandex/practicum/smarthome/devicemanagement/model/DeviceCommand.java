package ru.yandex.practicum.smarthome.devicemanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DeviceCommand {
    private UUID deviceId;
    private String command;
    private Map<String, Object> parameters;
}