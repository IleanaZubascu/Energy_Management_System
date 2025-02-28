package com.example.monitoring_communication.persistance.repository;

import com.example.monitoring_communication.persistance.EnergyConsumptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumptionEntity, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE EnergyConsumptionEntity e  " +
            "SET e.hourlyConsumption = :hourlyConsumption, e.timestamp = :timestamp " +
            "WHERE e.deviceId = :deviceId")
    void updateHourlyConsumptionAndTimestampByDeviceId(@Param("deviceId") Long deviceId,
                                           @Param("hourlyConsumption") Double hourlyConsumption,
                                           @Param("timestamp") Long timestamp);
    boolean existsByDeviceId(Long deviceId);

    EnergyConsumptionEntity getEnergyConsumptionEntityByDeviceId(Long deviceId);
}
