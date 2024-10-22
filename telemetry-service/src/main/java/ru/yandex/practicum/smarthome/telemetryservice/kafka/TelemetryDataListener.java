package ru.yandex.practicum.smarthome.telemetryservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.telemetryservice.model.TelemetryData;
import ru.yandex.practicum.smarthome.telemetryservice.model.TelemetryDataDTO;
import ru.yandex.practicum.smarthome.telemetryservice.repository.TelemetryDataRepository;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TelemetryDataListener {

    private final TelemetryDataRepository telemetryDataRepository;
    private static final Logger logger = Logger.getLogger(TelemetryDataListener.class.getName());

    public TelemetryDataListener(TelemetryDataRepository telemetryDataRepository) {
        this.telemetryDataRepository = telemetryDataRepository;
    }

    @KafkaListener(topics = "sensor_data")
    public void listenTelemetryData(TelemetryDataDTO telemetryDataDTO) {
        TelemetryData telemetryData = new TelemetryData();
        telemetryData.setDeviceId(telemetryDataDTO.getDeviceId());
        telemetryData.setTemperature(telemetryDataDTO.getTemperature());
        telemetryData.setTimestamp(LocalDateTime.now());
        logger.log(Level.INFO, "Received telemetry data: {0}", telemetryDataDTO);

        telemetryDataRepository.save(telemetryData);
    }
}