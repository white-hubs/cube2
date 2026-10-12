package com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.depository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyBimConfigDeleteParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyBimConfigPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyBimConfigParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedExcelParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyBimConfigVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyBimConfigMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyDetailedMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyBimConfig;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertyBimConfigEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertyStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyBimConfigService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  09:52
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyBimConfigServiceImpl extends ServiceImpl<PropertyBimConfigMapper, PropertyBimConfig> implements PropertyBimConfigService {
    private final AreaClient areaClient;
    private final PropertyDetailedMapper propertyDetailedMapper;

    @Override
    public Pagination<PropertyBimConfigVO> queryByPage(PropertyBimConfigPageParam param) {
        Page<PropertyBimConfigVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.queryByPage(page, param);
        page.getRecords().forEach(ex -> {
            if (ObjectUtils.isNotEmpty(ex.getAreaId())) {
                List<AreaDTO> area = areaClient.getByIds(StringUtils.splitAndTranslate(ex.getAreaId(), Long::valueOf));
                ex.setArea(area);
            }
        });
        return PagingUtils.convert(page);
    }

    @Override
    public Boolean insert(PropertyBimConfigParam param) {
        List<PropertyBimConfig> propertyBimConfigList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(param.getBindWay()) && PropertyBimConfigEnum.ALL.name().equals(param.getBindWay())) {
            PropertyDetailedExcelParam propertyDetailedParam = new PropertyDetailedExcelParam();
            propertyDetailedParam.setKeyword(param.getKeyword());
            propertyDetailedParam.setTypeId(param.getTypeId());
            propertyDetailedParam.setSceneType(param.getSceneType());
            List<String> statusList = PropertyStatusEnum.getStatusByBusinessType(null, null);
            List<PropertyDetailedPageVO> propertyDetailedPageList = propertyDetailedMapper.getPropertyDetailedAll(propertyDetailedParam, statusList);

            propertyDetailedPageList.forEach(ex -> {
                PropertyBimConfig bimConfig = new PropertyBimConfig();
                bimConfig.setSceneType(param.getSceneType());
                bimConfig.setBusinessId(ex.getId().toString());
                propertyBimConfigList.add(bimConfig);
            });
        } else {
            param.getBusinessIds().forEach(ex -> {
                PropertyBimConfig bimConfig = new PropertyBimConfig();
                bimConfig.setSceneType(param.getSceneType());
                bimConfig.setBusinessId(ex);
                propertyBimConfigList.add(bimConfig);
            });
        }
        return this.saveBatch(propertyBimConfigList);
    }

    @Override
    public Boolean deleteById(PropertyBimConfigDeleteParam ids) {
        ids.getIds().forEach(ex -> {
            DatabaseContextHolder.softDelete(this, ex);
        });
        return true;
    }
}
