package com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.commandgateway.NoticeConfigCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.commandgateway.TechnicalNoticeCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.commandgateway.command.SubmitTechnicalNoticeCommand;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.dao.TechnicalNoticeMapper;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.po.TechnicalNotice;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lzy
 * @date 2022/5/20
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TechnicalNoticeCommandServiceImpl extends ServiceImpl<TechnicalNoticeMapper, TechnicalNotice> implements TechnicalNoticeCommandService {

    private final UserClient userClient;
    private final FileClient fileClient;

    private final NoticeConfigCommandService noticeConfigCommandService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull SubmitTechnicalNoticeCommand command) {
        // 新增
        TechnicalNotice technicalNotice = this.buildTechnicalNotice(command);
        this.baseMapper.insert(technicalNotice);

        // 新增通知配置信息
        this.noticeConfigCommandService.save(command.getNoticeConfigCommands(), technicalNotice.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    private TechnicalNotice buildTechnicalNotice(SubmitTechnicalNoticeCommand command) {
        // 获取用户信息
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserDTO user = this.userClient.getUser(userId);

        // 获取图片信息
        FileVO file = this.fileClient.getById(command.getFileId());

        // 构建信息
        TechnicalNotice technicalNotice = ObjectUtils.copying(command, TechnicalNotice.class);
        technicalNotice.setNoticeTime(DateUtils.now());
        technicalNotice.setUserId(userId);
        technicalNotice.setUserName(user == null ? "" : user.getName());
        technicalNotice.setFileName(file == null ? "" : file.getName());
        return technicalNotice;
    }
}
