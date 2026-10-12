package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class TimeControlLogQueryParam extends Paging {
    private String period;

    /**
     * @description: 将字符串转换成日期
     * @author: lijie
     * @date: 2024/8/15 21:19
     * @param: java.lang.String
     * @return:
     **/
    public Map<String, LocalDateTime> getDate() {
        var map = new HashMap<String, LocalDateTime>();
        if (StringUtils.isEmpty(this.period)) {
            return map;
        }
        String[] split = this.period.split(",");
        var startTimeStr = split[0];
        var endTimeStr = split[1];
        var beginTime = DateUtils.parse(startTimeStr);
        var endTime = DateUtils.parse(endTimeStr);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return map;
    }
}
