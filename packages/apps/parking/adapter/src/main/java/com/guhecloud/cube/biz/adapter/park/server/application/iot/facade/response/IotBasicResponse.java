package com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class IotBasicResponse<T> {
    private Integer code;
    private String msg;
    private T data;

    public boolean isFail() {
        if (this.code != 200) {
            return true;
        }
        return false;
    }

    public boolean isSuccess() {
        if (this.code == 200) {
            return true;
        } else {
            return false;
        }
    }
}
