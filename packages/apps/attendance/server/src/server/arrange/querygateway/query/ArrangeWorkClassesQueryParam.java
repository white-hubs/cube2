package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ArrangeWorkClassesQueryParam extends Paging {

    private Long userId;

    private Long groupId;

    private Period period;

    private String date;

}
