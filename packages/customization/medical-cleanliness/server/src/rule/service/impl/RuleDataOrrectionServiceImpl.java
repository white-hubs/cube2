package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.dao.RuleDataOrrectionMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.RuleDataOrrection;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.enumeration.RuleDataOrrectionEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleDataOrrectionPageParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleDataOrrectionParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.RuleDataOrrectionService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleDataOrrectionVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author: lijie
 * @CreateTime: 2024-06-21  14:58
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleDataOrrectionServiceImpl extends ServiceImpl<RuleDataOrrectionMapper, RuleDataOrrection> implements RuleDataOrrectionService {
    private final UserClient userClient;

    @Override
    public List<RuleDataOrrectionVO> getAll() {
        List<RuleDataOrrection> ruleDataOrrections = this.baseMapper.selectList(new LambdaQueryWrapper<RuleDataOrrection>()
                                                                                        .eq(RuleDataOrrection::getStatus, RuleDataOrrectionEnum.VALID.name()));
        return ConvertUtils.toList(RuleDataOrrectionVO.class, ruleDataOrrections);
    }

    @Override
    public Pagination<RuleDataOrrectionVO> getPage(RuleDataOrrectionPageParam param) {
        Page<RuleDataOrrection> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<RuleDataOrrection>()
                .like(ObjectUtils.isNotEmpty(param.getDeviceId()), RuleDataOrrection::getDeviceIds, param.getDeviceId())
                .eq(ObjectUtils.isNotEmpty(param.getRuleNormCode()), RuleDataOrrection::getRuleNormCode, param.getRuleNormCode()));
        List<RuleDataOrrectionVO> records = ConvertUtils.toList(RuleDataOrrectionVO.class, page.getRecords());
        records.forEach(ex -> {
            if (ObjectUtils.isNotEmpty(ex.getCreateBy())) {
                ex.setCreateByName(Optional.ofNullable(userClient.getUserByMobile(ex.getCreateBy())).orElse(new UserDTO()).getName());
            }
        });
        return new Pagination<>(page.getTotal(), page.getSize(), ConvertUtils.toList(RuleDataOrrectionVO.class, records));
    }

    @Override
    public Boolean insert(RuleDataOrrectionParam param) {
        RuleDataOrrection ruleDataOrrection = ConvertUtils.convert(RuleDataOrrection.class, param);
        return this.baseMapper.insert(ruleDataOrrection) > 0;
    }

    @Override
    public Boolean updateStatus(Long id) {
        RuleDataOrrection ruleDataOrrection = this.baseMapper.selectById(id);
        ruleDataOrrection.setStatus(
                RuleDataOrrectionEnum.VALID.name().equals(ruleDataOrrection.getStatus()) ? RuleDataOrrectionEnum.INVAILD.name()
                                                                                         : RuleDataOrrectionEnum.VALID.name());
        return this.baseMapper.updateById(ruleDataOrrection) > 0;
    }
}
