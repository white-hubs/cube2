package com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.commandgateway.command.SubmitNoticeConfigCommand;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.po.NoticeConfig;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/23
 */
public interface NoticeConfigCommandService extends IService<NoticeConfig> {

    /**
     * 新增消息配置
     *
     * @param commands    新增参数
     * @param businessKey 业务主键
     */
    void save(List<SubmitNoticeConfigCommand> commands, Long businessKey);

}
