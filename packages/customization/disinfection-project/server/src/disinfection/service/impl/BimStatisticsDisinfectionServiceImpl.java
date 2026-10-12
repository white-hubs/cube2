package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionProjectMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionDataType;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionProjectStatusEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionFileHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.DisinfectionUserPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.BimBaseQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.KeyPlacesQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BimStatisticsDisinfectionService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionDataTypeService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.BimProjectStatisticsVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.BimUserStatisticsVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.DisinfectionUserInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectStatisticsBaseVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectStatisticsCycleVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/22 16:53
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BimStatisticsDisinfectionServiceImpl implements BimStatisticsDisinfectionService {

    private final DisinfectionProjectMapper disinfectionProjectMapper;
    private final DisinfectionDataTypeService dataTypeService;
    private final UserClient userClient;
    private final DisinfectionFileHelper fileHelper;

    @Override
    public BimProjectStatisticsVO disinfectionProjectStatistics(BimBaseQueryParam param) {

        List<ProjectStatisticsBaseVO> projectStatisticsBaseVOS = disinfectionProjectMapper.selectProjectNumbGroupByParam("status", param);
        if (CollectionUtils.isEmpty(projectStatisticsBaseVOS)) {
            return BimProjectStatisticsVO.builder()
                    .total(0)
                    .inProgress(0)
                    .toStart(0)
                    .completed(0)
                    .build();
        }
        Map<String, Integer> statusMap = projectStatisticsBaseVOS.stream()
                .collect(Collectors.toMap(ProjectStatisticsBaseVO::getKeyName, ProjectStatisticsBaseVO::getNumber, (k1, k2) -> k1));
        return BimProjectStatisticsVO.builder()
                .total(projectStatisticsBaseVOS.stream().map(ProjectStatisticsBaseVO::getNumber).reduce(Integer::sum).orElse(0))
                .inProgress(statusMap.getOrDefault(DisinfectionProjectStatusEnum.PROCESSING.name(), 0))
                .toStart(statusMap.getOrDefault(DisinfectionProjectStatusEnum.WAITING.name(), 0))
                .completed(statusMap.getOrDefault(DisinfectionProjectStatusEnum.COMPLETED.name(), 0))
                .build();
    }

    @Override
    public List<ProjectStatisticsBaseVO> disinfectionProjectDistribution() {
        return disinfectionProjectMapper.selectProjectNumbGroupByParam("district", new BimBaseQueryParam());
    }

    @Override
    public ProjectStatisticsCycleVO disinfectionProjectCycle() {
        List<ProjectStatisticsBaseVO> projectStatisticsBaseVOS = disinfectionProjectMapper.selectProjectNumbGroupByParam("TIMESTAMPDIFF( MONTH, begin_time, end_time )", new BimBaseQueryParam());
        if (CollectionUtils.isEmpty(projectStatisticsBaseVOS)) {
            return ProjectStatisticsCycleVO.builder()
                    .oneMonth(BigDecimal.ZERO)
                    .oneToThreeMonth(BigDecimal.ZERO)
                    .threeToSixMonth(BigDecimal.ZERO)
                    .sixMonth(BigDecimal.ZERO)
                    .build();
        }
        BigDecimal total = projectStatisticsBaseVOS.stream().map(t -> BigDecimal.valueOf(t.getNumber()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal oneMonth = projectStatisticsBaseVOS.stream().filter(p -> Integer.parseInt(p.getKeyName()) < 1)
                .map(t -> BigDecimal.valueOf(t.getNumber()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal oneToThreeMonth = projectStatisticsBaseVOS.stream()
                .filter(p -> Integer.parseInt(p.getKeyName()) >= 1 && Integer.parseInt(p.getKeyName()) <= 3)
                .map(t -> BigDecimal.valueOf(t.getNumber()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal threeToSixMonth = projectStatisticsBaseVOS.stream()
                .filter(p -> Integer.parseInt(p.getKeyName()) > 3 && Integer.parseInt(p.getKeyName()) <= 6)
                .map(t -> BigDecimal.valueOf(t.getNumber()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sixMonth = projectStatisticsBaseVOS.stream().filter(p -> Integer.parseInt(p.getKeyName()) > 6)
                .map(t -> BigDecimal.valueOf(t.getNumber()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ProjectStatisticsCycleVO.builder()
                .total(total)
                .oneMonth(percentage(oneMonth, total))
                .oneToThreeMonth(percentage(oneToThreeMonth, total))
                .threeToSixMonth(percentage(threeToSixMonth, total))
                .sixMonth(percentage(sixMonth, total))
                .build();
    }

    @Override
    public List<ProjectStatisticsBaseVO> disinfectionProjectFocusDistribution(KeyPlacesQueryParam param) {
        List<ProjectStatisticsBaseVO> projectStatisticsBaseVOS = disinfectionProjectMapper.selectProjectNumberGroupByType(param);
        if (CollectionUtils.isEmpty(projectStatisticsBaseVOS)) {
            return new ArrayList<>();
        }
        List<Long> keyIds = projectStatisticsBaseVOS.stream().map(ProjectStatisticsBaseVO::getKeyId)
                .distinct().collect(Collectors.toList());
        List<DisinfectionDataType> disinfectionDataTypes = dataTypeService.lambdaQuery()
                .in(DisinfectionDataType::getId, keyIds).list();
        Map<Long, FileVO> fileVOMap = dataTypeService.getFileVOMap(disinfectionDataTypes);
        projectStatisticsBaseVOS.forEach(t -> {
            t.setIcon(fileVOMap.get(t.getKeyId()));
        });
        return projectStatisticsBaseVOS;
    }

    @Override
    public BimUserStatisticsVO disinfectionPersonnelDistribution(DisinfectionUserPageParam param) {
        List<DisinfectionUserInfoVO> disinfectionUserList = disinfectionProjectMapper.selectDisinfectionUserStatistics(param);
        if (CollectionUtils.isEmpty(disinfectionUserList)) {
            return BimUserStatisticsVO.builder()
                    .total(0)
                    .disinfectionUserList(new ArrayList<>())
                    .build();
        }
        buildResult(disinfectionUserList);
        return BimUserStatisticsVO.builder()
                .total(disinfectionUserList.size())
                .disinfectionUserList(disinfectionUserList)
                .build();
    }

    private void buildResult(List<DisinfectionUserInfoVO> disinfectionUserList) {
        if (CollectionUtils.isEmpty(disinfectionUserList)) {
            return;
        }
        Set<Long> userIds = new HashSet<>();
        for (DisinfectionUserInfoVO disinfectionUserInfoVO : disinfectionUserList) {
            userIds.add(disinfectionUserInfoVO.getUserId());
        }
        UserQuery userQuery = new UserQuery();
        userQuery.setUserIds(userIds);
        List<UserDTO> userDTOS = userClient.postList(userQuery);
        Map<Long, UserDTO> userMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        List<Long> businessKeys = new ArrayList<>();
        for (DisinfectionUserInfoVO disinfectionUserInfoVO : disinfectionUserList) {
            UserDTO userDTO = userMap.getOrDefault(disinfectionUserInfoVO.getUserId(), new UserDTO());
            disinfectionUserInfoVO.setName(userDTO.getName());
            disinfectionUserInfoVO.setPhone(userDTO.getMobile());
            disinfectionUserInfoVO.setDepartment(Objects.nonNull(userDTO.getOrganization()) ? userDTO.getOrganization().getName() : "");
            disinfectionUserInfoVO.setPosition(Optional.ofNullable(userDTO.getAppointments()).orElse(new ArrayList<>()).stream()
                                                       .map(UserDTO.Appointment::getName).collect(Collectors.joining(",")));
            disinfectionUserInfoVO.setRole(Optional.ofNullable(userDTO.getRoles()).orElse(new ArrayList<>()).stream()
                                                   .map(UserDTO.Role::getName).collect(Collectors.joining(",")));
            disinfectionUserInfoVO.setPhoto(userDTO.getPhotographList());
            if (Objects.nonNull(userDTO.getPhotographKey())) {
                businessKeys.add(userDTO.getPhotographKey());
            }
        }
        if (CollectionUtils.isNotEmpty(businessKeys)) {
            Map<Long, List<FileVO>> fileMap = fileHelper.getByBusinessKeys(businessKeys);
            for (DisinfectionUserInfoVO disinfectionUserInfoVO : disinfectionUserList) {
                UserDTO userDTO = userMap.getOrDefault(disinfectionUserInfoVO.getUserId(), new UserDTO());
                disinfectionUserInfoVO.setPhoto(fileMap.get(userDTO.getPhotographKey()));
            }
        }
    }

    @Override
    public List<DisinfectionUserInfoVO> disinfectionPersonnelDistributionList(DisinfectionUserPageParam param) {
        Set<Long> userIds = new HashSet<>();
        if (Objects.nonNull(param.getOrganizationId()) || Objects.nonNull(param.getPositionId())) {
            List<Long> organizationUserIds = userClient.listEnableUserByOrgAndPost(Collections.singletonList(param.getOrganizationId()), Collections.singletonList(param.getPositionId()));
            if (CollectionUtils.isEmpty(organizationUserIds)) {
                return new ArrayList<>();
            }
            userIds.addAll(organizationUserIds);
        }
        if (StringUtils.isNotBlank(param.getKeyword())) {
            List<UserDTO> userDTOS = userClient.listUsers(param.getKeyword());
            if (CollectionUtils.isEmpty(userDTOS)) {
                return new ArrayList<>();
            }
            userIds.addAll(userDTOS.stream().map(UserDTO::getId).collect(Collectors.toList()));

        }
        param.setUserIds(userIds);
        List<DisinfectionUserInfoVO> disinfectionUserInfoVOList = disinfectionProjectMapper.selectDisinfectionUserStatistics(param);
        buildResult(disinfectionUserInfoVOList);
        return disinfectionUserInfoVOList;
    }

    @Override
    public List<ProjectStatisticsBaseVO> disinfectionPersonnelDepartmentDistribution() {
        List<Long> userIdList = disinfectionProjectMapper.selectDisinfectionUserIdList();
        if (CollectionUtils.isEmpty(userIdList)) {
            return new ArrayList<>();
        }
        UserQuery userQuery = new UserQuery();
        userQuery.setUserIds(new HashSet<>(userIdList));
        List<UserDTO> userDTOS = userClient.postList(userQuery);
        Map<UserDTO.Organization, Long> organizationLongMap = userDTOS.stream()
                .filter(user -> Objects.nonNull(user.getOrganization()))
                .collect(Collectors.groupingBy(UserDTO::getOrganization, Collectors.counting()));
        List<ProjectStatisticsBaseVO> projectStatisticsBaseVOS = new ArrayList<>();
        organizationLongMap.forEach((t, u) -> {
            projectStatisticsBaseVOS.add(ProjectStatisticsBaseVO.builder()
                                                 .keyId(t.getId())
                                                 .keyName(t.getName())
                                                 .number(u.intValue())
                                                 .build());
        });
        return projectStatisticsBaseVOS.stream().sorted(Comparator.comparing(ProjectStatisticsBaseVO::getNumber).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectStatisticsBaseVO> disinfectionPersonnelPostDistribution() {
        List<Long> userIdList = disinfectionProjectMapper.selectDisinfectionUserIdList();
        if (CollectionUtils.isEmpty(userIdList)) {
            return new ArrayList<>();
        }
        UserQuery userQuery = new UserQuery();
        userQuery.setUserIds(new HashSet<>(userIdList));
        List<UserDTO> userDTOS = userClient.postList(userQuery);
        Map<UserDTO.Appointment, Long> positionLongMap = new HashMap<>();
        for (UserDTO userDTO : userDTOS) {
            for (UserDTO.Appointment appointment : userDTO.getAppointments()) {
                if (positionLongMap.containsKey(appointment)) {
                    positionLongMap.put(appointment, positionLongMap.get(appointment) + 1);
                } else {
                    positionLongMap.put(appointment, 1L);
                }
            }
        }
        List<ProjectStatisticsBaseVO> projectStatisticsBaseVOS = new ArrayList<>();
        positionLongMap.forEach((t, u) -> {
            projectStatisticsBaseVOS.add(ProjectStatisticsBaseVO.builder()
                                                 .keyId(t.getId())
                                                 .keyName(t.getName())
                                                 .number(u.intValue())
                                                 .percentage(percentage(new BigDecimal(u), new BigDecimal(userDTOS.size())))
                                                 .build());
        });
        return projectStatisticsBaseVOS;
    }

    @Override
    public List<ProjectStatisticsBaseVO> disinfectionPersonnelTaskComplete(Period period) {
        List<ProjectStatisticsBaseVO> projectStatisticsBaseVOS = disinfectionProjectMapper.selectDisinfectionTaskNumberGroupByUser(period);
        if (CollectionUtils.isEmpty(projectStatisticsBaseVOS)) {
            return new ArrayList<>();
        }
        Optional<ProjectStatisticsBaseVO> projectStatisticsBaseVO = projectStatisticsBaseVOS.stream()
                .filter(t -> Objects.nonNull(t.getKeyId()))
                .findFirst();
        if (!projectStatisticsBaseVO.isPresent()) {
            return new ArrayList<>();
        }
        List<Long> userIds = projectStatisticsBaseVOS.stream().filter(t -> Objects.nonNull(t.getKeyId()))
                .map(ProjectStatisticsBaseVO::getKeyId).distinct().collect(Collectors.toList());
        Map<Long, String> userMap = Optional.ofNullable(userClient.listUserByIds(userIds)).orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));

        return projectStatisticsBaseVOS.stream().map(t -> {
            t.setKeyName(userMap.get(t.getKeyId()));
            return t;
        }).sorted(Comparator.comparing(ProjectStatisticsBaseVO::getNumber).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<ProjectStatisticsBaseVO> disinfectionFocusDistribution() {
        return disinfectionProjectMapper.selectProjectNumberGroupByAddress();
    }

    private BigDecimal percentage(BigDecimal current, BigDecimal total) {
        // 本期为0 百分比为 0
        if (Objects.isNull(current) || Objects.isNull(total) || BigDecimal.ZERO.compareTo(current) == 0 ||
            java.math.BigDecimal.ZERO.compareTo(total) == 0) {
            return new BigDecimal("0");
        }
        // 计算百分比
        return current.divide(total, 2, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100));
    }

}
