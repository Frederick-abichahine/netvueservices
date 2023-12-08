package org.bcom.netvueservices.services.impl;

import org.bcom.netvueservices.entities.DeviceKpi;
import org.bcom.netvueservices.properties.ApiProperties;
import org.bcom.netvueservices.repositories.IDeviceKpiRepository;
import org.bcom.netvueservices.services.IDataMinerService;
import org.bcom.netvueservices.services.IDataMinerSyncService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class DataMinerSyncService implements IDataMinerSyncService {
    private final ApiProperties apiProperties;
    private final IDeviceKpiRepository deviceKpiRepository;
    private final RestTemplate restTemplate;
    private final List<IDataMinerService> dataMinerServices;

    public DataMinerSyncService(
            ApiProperties apiProperties,
            IDeviceKpiRepository deviceKpiRepository,
            RestTemplate restTemplate
    ) {
        this.apiProperties = apiProperties;
        this.deviceKpiRepository = deviceKpiRepository;
        this.restTemplate = restTemplate;
        this.dataMinerServices = createDataMinerServices();
    }

    @Override
    @Transactional
    public void syncPing() {
        List<DeviceKpi> oldDeviceKpis = getDeviceKpis();
        Map<String, Double> mapIpOldPing = getMapIpPing(oldDeviceKpis);

        List<DeviceKpi> deviceKpis = new ArrayList<>();
        for (IDataMinerService dataMinerService : dataMinerServices) {
            deviceKpis.addAll(dataMinerService.getDeviceKpis(mapIpOldPing));
        }

        List<DeviceKpi> newDeviceKpis = saveDeviceKpis(deviceKpis);
        deleteOldDeviceKpis(newDeviceKpis);
    }

    private List<DeviceKpi> getDeviceKpis() {
        return deviceKpiRepository.findAll();
    }

    private Map<String, Double> getMapIpPing(List<DeviceKpi> deviceKpis) {
        Map<String, Double> mapIpPing = new HashMap<>();
        if (deviceKpis != null && !deviceKpis.isEmpty())
            for (DeviceKpi deviceKpi : deviceKpis) {
                if (deviceKpi.getIpAddress() == null)
                    continue;

                mapIpPing.put(deviceKpi.getIpAddress(), deviceKpi.getPing());
            }
        return mapIpPing;
    }

    private List<DeviceKpi> saveDeviceKpis(List<DeviceKpi> deviceKpis) {
        if (deviceKpis.isEmpty())
            return new ArrayList<>();

        return deviceKpiRepository.saveAll(deviceKpis);
    }

    private void deleteOldDeviceKpis(List<DeviceKpi> deviceKpis) {
        List<UUID> newIds = deviceKpis.stream().map(DeviceKpi::getId).toList();
        deviceKpiRepository.deleteAllByIdIsNotIn(newIds);
    }

    private List<IDataMinerService> createDataMinerServices() {
        int size = apiProperties.getHosts().size();
        List<IDataMinerService> result = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            result.add(new DataMinerService(
                    apiProperties.getHosts().get(i),
                    apiProperties.getUsernames().get(i),
                    apiProperties.getPasswords().get(i),
                    apiProperties.getDmaIds().get(i),
                    apiProperties.getElementIds().get(i),
                    apiProperties.getParameterIds().get(i),
                    restTemplate
            ));
        }
        return result;
    }
}
