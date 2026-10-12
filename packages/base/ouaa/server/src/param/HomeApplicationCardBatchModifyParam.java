package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import java.util.List;

/**
 * 首页应用卡片新增参数
 *
 * @author lanchun
 * @date 2022/5/13 11:35
 */
@Data
public class HomeApplicationCardBatchModifyParam {

    /***
     * 保存参数
     */
    private List<HomeApplicationCardModifyParam> cardModifyParams;
}
