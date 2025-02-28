package com.example.device.persistance;

import com.example.device.core.model.Device;
import com.example.device.core.ports.outgoing.DeviceRepository;
import com.example.device.persistance.model.DeviceEntity;
import com.example.device.persistance.model.DeviceMapper;
import com.example.device.persistance.repository.DevicePsqlRepository;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
@Setter
public class DeviceRepositoryAdapter implements DeviceRepository {

    private DevicePsqlRepository devicePsqlRepository;
    private DeviceMapper deviceMapper;

    @Autowired
    public DeviceRepositoryAdapter(DevicePsqlRepository devicePsqlRepository,
                                   DeviceMapper deviceMapper) {
        this.devicePsqlRepository = devicePsqlRepository;
        this.deviceMapper = deviceMapper;
    }


    @Override
    public Device save(Device device) {
        final var savedDevice = devicePsqlRepository.save(
                DeviceEntity.builder()
                        .address(device.getAddress())
                        .description(device.getDescription())
                        .maxEnergy(device.getMaxEnergy())
                        .build()

        );
        return deviceMapper.toModel(savedDevice);
    }

    @Override
    public List<Device> findByUserId(Long userId) {
        return deviceMapper.toModels(
                devicePsqlRepository.findByUserId(userId)
        );
    }

    @Override
    public Device updateDevice(Device device) {
        return deviceMapper.toModel(devicePsqlRepository.save(deviceMapper.toDeviceEntity(device)));
    }

    @Override
    public void delete(Long deviceId) {
        devicePsqlRepository.deleteById(deviceId);
    }

    @Override
    public List<Device> findAll() {
        return deviceMapper.toModels(devicePsqlRepository.findAll());
    }

    @Override
    public Device findById(Long id) {
        return deviceMapper.toModel(devicePsqlRepository.findFirstById(id));
    }
}
