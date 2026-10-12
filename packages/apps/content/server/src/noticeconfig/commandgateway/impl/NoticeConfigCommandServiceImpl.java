package com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.commandgateway.NoticeConfigCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.commandgateway.command.SubmitNoticeConfigCommand;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.dao.NoticeConfigMapper;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.po.NoticeConfig;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @date 2022/5/23
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoticeConfigCommandServiceImpl extends ServiceImpl<NoticeConfigMapper, NoticeConfig> implements NoticeConfigCommandService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull List<SubmitNoticeConfigCommand> commands, @NonNull Long businessKey) {
        // 构建信息
        List<NoticeConfig> noticeConfigs = this.buildNoticeConfigInfo(commands, businessKey);

        // 新增
        this.saveBatch(noticeConfigs);

    }

    private List<NoticeConfig> buildNoticeConfigInfo(List<SubmitNoticeConfigCommand> commands, Long businessKey) {
        return commands.stream().map(command -> {
            NoticeConfig noticeConfig = ObjectUtils.copying(command, NoticeConfig.class);
            noticeConfig.setBusinessKey(businessKey);
            noticeConfig.setUser(CollectionUtils.isEmpty(command.getUserIds()) ? null : String.join("-", command.getUserIds()));
            noticeConfig.setDepartment(CollectionUtils.isEmpty(command.getDeptIds()) ? null : String.join("-", command.getDeptIds()));
            noticeConfig.setPost(CollectionUtils.isEmpty(command.getPostIds()) ? null : String.join("-", command.getPostIds()));
            noticeConfig.setRole(CollectionUtils.isEmpty(command.getRoleIds()) ? null : String.join("-", command.getRoleIds()));
            return noticeConfig;
        }).collect(Collectors.toList());
    }

}
