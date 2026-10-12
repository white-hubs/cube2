package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.ProductCategoryInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.ProductCategoryPageParam;
import com.guhecloud.cube.base.device.center.std.devcenter.ProductCategoryClient;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceProjectMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.DeviceCategoryMaintenanceVO;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenanceProjectVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceProject;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenaceProjectService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenanceProjectQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：维保配置项服务层
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenaceProjectServiceImpl extends ServiceImpl<MaintenanceProjectMapper, MaintenanceProject> implements MaintenaceProjectService {

    private final MaintenanceProjectMapper maintenanceProjectMapper;
    private final ProductCategoryClient productCategoryClient;

    @Override
    public Pagination<DeviceCategoryMaintenanceVO> selectMaintenaceProject(MaintenanceProjectQueryParam param) {
        ProductCategoryPageParam pageParam = new ProductCategoryPageParam();
        pageParam.setPageNumber(param.getPageNumber());
        pageParam.setPageSize(param.getPageSize());
        pageParam.setCodes(param.getCategoryCode());
        Pagination<ProductCategoryInfoDTO> page = productCategoryClient.categoryPage(pageParam);
        List<DeviceCategoryMaintenanceVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(record -> {
                vos.add(DeviceCategoryMaintenanceVO.builder()
                                .categoryCode(record.getCode())
                                .categoryName(record.getName())
                                .categoryPathCode(record.getPathCode())
                                .maintenanceItemNum(countCategoryMaintenanceItemNum(record.getCode()))
                                .build());
            });
        }
        return PagingUtils.convert(page, vos);
    }

    /**
     * 统计产品品类对应的维保项数量
     *
     * @param categoryCode 产品品类
     *
     * @return 维保项数量
     */
    private int countCategoryMaintenanceItemNum(String categoryCode) {
        return maintenanceProjectMapper.countCategoryMaintenanceItemNum(categoryCode);
    }

    @Override
    public List<MaintenanceProjectVo> selectMaintenaceProjectDetail(MaintenanceProjectQueryParam param) {
        List<MaintenanceProject> maintenanceProjects = maintenanceProjectMapper.selectList(Wrappers.<MaintenanceProject>lambdaQuery()
                                                                                                   .eq(MaintenanceProject::getCategoryCode, param.getCategoryCode())
                                                                                          );
        return ObjectUtils.copying(maintenanceProjects, MaintenanceProjectVo.class);
    }

}