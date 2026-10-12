package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.biz.ouaa.client.AppointmentClient;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.AppointmentDetailDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceStatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceProgetRecordMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceTasksMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenanceProgetRecordVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenanceTasksVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceProgetRecord;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceTasks;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenanceTasksService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenanceTasksQueryParam;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：维保任务服务层
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenanceTasksServiceImpl extends ServiceImpl<MaintenanceTasksMapper, MaintenanceTasks> implements MaintenanceTasksService {

    private final MaintenanceTasksMapper maintenanceTasksMapper;

    private final FileClient fileClient;

    private final MaintenanceProgetRecordMapper maintenanceProgetRecordMapper;

    private final UserClient userClient;

    private final OrganizationClient organizationClient;

    private final AppointmentClient appointmentClient;

    @Override
    public Page<MaintenanceTasksVo> selectTasks(MaintenanceTasksQueryParam param) {
        if (StringUtils.isNotEmpty(param.getPeriod())) {
            String[] split = param.getPeriod().split(",");
            param.setReleaseStartTime(split[0]);
            String s = split[1];
            var dateTime = DateUtils.parseDate(s);
            var dateTime1 = DateUtils.offsetDay(dateTime, 1);
            String format = DateUtils.format(dateTime1, "yyyy-MM-dd");
            param.setReleaseEndTime(format);
        }
        Page<MaintenanceTasksVo> maintenanceTasksVoPage = maintenanceTasksMapper.selectTasks(param, PagingUtils.convert(param));
        maintenanceTasksVoPage.getRecords().forEach(maintenanceTasksVo -> {
            if (("per").equals(maintenanceTasksVo.getResponsibilityType())) {
                //人员
                if (StringUtils.isNotEmpty(maintenanceTasksVo.getResponsibilityPersonId())) {
                    String[] split = maintenanceTasksVo.getResponsibilityPersonId().split("-");
                    ArrayList<Long> objects1 = new ArrayList<>();
                    for (String s1 : split) {
                        objects1.add(Long.valueOf(s1));
                    }
                    List<UserDTO> userDTOS = userClient.listUserByIds(objects1);
                    if (userDTOS.size() > 0) {
                        List<String> collect = userDTOS.stream().map(UserDTO::getName).collect(Collectors.toList());
                        String join = String.join("、", collect);
                        maintenanceTasksVo.setResponsibilityPersonName(join);
                    }
                }
            } else {
                //部门
                if (StringUtils.isNotEmpty(maintenanceTasksVo.getReceiveDeptId())) {
                    String receiveDeptId = maintenanceTasksVo.getReceiveDeptId();
                    List<String> strings = Arrays.asList(receiveDeptId.split("-"));
                    List<OrganizationDTO> organizationDTOS = organizationClient.listOrganizationByDepts(strings);
                    if (organizationDTOS.size() > 0) {
                        List<String> collect = organizationDTOS.stream().map(OrganizationDTO::getName).collect(Collectors.toList());
                        String join = String.join("、", collect);
                        maintenanceTasksVo.setResponsibilityOrganizationName(join);
                        maintenanceTasksVo.setResponsibilityPersonName(join);
                    }
                }

                //岗位
                if (StringUtils.isNotEmpty(maintenanceTasksVo.getReceivePostId())) {
                    String[] split = maintenanceTasksVo.getReceivePostId().split("-");
                    ArrayList<String> objects1 = new ArrayList<>(Arrays.asList(split));
                    List<AppointmentDetailDTO> appointments = appointmentClient.getAppointmentByIds(String.join(",", objects1));
                    if (CollectionUtils.isNotEmpty(appointments)) {
                        List<String> collect = appointments.stream().map(AppointmentDetailDTO::getName).collect(Collectors.toList());
                        String join = String.join("、", collect);
                        maintenanceTasksVo.setResponsibilityPositionName(join);
                        maintenanceTasksVo.setResponsibilityPersonName(join);
                    }
                }
            }
        });
        return maintenanceTasksVoPage;
    }

    @Override
    public Page<MaintenanceTasksVo> selectAppTasks(MaintenanceTasksQueryParam param) {
        Long id = SecurityContextHolder.getPrincipal().getId();
        UserDTO user = userClient.getUser(id);
        UserDTO.Organization organization1 = user.getOrganization();
        ArrayList<String> orList = new ArrayList<>();
        orList.add(String.valueOf(organization1.getId()));
        String organization = String.join("-", orList);
        List<UserDTO.Position> positions1 = user.getPositions();
        ArrayList<String> poList = new ArrayList<>();
        positions1.forEach(s -> {
            poList.add(String.valueOf(s.getId()));
        });
        String position = String.join("-", poList);
        param.setPosition(position);
        param.setOrganization(organization);
        param.setUserId(String.valueOf(id));
        Page<MaintenanceTasksVo> maintenanceTasksVoPage = maintenanceTasksMapper.selectAppTasks(param, PagingUtils.convert(param));
        maintenanceTasksVoPage.getRecords().forEach(maintenanceTasksVo -> {
            if (maintenanceTasksVo.getResponsibilityType().equals("per")) {
                //人员
                if (StringUtils.isNotEmpty(maintenanceTasksVo.getResponsibilityPersonId())) {
                    String[] split = maintenanceTasksVo.getResponsibilityPersonId().split("-");
                    ArrayList<Long> objects1 = new ArrayList<>();
                    for (String s1 : split) {
                        objects1.add(Long.valueOf(s1));
                    }
                    List<UserDTO> userDTOS = userClient.listUserByIds(objects1);
                    if (userDTOS.size() > 0) {
                        List<String> collect = userDTOS.stream().map(UserDTO::getName).collect(Collectors.toList());
                        String join = String.join("、", collect);
                        maintenanceTasksVo.setResponsibilityPersonName(join);
                    }
                }
            } else {
                //部门
                if (StringUtils.isNotEmpty(maintenanceTasksVo.getReceiveDeptId())) {
                    String receiveDeptId = maintenanceTasksVo.getReceiveDeptId();
                    List<String> strings = Arrays.asList(receiveDeptId.split("-"));
                    List<OrganizationDTO> organizationDTOS = organizationClient.listOrganizationByDepts(strings);
                    if (organizationDTOS.size() > 0) {
                        List<String> collect = organizationDTOS.stream().map(OrganizationDTO::getName).collect(Collectors.toList());
                        String join = String.join("、", collect);
                        maintenanceTasksVo.setResponsibilityOrganizationName(join);
                    }
                }
                //岗位
                if (StringUtils.isNotEmpty(maintenanceTasksVo.getReceivePostId())) {
                    String[] split = maintenanceTasksVo.getReceivePostId().split("-");
                    ArrayList<String> objects1 = new ArrayList<>(Arrays.asList(split));
                    List<AppointmentDetailDTO> appointments = appointmentClient.getAppointmentByIds(String.join(",", objects1));
                    if (CollectionUtils.isNotEmpty(appointments)) {
                        List<String> collect = appointments.stream().map(AppointmentDetailDTO::getName).collect(Collectors.toList());
                        String join = String.join("、", collect);
                        maintenanceTasksVo.setResponsibilityPositionName(join);
                    }
                }
            }
        });
        return maintenanceTasksVoPage;
    }

    @Override
    public MaintenanceTasksVo selectTasksDetail(Long id) {
        MaintenanceTasksVo maintenanceTasksVo = maintenanceTasksMapper.selectTasksDetail(id);

        //图片转换
        if (StringUtils.isNotEmpty(maintenanceTasksVo.getPicture())) {
            List<String> strings = Arrays.asList(maintenanceTasksVo.getPicture().split("-"));
            List<Long> collect = strings.stream().map(Long::valueOf).collect(Collectors.toList());
            maintenanceTasksVo.setFileVOS(fileClient.getByIds(collect));
        }

        if (maintenanceTasksVo.getResponsibilityType().equals("per")) {
            //人员
            if (StringUtils.isNotEmpty(maintenanceTasksVo.getResponsibilityPersonId())) {
                maintenanceTasksVo.setReceiveIds(Arrays.asList(maintenanceTasksVo.getResponsibilityPersonId().split("-")));
                String[] split = maintenanceTasksVo.getResponsibilityPersonId().split("-");
                ArrayList<Long> objects1 = new ArrayList<>();
                for (String s1 : split) {
                    objects1.add(Long.valueOf(s1));
                }
                List<UserDTO> userDTOS = userClient.listUserByIds(objects1);
                if (userDTOS.size() > 0) {
                    List<String> collect = userDTOS.stream().map(UserDTO::getName).collect(Collectors.toList());
                    String join = String.join("、", collect);
                    maintenanceTasksVo.setResponsibilityPersonName(join);
                }
            }
        } else {
            //部门
            if (StringUtils.isNotEmpty(maintenanceTasksVo.getReceiveDeptId())) {
                maintenanceTasksVo.setReceiveDeptIds(Arrays.asList(maintenanceTasksVo.getReceiveDeptId().split("-")));
                String receiveDeptId = maintenanceTasksVo.getReceiveDeptId();
                List<String> strings = Arrays.asList(receiveDeptId.split("-"));
                List<OrganizationDTO> organizationDTOS = organizationClient.listOrganizationByDepts(strings);
                if (organizationDTOS.size() > 0) {
                    List<String> collect = organizationDTOS.stream().map(OrganizationDTO::getName).collect(Collectors.toList());
                    String join = String.join("、", collect);
                    maintenanceTasksVo.setResponsibilityOrganizationName(join);
                    maintenanceTasksVo.setResponsibilityPersonName(join);
                }
            }

            //岗位
            if (StringUtils.isNotEmpty(maintenanceTasksVo.getReceivePostId())) {
                maintenanceTasksVo.setReceivePostIds(Arrays.asList(maintenanceTasksVo.getReceivePostId().split("-")));
                String[] split = maintenanceTasksVo.getReceivePostId().split("-");
                ArrayList<String> objects1 = new ArrayList<>(Arrays.asList(split));
                List<AppointmentDetailDTO> appointments = appointmentClient.getAppointmentByIds(String.join(",", objects1));
                if (CollectionUtils.isNotEmpty(appointments)) {
                    List<String> collect = appointments.stream().map(AppointmentDetailDTO::getName).collect(Collectors.toList());
                    String join = String.join("、", collect);
                    maintenanceTasksVo.setResponsibilityPositionName(join);
                    maintenanceTasksVo.setResponsibilityPersonName(join);
                }
            }
        }

        List<MaintenanceProgetRecord> maintenanceProgetRecords = maintenanceProgetRecordMapper.selectList(Wrappers
                                                                                                                  .<MaintenanceProgetRecord>lambdaQuery()
                                                                                                                  .eq(MaintenanceProgetRecord::getTaskId, id));

        if (maintenanceProgetRecords.size() > 0) {
            List<MaintenanceProgetRecordVo> maintenanceProgetRecordVos = ObjectUtils.copying(maintenanceProgetRecords, MaintenanceProgetRecordVo.class);
            maintenanceTasksVo.setMaintenanceProgetRecordVos(maintenanceProgetRecordVos);
        }

        return maintenanceTasksVo;
    }

    @Override
    public List<DeviceOperationStatisticsDTO> deviceMaintenanceNumStatistics(DeviceStatisticParam param) {
        if (ObjectUtils.isNotEmpty(param.getDeviceIds())) {
            return maintenanceTasksMapper.deviceMaintenanceNumStatistics(param);
        }
        return new ArrayList<>();
    }
}