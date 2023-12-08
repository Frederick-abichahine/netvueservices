package org.bcom.netvueservices.models.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectRequest extends BaseRequest {
    private String host;
    private String login;
    private String password;
    private String clientAppName;
    private String clientAppVersion;
    private String clientComputerName;
}
