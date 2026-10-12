package com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command.PublicAnnouncementCommand;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.AnnouncementVO;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.PublicAnnouncementVO1;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.querygateway.AnnouncementQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/9 10:31
 * @Description: 通知公告对外暴露公共接口
 */

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicAnnouncementController {
    private final AnnouncementQueryService announcementQueryService;

    /**
     * 获取置顶资讯接口
     *
     * @param command 置顶资讯数量、默认三条
     *
     * @return
     */
    @GetMapping("/public/information/list")
    public Page<PublicAnnouncementVO1> getTopInformationListForPublic(PublicAnnouncementCommand command) {
        return announcementQueryService.getTopInformationListForPublic(command);
    }

    /**
     * 公告详情
     *
     * @param id 公告id
     *
     * @return
     */
    @GetMapping("/public/announcement/detail")
    public AnnouncementVO getAnnouncementDetailForPublic(@RequestParam(value = "id") Long id) {
        return announcementQueryService.getDetail(id);
    }
}
