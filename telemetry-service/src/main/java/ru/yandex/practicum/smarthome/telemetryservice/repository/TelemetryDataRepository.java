package ru.yandex.practicum.smarthome.telemetryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.smarthome.telemetryservice.model.TelemetryData;

import java.util.UUID;

public interface TelemetryDataRepository extends JpaRepository<TelemetryData, UUID> {
}