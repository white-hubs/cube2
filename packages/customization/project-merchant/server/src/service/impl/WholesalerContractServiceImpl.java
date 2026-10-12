package com.guhecloud.cube.deliver.project.merchant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.*;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.*;
import com.guhecloud.cube.deliver.project.merchant.enumeration.ProjectMerchantMessageEnum;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerContractMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerContractTypeMapper;
import com.guhecloud.cube.deliver.project.merchant.service.*;
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
 * @since 2022-01-14
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerContractServiceImpl extends ServiceImpl<WholesalerContractMapper, WholesalerContract> implements WholesalerContractService {

    private final WholesalerContractMapper mapper;
    private final WholesalerFirstPartService firstService;
    private final WholesalerRelationshipService relationshipService;
    private final FileClient fileClient;
    private final SpaceService spaceService;
    private final MerchantAreaService merchantAreaService;
    private final WholesalerSecondPartService secondPartService;
    private final WholesalerNoticeService wholesalerNoticeService;
    private final RedisTemplate<String, Object> redisTemplate;
    //    private final SmsApi smsApi;
    private final WholesalerContractTypeMapper typeMapper;
    private final CompanyUserService cuService;
    private final TaskExecutor taskExecutor;
    //    private final SiteMsgApi siteMsgApi;
    //    private final WeChatMsgApi weChatMsgApi;

    @Override
    public void sendNotice(String projectId, String id) {
        //首先根据ID找到协议，
        WholesalerContract contract = this.lambdaQuery().eq(WholesalerContract::getId, id).one();
        Set<Long> userIds = new HashSet<>();
        //根据协议找到对应的没有签约的档位
        //在根据这些档位找到对应的user
        for (WholesalerRelationship wholesalerRelationship : relationshipService.lambdaQuery()
                .eq(WholesalerRelationship::getType, TYPE_CONTRACT)
                .eq(WholesalerRelationship::getBusinessId, contract.getId())
                .eq(WholesalerRelationship::getStatus, 0)
                .list()) {
            CompanyUser companyUser = cuService.lambdaQuery().eq(CompanyUser::getStallId, wholesalerRelationship.getStallId())
                    .eq(CompanyUser::getProjectId, projectId)
                    .eq(CompanyUser::getOwner, 1).one();
            userIds.add(Long.parseLong(companyUser.getUserId()));
        }
        sendUrgeNotice(contract, projectId, userIds, "");
    }

    @Override
    public List<WholesalerContractDTO> getContracts(WholesalerContractQueryDTO queryDTO) {
        List<WholesalerContract> contracts = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(queryDTO.getProjectId()), WholesalerContract::getProjectId, queryDTO.getProjectId())
                .eq(queryDTO.getSigningWay() != null, WholesalerContract::getSigningWay, queryDTO.getSigningWay())
                .eq(queryDTO.getStatus() != null, WholesalerContract::getStatus, queryDTO.getStatus())
                .and(StrUtil.isNotBlank(queryDTO.getSearch()), q -> q
                        .like(WholesalerContract::getCreateBy, queryDTO.getSearch())
                        .or()
                        .like(WholesalerContract::getCode, queryDTO.getSearch())
                    )
                .eq(StringUtils.isNotEmpty(queryDTO.getContractType()), WholesalerContract::getContractTypeId, queryDTO.getContractType())
                .between(null != queryDTO.getBeginTime() &&
                         null != queryDTO.getEndTime(), WholesalerContract::getCreateTime, queryDTO.getBeginTime(), queryDTO.getEndTime())
                .list();
        List<WholesalerContractDTO> dtos = new WholesalerContractDTO(contracts).list();
        dtos.forEach(dto -> {
            List<WholeSpaceDTO> stallDtos = new ArrayList<>();
            WholesalerContractType type = typeMapper.selectOne(Wrappers.<WholesalerContractType>lambdaQuery()
                                                                       .eq(WholesalerContractType::getId, dto.getContractTypeId()));
            WholesalerContractTypeDTO typeDTO = new WholesalerContractTypeDTO(type);
            dto.setContractTypeDto(typeDTO);
            //找到对应的关系，然后对应的spaceDao
            List<WholesalerRelationship> relationships = relationshipService.lambdaQuery()
                    .eq(WholesalerRelationship::getType, TYPE_CONTRACT)
                    .eq(WholesalerRelationship::getBusinessId, dto.getId())
                    .list();

            relationships.forEach((relationship -> {
                stallDtos.add(new WholeSpaceDTO(spaceService.lambdaQuery().eq(Space::getId, relationship.getStallId()).one()));
            }));
            dto.setStallDtos(stallDtos);
        });
        // 根据档位ID筛选
        if (StrUtil.isNotBlank(queryDTO.getStallId())) {
            dtos = dtos.stream().map(dto -> {
                List<WholeSpaceDTO> stallDtos = dto.getStallDtos();
                if (stallDtos.stream().anyMatch(stallDto -> queryDTO.getStallId().equals(stallDto.getId()))) {
                    return dto;
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        //        if (StrUtil.isNotBlank(queryDTO.getContractType())) {
        //            dtos = dtos.stream().filter(dto -> dto.getContractTypeDto().getName().equals(queryDTO.getContractType()))
        //                    .collect(Collectors.toList());
        //        }
        dtos.sort(Comparator.comparing(WholesalerContractDTO::getCreateTime, Comparator.nullsFirst(LocalDateTime::compareTo)).reversed());
        return dtos;
    }

    @Override
    public PageResponse<WholesalerContractDTO> getMyContractDTOS(WholesalerContractQueryDTO queryDTO, PageRequest pageRequest) {
        List<WholesalerContractDTO> dtos = new ArrayList<>();
        List<WholesalerRelationship> relationships = relationshipService.lambdaQuery().eq(WholesalerRelationship::getType, TYPE_CONTRACT)
                .eq(WholesalerRelationship::getStallId, queryDTO.getStallId())
                .list();
        List<Long> contractIds = relationships.stream().map(WholesalerRelationship::getBusinessId).collect(Collectors.toList());
        if (contractIds.size() > 0) {
            List<WholesalerContract> contracts = this.lambdaQuery()
                    .eq(StrUtil.isNotBlank(queryDTO.getProjectId()), WholesalerContract::getProjectId, queryDTO.getProjectId())
                    .ne(WholesalerContract::getStatus, 0)
                    .in(WholesalerContract::getId, contractIds)
                    .list();
            dtos.addAll(new WholesalerContractDTO(contracts).list());
            dtos.forEach(dto -> {
                WholesalerContractType type = typeMapper.selectOne(Wrappers.<WholesalerContractType>lambdaQuery()
                                                                           .eq(WholesalerContractType::getId, dto.getContractTypeId()));
                WholesalerContractTypeDTO typeDTO = new WholesalerContractTypeDTO(type);
                dto.setContractTypeDto(typeDTO);
            });
        }
        dtos.sort(Comparator.comparing(WholesalerContractDTO::getStatus)
                          .thenComparing(WholesalerContractDTO::getCreateTime, Comparator.reverseOrder()));
        return PageResponse.doPage(dtos, pageRequest);
    }

    @Override
    public WholesalerContractDTO getContractById(String id) {
        WholesalerContract contract = this.lambdaQuery().eq(WholesalerContract::getId, id).one();
        WholesalerContractDTO dto = new WholesalerContractDTO(contract);
        // 查询甲方信息
        WholesalerFirstPart firstPart = firstService.lambdaQuery()
                .eq(WholesalerFirstPart::getProjectId, contract.getProjectId()).one();
        if (firstPart != null) {
            WholesalerFirstPartDTO firstPartDTO = new WholesalerFirstPartDTO(firstPart);
            // 甲方签章
            //            firstPartDTO.setFileDtos(fileApi.getList(WholesalerFirstPartService.TYPE_FIRST_PART, firstPart.getId()));
            dto.setFirstPartDto(firstPartDTO);
        }
        if (EFFECTED_STATUS.equals(contract.getStatus())) {
            //            dto.setSeatDtos(fileApi.getList(WholesalerFirstPartService.TYPE_FIRST_PART, firstPart.getId()));
        }
        WholesalerContractType type = typeMapper.selectOne(Wrappers.<WholesalerContractType>lambdaQuery()
                                                                   .eq(WholesalerContractType::getId, dto.getContractTypeId()));
        WholesalerContractTypeDTO typeDTO = new WholesalerContractTypeDTO(type);
        dto.setContractTypeDto(typeDTO);
        //查找对应的档位
        List<WholesalerRelationship> relationships = relationshipService.lambdaQuery()
                .eq(WholesalerRelationship::getType, TYPE_CONTRACT)
                .eq(WholesalerRelationship::getBusinessId, id)
                .list();
        List<WholeSpaceDTO> spaceDtoList = new ArrayList<>();
        relationships.forEach(relationship -> {
            //根据关系中的STALLID找到对应的档位，获取对应的档位信息
            Space space = spaceService.lambdaQuery().eq(Space::getId, relationship.getStallId()).one();
            if (space != null) {
                WholeSpaceDTO spaceDto = new WholeSpaceDTO(space);
                spaceDto.setArea(merchantAreaService.parseNames(Arrays.asList(space.getAreaId1(), space.getAreaId2(), space.getAreaId3())));
                spaceDtoList.add(spaceDto);
            }
        });
        dto.setStallDtos(spaceDtoList);
        //查找对应的合同附件
        this.getMarkFile(dto);
        //找对应的乙方签约消息
        WholesalerSecondPart secondPart = secondPartService.lambdaQuery()
                .eq(WholesalerSecondPart::getContractId, id).one();
        dto.setSecondPart(secondPart);
        return dto;
    }

    @Override
    public Long saveOrUpdate(WholesalerContractDTO dto) {
        WholesalerContract contract = dto.convert();
        //首先还是老规矩，先判断一下是update还是insert
        boolean exist = dto.getId() != null && (this.lambdaQuery().eq(WholesalerContract::getId, dto.getId()).one() != null);
        if (exist) {
            //修改的时候，将之前的关系先清除掉
            //            fileApi.removeBatch(TYPE_CONTRACT, contract.getId());
            List<WholesalerRelationship> relationships = relationshipService.lambdaQuery()
                    .eq(WholesalerRelationship::getBusinessId, contract.getId())
                    .eq(WholesalerRelationship::getType, TYPE_CONTRACT)
                    .list();
            relationships.forEach(relationship -> relationshipService.removeById(relationship.getId()));
            mapper.updateById(contract);
        } else {
            contract.setCode(BizCodeUtils.generate("HT"));
            contract.setCreateBy(SecurityContextHolder.getPrincipal().getName());
            contract.setCreateTime(LocalDateTime.now());
            mapper.insert(contract);
        }
        //将档位关系，还有合同图片关系管理进对应的表中
        if (dto.getStallIds() != null && !dto.getStallIds().isEmpty()) {
            dto.getStallIds().forEach(stallId -> {
                WholesalerRelationship relationship = new WholesalerRelationship();
                relationship.setType(TYPE_CONTRACT);
                relationship.setStallId(stallId);
                relationship.setBusinessId(contract.getId());
                relationshipService.save(relationship);
            });
        }
        if (!dto.getPaperContractIds().isEmpty()) {
            //            dto.getPaperContractIds().forEach(contractId -> fileApi.mark(contractId, TYPE_CONTRACT, contract.getId()));
        }
        // 保存并提交的时候，通知商户
        if (1 == dto.getStatus()) {
            this.firstSendNotice(dto.getStallIds(), dto.getProjectId(), contract);
        }
        return contract.getId();
    }

    @Override
    public String signBySecondPart(WholesalerSecondPartDTO dto) {
        dto.setId("");
        WholesalerSecondPart secondPart = dto.convert();
        //如果该合同已经认证过了，需要再认证，就是UPDATE
        WholesalerSecondPart secondPartEntity = secondPartService.lambdaQuery()
                .eq(WholesalerSecondPart::getContractId, dto.getContractId()).one();
        if (secondPartEntity != null) {
            dto.setId(secondPartEntity.getId());
            secondPartService.updateById(dto.convert());
            return secondPartEntity.getId();
        }

        secondPartService.save(secondPart);
        return secondPart.getId();
    }

    @Override
    public String sendVerifyCode(String projectId, String contractId, String mobile, LoginUser user) {
        //verifyCode为全局变量，下一个方法也可以用
        String verifyCode = RandomUtil.randomNumbers(RANDOM_NUMBER_LENGTH);
        List<Long> userIds = new ArrayList<>();
        userIds.add(user.getId());
        //发送信息
        String key = projectId + REDIS_TYPE + mobile + ":" + contractId;
        redisTemplate.opsForValue().set(key, verifyCode, 5, TimeUnit.MINUTES);
        //        smsApi.send(projectId, VERIFY_CODE_TYPE, true, mobile, userIds,
        //                JSmsTemplateEnum.T_VERIFY.getSmsTempId(), verifyCode);
        return verifyCode;
    }

    @Override
    public void signConfirm(WholesalerContractSignDTO dto, LoginUser user) {
        // 校验实名认证
        String key = dto.getProjectId() + REDIS_TYPE + dto.getMobile() + ":" + dto.getContractId();
        String value = (String) redisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new ServiceException("请先获取验证码");
        }
        if (!dto.getVerifyCode().equals(value)) {
            throw new ServiceException("验证码输入不正确，请重新输入，验证码5分钟内均有效");
        }
        //验证通过，将乙方ID信息和甲方信息确认连接起来
        secondPartService.lambdaUpdate().eq(WholesalerSecondPart::getId, dto.getSecondPartId())
                .set(WholesalerSecondPart::getStatus, 1)
                .update();
        //设置中间关系表
        relationshipService.lambdaUpdate().eq(WholesalerRelationship::getType, TYPE_CONTRACT)
                .eq(WholesalerRelationship::getBusinessId, dto.getContractId())
                .set(WholesalerRelationship::getSigendTime, LocalDateTime.now())
                .set(WholesalerRelationship::getSignedWho, user.getUsername())
                .update();
        //然后合同状态也修改为2
        this.lambdaUpdate().eq(WholesalerContract::getId, dto.getContractId())
                .set(WholesalerContract::getStatus, 2)
                .set(WholesalerContract::getSignatoryName, user.getUsername())
                .set(WholesalerContract::getSignatoryTime, LocalDateTime.now())
                .update();
        // 发送消息给市场方
        this.sendNoticeToProject(dto.getContractId(), dto.getSecondPartId());
    }

    @Override
    public void signConfirmFirstPart(String signatoryName, String signatoryId, String contractId) {
        //首先
        this.lambdaUpdate().eq(WholesalerContract::getId, contractId)
                .set(WholesalerContract::getSignatoryName, signatoryName)
                .set(WholesalerContract::getSignatoryId, signatoryId)
                .set(WholesalerContract::getSignatoryTime, LocalDateTime.now())
                .set(WholesalerContract::getStatus, 3)
                .update();
    }

    @Override
    public List<WholesalerContract> getContractList(String projectId) {
        return this.lambdaQuery()
                .eq(WholesalerContract::getProjectId, projectId).list();
    }

    @Override
    public void commit(String id) {
        WholesalerContract contract = mapper.selectById(id);
        if (null == contract) {
            return;
        }
        List<WholesalerRelationship> relationships = relationshipService.lambdaQuery()
                .eq(WholesalerRelationship::getBusinessId, contract.getId())
                .eq(WholesalerRelationship::getType, TYPE_CONTRACT)
                .list();
        this.lambdaUpdate().eq(WholesalerContract::getId, id).set(WholesalerContract::getStatus, 1).update();
        // 通知商户
        this.firstSendNotice(relationships.stream().map(WholesalerRelationship::getStallId)
                                     .collect(Collectors.toList()), contract.getProjectId(), contract);
    }

    private void firstSendNotice(List<String> stallIds, String projectId, WholesalerContract contract) {
        if (stallIds == null || stallIds.size() < 1) {
            return;
        }
        WholesalerContractServiceImpl _this = this;
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //                DataSourceContextHolder.setDataSourceKey(DataSources.PROJECT.getValue());
                // 查到档位的档主信息并发送通知
                Set<Long> userIds = new HashSet<>();
                stallIds.forEach(stallId -> {
                    String stallOwner = _this.getStallOwner(projectId, stallId);
                    if (StringUtils.isNotEmpty(stallOwner)) {
                        userIds.add(Long.parseLong(stallOwner));
                    }
                });
                _this.sendUrgeNotice(contract, projectId, userIds, "");
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

    /**
     * 获取评论DTO关联的文件
     */
    private void getMarkFile(WholesalerContractDTO dto) {
        List<FileVO> fileDtos = null;
        try {
            fileDtos = fileClient.getByBusinessKey(Long.parseLong(dto.getId()));
        } catch (Exception e) {
            log.error("获取文件异常", e);
        }
        if (fileDtos != null) {
            dto.setPaperContractIds(fileDtos.stream().map(FileVO::getId).collect(Collectors.toList()));
            dto.setPaperContractDtos(fileDtos);
        }

    }

    // 签订完成通知市场方
    private void sendNoticeToProject(String contractId, String secondPartId) {
        WholesalerContract contract = this.getById(contractId);
        WholesalerSecondPart secondPart = secondPartService.getById(secondPartId);
        if (contract == null || secondPart == null) {
            return;
        }
        try {
            // 获取发送通知的用户
            List<WholesalerNoticeDTO> noticeDTOS = wholesalerNoticeService.getNoticeIds(CONTRACT_NOTICE);
            noticeDTOS.forEach(notice -> {
                Set<String> userIds = notice.getNoticeUserIds();
                if (userIds.size() < 1) {
                    return;
                }
                if (notice.isSite()) {
                    //                    siteMsgApi.send(contract.getProjectId(), CONTRACT_NOTICE, contract.getId(), new ArrayList<>(userIds), CONTRACT_TITLE_PROJECT, secondPart.getName() + CONTRACT_CONTENT_TO_PROJECT, 7 * 24, true, notice.isPop(), contract);
                }
                if (notice.isSms()) {
                    //                    smsApi.send(contract.getProjectId(), CONTRACT_NOTICE, true, contract.getId(), new ArrayList<>(userIds), JSmsTemplateEnum.T_CONTRACT_TO_PROJECT.getSmsTempId(), secondPart.getName());
                }
                if (notice.isWx()) {
                    //                    weChatMsgApi.send(contract.getProjectId(), CONTRACT_NOTICE, new ArrayList<>(userIds), secondPart.getName() + CONTRACT_CONTENT_TO_PROJECT, true);
                }
            });

        } catch (Exception e) {
            log.error("完成合同签订通知市场方失败，任务ID：" + contract.getId(), e);
        }
    }

    private void sendUrgeNotice(WholesalerContract contract, String projectId, Set<Long> userIds, String sceneCodes) {
        try {

        } catch (Exception e) {
            log.error("催送签约消息发送失败，任务ID：" + contract.getId(), e);
        }
    }

}
