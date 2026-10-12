package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.WeightAllotMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.WeightAllotQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.WeightAllotVO;
import com.guhecloud.cube.srv.gen.safety.management.po.WeightAllotPO;
import com.guhecloud.cube.srv.gen.safety.management.service.WeightAllotService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wql
 * @describe 权重分配
 * @date 2023/10/12 16:00
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeightAllotServiceImpl extends ServiceImpl<WeightAllotMapper, WeightAllotPO> implements WeightAllotService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWeightAllot(WeightAllotVO weightAllotVO) {
        List<WeightAllotPO> list = this.lambdaQuery().eq(WeightAllotPO::getIndexType, weightAllotVO.getIndexType())
                .eq(ObjectUtils.isNotEmpty(weightAllotVO.getIndexItem()), WeightAllotPO::getIndexItem, weightAllotVO.getIndexItem()).list();
        Assert.isConflict((ObjectUtils.isNotEmpty(list) && weightAllotVO.getId() == null), "该权重配置已存在，无法新增");
        WeightAllotPO po = new WeightAllotPO();
        BeanUtils.copyProperties(weightAllotVO, po);
        this.saveOrUpdate(po);
    }

    @Override
    public void deleteWeightAllot(Long id) {
        WeightAllotPO byId = this.getById(id);
        if (ObjectUtils.isNotEmpty(byId)) {
            byId.setDeleted(true);
            this.updateById(byId);
        }
    }

    @Override
    public Pagination<WeightAllotVO> weightAllotList(WeightAllotQuery query) {
        Page<WeightAllotPO> page = this.lambdaQuery().page(PagingUtils.convert(query));
        return PagingUtils.convert(page, WeightAllotVO.class);
    }

    @Override
    public WeightAllotVO weightAllotDetail(Long id) {
        WeightAllotPO byId = this.getById(id);
        return ObjectUtils.copying(byId, WeightAllotVO.class);
    }

    @Override
    public int getWeightAllotByItem(WeightAllotQuery query) {
        List<WeightAllotPO> list = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(query.getIndexItem()), WeightAllotPO::getIndexItem, query.getIndexItem())
                .eq(StringUtils.isNotBlank(query.getIndexType()), WeightAllotPO::getIndexType, query.getIndexType()).list();
        return ObjectUtils.isNotEmpty(list) ? list.getFirst().getWeight() : 0;
    }

}
