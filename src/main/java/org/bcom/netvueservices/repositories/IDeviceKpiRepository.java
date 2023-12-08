package org.bcom.netvueservices.repositories;

import org.bcom.netvueservices.entities.DeviceKpi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;

public interface IDeviceKpiRepository extends JpaRepository<DeviceKpi, UUID> {
    void deleteAllByIdIsNotIn(Collection<UUID> id);
}
