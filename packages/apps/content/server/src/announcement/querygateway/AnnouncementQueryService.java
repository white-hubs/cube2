package com.guhecloud.cube.srv.gen.content.management.server.announcement.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command.PublicAnnouncementCommand;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.AnnouncementVO;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.PublicAnnouncementVO1;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.po.Announcement;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.querygateway.query.AnnouncementCollectionQuery;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.interfaces.vo.NoticeConfigVO;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.po.NoticeConfig;

import java.util.List;
import java.util.Map;

/**
 * @author lzy
 * @date 2022/5/20
 */
public interface AnnouncementQueryService extends IService<Announcement> {

    /**
     * 公告分页信息
     *
     * @param query 查询参数
     *
     * @return 分页信息
     */
    Page<AnnouncementVO> pageAnnouncement(AnnouncementCollectionQuery query);

    /**
     * @Description app公告总览
     * @Author lrj
     */
    Map<String, Object> appTotalAnnouncement();

    /**
     * @Description 详情
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    AnnouncementVO getDetail(Long id);

    Long getAnnouncementNoViewedNum();

    /**
     * 获取置顶资讯
     *
     * @param command
     *
     * @return
     */
    Page<PublicAnnouncementVO1> getTopInformationListForPublic(PublicAnnouncementCommand command);

    /**
     * 构建通知人信息
     *
     * @param noticeConfigs
     *
     * @return
     */
    List<NoticeConfigVO> buildNoticeConfigInfo(List<NoticeConfig> noticeConfigs);
}
