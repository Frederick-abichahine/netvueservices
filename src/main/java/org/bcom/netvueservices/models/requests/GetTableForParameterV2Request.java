package org.bcom.netvueservices.models.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetTableForParameterV2Request extends BaseRequest {
    private String connection;
    private Integer dmaID;
    private Integer elementID;
    private Integer parameterID;
    private List<String> filters;
}
