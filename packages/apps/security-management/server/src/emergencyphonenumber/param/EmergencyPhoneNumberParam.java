package com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class EmergencyPhoneNumberParam extends Paging {
    private Period period;
    private String type;
}
