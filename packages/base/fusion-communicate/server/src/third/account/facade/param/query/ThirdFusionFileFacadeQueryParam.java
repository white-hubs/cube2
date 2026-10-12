package com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.param.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdFusionFileFacadeQueryParam extends Paging {

    private static final long serialVersionUID = 1L;
    /**
     * 时间范围
     */
    private Period period;

    /**
     * 文件类型(语音、视频)
     */
    private String type;

    /**
     * 关键字
     */
    private String keyword;
}
