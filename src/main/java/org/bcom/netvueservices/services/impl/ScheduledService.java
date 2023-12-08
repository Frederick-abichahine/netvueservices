package org.bcom.netvueservices.services.impl;

import org.bcom.netvueservices.annotations.LogMethodExecution;
import org.bcom.netvueservices.services.IDataMinerSyncService;
import org.bcom.netvueservices.services.IScheduledService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService implements IScheduledService {
    private final IDataMinerSyncService dataMinerSyncService;

    public ScheduledService(
            IDataMinerSyncService dataMinerSyncService
    ) {
        this.dataMinerSyncService = dataMinerSyncService;
    }

    @LogMethodExecution
    @Scheduled(initialDelay = 10000, fixedDelay = 15000)
    public void syncDataMinerPing() {
        dataMinerSyncService.syncPing();
    }
}
