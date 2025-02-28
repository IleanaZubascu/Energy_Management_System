package com.example.monitoring_communication.persistance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Entity
@SpringBootApplication
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name ="energy_consumption")
public class EnergyConsumptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id")
    private Long deviceId;

    private Long timestamp;

    @Column(name = "hourly_consumption")
    private Double hourlyConsumption;
}
