package com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.querygateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.dao.NoticeConfigMapper;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.po.NoticeConfig;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.querygateway.NoticeConfigQueryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/23
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoticeConfigQueryServiceImpl extends ServiceImpl<NoticeConfigMapper, NoticeConfig> implements NoticeConfigQueryService {

    @Override
    public List<NoticeConfig> listByBusinessKey(@NonNull Long businessKey) {
        return this.baseMapper.selectList(Wrappers.<NoticeConfig>lambdaQuery().eq(NoticeConfig::getBusinessKey, businessKey));
    }
}
