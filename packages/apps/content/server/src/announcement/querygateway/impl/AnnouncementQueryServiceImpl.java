package com.guhecloud.cube.srv.gen.content.management.server.announcement.querygateway.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.PositionClient;
import com.guhecloud.cube.biz.ouaa.client.RoleClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.PositionDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.RoleDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command.PublicAnnouncementCommand;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.dao.AnnouncementMapper;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.enumeration.AnnouncementLevelEnum;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.AnnouncementTotalVo;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.AnnouncementVO;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.PublicAnnouncementVO1;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.po.Announcement;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.querygateway.AnnouncementQueryService;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.querygateway.query.AnnouncementCollectionQuery;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.interfaces.vo.NoticeConfigVO;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.po.NoticeConfig;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @date 2022/5/20
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnouncementQueryServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementQueryService {

    private final AnnouncementMapper announcementMapper;

    private final UserClient userClient;
    private final FileClient fileClient;
    private final OrganizationClient organizationClient;
    private final PositionClient positionClient;
    private final RoleClient roleClient;

    @Override
    public Page<AnnouncementVO> pageAnnouncement(@NonNull AnnouncementCollectionQuery query) {
        Page<AnnouncementVO> announcementVOPage = announcementMapper.pageAnnouncement(PagingUtils.convert(query), query, "", "");
        announcementVOPage.getRecords().forEach(s -> {
            for (AnnouncementLevelEnum value : AnnouncementLevelEnum.values()) {
                if (value.getCode().equals(s.getLevel())) {
                    s.setLevelName(value.getName());
                }
            }
            UserDTO user;
            if (StringUtils.isNotBlank(s.getCreatorId()) && ActorTypeEnum.USER.name().equals(s.getCreatorType())) {
                user = userClient.getUser(Long.valueOf(s.getCreatorId()));
            } else {
                user = userClient.getUserByUserName(s.getCreateBy());
            }
            if (user != null) {
                s.setCreateBy(user.getName());
            }

        });
        return announcementVOPage;
    }

    @Override
    public Map<String, Object> appTotalAnnouncement() {
        HashMap<String, Object> remap = new HashMap<>();
        long count = this.count();
        List<AnnouncementTotalVo> announcementTotal = announcementMapper.getAnnouncementTotal();
        remap.put("total", count);
        remap.put("totalList", announcementTotal);
        return remap;
    }

    @Override
    public AnnouncementVO getDetail(Long id) {

        return announcementMapper.getDetail(id);
    }

    @Override
    public Long getAnnouncementNoViewedNum() {
        return announcementMapper.selectCount(new LambdaQueryWrapper<Announcement>()
                                                      .ne(Announcement::getViewed, 1)
                                                      .or().isNull(Announcement::getViewed));
    }

    @Override
    public Page<PublicAnnouncementVO1> getTopInformationListForPublic(PublicAnnouncementCommand command) {
        Page<PublicAnnouncementVO1> announcementVO1Page = announcementMapper.getAnnouncementsForPublic(PagingUtils.convert(command), command);
        if (Objects.isNull(announcementVO1Page) || CollectionUtils.isEmpty(announcementVO1Page.getRecords())) {
            return announcementVO1Page;
        }
        List<PublicAnnouncementVO1> announcementsForPublic = announcementVO1Page.getRecords();
        BusinessKeysParam businessKeysParam = new BusinessKeysParam();
        businessKeysParam.setBusinessKeys(announcementsForPublic.stream().map(PublicAnnouncementVO1::getId).collect(Collectors.toList()));
        Map<Long, List<FileVO>> byBusinessKeys = this.fileClient.getByBusinessKeys(businessKeysParam);
        for (PublicAnnouncementVO1 publicAnnouncementVO1 : announcementVO1Page.getRecords()) {
            for (AnnouncementLevelEnum value : AnnouncementLevelEnum.values()) {
                if (value.getCode().equals(publicAnnouncementVO1.getLevel())) {
                    publicAnnouncementVO1.setLevelName(value.getName());
                }
            }
            UserDTO user = userClient.getUserByUserName(publicAnnouncementVO1.getCreateBy());
            if (user != null) {
                publicAnnouncementVO1.setCreateBy(user.getName());
            }
            publicAnnouncementVO1.setFileVOS(byBusinessKeys.getOrDefault(publicAnnouncementVO1.getId(), new ArrayList<>()));
        }
        return announcementVO1Page;
    }

    @Override
    public List<NoticeConfigVO> buildNoticeConfigInfo(List<NoticeConfig> noticeConfigs) {
        Set<Long> userIds = new HashSet<>();
        Set<Long> departIds = new HashSet<>();
        Set<Long> postIds = new HashSet<>();
        Set<Long> roles = new HashSet<>();
        for (NoticeConfig noticeConfig : noticeConfigs) {
            if (StringUtils.isNotBlank(noticeConfig.getUser())) {
                userIds.addAll(Arrays.stream(noticeConfig.getUser().split("-")).map(Long::parseLong).toList());
            }
            if (StringUtils.isNotBlank(noticeConfig.getDepartment())) {
                departIds.addAll(Arrays.stream(noticeConfig.getDepartment().split("-")).map(Long::parseLong).toList());
            }
            if (StringUtils.isNotBlank(noticeConfig.getPost())) {
                postIds.addAll(Arrays.stream(noticeConfig.getPost().split("-")).map(Long::parseLong).toList());
            }

            if (StringUtils.isNotBlank(noticeConfig.getRole())) {
                roles.addAll(Arrays.stream(noticeConfig.getRole().split("-")).map(Long::parseLong).toList());
            }
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userIds)) {
            userDTOS = userClient.listUserByIds(userIds);
        }
        List<OrganizationDTO> organizationsByIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(departIds)) {
            organizationsByIds = organizationClient.getOrganizationsByIds(new ArrayList<>(departIds));
        }
        List<PositionDTO> positions = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(postIds)) {
            positions = positionClient.getPositions(new ArrayList<>(postIds));
        }
        List<RoleDTO> roleDTOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(roles)) {
            roleDTOS = roleClient.listRolesByIds(new ArrayList<>(roles));
        }
        List<NoticeConfigVO> noticeConfigVOS = new ArrayList<>();
        for (NoticeConfig noticeConfig : noticeConfigs) {
            NoticeConfigVO noticeConfigVO = ObjectUtils.copying(noticeConfig, NoticeConfigVO.class);
            if (StringUtils.isNotBlank(noticeConfig.getUser())) {
                noticeConfigVO.setUserIds(userDTOS.stream().filter(t -> Arrays.stream(noticeConfig.getUser().split("-"))
                                .map(Long::parseLong).toList()
                                .contains(t.getId()))
                                                  .map(user -> {
                                                      return NoticeConfigVO.InfoDTO.builder()
                                                              .id(user.getId())
                                                              .name(user.getName())
                                                              .build();
                                                  }).collect(Collectors.toList()));
            }
            if (StringUtils.isNotBlank(noticeConfig.getDepartment())) {
                noticeConfigVO.setUserIds(organizationsByIds.stream().filter(t -> Arrays.stream(noticeConfig.getDepartment().split("-"))
                                .map(Long::parseLong).toList()
                                .contains(t.getId()))
                                                  .map(depart -> NoticeConfigVO.InfoDTO.builder()
                                                          .id(depart.getId())
                                                          .name(depart.getName())
                                                          .build()).collect(Collectors.toList()));
            }
            if (StringUtils.isNotBlank(noticeConfig.getPost())) {
                noticeConfigVO.setUserIds(positions.stream().filter(t -> Arrays.stream(noticeConfig.getPost().split("-"))
                                .map(Long::parseLong).toList()
                                .contains(t.getId()))
                                                  .map(position -> NoticeConfigVO.InfoDTO.builder()
                                                          .id(position.getId())
                                                          .name(position.getName())
                                                          .build()).collect(Collectors.toList()));
            }

            if (StringUtils.isNotBlank(noticeConfig.getRole())) {
                noticeConfigVO.setUserIds(roleDTOS.stream().filter(t -> Arrays.stream(noticeConfig.getRole().split("-"))
                                .map(Long::parseLong).toList()
                                .contains(t.getId()))
                                                  .map(role -> NoticeConfigVO.InfoDTO.builder()
                                                          .id(role.getId())
                                                          .name(role.getName())
                                                          .build()).collect(Collectors.toList()));
            }
            noticeConfigVOS.add(noticeConfigVO);
        }
        return noticeConfigVOS;
    }
}
