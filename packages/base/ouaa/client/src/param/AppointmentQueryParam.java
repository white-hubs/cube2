package com.guhecloud.cube.biz.ouaa.client.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.Set;

@Data
public class AppointmentQueryParam extends Paging {

    private String keyword;

    private Boolean enable;

    private Set<Long> ids;
}
