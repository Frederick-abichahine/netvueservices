package org.bcom.netvueservices.models.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PingTableColumn {
    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("ParameterName")
    private String parameterName;
}
