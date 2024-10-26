package ru.yandex.practicum.smarthome.devicemanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.smarthome.devicemanagement.model.Device;
import ru.yandex.practicum.smarthome.devicemanagement.model.DeviceStatus;
import ru.yandex.practicum.smarthome.devicemanagement.service.DeviceService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public List<Device> getDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/{deviceId}")
    public Device getDeviceById(@PathVariable UUID deviceId) {
        return deviceService.getDeviceById(deviceId);
    }

    @PostMapping
    public Device createDevice(@RequestBody Device device) {
        return deviceService.createDevice(device);
    }

    @PutMapping("/{deviceId}")
    public Device updateDevice(@PathVariable UUID deviceId, @RequestBody Device device) {
        return deviceService.updateDevice(deviceId, device);
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID deviceId) {
        deviceService.deleteDevice(deviceId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{deviceId}/status")
    public Device updateDeviceStatus(@PathVariable UUID deviceId, @RequestBody Map<String, String> statusRequest) {
        String statusStr = statusRequest.get("status");
        DeviceStatus status = DeviceStatus.valueOf(statusStr.toUpperCase());
        return deviceService.updateDeviceStatus(deviceId, status);
    }
}