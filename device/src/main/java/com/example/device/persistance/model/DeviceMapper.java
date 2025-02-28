package com.example.device.persistance.model;

import com.example.device.core.model.Device;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class DeviceMapper {

    public abstract Device toModel(DeviceEntity deviceEntity);

    public abstract List<Device> toModels(List<DeviceEntity> deviceEntities);

    public abstract DeviceEntity toDeviceEntity(Device device);
}
