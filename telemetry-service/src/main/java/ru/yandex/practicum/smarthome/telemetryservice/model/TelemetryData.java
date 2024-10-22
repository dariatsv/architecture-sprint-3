package ru.yandex.practicum.smarthome.telemetryservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "telemetry_data")
public class TelemetryData {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID deviceId;

    private Double temperature;

    private LocalDateTime timestamp;
}