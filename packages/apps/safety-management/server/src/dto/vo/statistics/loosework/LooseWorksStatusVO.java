package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework;

import lombok.Data;

/**
 * 小散工程排名统计
 *
 * @author lanchun
 * @date 2024/4/8 17:46
 */
@Data
public class LooseWorksStatusVO {
    /**
     * 状态
     */
    private String status;
    /**
     * 值
     */
    private Integer value;

    public static LooseWorksStatusVO getLooseWorksStatusVO(String status) {
        LooseWorksStatusVO looseWorksStatusVO = new LooseWorksStatusVO();
        looseWorksStatusVO.setStatus(status);
        looseWorksStatusVO.setValue(0);
        return looseWorksStatusVO;
    }
}
