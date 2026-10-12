package com.guhecloud.cube.srv.gen.conferencemanage.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.FieldConfigurationMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.FieldConfiguration;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.FieldConfigurationParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.FieldConfigurationService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.FieldConfigurationVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author 15561
 * @description 针对表【field_configuration(字段必输配置)】的数据库操作Service实现
 * @createDate 2024-07-18 18:50:08
 */
@Service
public class FieldConfigurationServiceImpl extends ServiceImpl<FieldConfigurationMapper, FieldConfiguration> implements FieldConfigurationService {

    private final static List<String> FILE_CONFIG_NAME = Arrays.asList("cancelReason",
                                                                       "participantIds",
                                                                       "conferenceType",
                                                                       "conferenceTheme",
                                                                       "goodsNames",
                                                                       "bookNum",
                                                                       "content");

    @Override
    public Pagination<FieldConfigurationVO> selectFieldConfigurationList(FieldConfigurationParam param) {
        Page<FieldConfiguration> list = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getFieldName()), FieldConfiguration::getFieldName, param.getFieldName())
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(list, FieldConfigurationVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateField(FieldConfiguration param) {
        if (Objects.isNull(param.getId())) {
            param.setId(IdWorker.getId());
            this.save(param);
        } else {
            FieldConfiguration byId = this.getById(param.getId());
            byId.setRequired(param.getRequired());
            byId.setHidden(param.getHidden());
            this.updateById(byId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initTenantConfig(Integer tenant) {
        List<FieldConfiguration> fieldConfigurations = new ArrayList<>();
        for (String fileName : FILE_CONFIG_NAME) {
            FieldConfiguration fieldConfiguration = new FieldConfiguration();
            fieldConfiguration.setId(IdWorker.getId());
            fieldConfiguration.setTenant(tenant);
            fieldConfiguration.setFieldName(fileName);
            fieldConfiguration.setRequired(true);
            fieldConfiguration.setHidden(false);
            fieldConfigurations.add(fieldConfiguration);
        }
        this.saveBatch(fieldConfigurations);
    }
}




