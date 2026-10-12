package com.guhecloud.cube.srv.gen.conferencemanage.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.ConferenceRoomTypeMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceRoomType;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceBaseParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceRoomTypeService;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/7 10:24
 * @Description:
 */

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceRoomTypeServiceImpl extends ServiceImpl<ConferenceRoomTypeMapper, ConferenceRoomType> implements ConferenceRoomTypeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(ConferenceBaseParam param) {
        ConferenceRoomType one = this.getOne(Wrappers.<ConferenceRoomType>lambdaQuery()
                                                     .eq(ConferenceRoomType::getName, param.getName()));
        if (Objects.nonNull(param.getId())) {
            Assert.isConflict(Objects.nonNull(one) && !one.getId().equals(param.getId()), "会议室类型名称已存在");
            return this.lambdaUpdate()
                    .eq(ConferenceRoomType::getId, param.getId())
                    .set(ConferenceRoomType::getName, param.getName())
                    .set(ConferenceRoomType::getStatus, param.getStatus())
                    .update();
        } else {
            Assert.isConflict(Objects.nonNull(one), "会议室类型名称已存在");
            ConferenceRoomType type = new ConferenceRoomType();
            type.setId(IdWorker.getId());
            type.setName(param.getName());
            type.setStatus(param.getStatus());
            return this.save(type);
        }
    }
}
