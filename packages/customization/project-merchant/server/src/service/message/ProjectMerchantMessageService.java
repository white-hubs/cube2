package com.guhecloud.cube.deliver.project.merchant.service.message;

import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.cube.deliver.project.merchant.dto.message.ProjectMerchantAssemblyNoticeParam;
import com.guhecloud.cube.deliver.project.merchant.dto.message.ProjectMerchantAssemblyNoticeSendParam;

import java.util.List;

/**
 * 消息配置
 *
 * @author : lijie
 * @date : 2023-8-3
 */
public interface ProjectMerchantMessageService {
    /**
     * 根据业务类型和条件模块类型查询消息条件配置
     *
     * @param businessType 业务类型 moduleType 小模块类型
     * @return 条件集合
     */
    JSONArray queryCondition(String businessType, String moduleType);

    /**
     * 新增
     *
     * @param param
     * @return 新增结果
     */
    Boolean insert(List<ProjectMerchantAssemblyNoticeParam> param);
    /**
     * 发送短信，通过指定用户
     */
    Boolean sendMsgUser(ProjectMerchantAssemblyNoticeSendParam param);
}
