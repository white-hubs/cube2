package com.guhecloud.cube.customized.guhecloudwebsite.service;

import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSitePublishConfigParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSitePublishConfigVO;

/**
 * 网站发布配置服务接口
 */
public interface SysSitePublishConfigService {

    /**
     * 查询发布配置（不传任何参数，取第一条记录）
     * @return 配置详情，无数据返回null
     */
    SysSitePublishConfigVO getDetail();

    /**
     * 保存或更新发布配置（有一则更新，没有则新增）
     * @param vo 请求参数实体，包含 provider 和 providerConfig 字段
     * @return 更新后的配置详情
     */
    void saveOrUpdate(SysSitePublishConfigParam vo);

}
