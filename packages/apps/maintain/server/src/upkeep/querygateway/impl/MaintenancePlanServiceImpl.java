package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.ouaa.client.AppointmentClient;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.AppointmentDetailDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenancePlanRuleCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceObjectMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenancePlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceRuleConditionMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceTasksMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.MaintenacePlanStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.InstanceListByGenreVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenanceObjectVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenancePlanVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceObject;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenancePlan;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenancePlanService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenancePlanQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenanceTasksQueryParam;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 描述：维保计划服务层
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenancePlanServiceImpl extends ServiceImpl<MaintenancePlanMapper, MaintenancePlan> implements MaintenancePlanService {

    private final MaintenancePlanMapper maintenancePlanMapper;

    private final MaintenanceObjectMapper maintenanceObjectMapper;

    private final MaintenanceTasksMapper maintenanceTasksMapper;

    private final OrganizationClient organizationClient;

    private final AppointmentClient appointmentClient;

    private final UserClient userClient;

    private final AreaClient areaClient;
    private final MaintenanceRuleConditionMapper ruleConditionMapper;
    private final DeviceCenterStdClient deviceCenterStdClient;

    @Override
    public Page<MaintenancePlanVo> selectPlan(MaintenancePlanQueryParam param) {
        Page<MaintenancePlanVo> maintenacePlanVoPage = maintenancePlanMapper.selectPlan(param, PagingUtils.convert(param));
        maintenacePlanVoPage.getRecords().forEach(this::personnelConversion);
        return maintenacePlanVoPage;
    }

    @Override
    public MaintenancePlanVo selectPlanDetail(Long id) {
        MaintenancePlan maintenancePlan = maintenancePlanMapper.selectById(id);
        Assert.isNotFound(maintenancePlan, "无改该维保计划");
        MaintenancePlanVo maintenancePlanVo1 = ObjectUtils.copying(maintenancePlan, MaintenancePlanVo.class);
        //转换
        MaintenancePlanVo maintenancePlanVo = personnelConversion(maintenancePlanVo1);
        List<MaintenanceObject> maintenanceObjects = maintenanceObjectMapper.selectList(Wrappers.<MaintenanceObject>lambdaQuery()
                                                                                                .eq(MaintenanceObject::getPlanId, id)
                                                                                       );
        if (CollectionUtils.isNotEmpty(maintenanceObjects)) {
            Set<Long> deviceIds = maintenanceObjects.stream().map(MaintenanceObject::getKeyId).collect(Collectors.toSet());
            Map<Long, DeviceBaseDetailsDTO> deviceBaseDetailsMap = deviceCenterStdClient.getDetailsListByDeviceId(deviceIds)
                    .stream().collect(Collectors.toMap(DeviceBaseDetailsDTO::getId, s -> s));
            //维保对象
            List<MaintenanceObjectVo> maintenaceObjectsList = maintenanceObjects.stream().map(maintenanceObjectVo -> {
                MaintenanceObjectVo objectVo = ObjectUtils.copying(maintenanceObjectVo, MaintenanceObjectVo.class);
                if (deviceBaseDetailsMap.containsKey(maintenanceObjectVo.getKeyId())) {
                    DeviceBaseDetailsDTO device = deviceBaseDetailsMap.get(maintenanceObjectVo.getKeyId());
                    objectVo.setObjectName(device.getDeviceName());
                }
                objectVo.setObjectProjects(maintenanceTasksMapper.getObjectProjects(maintenanceObjectVo.getId()));
                return objectVo;
            }).collect(Collectors.toList());
            maintenancePlanVo.setMaintenaceObjectsList(maintenaceObjectsList);
        }
        List<MaintenancePlanRuleCommand> planRuleCommandList = ruleConditionMapper.selectAllByPlanId(id, null);
        maintenancePlanVo.setRuleDetails(planRuleCommandList);
        return maintenancePlanVo;
    }

    @Override
    public Map<String, Object> getPlanAndTaskCount() {
        MaintenanceTasksQueryParam param = new MaintenanceTasksQueryParam();
        Long id = SecurityContextHolder.getPrincipal().getId();
        UserDTO user = userClient.getUser(id);
        UserDTO.Organization organization1 = user.getOrganization();
        if (Objects.nonNull(organization1)) {
            ArrayList<String> orlLst = new ArrayList<>();
            orlLst.add(String.valueOf(organization1.getId()));
            String organization = String.join("-", orlLst);
            param.setOrganization(organization);
        } else {
            param.setOrganization("");
        }

        List<UserDTO.Position> positions1 = user.getPositions();
        if (CollectionUtils.isNotEmpty(positions1)) {
            ArrayList<String> poList = new ArrayList<>();
            positions1.forEach(s -> {
                poList.add(String.valueOf(s.getId()));
            });
            String position = String.join("-", poList);
            param.setPosition(position);
        } else {
            param.setPosition("");
        }
        param.setUserId(String.valueOf(id));
        HashMap<String, Object> remap = new HashMap<>();
        long planCount = maintenancePlanMapper.selectCount(Wrappers.<MaintenancePlan>lambdaQuery()
                                                                   .eq(MaintenancePlan::getStatus, MaintenacePlanStatusEnum.ENABLED.name()));
        long taskCount = maintenanceTasksMapper.selectAppTasksCount(param);
        remap.put("planCount", planCount);
        remap.put("taskCount", taskCount);
        return remap;
    }

    @Override
    public List<InstanceListByGenreVo> instanceListByGenre() {
        return Collections.emptyList();
    }

    //人员岗位转换
    public MaintenancePlanVo personnelConversion(MaintenancePlanVo maintenancePlanVo) {
        if (maintenancePlanVo.getResponsibilityType().equals("per")) {
            //人员
            if (StringUtils.isNotEmpty(maintenancePlanVo.getResponsibilityPersonId())) {
                maintenancePlanVo.setReceiveIds(Arrays.asList(maintenancePlanVo.getResponsibilityPersonId().split("-")));
                String[] split = maintenancePlanVo.getResponsibilityPersonId().split("-");
                ArrayList<Long> objects1 = new ArrayList<>();
                for (String s1 : split) {
                    objects1.add(Long.valueOf(s1));
                }
                List<UserDTO> userDTOS = userClient.listUserByIds(objects1);
                if (userDTOS.size() > 0) {
                    List<String> collect = userDTOS.stream().map(UserDTO::getName).collect(Collectors.toList());
                    String join = String.join("、", collect);
                    maintenancePlanVo.setResponsibilityPersonName(join);
                }
            }
        } else {
            //部门
            if (StringUtils.isNotEmpty(maintenancePlanVo.getReceiveDeptId())) {
                String receiveDeptId = maintenancePlanVo.getReceiveDeptId();
                List<String> strings = Arrays.asList(receiveDeptId.split("-"));
                maintenancePlanVo.setReceiveDeptIds(strings);
                List<OrganizationDTO> organizationDTOS = organizationClient.listOrganizationByDepts(strings);
                if (organizationDTOS.size() > 0) {
                    List<String> collect = organizationDTOS.stream().map(OrganizationDTO::getName).collect(Collectors.toList());
                    String join = String.join("、", collect);
                    maintenancePlanVo.setResponsibilityOrganizationName(join);
                    maintenancePlanVo.setResponsibilityPersonName(join);
                }
            }

            //岗位
            if (StringUtils.isNotEmpty(maintenancePlanVo.getReceivePostId())) {
                maintenancePlanVo.setReceivePostIds(Arrays.asList(maintenancePlanVo.getReceivePostId().split("-")));
                List<AppointmentDetailDTO> appointments = appointmentClient.getAppointmentByIds(String.join(",", maintenancePlanVo.getReceivePostIds()));
                if (CollectionUtils.isNotEmpty(appointments)) {
                    List<String> collect = appointments.stream().map(AppointmentDetailDTO::getName).collect(Collectors.toList());
                    String join = String.join("、", collect);
                    maintenancePlanVo.setResponsibilityPositionName(join);
                    maintenancePlanVo.setResponsibilityPersonName(join);
                }
            }
        }
        return maintenancePlanVo;
    }

    @Override
    public String getPlanName(@PathVariable Long id) {
        if (id == null) {
            return "";
        }
        var po = this.getById(id);
        return po == null ? "" : po.getMaintenancePlanName();
    }

}