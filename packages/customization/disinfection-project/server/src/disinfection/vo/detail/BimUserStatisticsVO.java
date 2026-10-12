package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/23 9:28
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BimUserStatisticsVO {

    /**
     * 用户总数
     */
    private Integer total;

    /**
     * 消杀人员
     */
    private List<DisinfectionUserInfoVO> disinfectionUserList;

}
