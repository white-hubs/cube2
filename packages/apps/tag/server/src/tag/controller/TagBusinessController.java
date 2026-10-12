package com.guhecloud.cube.srv.gen.tag.tag.controller;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceIdTypeDTO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.dto.AreaIdTypeDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallIdTypeDTO;
import com.guhecloud.cube.property.management.client.property.vo.PropertyDetailIdTypeDTO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedTagPageVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceIdTypeVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectScheduleIdTypeDTO;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectScheduleVO;
import com.guhecloud.cube.srv.gen.maintain.dto.PatrolLocationIdTypeDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.ScenePointIdTypeDTO;
import com.guhecloud.cube.srv.gen.maintain.vo.PatrolLocationVO;
import com.guhecloud.cube.srv.gen.maintain.vo.ScenePointVO;
import com.guhecloud.cube.srv.gen.tag.tag.dto.request.DeviceTagRefPageRequest;
import com.guhecloud.cube.srv.gen.tag.tag.dto.request.LooseWorkRefPageRequest;
import com.guhecloud.cube.srv.gen.tag.tag.dto.request.PatrolPointTagRefPageRequest;
import com.guhecloud.cube.srv.gen.tag.tag.dto.request.PropertyTagRefPageRequest;
import com.guhecloud.cube.srv.gen.tag.tag.dto.request.ScenePointTagRefPageRequest;
import com.guhecloud.cube.srv.gen.tag.tag.dto.request.StallTagRefPageRequest;
import com.guhecloud.cube.srv.gen.tag.tag.dto.request.TagRefListRequest;
import com.guhecloud.cube.srv.gen.tag.tag.dto.request.TagRefPageRequest;
import com.guhecloud.cube.srv.gen.tag.tag.dto.request.TagRefRemoveRequest;
import com.guhecloud.cube.srv.gen.tag.tag.dto.request.TagRefRequest;
import com.guhecloud.cube.srv.gen.tag.tag.dto.request.TagRequest;
import com.guhecloud.cube.srv.gen.tag.tag.dto.request.TagTreeRequest;
import com.guhecloud.cube.srv.gen.tag.tag.dto.response.BaseTargetInfoResponse;
import com.guhecloud.cube.srv.gen.tag.tag.dto.response.TagBusinessResponse;
import com.guhecloud.cube.srv.gen.tag.tag.dto.response.TagDetailResponse;
import com.guhecloud.cube.srv.gen.tag.tag.dto.response.TagTreeResponse;
import com.guhecloud.cube.srv.gen.tag.tag.enums.TagRefTypeEnum;
import com.guhecloud.cube.srv.gen.tag.tag.service.TagBusinessService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 标签业务相关接口
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class TagBusinessController {
    private final TagBusinessService tagBusinessService;

    /**
     * 新增标签
     */
    @PostMapping("/business-tag")
    public boolean addTag(@RequestBody @Valid TagRequest tag) {
        return tagBusinessService.addTag(tag);
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/business-tag/{id}")
    public boolean deleteTag(@PathVariable Long id) {
        return tagBusinessService.removeTag(id);
    }

    /**
     * 修改标签
     */
    @PutMapping("/business-tag/{id}")
    public boolean updateTag(@PathVariable Long id, @RequestBody TagRequest tag) {
        return tagBusinessService.updateTag(id, tag);
    }

    /**
     * 标签树查询
     */
    @GetMapping("/business-tag/tree")
    //    @DataPermissionSwitch(dataModel = "tag:dept")
    public List<TagTreeResponse> tagTree(TagTreeRequest req) {
        return tagBusinessService.tagTree(req);
    }

    /**
     * 获取标签详情
     */
    @GetMapping("/business-tag/{id}")
    public TagDetailResponse detailTag(@PathVariable Long id) {
        return tagBusinessService.detailTag(id);
    }

    /**
     * 添加关联
     */
    @PostMapping("/business-tag/ref")
    public boolean addTagRef(@RequestBody @Valid TagRefRequest tagRef) {
        return tagBusinessService.addTagRef(tagRef);
    }

    /**
     * 删除关联
     */
    @PostMapping("/business-tag/ref/remove")
    public boolean deleteTagRef(@RequestBody @Valid TagRefRemoveRequest tagRefRemoveRequest) {
        return tagBusinessService.removeTagRef(tagRefRemoveRequest);
    }

    /**
     * 根据类型，查询标签关联的对象Id列表
     */
    @GetMapping("/business-tag/{tagId}/ref-ids")
    public Set<Long> tagRefIdsList(@PathVariable Long tagId, TagRefTypeEnum refType) {
        return tagBusinessService.tagRefIdsList(tagId, refType);
    }

    /**
     * 获取标签关联的档位分页信息
     */
    @PostMapping("/business-tag/ref/stall")
    public Pagination<StallDTO> tagRefStall(@RequestBody StallTagRefPageRequest tagRefPageRequest) {
        return tagBusinessService.tagRefStall(tagRefPageRequest);
    }

    /**
     * 查询标签区域列表树形信息
     */
    @PostMapping("/business-tag/ref/area")
    public List<AreaDTO> tagRefArea(@RequestBody TagRefPageRequest tagRefPageRequest) {
        return tagBusinessService.tagRefArea(tagRefPageRequest);
    }

    /**
     * 获取标签关联的设备分页信息
     */
    @PostMapping("/business-tag/ref/device")
    public Pagination<DevicePageInfoVO> tagRefDevice(@RequestBody DeviceTagRefPageRequest tagRefPageRequest) {
        return tagBusinessService.tagRefDevice(tagRefPageRequest);
    }

    /**
     * 获取标签关联的现场点分页信息
     */
    @PostMapping("/business-tag/ref/scene-point")
    public Pagination<ScenePointVO> tagRefScenePoint(@RequestBody ScenePointTagRefPageRequest tagRefPageRequest) {
        return tagBusinessService.tagRefScenePoint(tagRefPageRequest);
    }

    /**
     * 获取标签关联的资产分页信息
     */
    @PostMapping("/business-tag/ref/property-detailed")
    public Pagination<PropertyDetailedTagPageVO> tagRefPropertyDetailed(@RequestBody PropertyTagRefPageRequest tagRefPageRequest) {
        return tagBusinessService.tagRefPropertyDetailed(tagRefPageRequest);
    }

    /**
     * 获取标签关联的风险源分页信息
     */
    @PostMapping("/business-tag/ref/risk-source")
    public Pagination<RiskSourceLedgerVO> tagRefRiskSource(@RequestBody TagRefPageRequest tagRefPageRequest) {
        return tagBusinessService.tagRefRiskSource(tagRefPageRequest);
    }

    /**
     * 获取标签关联的小散工程分页信息
     */
    @PostMapping("/business-tag/ref/project-schedule")
    public Pagination<ProjectScheduleVO> tagRefProjectSchedule(@RequestBody LooseWorkRefPageRequest tagRefPageRequest) {
        return tagBusinessService.tagRefProjectSchedule(tagRefPageRequest);
    }

    /**
     * 获取标签关联的巡更点分页信息
     */
    @PostMapping("/business-tag/ref/patrol-point")
    public Pagination<PatrolLocationVO> tagRefPatrolPoint(@RequestBody PatrolPointTagRefPageRequest tagRefPageRequest) {
        return tagBusinessService.tagRefPatrolPoint(tagRefPageRequest);
    }

    /**
     * 获取标签关联的档位ID,TYPE列表
     */
    @PostMapping("/business-tag/ref/stall/idTypes")
    public List<StallIdTypeDTO> tagRefStallIdTypes(@RequestBody TagRefListRequest tagRefListRequest) {
        return tagBusinessService.tagRefStallIdTypes(tagRefListRequest);
    }

    /**
     * 获取标签关联的区域ID列表
     */
    @PostMapping("/business-tag/ref/area/idTypes")
    public List<AreaIdTypeDTO> tagRefAreaIds(@RequestBody TagRefListRequest tagRefListRequest) {
        return tagBusinessService.tagRefAreaIds(tagRefListRequest);
    }

    /**
     * 获取标签关联的设备ID列表
     */
    @PostMapping("/business-tag/ref/device/idTypes")
    public List<DeviceIdTypeDTO> tagRefDeviceIdTypes(@RequestBody TagRefListRequest tagRefListRequest) {
        return tagBusinessService.tagRefDeviceIdTypes(tagRefListRequest);
    }

    /**
     * 获取标签关联的现场点ID列表
     */
    @PostMapping("/business-tag/ref/scene-point/idTypes")
    public List<ScenePointIdTypeDTO> tagRefScenePointIds(@RequestBody TagRefListRequest tagRefListRequest) {
        return tagBusinessService.tagRefScenePointIds(tagRefListRequest);
    }

    /**
     * 获取标签关联的资产ID列表
     */
    @PostMapping("/business-tag/ref/property-detailed/idTypes")
    public List<PropertyDetailIdTypeDTO> tagRefPropertyDetailedIds(@RequestBody TagRefListRequest tagRefListRequest) {
        return tagBusinessService.tagRefPropertyDetailedIds(tagRefListRequest);
    }

    /**
     * 获取标签关联的风险源ID列表
     */
    @PostMapping("/business-tag/ref/risk-source/idTypes")
    public List<RiskSourceIdTypeVO> tagRefRiskSourceIds(@RequestBody TagRefListRequest tagRefListRequest) {
        return tagBusinessService.tagRefRiskSourceIds(tagRefListRequest);
    }

    /**
     * 获取标签关联的小散工程ID列表
     */
    @PostMapping("/business-tag/ref/project-schedule/idTypes")
    public List<ProjectScheduleIdTypeDTO> tagRefProjectScheduleIds(@RequestBody TagRefListRequest tagRefListRequest) {
        return tagBusinessService.tagRefProjectScheduleIds(tagRefListRequest);
    }

    /**
     * 获取标签关联的巡更点ID列表
     */
    @PostMapping("/business-tag/ref/patrol-point/idTypes")
    public List<PatrolLocationIdTypeDTO> tagRefPatrolPointIds(@RequestBody TagRefListRequest tagRefListRequest) {
        return tagBusinessService.tagRefPatrolPointIds(tagRefListRequest);
    }

    /**
     * 获取标签关联负责人
     */
    @PostMapping("/business-tag/ref/user")
    public Map<Long, List<Long>> tagRefUser(@RequestBody TagRefListRequest tagRefListRequest) {
        return tagBusinessService.tagRefUser(tagRefListRequest);
    }

    /**
     * 获取标签信息
     */
    @PostMapping("/business-tag/ref/list")
    public List<TagBusinessResponse> tagRefList(@RequestBody TagRefListRequest tagRefListRequest) {
        return tagBusinessService.tagRefList(tagRefListRequest);
    }

    /**
     * 获取对象数据
     */
    @PostMapping("/business-tag/ref/data")
    public List<BaseTargetInfoResponse> tagRefData(@RequestBody TagRefListRequest tagRefListRequest) {
        return tagBusinessService.tagRefData(tagRefListRequest);
    }
}
