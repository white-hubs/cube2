package com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.commandgateway.command.SubmitTechnicalNoticeCommand;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.po.TechnicalNotice;

/**
 * @author lzy
 * @date 2022/5/20
 */
public interface TechnicalNoticeCommandService extends IService<TechnicalNotice> {

    /**
     * 技术通报新增
     *
     * @param command 新增参数
     */
    void save(SubmitTechnicalNoticeCommand command);

    /**
     * 技术通报删除
     *
     * @param id 主键
     */
    void remove(Long id);
}
