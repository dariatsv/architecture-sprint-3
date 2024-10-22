package ru.yandex.practicum.smarthome.devicemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.smarthome.devicemanagement.model.Device;

import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

}
