package org.bcom.netvueservices.models.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseResponse<T> {
    private T d;
}
