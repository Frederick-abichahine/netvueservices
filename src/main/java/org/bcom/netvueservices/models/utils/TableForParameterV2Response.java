package org.bcom.netvueservices.models.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TableForParameterV2Response {
    @JsonProperty("Columns")
    private List<PingTableColumn> columns;

    @JsonProperty("Rows")
    private List<PingTableRow> rows;

    @JsonProperty("PageCount")
    private Integer pageCount;

    @JsonProperty("CurrentPage")
    private Integer currentPage;

    @JsonProperty("TotalAmountRows")
    private Integer totalAmountRows;
}
