package com.example.device.controller.model;

import com.example.device.core.model.Device;
import com.example.device.persistance.model.DeviceEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class DeviceControllerMapper {

    public abstract DeviceDTO toDTO(Device device);

    public abstract Device toModel(DeviceDTO deviceDTO);

    public abstract List<DeviceDTO> toDTOs(List<Device> devices);

}
