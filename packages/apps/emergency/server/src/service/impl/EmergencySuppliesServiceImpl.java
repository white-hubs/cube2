package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencySuppliesMapper;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencySuppliesTypeMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencySupplies;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencySuppliesType;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencySuppliesService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencySuppliesStatVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencySuppliesVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 描述：应急物资管理服务层
 *
 * @author Appet
 * @date 2024-01-016 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencySuppliesServiceImpl extends ServiceImpl<EmergencySuppliesMapper, EmergencySupplies> implements EmergencySuppliesService {
    private final EmergencySuppliesTypeMapper suppliesTypeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EmergencySuppliesParam param) {
        EmergencySupplies emergencySupplies = ObjectUtils.copying(param, EmergencySupplies.class);
        this.baseMapper.insert(emergencySupplies);
        return emergencySupplies.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EmergencySuppliesParam param) {
        EmergencySupplies emergencySupplies = this.baseMapper.selectById(id);
        Assert.isNotFound(emergencySupplies, "未查询到相关信息");
        ObjectUtils.copying(param, emergencySupplies);
        this.baseMapper.updateById(emergencySupplies);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public EmergencySuppliesVO getById(Long id) {
        EmergencySupplies emergencySupplies = this.baseMapper.selectById(id);
        EmergencySuppliesVO suppliesVO = ObjectUtils.copying(emergencySupplies, EmergencySuppliesVO.class);
        //扩展应急物资信息
        this.extendSuppliesInfo(suppliesVO);
        return suppliesVO;
    }

    @Override
    public Pagination<EmergencySuppliesVO> getPage(EmergencySuppliesQueryParam param) {
        //请自行添加查询条件
        Page<EmergencySupplies> page = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getName()), EmergencySupplies::getName, param.getName())
                .page(PagingUtils.convert(param));
        Pagination<EmergencySuppliesVO> convert = PagingUtils.convert(page, EmergencySuppliesVO.class);
        if (CollectionUtils.isNotEmpty(convert.getRecords())) {
            convert.getRecords().forEach(this::extendSuppliesInfo);
        }
        return convert;
    }

    /**
     * 扩展应急物资信息
     *
     * @param vo
     */
    private void extendSuppliesInfo(EmergencySuppliesVO vo) {
        EmergencySuppliesType suppliesType = suppliesTypeMapper.selectById(vo.getType());
        if (suppliesType != null) {
            vo.setTypeName(suppliesType.getName());
        }
    }

    @Override
    public List<EmergencySuppliesStatVO> getEmergencySuppliesStatForStock() {
        List<EmergencySuppliesStatVO> statForStock = this.baseMapper.getEmergencySuppliesStatForStock();
        if (CollectionUtils.isNotEmpty(statForStock)) {
            BigDecimal total = BigDecimal.valueOf(statForStock.stream().mapToDouble(s -> s.getStockValue().doubleValue()).sum());
            statForStock.forEach(s -> {
                BigDecimal percentage = s.getStockValue().multiply(BigDecimal.valueOf(100)).divide(total, 2, RoundingMode.HALF_UP);
                s.setPercentage(percentage);
            });
        }
        return statForStock;
    }
}