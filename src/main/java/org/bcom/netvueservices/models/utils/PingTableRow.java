package org.bcom.netvueservices.models.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PingTableRow {
    @JsonProperty("Index")
    private Integer index;

    @JsonProperty("DisplayIndexValue")
    private String displayIndexValue;

    @JsonProperty("Cells")
    private List<PingTableCell> cells;
}
