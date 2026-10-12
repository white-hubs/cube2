package com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.po.NoticeConfig;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/23
 */
public interface NoticeConfigQueryService extends IService<NoticeConfig> {

    /**
     * 根据业务主键获取消息配置信息
     *
     * @param businessKey 查询参数
     *
     * @return 消息配置信息
     */
    List<NoticeConfig> listByBusinessKey(Long businessKey);

}
