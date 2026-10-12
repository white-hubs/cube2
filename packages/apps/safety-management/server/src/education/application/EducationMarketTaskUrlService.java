package com.guhecloud.cube.srv.gen.safety.management.education.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.education.application.commandgateway.command.ChangeEducationMarketUrlEnabledCommand;
import com.guhecloud.cube.srv.gen.safety.management.education.application.querygateway.query.EducationMarketTaskUrlQuery;
import com.guhecloud.cube.srv.gen.safety.management.education.interfaces.vo.EducationMarketTaskUrlVO;
import com.guhecloud.cube.srv.gen.safety.management.education.mapper.EducationMarketTaskUrlMapper;
import com.guhecloud.cube.srv.gen.safety.management.education.po.EducationMarketTaskUrlPO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EducationMarketTaskUrlService extends ServiceImpl<EducationMarketTaskUrlMapper, EducationMarketTaskUrlPO> {

    /**
     * 获取市场教育任务外链列表
     *
     * @param query
     *
     * @return
     */
    public Pagination<EducationMarketTaskUrlVO> getMarketTaskUrls(EducationMarketTaskUrlQuery query) {
        Page<EducationMarketTaskUrlVO> educationMarketTaskUrlVOPage = this.baseMapper.selectMarketTaskUrls(PagingUtils.convert(query), query);
        return PagingUtils.convert(educationMarketTaskUrlVOPage);
    }

    /**
     * 修改链接状态
     *
     * @param command
     */
    public void changeEnabled(ChangeEducationMarketUrlEnabledCommand command) {
        EducationMarketTaskUrlPO educationMarketTaskUrlPO = this.baseMapper.selectById(command.getId());
        Assert.isNotFound(educationMarketTaskUrlPO);
        educationMarketTaskUrlPO.setEnabled(command.getEnabled());
        this.baseMapper.updateById(educationMarketTaskUrlPO);
    }

    /**
     * 获取任务链接详情
     *
     * @return
     */
    public EducationMarketTaskUrlVO getMarketTaskUrlDetail(Long id) {
        EducationMarketTaskUrlPO educationMarketTaskUrlPO = this.baseMapper.selectById(id);
        return ObjectUtils.copying(educationMarketTaskUrlPO, EducationMarketTaskUrlVO.class);
    }
}
