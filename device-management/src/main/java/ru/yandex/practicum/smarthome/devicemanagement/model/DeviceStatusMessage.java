package ru.yandex.practicum.smarthome.devicemanagement.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatusMessage {
    @NotBlank
    private UUID deviceId;

    @NotNull
    private DeviceStatus status;
}