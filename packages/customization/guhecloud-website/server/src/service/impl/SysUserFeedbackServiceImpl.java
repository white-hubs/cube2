package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.guhecloudwebsite.converts.SysUserFeedbackConverts;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysUserFeedbackMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysUserFeedback;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysUserFeedbackParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysUserFeedbackQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysUserFeedbackService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysArticleVO;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysUserFeedbackVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户留言Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUserFeedbackServiceImpl extends ServiceImpl<SysUserFeedbackMapper, SysUserFeedback>
        implements SysUserFeedbackService {

    private final SysUserFeedbackMapper sysUserFeedbackMapper;

    @Override
    public Pagination<SysUserFeedbackVO> page(SysUserFeedbackQueryParam param) {
        LambdaQueryWrapper<SysUserFeedback> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotEmpty(param.getUserName())) {
            wrapper.like(SysUserFeedback::getUserName, param.getUserName());
        }
        if (ObjectUtils.isNotEmpty(param.getContactWay())) {
            wrapper.eq(SysUserFeedback::getContactWay, param.getContactWay());
        }
        if (ObjectUtils.isNotEmpty(param.getCompanyName())) {
            wrapper.like(SysUserFeedback::getCompanyName, param.getCompanyName());
        }

        wrapper.orderByDesc(SysUserFeedback::getCreateTime);
        Page<SysUserFeedback> result = sysUserFeedbackMapper.selectPage(PagingUtils.convert(param), wrapper);

        return PagingUtils.convert(result, SysUserFeedbackVO.class);

    }

    @Override
    public SysUserFeedbackVO detail(Long id) {
        SysUserFeedback entity = sysUserFeedbackMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            return null;
        }
        return SysUserFeedbackConverts.toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysUserFeedbackParam param) {
        SysUserFeedback entity = SysUserFeedbackConverts.toEntity(param);
        sysUserFeedbackMapper.insert(entity);
    }

    @Override
    public void delete(Long id) {
        sysUserFeedbackMapper.deleteById(id);
    }

}
