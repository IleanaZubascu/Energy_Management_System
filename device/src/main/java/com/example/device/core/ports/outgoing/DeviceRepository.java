package com.example.device.core.ports.outgoing;

import com.example.device.core.model.Device;

import java.util.List;

public interface DeviceRepository {

    Device save(Device device);

    List<Device> findByUserId(Long userId);

    Device updateDevice(Device device);

    void delete(Long deviceId);

    List<Device> findAll();

    Device findById(Long id);

}
