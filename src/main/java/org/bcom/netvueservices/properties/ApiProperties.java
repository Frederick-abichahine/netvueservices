package org.bcom.netvueservices.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
    private List<String> hosts;
    private List<String> usernames;
    private List<String> passwords;
    private List<Integer> dmaIds;
    private List<Integer> elementIds;
    private List<Integer> parameterIds;
}
