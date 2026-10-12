package com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.AnnouncementCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command.AnnouncementStatusCommand;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command.SubmitAnnouncementCommand;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.enumeration.AnnouncementLevelEnum;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.AnnouncementLevelEnumVo;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.AnnouncementVO;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.po.Announcement;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.querygateway.AnnouncementQueryService;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.querygateway.query.AnnouncementCollectionQuery;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.po.NoticeConfig;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.querygateway.NoticeConfigQueryService;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 公告通知接口
 *
 * @author lzy
 * @date 2022/5/20
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnouncementController {

    private final AnnouncementCommandService announcementCommandService;
    private final AnnouncementQueryService announcementQueryService;
    private final NoticeConfigQueryService noticeConfigQueryService;
    private final FileClient fileClient;
    private final UserClient userClient;

    /**
     * 公告新增
     *
     * @param command 新增参数
     */
    @PostMapping("/announcement")
    public void save(@Valid @RequestBody SubmitAnnouncementCommand command) {
        this.announcementCommandService.save(command);
    }

    /**
     * 公告修改
     *
     * @param command 公告修改
     */
    @PutMapping("/announcement/update/{id}")
    public void updateAnnouncement(@PathVariable Long id, @Valid @RequestBody SubmitAnnouncementCommand command) {
        this.announcementCommandService.updateAnnouncement(id, command);
    }

    /**
     * 公告修改-置顶、隐藏
     * 支持批量
     *
     * @param listCommand 公告修改
     */
    @PostMapping("/announcement/update/hide-top")
    public void updateAnnouncementHideAndTop(@RequestBody List<AnnouncementStatusCommand> listCommand) {
        this.announcementCommandService.updateAnnouncementHideAndTop(listCommand);
    }

    /**
     * 公告删除
     *
     * @param id 主键
     */
    @DeleteMapping("/announcement/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.announcementCommandService.remove(id);
    }

    /**
     * 公告删除批量
     *
     * @param ids 主键
     */
    @DeleteMapping("/announcement/bath")
    public void removeBath(@RequestParam("ids") List<Long> ids) {
        this.announcementCommandService.removeBath(ids);
    }

    /**
     * 公告详情
     *
     * @param id 主键
     *
     * @return 公告信息
     */
    @GetMapping("/announcement/{id}")
    public AnnouncementVO getById(@PathVariable("id") Long id) {
        // 获取信息
        AnnouncementVO announcementVO = this.announcementQueryService.getDetail(id);

        // 获取通知人员
        List<NoticeConfig> noticeConfigs = this.noticeConfigQueryService.listByBusinessKey(id);
        for (AnnouncementLevelEnum value : AnnouncementLevelEnum.values()) {
            if (value.getCode().equals(announcementVO.getLevel())) {
                announcementVO.setLevelName(value.getName());
            }
        }
        UserDTO user;
        if (StringUtils.isNotBlank(announcementVO.getCreatorId()) && ActorTypeEnum.USER.name().equals(announcementVO.getCreatorType())) {
            user = userClient.getUser(Long.valueOf(announcementVO.getCreatorId()));
        } else {
            user = userClient.getUserByUserName(announcementVO.getCreateBy());
        }
        if (Objects.nonNull(user)) {
            announcementVO.setCreateBy(user.getName());
        }
        // 构建信息
        announcementVO.setNoticeConfigs(announcementQueryService.buildNoticeConfigInfo(noticeConfigs));
        // 获取文件信息
        List<FileVO> files = this.fileClient.getByBusinessKey(id);
        announcementVO.setFiles(files);
        Announcement announcement = this.announcementQueryService.getById(announcementVO.getId());
        announcement.setId(announcementVO.getId());
        announcement.setViewed(1);
        announcementQueryService.updateById(announcement);
        return announcementVO;
    }

    /**
     * 公告分页信息
     *
     * @param query 查询参数
     *
     * @return 公告信息
     */
    @GetMapping("/announcement/page")
    public Page<AnnouncementVO> pageAnnouncement(@Valid AnnouncementCollectionQuery query) {
        // 查询
        return this.announcementQueryService.pageAnnouncement(query);

    }

    /**
     * @Description 通告等级
     * @Author lrj
     */
    @GetMapping("/getAnnouncementLevels")
    public List<AnnouncementLevelEnumVo> getAnnouncementLevels() {
        return AnnouncementLevelEnum.getEnumTypeVo();
    }

    /**
     * app公告总览
     *
     * @return 公告信息
     */
    @GetMapping("/announcement/appTotal")
    public Map<String, Object> appTotalAnnouncement() {

        return this.announcementQueryService.appTotalAnnouncement();
    }

    /**
     * 获取未读公告数
     *
     * @return 公告信息
     */
    @GetMapping("/announcement/no-viewed")
    public Long getAnnouncementNoViewedNum() {

        return this.announcementQueryService.getAnnouncementNoViewedNum();
    }

}
