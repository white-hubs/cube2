package com.guhecloud.cube.srv.gen.conferencemanage.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.FieldConfiguration;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.FieldConfigurationParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.FieldConfigurationVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @author 15561
 * @description 针对表【field_configuration(字段必输配置)】的数据库操作Service
 * @createDate 2024-07-18 18:50:08
 */
public interface FieldConfigurationService extends IService<FieldConfiguration> {

    /**
     * 获取字段必输配置列表
     *
     * @param param
     *
     * @return
     */
    Pagination<FieldConfigurationVO> selectFieldConfigurationList(FieldConfigurationParam param);

    /**
     * 保存或更新字段必输配置
     *
     * @param param
     */
    void saveOrUpdateField(FieldConfiguration param);

    /**
     * 初始化租户配置
     *
     * @param tenant
     */
    void initTenantConfig(Integer tenant);
}
