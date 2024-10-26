package ru.yandex.practicum.smarthome.devicemanagement.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.smarthome.devicemanagement.kafka.DeviceStatusProducer;
import ru.yandex.practicum.smarthome.devicemanagement.model.Device;
import ru.yandex.practicum.smarthome.devicemanagement.model.DeviceCommand;
import ru.yandex.practicum.smarthome.devicemanagement.model.DeviceStatus;
import ru.yandex.practicum.smarthome.devicemanagement.model.DeviceStatusMessage;
import ru.yandex.practicum.smarthome.devicemanagement.repository.DeviceRepository;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;
import java.util.UUID;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceStatusProducer deviceStatusProducer;
    private static final Logger logger = Logger.getLogger(DeviceService.class.getName());


    public DeviceService(DeviceRepository deviceRepository, DeviceStatusProducer deviceStatusProducer) {
        this.deviceRepository = deviceRepository;
        this.deviceStatusProducer = deviceStatusProducer;
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Device getDeviceById(UUID deviceId) {
        return deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found"));
    }

    public Device createDevice(Device device) {
        logger.log(Level.INFO, "Device created: " + device);
        return deviceRepository.save(device);
    }

    public Device updateDevice(UUID deviceId, Device device) {
        Device existingDevice = getDeviceById(deviceId);
        existingDevice.setName(device.getName());
        existingDevice.setType(device.getType());
        existingDevice.setMetadata(device.getMetadata());
        logger.log(Level.INFO, "Device updated: {0}" , existingDevice);
        return deviceRepository.save(existingDevice);
    }

    public void deleteDevice(UUID deviceId) {
        logger.log(Level.INFO, "Device deleted: {0}", deviceId);
        deviceRepository.deleteById(deviceId);
    }

    public Device updateDeviceStatus(UUID deviceId, DeviceStatus status) {
        Device device = getDeviceById(deviceId);
        device.setStatus(status);
        Device updatedDevice = deviceRepository.save(device);
        logger.log(Level.INFO, "Device status updated: {0}", updatedDevice);

        String topic = "device_statuses";
        DeviceStatusMessage message = new DeviceStatusMessage(deviceId, status);
        deviceStatusProducer.sendMessage(topic, message);

        logger.log(Level.INFO, "Posting to kafka topic: {0}", device);
        return updatedDevice;
    }

    public void processDeviceCommand(DeviceCommand deviceCommand) {
        UUID deviceId = deviceCommand.getDeviceId();
        Device device = getDeviceById(deviceId);

        String command = deviceCommand.getCommand();

        logger.log(Level.INFO, "Processing command: {0}", deviceCommand);

        switch (command.toLowerCase()) {
            case "turn_on" -> device.setStatus(DeviceStatus.ONLINE);
            case "turn_off" -> device.setStatus(DeviceStatus.OFFLINE);
            default -> {
                logger.log(Level.WARNING, "Unknown command:  {0}", command);
                return;
            }
        }

        deviceRepository.save(device);
    }
}