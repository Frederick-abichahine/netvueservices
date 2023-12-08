package org.bcom.netvueservices.models.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectResponse {
    @JsonProperty("Connection")
    private String connection;
}
