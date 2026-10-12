package com.guhecloud.cube.srv.gen.tag.tag.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
import com.guhecloud.cube.srv.gen.tag.tag.entity.TagBusiness;
import com.guhecloud.cube.srv.gen.tag.tag.enums.TagRefTypeEnum;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TagBusinessService extends IService<TagBusiness> {
    boolean addTag(TagRequest tag);

    boolean removeTag(Long id);

    boolean updateTag(Long id, TagRequest tag);

    TagDetailResponse detailTag(Long id);

    List<TagTreeResponse> tagTree(TagTreeRequest req);

    boolean addTagRef(TagRefRequest tagRef);

    boolean removeTagRef(TagRefRemoveRequest tagRefRemoveRequest);

    Set<Long> tagRefIdsList(Long tagId, TagRefTypeEnum refType);

    List<AreaDTO> tagRefArea(TagRefPageRequest tagRefPageRequest);

    Pagination<StallDTO> tagRefStall(StallTagRefPageRequest tagRefPageRequest);

    Pagination<DevicePageInfoVO> tagRefDevice(DeviceTagRefPageRequest tagRefPageRequest);

    Pagination<ScenePointVO> tagRefScenePoint(ScenePointTagRefPageRequest tagRefPageRequest);

    Pagination<PropertyDetailedTagPageVO> tagRefPropertyDetailed(PropertyTagRefPageRequest tagRefPageRequest);

    Pagination<RiskSourceLedgerVO> tagRefRiskSource(TagRefPageRequest tagRefPageRequest);

    Pagination<ProjectScheduleVO> tagRefProjectSchedule(LooseWorkRefPageRequest tagRefPageRequest);

    Pagination<PatrolLocationVO> tagRefPatrolPoint(PatrolPointTagRefPageRequest tagRefPageRequest);

    /**
     * 获取标签关联的区域ID列表
     */
    List<AreaIdTypeDTO> tagRefAreaIds(TagRefListRequest tagRefListRequest);

    /**
     * 获取标签关联的档位ID列表
     */
    List<StallIdTypeDTO> tagRefStallIdTypes(TagRefListRequest tagRefListRequest);

    /**
     * 获取标签关联的设备ID列表
     */
    List<DeviceIdTypeDTO> tagRefDeviceIdTypes(TagRefListRequest tagRefListRequest);

    /**
     * 获取标签关联的现场点ID列表
     */
    List<ScenePointIdTypeDTO> tagRefScenePointIds(TagRefListRequest tagRefListRequest);

    /**
     * 获取标签关联的资产ID列表
     */
    List<PropertyDetailIdTypeDTO> tagRefPropertyDetailedIds(TagRefListRequest tagRefListRequest);

    /**
     * 获取标签关联的风险源ID列表
     */
    List<RiskSourceIdTypeVO> tagRefRiskSourceIds(TagRefListRequest tagRefListRequest);

    /**
     * 获取标签关联的小散工程ID列表
     */
    List<ProjectScheduleIdTypeDTO> tagRefProjectScheduleIds(TagRefListRequest tagRefListRequest);

    /**
     * 获取标签关联的巡更点ID列表
     */
    List<PatrolLocationIdTypeDTO> tagRefPatrolPointIds(TagRefListRequest tagRefListRequest);

    /**
     * 获取标签关联的用户列表
     *
     * @param tagRefListRequest
     *
     * @return
     */
    Map<Long, List<Long>> tagRefUser(TagRefListRequest tagRefListRequest);

    List<BaseTargetInfoResponse> tagRefData(TagRefListRequest tagRefListRequest);

    List<TagBusinessResponse> tagRefList(TagRefListRequest tagRefListRequest);
}
