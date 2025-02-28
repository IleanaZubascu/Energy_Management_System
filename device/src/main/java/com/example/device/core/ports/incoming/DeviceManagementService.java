package com.example.device.core.ports.incoming;

import com.example.device.core.model.Device;

import java.util.List;

public interface DeviceManagementService {

    Device save(Device device);

    List<Device> findDevicesByUserId(Long userId);

    Device updateDevice(Device device);

    void deleteDevice(Long deviceId);
    List<Device> findDevices();
    public boolean checkUserExists(Long userId);

    public Device findById(Long id);
}
