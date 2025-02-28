package com.example.device.persistance.repository;

import com.example.device.persistance.model.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevicePsqlRepository extends JpaRepository<DeviceEntity, Long>
{

    List<DeviceEntity> findByUserId(Long userId);

    DeviceEntity findFirstById(Long id);
}
