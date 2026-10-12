package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentQueryParam extends Paging {

    private String keyword;

    private Boolean enable;

    private Set<Long> ids;

}
