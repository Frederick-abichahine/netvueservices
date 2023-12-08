package org.bcom.netvueservices.models.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PingTableCell {
    @JsonProperty("Value")
    private String value;

    @JsonProperty("DisplayValue")
    private String displayValue;
}
