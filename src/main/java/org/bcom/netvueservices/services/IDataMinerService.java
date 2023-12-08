package org.bcom.netvueservices.services;

import org.bcom.netvueservices.entities.DeviceKpi;

import java.util.List;
import java.util.Map;

public interface IDataMinerService {
    List<DeviceKpi> getDeviceKpis(Map<String, Double> mapIpOldPing);
}
