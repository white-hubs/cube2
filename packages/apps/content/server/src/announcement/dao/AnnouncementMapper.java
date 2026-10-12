package com.guhecloud.cube.srv.gen.content.management.server.announcement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command.PublicAnnouncementCommand;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.AnnouncementTotalVo;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.AnnouncementVO;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.PublicAnnouncementVO1;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.po.Announcement;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.querygateway.query.AnnouncementCollectionQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/20
 */
@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
    List<AnnouncementTotalVo> getAnnouncementTotal();

    Page<AnnouncementVO> pageAnnouncement(Page<?> page, @Param("query") AnnouncementCollectionQuery query, @Param("begin") String begin,
                                          @Param("end") String end
                                         );

    AnnouncementVO getDetail(Long id);

    Page<PublicAnnouncementVO1> getAnnouncementsForPublic(Page<?> page, @Param("command") PublicAnnouncementCommand command);
}
