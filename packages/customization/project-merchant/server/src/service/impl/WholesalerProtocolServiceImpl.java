package com.guhecloud.cube.deliver.project.merchant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.*;
import com.guhecloud.cube.deliver.project.merchant.entity.AreaDeliver;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerProtocol;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerProtocolTemplate;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerRelationship;
import com.guhecloud.cube.deliver.project.merchant.enums.ProjectMerchantAreaEnum;
import com.guhecloud.cube.deliver.project.merchant.enums.WholesalerEnum;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerProtocalTemplateMapper;
import com.guhecloud.cube.deliver.project.merchant.service.*;
import com.guhecloud.cube.deliver.project.merchant.job.CheckTaskJob;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerProtocalMapper;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-10
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerProtocolServiceImpl extends ServiceImpl<WholesalerProtocalMapper, WholesalerProtocol> implements WholesalerProtocolService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final WholesalerProtocalMapper mapper;
    private final WholesalerRelationshipService wrService;
    private final SpaceService spaceService;
    private final MerchantAreaService merchantAreaService;
    //    private final SmsApi smsApi;
    //    private final SiteMsgApi siteMsgApi;
    //    private final WeChatMsgApi weChatMsgApi;
    private final CompanyUserService cuService;
    private final WholesalerProtocalTemplateMapper templateMapper;
    private final WholesalerService wholesalerService;
    //    private final WholesalerAuthService authService;
    private final CheckTaskJob checkTaskJob;
    private final TaskExecutor taskExecutor;
    private final FileClient fileClient;

    @Override
    public void sendNotice(String projectId, String id) {
        //首先根据ID找到协议，
        WholesalerProtocol protocol = this.lambdaQuery().eq(WholesalerProtocol::getId, id).one();
        List<String> userIds = new ArrayList<>();
        //根据协议找到对应的没有签约的档位
        //在根据这些档位找到对应的user
        for (WholesalerRelationship wholesalerRelationship : wrService.lambdaQuery().eq(WholesalerRelationship::getType, PRO_RELATIONSHIP)
                .eq(WholesalerRelationship::getBusinessId, protocol.getId())
                .eq(WholesalerRelationship::getStatus, 0)
                .list()) {
            String stallOwner = this.getStallOwner(projectId, wholesalerRelationship.getStallId());
            if (StringUtils.isNotEmpty(stallOwner)) {
                userIds.add(stallOwner);
            }
        }
        if (!userIds.isEmpty()) {
            this.sendUrgeNotice(protocol, projectId, userIds);
        }
    }

    @Override
    public List<WholesalerProtocolAppDTO> getProtocolsOnApp(String stallId) {
        List<WholesalerProtocolAppDTO> dtos = new ArrayList<>();
        //获取档位下的列表

        for (WholesalerRelationship relationship :
                wrService.lambdaQuery()
                        .eq(WholesalerRelationship::getType, PRO_RELATIONSHIP)
                        .eq(WholesalerRelationship::getStallId, stallId).list()) {
            WholesalerProtocolAppDTO dto = new WholesalerProtocolAppDTO(this.lambdaQuery()
                                                                                .eq(WholesalerProtocol::getId, relationship.getBusinessId())
                                                                                .one());
            dto.setSigned(relationship.getSignedWho() != null ? 1 : 0);
            dtos.add(dto);
        }
        dtos.sort(Comparator.comparing(WholesalerProtocolAppDTO::getSigned)
                          .thenComparing(WholesalerProtocolAppDTO::getCreateTime, Comparator.reverseOrder()));
        return dtos;
    }

    @Override
    public WholesalerProtocolAppDTO getProtocolOnAppById(String stallId, String protocolId) {
        Space space = spaceService.lambdaQuery().eq(Space::getId, stallId).one();
        WholesalerRelationship relationship =
                wrService.lambdaQuery().eq(WholesalerRelationship::getType, PRO_RELATIONSHIP)
                        .eq(WholesalerRelationship::getStallId, stallId)
                        .eq(WholesalerRelationship::getBusinessId, protocolId).one();
        WholeSpaceDTO wholeSpaceDto = new WholeSpaceDTO(space);
        AreaDeliver area1 = merchantAreaService.lambdaQuery().eq(AreaDeliver::getId, space.getAreaId1()).one();
        AreaDeliver area2 = merchantAreaService.lambdaQuery().eq(AreaDeliver::getId, space.getAreaId2()).one();
        String area2Name = area2 != null ? area2.getName() : "";
        List<String> area = new ArrayList<>();
        area.add(ObjectUtil.isNotEmpty(area1) ? area1.getName() : "");
        area.add(area2Name);
        wholeSpaceDto.setArea(area);
        wholeSpaceDto.setStatus(space.getCompanyId() == null ? 0 : 1);

        wholeSpaceDto.setSignedWho(relationship.getSignedWho());
        wholeSpaceDto.setSignedTime(relationship.getSigendTime());
        WholesalerProtocol protocol = this.lambdaQuery().eq(WholesalerProtocol::getId, protocolId).one();
        WholesalerProtocolAppDTO dto = new WholesalerProtocolAppDTO(protocol);
        dto.setStallDto(wholeSpaceDto);
        dto.setSigned(relationship.getSignedWho() == null ? 0 : 1);
        // 扩展协议模板附件
        dto.setTemplateFileVOS(fileClient.getByBusinessKey(Long.parseLong(dto.getTemplateId())));
        return dto;
    }

    @Override
    public WholesalerProtocolDTO getProtocolById(String projectId, String id) {
        //获取所有档位先
        //        List<WholeSpaceDTO> spaceDTOS = wholesalerService.getWholesalerStalls(projectId);
        WholesalerProtocol protocol = this.lambdaQuery().eq(WholesalerProtocol::getId, id).one();
        WholesalerProtocolDTO dto = new WholesalerProtocolDTO(protocol);
        int count = 0;
        int signedCount = 0;
        //找关联的档位
        List<WholeSpaceDTO> spaceDtos = new ArrayList<>();
        //遍历，在中间表找到对应的档位ID集合，然后找到对应的所有spacedto
        for (WholesalerRelationship wholesalerRelationship : wrService.lambdaQuery().eq(WholesalerRelationship::getType, PRO_RELATIONSHIP)
                .eq(WholesalerRelationship::getBusinessId, protocol.getId())
                .list()) {
            String stallId = wholesalerRelationship.getStallId();
            Space space = spaceService.lambdaQuery().eq(Space::getId, stallId).one();
            if (space == null) {
                throw new ServiceException("档位关联有误，系统中档位ID" + stallId + "不存在");
            }
            WholeSpaceDTO wholeSpaceDto = wholesalerService.getWholesalerStalls(projectId, Long.parseLong(space.getId()));// spaceDTOS.stream().filter(s -> s.getId().equals(space.getId())).collect(Collectors.toList()).get(0);
            wholeSpaceDto.setSignedTime(wholesalerRelationship.getSigendTime());
            wholeSpaceDto.setSignedWho(wholesalerRelationship.getSignedWho());
            wholeSpaceDto.setOwner(wholesalerRelationship.getOwner());
            wholeSpaceDto.setStatus(wholesalerRelationship.getStatus());
            if (wholesalerRelationship.getStatus() != null && wholesalerRelationship.getStatus() == 1) {
                signedCount += 1;
            }
            count++;
            spaceDtos.add(wholeSpaceDto);
        }
        dto.setTemplateTypeName(ProjectMerchantAreaEnum.getCodeByName(dto.getTemplateType()));
        dto.setCount(count);
        dto.setSignedCount(signedCount);
        dto.setNotSignedCount(count - signedCount);
        dto.setStallDtos(spaceDtos);
        //有任意一个没有签约的签约的话，该协议的签约状态就是未完成0,
        Integer processingStatus = spaceDtos.stream().anyMatch(spaceDto -> spaceDto.getSignedWho() == null) ? 0 : 1;
        dto.setProcessingStatus(processingStatus);
        dto.setStallIds(spaceDtos.stream().map(WholeSpaceDTO::getId).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public String sendVerifyCode(String projectId, String mobile, LoginUser user) {
        //verifyCode为全局变量，下一个方法也可以用
        String verifyCode = RandomUtil.randomNumbers(PRO_RANDOM_NUMBER_LENGTH);
        List<Long> userIds = new ArrayList<>();
        userIds.add(user.getId());
        //发送信息
        String key = projectId + PRO_VERIFY_CODE_TYPE + mobile + ":" + user.getUsername();
        redisTemplate.opsForValue().set(key, verifyCode, 5, TimeUnit.MINUTES);
        //        smsApi.send(projectId, PRO_VERIFY_CODE_TYPE, true, mobile, userIds,
        //                JSmsTemplateEnum.T_VERIFY.getSmsTempId(), verifyCode);
        return verifyCode;
    }

    @Override
    public String saveOrUpdate(WholesalerProtocolDTO dto) {
        // 检查有效状态
        this.checkValidStatus(dto);
        WholesalerProtocolTemplate template = templateMapper.selectOne(
                Wrappers.<WholesalerProtocolTemplate>lambdaQuery()
                        .eq(WholesalerProtocolTemplate::getId, dto.getTemplateId())
                                                                      );
        dto.setTemplateName(template.getName());
        dto.setTemplateType(template.getType());
        WholesalerProtocol protocol = dto.convert();
        //count决定是insert还是save
        long count = this.lambdaQuery().eq(WholesalerProtocol::getId, dto.getId()).count();
        //如果传进来的dto有ID，并且在数据库中有，那么就是update
        //update 需要将之前的中间表关系（协议和档位的关系）删除掉，然后新增对应关系
        if (StrUtil.isNotBlank(dto.getId()) && count == 1) {
            mapper.updateById(protocol);
            //删除掉之前的和档位的对应关系
            wrService.removeBatch(dto.getId(), PRO_RELATIONSHIP);
        } else {
            protocol.setCode(BizCodeUtils.generate("PRO"));
            protocol.setCreateTime(LocalDateTime.now());
            protocol.setCreateBy(SecurityContextHolder.getPrincipal().getName());
            mapper.insert(protocol);
            dto.setId(protocol.getId());
            // 发送通知
            this.firstSendNotice(dto.getStallIds(), dto.getProjectId(), protocol);
        }
        String protocolId = count == 1 ? dto.getId() : protocol.getId();
        //添加关系到中间表
        wrService.saveBatch(dto.getStallIds(), protocolId, PRO_RELATIONSHIP);
        // 生成执行器
        this.createXxlJob(dto);
        return protocolId;
    }

    @Override
    public void signProtocol(WholesalerProtocolVerCodeDTO dto, LoginUser user) {
        //        String key = dto.getProjectId() + PRO_VERIFY_CODE_TYPE + dto.getMobile() + ":" + user.getUsername();
        //        String value = (String) redisTemplate.opsForValue().get(key);
        //        if (value == null) {
        //            throw new ServiceException("请先获取验证码");
        //        }
        //        if (!dto.getVerifyCode().equals(value)) {
        //            throw new ServiceException("验证码输入不正确，请重新输入，验证码5分钟内均有效");
        //        }
        // 判断是否实名认证
        //        List<WholesalerAuth> auths = authService.lambdaQuery().eq(WholesalerAuth::getProjectId, dto.getProjectId()).eq(WholesalerAuth::getUserId, user.getId()).like(WholesalerAuth::getStallIds, dto.getStallId()).eq(WholesalerAuth::getStatus, 1).list();
        //        if (auths.size() < 1) throw new ServiceException("签订失败，您未实名认证该签约档位！");
        // 签署人
        //        UserInfoDTO infoDTO = userApi.getById(user.getId());
        // 签署
        wrService.lambdaUpdate().eq(WholesalerRelationship::getStallId, dto.getStallId())
                .eq(WholesalerRelationship::getBusinessId, dto.getProtocolId())
                .set(WholesalerRelationship::getSigendTime, LocalDateTime.now())
                .set(WholesalerRelationship::getSignedWho, SecurityContextHolder.getPrincipal().getName())
                .set(WholesalerRelationship::getOwner, 1)
                .set(WholesalerRelationship::getStatus, 1)
                .update();
    }

    @Override
    public List<WholesalerProtocolDTO> getProtocolsOnPc(WholesalerProQueryDTO queryDTO) {
        List<WholesalerProtocol> protocols = this.lambdaQuery()
                .eq(WholesalerProtocol::getProjectId, queryDTO.getProjectId())
                .eq(queryDTO.getStatus() != null, WholesalerProtocol::getStatus, queryDTO.getStatus())
                .eq(StrUtil.isNotBlank(queryDTO.getType()), WholesalerProtocol::getTemplateType, queryDTO.getType())
                .between(queryDTO.getBeginTime() != null && queryDTO.getEndTime() != null,
                         WholesalerProtocol::getCreateTime, queryDTO.getBeginTime(), queryDTO.getEndTime())
                .and(StrUtil.isNotBlank(queryDTO.getSearch()), q -> q
                        .like(WholesalerProtocol::getName, queryDTO.getSearch())
                        .or()
                        .like(WholesalerProtocol::getCreateBy, queryDTO.getSearch())
                        .or()
                        .like(WholesalerProtocol::getCode, queryDTO.getSearch())
                    )
                .list();
        List<WholesalerProtocolDTO> dtos = new WholesalerProtocolDTO(protocols).list();
        // 查询商户下所关联的档位
        List<String> stallByCompany = this.getStallByCompany(queryDTO.getProjectId(), queryDTO.getCompanyId());
        dtos = dtos.stream().map(dto -> {
            List<WholesalerRelationship> relationships = new ArrayList<>();
            if (StringUtils.isEmpty(queryDTO.getCompanyId())) {
                relationships = wrService.lambdaQuery().eq(WholesalerRelationship::getType, PRO_RELATIONSHIP)
                        .eq(WholesalerRelationship::getBusinessId, dto.getId()).list();
            } else {
                if (stallByCompany.size() < 1) {
                    return null;
                }
                relationships = wrService.lambdaQuery().eq(WholesalerRelationship::getType, PRO_RELATIONSHIP)
                        .in(WholesalerRelationship::getStallId, stallByCompany)
                        .eq(WholesalerRelationship::getBusinessId, dto.getId()).list();
                if (relationships.size() < 1) {
                    return null;
                }
            }
            int count = relationships.size();
            int sign = (int) relationships.stream().filter(r -> r.getStatus() == 1).count();
            dto.setCount(count);
            dto.setSignedCount(sign);
            dto.setNotSignedCount(count - sign);
            dto.setProcessingStatus(count == sign ? 1 : 0);
            return dto;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (queryDTO.getProcessingStatus() != null) {
            dtos = dtos.stream().filter(dto -> dto.getProcessingStatus().equals(queryDTO.getProcessingStatus()))
                    .collect(Collectors.toList());
        }
        return dtos;
    }

    @Override
    public void remove(String id) {
        WholesalerProtocol protocol = this.getById(id);
        if (protocol == null) {
            return;
        }
        //删除协议对应的关系relationship
        List<WholesalerRelationship> relationships = wrService.lambdaQuery().eq(WholesalerRelationship::getType, PRO_RELATIONSHIP)
                .eq(WholesalerRelationship::getBusinessId, id)
                .list();
        relationships.forEach(relationship -> wrService.removeById(relationship.getId()));
        //再删除对应的协议
        mapper.deleteById(id);
        checkTaskJob.removeTask(protocol.getXxlTaskIds());
    }

    @Override
    public List<WholesalerProtocolDTO> getProtocolsByCompanyIdOrStallId(String projectId, String companyId, String stallId) {
        List<WholesalerProtocolDTO> dtos = new ArrayList<>();
        WholesalerProQueryDTO queryDTO = new WholesalerProQueryDTO();
        queryDTO.setProjectId(projectId);
        //        List<WholesalerProtocolDTO> protocolDTOS = getProtocolsOnPc(queryDTO);
        //首先根据companyId找到stallid，然后根据stallId和type找到对应的 relationship
        List<Space> spaceList = spaceService.lambdaQuery().eq(StringUtils.isNotEmpty(stallId), Space::getId, stallId)
                .eq(StringUtils.isNotEmpty(companyId), Space::getCompanyId, companyId).list();
        spaceList.forEach(space -> {
            List<WholesalerRelationship> relationships = wrService.lambdaQuery().eq(WholesalerRelationship::getType, PRO_RELATIONSHIP)
                    .eq(WholesalerRelationship::getStallId, space.getId())
                    .list();
            if (relationships != null) {
                relationships.forEach(relationship -> {
                    WholesalerProtocol protocol = this.getById(relationship.getBusinessId());
                    if (protocol != null) {
                        WholesalerProtocolDTO protocolDTO = new WholesalerProtocolDTO(protocol);
                        protocolDTO.setSignedStatus(relationship.getStatus());
                        dtos.add(protocolDTO);
                    }
                });
            }
        });
        return dtos;
    }

    private void sendUrgeNotice(WholesalerProtocol protocol, String projectId, List<String> userIds) {
        //暂时用的其他模板 ，自己模板还没申请下来
        try {
            //            smsApi.send(projectId, PRO_NOTICE, protocol.getId(), new ArrayList<>(userIds),
            //                    JSmsTemplateEnum.T_PROTOCOL.getSmsTempId(), protocol.getName(), DateUtils.getDate(protocol.getBeginTime(), "yyyy-MM-dd"), DateUtils.getDate(protocol.getEndTime(), "yyyy-MM-dd"));
            //            weChatMsgApi.send(projectId, PRO_NOTICE, new ArrayList<>(userIds), PRO_CONTENT);
            //            siteMsgApi.send(projectId, PRO_NOTICE, protocol.getId(), userIds, PRO_TITLE, PRO_CONTENT, 7 * 24);
        } catch (Exception e) {
            log.error("催送签约消息发送失败，任务ID：" + protocol.getId(), e);
        }
    }

    private void createXxlJob(WholesalerProtocolDTO dto) {
        // 生成定时任务，准时更新任务的生效和失效状态
        if (!WholesalerEnum.PROTOCOL_STATUS_NOT_VALID.getCode().equals(dto.getStatus())) {
            //            String xxlIds = checkXxlTaskJob.createXxlTask(WholesalerEnum.BUSINESS_TYPE_PROTOCOL.getCode(), dto.getId(), WholesalerEnum.PROTOCOL_STATUS_VALID.getCode().equals(dto.getStatus()) ? null : dto.getBeginTime(), dto.getEndTime());
            //            WholesalerProtocol protocol = dto.convert();
            //            protocol.setXxlTaskIds(xxlIds);
            //            this.updateById(protocol);
        }
    }

    private void checkValidStatus(WholesalerProtocolDTO dto) {
        // 判断状态
        if (dto.getEndTime().isBefore(LocalDateTime.now())) {
            dto.setStatus(WholesalerEnum.PROTOCOL_STATUS_NOT_VALID.getCode());
            dto.setBeginTime(LocalDateTime.now());
        } else if (dto.isBeginTimeIsNow() || dto.getBeginTime().isBefore(LocalDateTime.now())) {
            if (dto.isBeginTimeIsNow()) {
                dto.setBeginTime(LocalDateTime.now());
            }
            dto.setStatus(WholesalerEnum.PROTOCOL_STATUS_VALID.getCode());
        } else {
            dto.setStatus(WholesalerEnum.PROTOCOL_STATUS_NOT_CREATE.getCode());
        }
        dto.setBeginTime(dto.getBeginTime() == null ? LocalDateTime.now() : dto.getBeginTime());
    }

    private void firstSendNotice(List<String> stallIds, String projectId, WholesalerProtocol protocol) {
        if (stallIds == null || stallIds.size() < 1) {
            return;
        }
        WholesalerProtocolServiceImpl _this = this;
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //                DataSourceContextHolder.setDataSourceKey(DataSources.PROJECT.getValue());
                // 查到档位的档主信息并发送通知
                List<String> userIds = new ArrayList<>();
                stallIds.forEach(stallId -> {
                    String stallOwner = _this.getStallOwner(projectId, stallId);
                    if (StringUtils.isNotEmpty(stallOwner)) {
                        userIds.add(stallOwner);
                    }
                });
                _this.sendUrgeNotice(protocol, projectId, userIds);
            }
        });
    }

    // 查询档位的档主
    private String getStallOwner(String projectId, String stallId) {
        CompanyUser companyUser = cuService.lambdaQuery().eq(CompanyUser::getStallId, stallId)
                .eq(CompanyUser::getProjectId, projectId)
                .eq(CompanyUser::getOwner, true).one();
        return companyUser == null ? "" : companyUser.getUserId();
    }

    // 查询商户所关联的档位
    private List<String> getStallByCompany(String projectId, String companyId) {
        return spaceService.lambdaQuery().eq(Space::getProjectId, projectId).eq(Space::getCompanyId, companyId).list().stream()
                .map(Space::getId).collect(Collectors.toList());
    }

}
