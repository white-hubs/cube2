package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/22 17:33
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStatisticsBaseVO {

    private Long keyId;

    /**
     * 名称
     */
    private String keyName;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 占比
     */
    private BigDecimal percentage;

    /**
     * 图标
     */
    private FileVO icon;
}
