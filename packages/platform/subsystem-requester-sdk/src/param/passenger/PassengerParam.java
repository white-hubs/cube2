package com.guhecloud.subsystem.requester.sdk.param.passenger;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class PassengerParam extends Paging {
    private String stTime;
    private String etTime;
}
