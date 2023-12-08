package org.bcom.netvueservices.services.impl;

import org.bcom.netvueservices.entities.DeviceKpi;
import org.bcom.netvueservices.exceptions.ApiException;
import org.bcom.netvueservices.models.requests.ConnectRequest;
import org.bcom.netvueservices.models.requests.GetTableForParameterV2Request;
import org.bcom.netvueservices.models.responses.ConnectAppAndInfoResponse;
import org.bcom.netvueservices.models.responses.GetTableForParameterV2Response;
import org.bcom.netvueservices.models.utils.PingTableCell;
import org.bcom.netvueservices.models.utils.PingTableColumn;
import org.bcom.netvueservices.models.utils.PingTableRow;
import org.bcom.netvueservices.services.IDataMinerService;
import org.bcom.netvueservices.utils.ConstantsUtil;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class DataMinerService implements IDataMinerService {
    private final String host;
    private final String username;
    private final String password;
    private final Integer dmaId;
    private final Integer elementId;
    private final Integer parameterId;
    private final RestTemplate restTemplate;

    public DataMinerService(
            String host,
            String username,
            String password,
            Integer dmaId,
            Integer elementId,
            Integer parameterId,
            RestTemplate restTemplate
    ) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.dmaId = dmaId;
        this.elementId = elementId;
        this.parameterId = parameterId;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<DeviceKpi> getDeviceKpis(Map<String, Double> mapIpOldPing) {
        String connectionId = this.getConnectionId();
        return this.getDeviceKpis(connectionId, mapIpOldPing);
    }

    private String getConnectionId() {
        String url = host + ConstantsUtil.API_BASE_PATH + "/ConnectAppAndInfo";

        ConnectRequest request = new ConnectRequest();
        request.setHost(ConstantsUtil.CONNECT_HOST);
        request.setLogin(username);
        request.setPassword(password);
        request.setClientAppName(ConstantsUtil.CONNECT_CLIENT_APP_NAME);
        request.setClientAppVersion(ConstantsUtil.CONNECT_CLIENT_APP_VERSION);
        request.setClientComputerName(ConstantsUtil.CONNECT_CLIENT_COMPUTER_NAME);

        HttpEntity<?> requestEntity = new HttpEntity<>(request);
        var response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ConnectAppAndInfoResponse.class);
        if (response.getStatusCode().isError()
                || response.getBody() == null
                || response.getBody().getD() == null
                || response.getBody().getD().getConnection() == null
                || response.getBody().getD().getConnection().isEmpty())
            throw new ApiException("Login failed");

        return response.getBody().getD().getConnection();
    }

    private List<DeviceKpi> getDeviceKpis(String connectionId, Map<String, Double> mapIpOldPing) {
        String url = host + ConstantsUtil.API_BASE_PATH + "/GetTableForParameterV2";

        GetTableForParameterV2Request request = new GetTableForParameterV2Request();
        request.setConnection(connectionId);
        request.setDmaID(dmaId);
        request.setElementID(elementId);
        request.setParameterID(parameterId);
        request.setFilters(ConstantsUtil.TABLE_FOR_PARAMETER_FILTERS);

        HttpEntity<?> requestEntity = new HttpEntity<>(request);
        var response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, GetTableForParameterV2Response.class);
        Date now = new Date();

        if (response.getStatusCode().isError()
                || response.getBody() == null
                || response.getBody().getD() == null
                || response.getBody().getD().getColumns() == null
                || response.getBody().getD().getColumns().isEmpty()
                || response.getBody().getD().getRows() == null
                || response.getBody().getD().getRows().isEmpty())
            throw new ApiException("Get Ping failed");

        List<PingTableColumn> columns = response.getBody().getD().getColumns();
        int pingResultIndex = -1;
        int destinationAddressIndex = -1;
        int descriptionIndex = -1;
        for (int i = 0; i < columns.size(); ++i) {
            PingTableColumn column = columns.get(i);
            if (column == null || column.getParameterName() == null || column.getParameterName().isEmpty())
                continue;

            switch (column.getParameterName()) {
                case "Ping Result" -> pingResultIndex = i;
                case "Destination Address" -> destinationAddressIndex = i;
                case "Description" -> descriptionIndex = i;
            }
        }

        List<DeviceKpi> deviceKpis = new ArrayList<>();
        for (PingTableRow row : response.getBody().getD().getRows()) {
            List<PingTableCell> cells = row.getCells();
            if (cells == null || cells.isEmpty())
                continue;

            DeviceKpi deviceKpi = new DeviceKpi();
            deviceKpi.setRecordedAt(now);
            for (int i = 0; i < cells.size(); ++i) {
                PingTableCell cell = cells.get(i);
                if (cell.getValue() == null || cell.getDisplayValue() == null)
                    continue;

                if (i == pingResultIndex) {
                    Double value = Double.valueOf(cell.getValue());
                    if (value < 0)
                        value = null;
                    deviceKpi.setPing(value);
                } else if (i == destinationAddressIndex) {
                    deviceKpi.setIpAddress(cell.getValue());
                } else if (i == descriptionIndex) {
                    deviceKpi.setName(cell.getValue());
                }
            }

            Double newPing = deviceKpi.getPing();
            Double oldPing = null;
            if (deviceKpi.getIpAddress() != null && mapIpOldPing.containsKey(deviceKpi.getIpAddress()))
                oldPing = mapIpOldPing.get(deviceKpi.getIpAddress());

            Double jitter = null;
            if (oldPing != null && newPing != null)
                jitter = Math.abs(newPing - oldPing);
            deviceKpi.setJitter(jitter);

            deviceKpis.add(deviceKpi);
        }

        return deviceKpis;
    }
}
