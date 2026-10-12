package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/25 10:29
 * @Description: 用户中心查询
 */

@Data
public class UserCenterQueryParam extends Paging {

    /**
     * 用户姓名
     */
    @Size(max = 100, message = "用户姓名长度不能大于100")
    private String keyword;

    /**
     * 电话
     */
    private String mobile;
}
