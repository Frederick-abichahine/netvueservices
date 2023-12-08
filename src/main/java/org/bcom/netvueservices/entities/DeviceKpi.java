package org.bcom.netvueservices.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "device_kpis")
public class DeviceKpi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @CreationTimestamp
    private Date recordedAt;

    @Column
    private String name;

    @Column
    private String ipAddress;

    @Column
    private Double ping;

    @Column
    private Double jitter;
}
