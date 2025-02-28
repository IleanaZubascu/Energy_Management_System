package com.example.monitoring_communication.core;

import com.example.monitoring_communication.core.model.EnergyConsumption;
import com.example.monitoring_communication.persistance.EnergyConsumptionEntity;
import com.example.monitoring_communication.persistance.repository.EnergyConsumptionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnergyConsumptionService {

    private final EnergyConsumptionRepository energyConsumptionRepository;


    public EnergyConsumptionService(EnergyConsumptionRepository energyConsumptionRepository) {
        this.energyConsumptionRepository = energyConsumptionRepository;
    }

    @Transactional
    public EnergyConsumptionEntity save(EnergyConsumption energyConsumption) {
        return energyConsumptionRepository.save(EnergyConsumptionEntity.builder()
                .deviceId(energyConsumption.getDeviceId())
                .timestamp(energyConsumption.getTimestamp())
                .hourlyConsumption(energyConsumption.getHourlyConsumption())
                .timestamp(energyConsumption.getTimestamp())
                .build());
    }
    @Transactional
    public void updateHourlyConsumptionAndTimestampByDeviceId(Long deviceId,Double hourlyConsumption,Long timestamp){
        energyConsumptionRepository.updateHourlyConsumptionAndTimestampByDeviceId(deviceId,hourlyConsumption,timestamp);
    }
    @Transactional
    public boolean existsByDeviceId(Long deviceId) {
        return energyConsumptionRepository.existsByDeviceId(deviceId);
    }

    @Transactional
    public Double getHourlyConsumptionByDeviceId(Long deviceId)
    {
        EnergyConsumptionEntity energyConsumptionEntity = energyConsumptionRepository.getEnergyConsumptionEntityByDeviceId(deviceId);
        return energyConsumptionEntity.getHourlyConsumption();
    }
}
