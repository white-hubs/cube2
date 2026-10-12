package com.guhecloud.cube.srv.electron.guide.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.electron.guide.dao.GuideDeviceManageMapper;
import com.guhecloud.cube.srv.electron.guide.entity.GuideDeviceManage;
import com.guhecloud.cube.srv.electron.guide.param.GuideDeviceManageModifyParam;
import com.guhecloud.cube.srv.electron.guide.param.query.GuideDeviceManagePageQueryParam;
import com.guhecloud.cube.srv.electron.guide.service.GuideDeviceManageService;
import com.guhecloud.cube.srv.electron.guide.vo.GuideDeviceManageDetailVO;
import com.guhecloud.cube.srv.electron.guide.vo.GuideDeviceManagePageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Objects;

@Slf4j
@Service
public class GuideDeviceManageServiceImpl extends ServiceImpl<GuideDeviceManageMapper, GuideDeviceManage> implements GuideDeviceManageService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@Nonnull GuideDeviceManageModifyParam param) {
        GuideDeviceManage guideDeviceManage = ObjectUtils.copying(param, GuideDeviceManage.class);
        this.baseMapper.insert(guideDeviceManage);
        return guideDeviceManage.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@Nonnull Long id, @Nonnull GuideDeviceManageModifyParam param) {
        GuideDeviceManage guideDeviceManage = this.baseMapper.selectById(id);
        if (Objects.nonNull(guideDeviceManage)) {
            ObjectUtils.copying(param, guideDeviceManage);
            this.baseMapper.updateById(guideDeviceManage);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(@Nonnull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public GuideDeviceManageDetailVO getDetail(@Nonnull Long id) {
        GuideDeviceManage guideDeviceManage = this.baseMapper.selectById(id);
        return ObjectUtils.copying(guideDeviceManage, GuideDeviceManageDetailVO.class);
    }

    @Override
    public Pagination<GuideDeviceManagePageVO> getPage(GuideDeviceManagePageQueryParam param) {
        Page<GuideDeviceManage> guideDeviceManagePage = this.baseMapper.selectPage(PagingUtils.convert(param), Wrappers
                                                                                           .<GuideDeviceManage>lambdaQuery()
                                                                                           .and(StringUtils.isNotBlank(param.getKeyword()), q -> q
                                                                                                   .like(GuideDeviceManage::getName, param.getKeyword())
                                                                                                   .or()
                                                                                                   .like(GuideDeviceManage::getTitle, param.getKeyword()))
                                                                                           .orderByDesc(GuideDeviceManage::getCreateTime)
                                                                                  );
        if (CollectionUtils.isEmpty(guideDeviceManagePage.getRecords())) {
            return Pagination.empty();
        }
        return PagingUtils.convert(guideDeviceManagePage, GuideDeviceManagePageVO.class);
    }

}
