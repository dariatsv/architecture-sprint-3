package ru.yandex.practicum.smarthome.telemetryservice.model;

import lombok.Data;

import java.util.UUID;

@Data
public class TelemetryDataDTO {
    private UUID deviceId;
    private Double temperature;
}