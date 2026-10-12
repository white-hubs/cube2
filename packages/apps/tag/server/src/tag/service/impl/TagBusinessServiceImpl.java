package com.guhecloud.cube.srv.gen.tag.tag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceIdTypeDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.dto.AreaIdTypeDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallIdTypeDTO;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.cube.biz.mdata.client.param.StallQueryParam;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.property.management.client.property.client.property.PropertyDetailedClient;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedPageParam;
import com.guhecloud.cube.property.management.client.property.vo.PropertyDetailIdTypeDTO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedTagPageVO;
import com.guhecloud.cube.property.management.client.risk.source.client.RiskSourceLedgerClient;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceIdTypeVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.srv.gen.loose.works.client.ProjectScheduleClient;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectScheduleIdTypeDTO;
import com.guhecloud.cube.srv.gen.loose.works.param.ProjectScheduleQuery;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectScheduleVO;
import com.guhecloud.cube.srv.gen.maintain.client.PatrolLocationClient;
import com.guhecloud.cube.srv.gen.maintain.client.ScenePointClient;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectScenePointQueryParam;
import com.guhecloud.cube.srv.gen.maintain.dto.PatrolLocationIdTypeDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.PatrolLocationPageParam;
import com.guhecloud.cube.srv.gen.maintain.dto.ScenePointIdTypeDTO;
import com.guhecloud.cube.srv.gen.maintain.vo.PatrolLocationVO;
import com.guhecloud.cube.srv.gen.maintain.vo.ScenePointVO;
import com.guhecloud.cube.srv.gen.tag.tag.dao.TagBusinessMapper;
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
import com.guhecloud.cube.srv.gen.tag.tag.entity.TagRef;
import com.guhecloud.cube.srv.gen.tag.tag.enums.TagRefTypeEnum;
import com.guhecloud.cube.srv.gen.tag.tag.service.TagBusinessService;
import com.guhecloud.cube.srv.gen.tag.tag.service.TagUserRefService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagBusinessServiceImpl extends ServiceImpl<TagBusinessMapper, TagBusiness> implements TagBusinessService {
    private final TagBusinessRefServiceImpl tagRefService;
    private final DeviceCenterStdClient deviceClient;
    private final AreaClient areaClient;
    private final PropertyDetailedClient propertyDetailedClient;
    private final StallClient stallClient;
    private final ScenePointClient scenePointClient;
    private final RiskSourceLedgerClient riskSourceLedgerClient;
    private final ProjectScheduleClient projectScheduleClient;

    private final PatrolLocationClient patrolLocationClient;
    private final TagUserRefService tagUserRefService;
    private final UserClient userClient;
    private final OrganizationClient organizationClient;
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            // 核心线程数
            4,
            //maximumPoolSize: 池中允许的最大的线程数
            20,
            //keepAliveTime: 当线程数大于核心线程数的时候，线程在最大多长时间没有接到新任务就会终止释放， 最终线程池维持在 corePoolSize 大小
            50,
            //时间单位
            TimeUnit.SECONDS,
            //workQueue: 阻塞队列，用来存储等待执行的任务，如果当前对线程的需求超过了 corePoolSize 大小，就会放在这里等待空闲线程执行。
            new LinkedBlockingQueue<>(101),
            //threadFactory: 创建线程的工厂，比如指定线程名等
            new ThreadFactoryBuilder().setNameFormat("tag-business-target-sync-pool-%d").build(),
            //拒绝策略，如果线程满了，线程池就会拒绝,被拒绝的任务的处理程序，该处理程序以静默方式丢弃
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Transactional
    @Override
    public boolean addTag(TagRequest tag) {
        //判断名称是否有重复
        var exist = lambdaQuery()
                .eq(TagBusiness::getName, tag.getName().trim())
                .eq(Objects.isNull(tag.getParentId()), TagBusiness::getParentId, 0)
                .eq(Objects.nonNull(tag.getParentId()), TagBusiness::getParentId, tag.getParentId())
                .exists();
        Assert.isConflict(exist, "同级下,标签名称已存在！");
        //保存标签信息
        var id = IdWorker.getId();
        var tagBusiness = tag.convert(id);
        tagUserRefService.saveOrUpdateUserRef(id, tag.getUserIdList());
        return save(tagBusiness);
    }

    @Transactional
    @Override
    public boolean removeTag(Long id) {
        //判断标签是否存在
        var exist = lambdaQuery().eq(TagBusiness::getId, id).exists();
        Assert.isNotFound(!exist, "标签不存在！");
        //判断是否有子标签，如果有则不允许删除
        boolean hasChild = lambdaQuery().eq(TagBusiness::getParentId, id).exists();
        Assert.isConflict(hasChild, "存在子标签，不允许删除！");
        //判断标签是否关联了资源，如果有则删除关联的资源
        boolean hasResource = tagRefService.lambdaQuery().eq(TagRef::getTagId, id).exists();
        if (hasResource) {
            tagRefService.remove(new LambdaQueryWrapper<TagRef>().eq(TagRef::getTagId, id));
        }
        //删除标签
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean updateTag(Long id, TagRequest tag) {
        //查询标签信息
        var tagBusiness = lambdaQuery().eq(TagBusiness::getId, id).last("limit 1").one();
        Assert.isNotFound(tagBusiness, "标签不存在！");
        //判断名称是否有重复
        var exist = lambdaQuery()
                .ne(TagBusiness::getId, id)
                .eq(TagBusiness::getName, tag.getName().trim())
                .eq(Objects.isNull(tag.getParentId()), TagBusiness::getParentId, 0)
                .eq(Objects.nonNull(tag.getParentId()), TagBusiness::getParentId, tag.getParentId())
                .exists();
        Assert.isConflict(exist, "同级下,标签名称已存在！");

        //更新标签信息
        tagBusiness.setDepIds(tag.getDepIds());
        tagBusiness.setName(tag.getName().trim());
        tagBusiness.setDescription(tag.getDescription());
        tagUserRefService.saveOrUpdateUserRef(id, tag.getUserIdList());
        return updateById(tagBusiness);
    }

    @Override
    public TagDetailResponse detailTag(Long id) {
        var tagBusiness = lambdaQuery().eq(TagBusiness::getId, id).last("limit 1").one();
        Assert.isNotFound(tagBusiness, "标签不存在！");
        Map<Long, List<Long>> tagRefUser = tagUserRefService.getTagRefUser(Collections.singletonList(tagBusiness.getId()));
        List<OrganizationDTO> organizationsByIds = new ArrayList<>();
        if (StringUtils.isNotBlank(tagBusiness.getDepIds())) {
            Set<Long> organizationIds = Arrays.stream(tagBusiness.getDepIds().split(",")).map(Long::valueOf).collect(Collectors.toSet());
            organizationsByIds = organizationClient.getOrganizationsByIds(new ArrayList<>(organizationIds));
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        if (MapUtils.isNotEmpty(tagRefUser)) {
            userDTOS = userClient.listUserByIds(tagRefUser.get(tagBusiness.getId()));
        }

        return TagDetailResponse.convert(tagBusiness, tagRefUser, userDTOS, organizationsByIds);
    }

    @Override
    public List<TagTreeResponse> tagTree(TagTreeRequest req) {
        //查询满足条件的标签
        var query = lambdaQuery();
        if (Objects.nonNull(req.getPeriod())) {
            query.between(TagBusiness::getCreateTime, req.getPeriod().getBegin(), req.getPeriod().getEnd());
        }
        var res = query.like(StringUtils.isNotBlank(req.getName()), TagBusiness::getName, req.getName()).list();

        return buildTree(res);
    }

    @Override
    @Transactional
    public boolean addTagRef(TagRefRequest tagRefRequest) {
        //判断标签是否存在
        var exist = lambdaQuery().eq(TagBusiness::getId, tagRefRequest.getTagId()).exists();
        Assert.isNotFound(!exist, "标签不存在！");
        var tagRefs = tagRefRequest.getRefRequests().stream().map(ref -> {
            var tagRef = new TagRef();
            tagRef.setTagId(tagRefRequest.getTagId());
            tagRef.setRefId(ref.getRefId());
            tagRef.setRefType(ref.getTagRefType());
            return tagRef;
        }).toList();
        return tagRefService.saveBatch(tagRefs);
    }

    @Override
    @Transactional
    public boolean removeTagRef(TagRefRemoveRequest tagRefRemoveRequest) {

        return tagRefService.remove(new LambdaQueryWrapper<TagRef>().eq(TagRef::getTagId, tagRefRemoveRequest.getTagId())
                                            .in(TagRef::getRefId, tagRefRemoveRequest.getRefIds()));
    }

    @Override
    public Set<Long> tagRefIdsList(Long tagId, TagRefTypeEnum refType) {
        return tagRefService.lambdaQuery()
                .eq(TagRef::getTagId, tagId)
                .eq(Objects.nonNull(refType), TagRef::getRefType, refType)
                .select(TagRef::getRefId).list()
                .stream()
                .map(TagRef::getRefId).collect(Collectors.toSet());
    }

    @Override
    public Pagination<StallDTO> tagRefStall(StallTagRefPageRequest tagRefPageRequest) {
        var stallIds = new HashSet<Long>();

        // 添加直接传入的ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            stallIds.addAll(tagRefPageRequest.getIds());
        }

        // 添加通过tagIds查询到的关联ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            var tagRelatedIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefPageRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.STALL).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toSet());

            // 如果提供了tagIds但查询结果为空，则返回空分页
            if (CollectionUtils.isEmpty(tagRelatedIds)) {
                return Pagination.empty();
            }

            stallIds.addAll(tagRelatedIds);
        }
        var query = new StallQueryParam();
        // 即使没有任何ids也不返回空分页，而是直接查询
        query.setIds(stallIds.isEmpty() ? null : stallIds);
        query.setKeyword(tagRefPageRequest.getName());
        query.setPageNumber(tagRefPageRequest.getPageNumber());
        query.setPageSize(tagRefPageRequest.getPageSize());
        query.setDepartmentId(tagRefPageRequest.getDeptId());
        query.setType(tagRefPageRequest.getType());
        query.setAreaId(tagRefPageRequest.getAreaId());
        query.setEnabled(tagRefPageRequest.getEnabled());

        var stallPagination = stallClient.postStallPage(query);
        var stalls = stallPagination.getRecords();
        if (CollectionUtils.isEmpty(stalls)) {
            return Pagination.empty();
        }
        //查询档位绑定的标签
        var tagRefs = tagRefService.lambdaQuery().in(TagRef::getRefId, ListUtils.convertItem(stalls, StallDTO::getId))
                .eq(TagRef::getRefType, TagRefTypeEnum.STALL).list();

        //查询标签信息
        var tagBusinesses = CollectionUtils.isNotEmpty(tagRefs)
                            ? lambdaQuery().in(TagBusiness::getId, ListUtils.convertItem(tagRefs, TagRef::getTagId)).list()
                            : new ArrayList<TagBusiness>();
        var tagNameMap = MapUtils.listToMap(tagBusinesses, TagBusiness::getId, TagBusiness::getName);
        //根据档位Id进行分组
        var tagRefMap = MapUtils.listGroupToMap(tagRefs, TagRef::getRefId);
        for (StallDTO stall : stalls) {
            var tagIds = tagRefMap.getOrDefault(stall.getId(), ListUtils.newArrayList()).stream().map(TagRef::getTagId).toList();
            stall.setTagIds(tagIds);
            var tagNames = tagIds.stream().map(tagId -> tagNameMap.getOrDefault(tagId, "")).collect(Collectors.joining(","));
            stall.setTagNames(tagNames);
            //设置typeId
            stall.setTypeId(stall.getType());
            //设置areaId
            stall.setAreaId(stall.getArea());
            //设置部门信息
            stall.setDepartmentIds(stall.getDeptIds());
            stall.setDepartmentNames(stall.getDeptNames() != null ? String.join(",", stall.getDeptNames()) : null);

        }
        return stallPagination;
    }

    @Override
    public List<AreaDTO> tagRefArea(TagRefPageRequest tagRefPageRequest) {
        var areaIds = new HashSet<Long>();

        // 添加直接传入的ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            areaIds.addAll(tagRefPageRequest.getIds());
        }

        // 添加通过tagIds查询到的关联ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            var tagRelatedIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefPageRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.AREA).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toSet());

            // 如果提供了tagIds但查询结果为空，则返回空列表
            if (CollectionUtils.isEmpty(tagRelatedIds)) {
                return ListUtils.newArrayList();
            }

            areaIds.addAll(tagRelatedIds);
        }

        // 如果既没有直接传入ids，也没有通过tagIds查询，则areaIds为空时直接进行查询（不返回空列表）
        var query = new AreaQueryParam();
        var queryIds = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds()) && CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            //取交集
            queryIds.addAll(CollectionUtils.intersection(tagRefPageRequest.getIds(), areaIds));
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            queryIds.addAll(areaIds);
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            queryIds.addAll(tagRefPageRequest.getIds());
        }
        query.setIds(queryIds.isEmpty() ? null : queryIds);
        query.setKeyword(tagRefPageRequest.getName());
        query.setDeptId(tagRefPageRequest.getDeptId() != null ? tagRefPageRequest.getDeptId().toString() : null);

        var areas = areaClient.getAreas(query);
        if (CollectionUtils.isEmpty(areas)) {
            return areas;
        }
        //查询区域绑定的标签
        var tagRefs = tagRefService.lambdaQuery().eq(TagRef::getRefType, TagRefTypeEnum.AREA).list();
        //查询标签信息
        var tagBusinesses = CollectionUtils.isNotEmpty(tagRefs)
                            ? lambdaQuery().in(TagBusiness::getId, ListUtils.convertItem(tagRefs, TagRef::getTagId)).list()
                            : new ArrayList<TagBusiness>();
        if (CollectionUtils.isEmpty(tagBusinesses)) {
            return areas;
        }
        var tagNameMap = MapUtils.listToMap(tagBusinesses, TagBusiness::getId, TagBusiness::getName);
        //根据区域Id进行分组
        var tagRefMap = MapUtils.listGroupToMap(tagRefs, TagRef::getRefId);
        for (AreaDTO area : areas) {
            handleAreaTree(area, tagNameMap, tagRefMap);
        }
        return areas;
    }

    private void handleAreaTree(AreaDTO topArea, Map<Long, String> tagNamesMap, Map<Long, List<TagRef>> tagRefMap) {
        if (topArea != null) {
            var tagIds = tagRefMap.getOrDefault(topArea.getId(), ListUtils.newArrayList()).stream().map(TagRef::getTagId).toList();
            var tagNames = tagIds.stream().map(tagId -> tagNamesMap.getOrDefault(tagId, "")).collect(Collectors.joining(","));
            topArea.setTagNames(tagNames);
            if (CollectionUtils.isNotEmpty(topArea.getChildren())) {
                for (AreaDTO child : topArea.getChildren()) {
                    handleAreaTree(child, tagNamesMap, tagRefMap);
                }
            }
        }
    }

    @Override
    public Pagination<DevicePageInfoVO> tagRefDevice(DeviceTagRefPageRequest tagRefPageRequest) {
        var deviceIds = new ArrayList<Long>();
        // 添加通过tagIds查询到的关联ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            var tagRelatedIds = tagRefService.lambdaQuery()
                    .in(TagRef::getTagId, tagRefPageRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.DEVICE).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .toList();

            // 如果提供了tagIds但查询结果为空，则返回空分页
            if (CollectionUtils.isEmpty(tagRelatedIds)) {
                return Pagination.empty();
            }

            deviceIds.addAll(tagRelatedIds);
        }

        // 即使没有任何ids，也不返回空分页，而是直接进行查询
        var query = new DeviceFilterPageParam();

        var queryIds = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds()) && CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            //取交集
            queryIds.addAll(CollectionUtils.intersection(tagRefPageRequest.getIds(), deviceIds));
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            queryIds.addAll(deviceIds);
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            queryIds.addAll(tagRefPageRequest.getIds());
        }
        query.setMatchedDeviceIds(queryIds.isEmpty() ? null : queryIds);

        query.setKeyword(tagRefPageRequest.getName());
        query.setPageNumber(tagRefPageRequest.getPageNumber());
        query.setPageSize(tagRefPageRequest.getPageSize());
        query.setAnyMatchedDepartmentId(tagRefPageRequest.getDeptId());
        query.setAnyMatchedAreaId(tagRefPageRequest.getAnyMatchedAreaId());
        query.setProductId(tagRefPageRequest.getProductId());
        query.setTagCodes(tagRefPageRequest.getTagCodes());
        var devicePagination = deviceClient.deviceInfoPage(query);
        var devices = devicePagination.getRecords();
        if (CollectionUtils.isEmpty(devices)) {
            return devicePagination;
        }
        //查询设备绑定的标签
        var tagRefs = tagRefService.lambdaQuery().in(TagRef::getRefId, ListUtils.convertItem(devices, DevicePageInfoVO::getId))
                .eq(TagRef::getRefType, TagRefTypeEnum.DEVICE).list();
        //查询标签信息
        var tagBusinesses = CollectionUtils.isNotEmpty(tagRefs)
                            ? lambdaQuery().in(TagBusiness::getId, ListUtils.convertItem(tagRefs, TagRef::getTagId)).list()
                            : new ArrayList<TagBusiness>();
        var tagNameMap = MapUtils.listToMap(tagBusinesses, TagBusiness::getId, TagBusiness::getName);
        //根据设备Id进行分组
        var tagRefMap = MapUtils.listGroupToMap(tagRefs, TagRef::getRefId);
        for (DevicePageInfoVO device : devices) {
            var tagIds = tagRefMap.getOrDefault(device.getId(), ListUtils.newArrayList()).stream().map(TagRef::getTagId).toList();
            device.setTagIds(tagIds);
            var tagNames = tagIds.stream().map(tagId -> tagNameMap.getOrDefault(tagId, "")).collect(Collectors.joining(","));
            device.setTagNames(tagNames);
            var productInfo = device.getProductInfo();
            if (productInfo != null) {
                var categoryCode = productInfo.getProductCategory();
                //设置类型id
                device.setTypeId(categoryCode != null ? categoryCode.getCode() : null);
                //设置类型名称
                device.setTypeName(categoryCode != null ? categoryCode.getName() : null);
            }
            var dept = device.getDepartment();
            //是指部门id
            device.setDepartmentIds(dept != null && dept.getId() != null ? List.of(dept.getId()) : null);
            //设置部门名称
            device.setDepartmentNames(dept != null ? dept.getName() : null);
            //设置区域id
            var area = device.getArea();
            device.setAreaId(area != null && area.getId() != null ? area.getId().toString() : null);
            //设置区域名称
            device.setAreaName(area != null ? area.getName() : null);

        }
        return devicePagination;
    }

    @Override
    public Pagination<ScenePointVO> tagRefScenePoint(ScenePointTagRefPageRequest tagRefPageRequest) {
        var scenePointIds = new HashSet<Long>();

        // 添加直接传入的ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            scenePointIds.addAll(tagRefPageRequest.getIds());
        }

        // 添加通过tagIds查询到的关联ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            var tagRelatedIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefPageRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.SITE_POINT).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toSet());

            // 如果提供了tagIds但查询结果为空，则返回空分页
            if (CollectionUtils.isEmpty(tagRelatedIds)) {
                return Pagination.empty();
            }

            scenePointIds.addAll(tagRelatedIds);
        }

        // 即使没有任何ids，也不返回空分页，而是直接进行查询
        var query = new InspectScenePointQueryParam();
        var queryIds = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds()) && CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            //取交集
            queryIds.addAll(CollectionUtils.intersection(tagRefPageRequest.getIds(), scenePointIds));
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            queryIds.addAll(scenePointIds);
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            queryIds.addAll(tagRefPageRequest.getIds());
        }
        query.setIds(queryIds.isEmpty() ? null : new HashSet<>(queryIds));
        query.setPointName(tagRefPageRequest.getName());
        query.setPageNumber(tagRefPageRequest.getPageNumber());
        query.setPageSize(tagRefPageRequest.getPageSize());
        query.setReceiveDepId(tagRefPageRequest.getDeptId());
        query.setTypeId(StringUtils.isNotBlank(tagRefPageRequest.getTypeId()) ? Long.parseLong(tagRefPageRequest.getTypeId()) : null);
        var scenePointPagination = scenePointClient.scenePointList(query);
        var scenePoints = scenePointPagination.getRecords();
        if (CollectionUtils.isEmpty(scenePoints)) {
            return scenePointPagination;
        }
        //查询场景点绑定的标签
        var tagRefs = tagRefService.lambdaQuery().in(TagRef::getRefId, ListUtils.convertItem(scenePoints, ScenePointVO::getId))
                .eq(TagRef::getRefType, TagRefTypeEnum.SITE_POINT).list();
        //查询标签信息
        var tagBusinesses = CollectionUtils.isNotEmpty(tagRefs)
                            ? lambdaQuery().in(TagBusiness::getId, ListUtils.convertItem(tagRefs, TagRef::getTagId)).list()
                            : new ArrayList<TagBusiness>();
        var tagNameMap = MapUtils.listToMap(tagBusinesses, TagBusiness::getId, TagBusiness::getName);
        //根据场景点Id进行分组
        var tagRefMap = MapUtils.listGroupToMap(tagRefs, TagRef::getRefId);
        for (ScenePointVO scenePoint : scenePoints) {
            var tagIds = tagRefMap.getOrDefault(scenePoint.getId(), ListUtils.newArrayList()).stream().map(TagRef::getTagId).toList();
            scenePoint.setTagIds(tagIds);
            var tagNames = tagIds.stream().map(tagId -> tagNameMap.getOrDefault(tagId, "")).collect(Collectors.joining(","));
            scenePoint.setTagNames(tagNames);
            //设置部门信息
            scenePoint.setDepartmentIds(scenePoint.getDepId() != null ? List.of(scenePoint.getDepId()) : null);
            scenePoint.setDepartmentNames(scenePoint.getDepName());
        }
        return scenePointPagination;
    }

    @Override
    public Pagination<PropertyDetailedTagPageVO> tagRefPropertyDetailed(PropertyTagRefPageRequest tagRefPageRequest) {
        var propertyDetailedIds = new HashSet<Long>();

        // 添加直接传入的ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            propertyDetailedIds.addAll(tagRefPageRequest.getIds());
        }

        // 添加通过tagIds查询到的关联ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            var tagRelatedIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefPageRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.PROPERTY).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toSet());

            // 如果提供了tagIds但查询结果为空，则返回空分页
            if (CollectionUtils.isEmpty(tagRelatedIds)) {
                return Pagination.empty();
            }

            propertyDetailedIds.addAll(tagRelatedIds);
        }
        var query = new PropertyDetailedPageParam();
        var queryIds = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds()) && CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            //取交集
            queryIds.addAll(CollectionUtils.intersection(tagRefPageRequest.getIds(), propertyDetailedIds));
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            queryIds.addAll(propertyDetailedIds);
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            queryIds.addAll(tagRefPageRequest.getIds());
        }
        // 即使没有任何ids，也不返回空分页，而是直接进行查询
        query.setIds(queryIds.isEmpty() ? null : new HashSet<>(queryIds));
        query.setKeyword(tagRefPageRequest.getName());
        query.setPageNumber(tagRefPageRequest.getPageNumber());
        query.setPageSize(tagRefPageRequest.getPageSize());
        query.setReceiveDepId(tagRefPageRequest.getDeptId() != null ? tagRefPageRequest.getDeptId().toString() : null);
        query.setTypeId(tagRefPageRequest.getTypeId());
        var propertyDetailedPagination = propertyDetailedClient.postPropertyDetailedPage(query);
        var propertyDetaileds = propertyDetailedPagination.getRecords();
        if (CollectionUtils.isEmpty(propertyDetaileds)) {
            return propertyDetailedPagination;
        }
        //查询资产明细绑定的标签
        var tagRefs = tagRefService.lambdaQuery()
                .in(TagRef::getRefId, ListUtils.convertItem(propertyDetaileds, PropertyDetailedTagPageVO::getId))
                .eq(TagRef::getRefType, TagRefTypeEnum.PROPERTY).list();
        //查询标签信息
        var tagBusinesses = CollectionUtils.isNotEmpty(tagRefs)
                            ? lambdaQuery().in(TagBusiness::getId, ListUtils.convertItem(tagRefs, TagRef::getTagId)).list()
                            : new ArrayList<TagBusiness>();
        var tagNameMap = MapUtils.listToMap(tagBusinesses, TagBusiness::getId, TagBusiness::getName);
        //根据资产明细Id进行分组
        var tagRefMap = MapUtils.listGroupToMap(tagRefs, TagRef::getRefId);
        for (PropertyDetailedTagPageVO propertyDetailed : propertyDetaileds) {
            var tagIds = tagRefMap.getOrDefault(propertyDetailed.getId(), ListUtils.newArrayList()).stream().map(TagRef::getTagId).toList();
            propertyDetailed.setTagIds(tagIds);
            var tagNames = tagIds.stream().map(tagId -> tagNameMap.getOrDefault(tagId, "")).collect(Collectors.joining(","));
            propertyDetailed.setTagNames(tagNames);
            //设置部门信息
            propertyDetailed.setDepartmentIds(
                    propertyDetailed.getReceiveDepId() != null ? List.of(propertyDetailed.getReceiveDepId()) : null);
            propertyDetailed.setDepartmentNames(propertyDetailed.getReceiveDepName());
        }
        return propertyDetailedPagination;
    }

    @Override
    public Pagination<RiskSourceLedgerVO> tagRefRiskSource(TagRefPageRequest tagRefPageRequest) {
        var riskSourceIds = new HashSet<Long>();

        // 添加直接传入的ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            riskSourceIds.addAll(tagRefPageRequest.getIds());
        }

        // 添加通过tagIds查询到的关联ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            var tagRelatedIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefPageRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.RISK_SOURCE).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toSet());

            // 如果提供了tagIds但查询结果为空，则返回空分页
            if (CollectionUtils.isEmpty(tagRelatedIds)) {
                return Pagination.empty();
            }

            riskSourceIds.addAll(tagRelatedIds);
        }
        var query = new RiskSourceLedgerPageParam();
        var queryIds = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds()) && CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            //取交集
            queryIds.addAll(CollectionUtils.intersection(tagRefPageRequest.getIds(), riskSourceIds));
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            queryIds.addAll(riskSourceIds);
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            queryIds.addAll(tagRefPageRequest.getIds());
        }
        // 即使没有任何ids，也不返回空分页，而是直接进行查询
        query.setIds(queryIds.isEmpty() ? null : new HashSet<>(queryIds));
        query.setKeyword(tagRefPageRequest.getName());
        query.setPageNumber(tagRefPageRequest.getPageNumber());
        query.setPageSize(tagRefPageRequest.getPageSize());
        query.setDeptId(tagRefPageRequest.getDeptId());
        query.setTypeCode(tagRefPageRequest.getTypeId());
        var riskSourcePagination = riskSourceLedgerClient.postPage(query);
        var riskSources = riskSourcePagination.getRecords();
        if (CollectionUtils.isEmpty(riskSources)) {
            return riskSourcePagination;
        }
        //查询风险源绑定的标签
        var tagRefs = tagRefService.lambdaQuery().in(TagRef::getRefId, ListUtils.convertItem(riskSources, RiskSourceLedgerVO::getId))
                .eq(TagRef::getRefType, TagRefTypeEnum.RISK_SOURCE).list();
        //查询标签信息
        var tagBusinesses = CollectionUtils.isNotEmpty(tagRefs)
                            ? lambdaQuery().in(TagBusiness::getId, ListUtils.convertItem(tagRefs, TagRef::getTagId)).list()
                            : new ArrayList<TagBusiness>();
        var tagNameMap = MapUtils.listToMap(tagBusinesses, TagBusiness::getId, TagBusiness::getName);
        //根据风险源Id进行分组
        var tagRefMap = MapUtils.listGroupToMap(tagRefs, TagRef::getRefId);
        for (RiskSourceLedgerVO riskSource : riskSources) {
            var tagIds = tagRefMap.getOrDefault(riskSource.getId(), ListUtils.newArrayList()).stream().map(TagRef::getTagId).toList();
            riskSource.setTagIds(tagIds);
            var tagNames = tagIds.stream().map(tagId -> tagNameMap.getOrDefault(tagId, "")).collect(Collectors.joining(","));
            riskSource.setTagNames(tagNames);
            //设置部门信息
            riskSource.setDepartmentIds(riskSource.getMaintenanceDepId() != null ? List.of(riskSource.getMaintenanceDepId()) : null);
            riskSource.setDepartmentNames(riskSource.getMaintenanceDepName());
            //设置类型信息
            riskSource.setTypeId(riskSource.getTypeCode());
        }
        return riskSourcePagination;
    }

    @Override
    public Pagination<ProjectScheduleVO> tagRefProjectSchedule(LooseWorkRefPageRequest tagRefPageRequest) {
        var projectScheduleIds = new ArrayList<Long>();

        // 添加直接传入的ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            projectScheduleIds.addAll(tagRefPageRequest.getIds());
        }

        // 添加通过tagIds查询到的关联ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            var tagRelatedIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefPageRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.SMALL_ENGINEERING).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toSet());

            // 如果提供了tagIds但查询结果为空，则返回空分页
            if (CollectionUtils.isEmpty(tagRelatedIds)) {
                return Pagination.empty();
            }

            projectScheduleIds.addAll(tagRelatedIds);
        }
        var query = new ProjectScheduleQuery();
        var queryIds = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds()) && CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            //取交集
            queryIds.addAll(CollectionUtils.intersection(tagRefPageRequest.getIds(), projectScheduleIds));
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            queryIds.addAll(projectScheduleIds);
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            queryIds.addAll(tagRefPageRequest.getIds());
        }
        // 即使没有任何ids，也不返回空分页，而是直接进行查询
        query.setIdList(queryIds.isEmpty() ? null : queryIds);
        query.setKeyword(tagRefPageRequest.getName());
        query.setPageNumber(tagRefPageRequest.getPageNumber());
        query.setPageSize(tagRefPageRequest.getPageSize());
        query.setDepId(tagRefPageRequest.getDeptId());
        query.setStatus(tagRefPageRequest.getStatus());

        var projectSchedulePagination = projectScheduleClient.getProjectScheduleList(query);
        var projectSchedules = projectSchedulePagination.getRecords();
        if (CollectionUtils.isEmpty(projectSchedules)) {
            return projectSchedulePagination;
        }
        //查询工程进度绑定的标签
        var tagRefs = tagRefService.lambdaQuery().in(TagRef::getRefId, ListUtils.convertItem(projectSchedules, ProjectScheduleVO::getId))
                .eq(TagRef::getRefType, TagRefTypeEnum.SMALL_ENGINEERING).list();
        //查询标签信息
        var tagBusinesses = CollectionUtils.isNotEmpty(tagRefs)
                            ? lambdaQuery().in(TagBusiness::getId, ListUtils.convertItem(tagRefs, TagRef::getTagId)).list()
                            : new ArrayList<TagBusiness>();
        var tagNameMap = MapUtils.listToMap(tagBusinesses, TagBusiness::getId, TagBusiness::getName);
        //根据工程进度Id进行分组
        var tagRefMap = MapUtils.listGroupToMap(tagRefs, TagRef::getRefId);
        for (ProjectScheduleVO projectSchedule : projectSchedules) {
            var tagIds = tagRefMap.getOrDefault(projectSchedule.getId(), ListUtils.newArrayList()).stream().map(TagRef::getTagId).toList();
            projectSchedule.setTagIds(tagIds);
            var tagNames = tagIds.stream().map(tagId -> tagNameMap.getOrDefault(tagId, "")).collect(Collectors.joining(","));
            projectSchedule.setTagNames(tagNames);
            //设置名称
            projectSchedule.setName(projectSchedule.getProjectName());
            //设置部门
            projectSchedule.setDepartmentNames(projectSchedule.getDepName());
            //设置区域
            projectSchedule.setAreaName(projectSchedule.getAddress());
            //设置类型
            projectSchedule.setTypeName(projectSchedule.getProjectTypeName());
            projectSchedule.setTypeId(projectSchedule.getProjectType() != null ? projectSchedule.getProjectType().toString() : null);
        }
        return projectSchedulePagination;
    }

    @Override
    public Pagination<PatrolLocationVO> tagRefPatrolPoint(PatrolPointTagRefPageRequest tagRefPageRequest) {
        var patrolLocationIds = new HashSet<Long>();

        // 添加直接传入的ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            patrolLocationIds.addAll(tagRefPageRequest.getIds());
        }

        // 添加通过tagIds查询到的关联ids
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            var tagRelatedIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefPageRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.PATROL_POINT).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toSet());

            // 如果有相关ID则添加到集合
            if (CollectionUtils.isEmpty(tagRelatedIds)) {
                return Pagination.empty();
            }
            patrolLocationIds.addAll(tagRelatedIds);
        }
        var query = new PatrolLocationPageParam();
        var queryIds = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds()) && CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            //取交集
            queryIds.addAll(CollectionUtils.intersection(tagRefPageRequest.getIds(), patrolLocationIds));
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getTagIds())) {
            queryIds.addAll(patrolLocationIds);
        } else if (CollectionUtils.isNotEmpty(tagRefPageRequest.getIds())) {
            queryIds.addAll(tagRefPageRequest.getIds());
        }
        // 即使没有任何ids，也不返回空分页，而是直接进行查询
        query.setIdSet(queryIds.isEmpty() ? null : new HashSet<>(queryIds));
        query.setKeyword(tagRefPageRequest.getName());
        query.setPageNumber(tagRefPageRequest.getPageNumber());
        query.setPageSize(tagRefPageRequest.getPageSize());
        query.setAreaId(tagRefPageRequest.getAreaId());

        var patrolLocationPagination = patrolLocationClient.points(query);
        var patrolLocations = patrolLocationPagination.getRecords();
        if (CollectionUtils.isEmpty(patrolLocations)) {
            return patrolLocationPagination;
        }
        //查询巡检点位绑定的标签
        var tagRefs = tagRefService.lambdaQuery().in(TagRef::getRefId, ListUtils.convertItem(patrolLocations, PatrolLocationVO::getId))
                .eq(TagRef::getRefType, TagRefTypeEnum.PATROL_POINT).list();
        //查询标签信息
        var tagBusinesses = CollectionUtils.isNotEmpty(tagRefs)
                            ? lambdaQuery().in(TagBusiness::getId, ListUtils.convertItem(tagRefs, TagRef::getTagId)).list()
                            : new ArrayList<TagBusiness>();
        var tagNameMap = MapUtils.listToMap(tagBusinesses, TagBusiness::getId, TagBusiness::getName);
        //根据巡检点位Id进行分组
        var tagRefMap = MapUtils.listGroupToMap(tagRefs, TagRef::getRefId);
        for (PatrolLocationVO patrolLocation : patrolLocations) {
            var tagIds = tagRefMap.getOrDefault(patrolLocation.getId(), ListUtils.newArrayList()).stream().map(TagRef::getTagId).toList();
            patrolLocation.setTagIds(tagIds);
            var tagNames = tagIds.stream().map(tagId -> tagNameMap.getOrDefault(tagId, "")).collect(Collectors.joining(","));
            patrolLocation.setTagNames(tagNames);
            //设置名称
            patrolLocation.setName(patrolLocation.getPatrolLocationName());

            //设置区域
            var areas = patrolLocation.getArea();
            patrolLocation.setAreaName(areas != null ? areas.stream().map(AreaDTO::getName).collect(Collectors.joining(",")) : null);
        }
        return patrolLocationPagination;
    }

    @Override
    public List<AreaIdTypeDTO> tagRefAreaIds(TagRefListRequest tagRefListRequest) {
        Set<Long> areaIds = null;
        if (CollectionUtils.isNotEmpty(tagRefListRequest.getTagIds())) {
            areaIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefListRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.AREA).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toSet());
            if (ListUtils.isEmpty(areaIds)) {
                return ListUtils.newArrayList();
            }
        }
        var query = new AreaQueryParam();
        query.setKeyword(tagRefListRequest.getName());
        query.setIds(areaIds);
        query.setDeptId(tagRefListRequest.getDeptId() != null ? tagRefListRequest.getDeptId().toString() : null);
        return areaClient.getAreaIdTypes(query);
    }

    @Override
    public List<StallIdTypeDTO> tagRefStallIdTypes(TagRefListRequest tagRefListRequest) {
        Set<Long> stallIds = null;
        if (CollectionUtils.isNotEmpty(tagRefListRequest.getTagIds())) {
            stallIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefListRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.STALL).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toSet());
            if (ListUtils.isEmpty(stallIds)) {
                return ListUtils.newArrayList();
            }
        }
        var query = new StallQueryParam();
        query.setIds(stallIds);
        query.setKeyword(tagRefListRequest.getName());
        query.setDepartmentId(tagRefListRequest.getDeptId());
        List<StallIdTypeDTO> stallIdTypes = stallClient.getStallIdTypes(query);
        if (CollectionUtils.isEmpty(stallIdTypes)) {
            return new ArrayList<>();
        }
        Set<Long> idList = stallIdTypes.stream().map(StallIdTypeDTO::getId).collect(Collectors.toSet());
        Map<Long, Set<Long>> groupByIdTagMap = getGroupByIdTagMap(idList);
        stallIdTypes.forEach(stallIdTypeDTO -> stallIdTypeDTO.setTagIds(groupByIdTagMap.get(stallIdTypeDTO.getId())));
        return stallIdTypes;
    }

    @Override
    public List<DeviceIdTypeDTO> tagRefDeviceIdTypes(TagRefListRequest tagRefListRequest) {
        List<Long> deviceIds = null;
        if (CollectionUtils.isNotEmpty(tagRefListRequest.getTagIds())) {
            deviceIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefListRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.DEVICE).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toList());
            if (ListUtils.isEmpty(deviceIds)) {
                return ListUtils.newArrayList();
            }
        }
        var query = new DeviceFilterPageParam();
        query.setMatchedDeviceIds(deviceIds);
        query.setKeyword(tagRefListRequest.getName());
        query.setAnyMatchedDepartmentId(tagRefListRequest.getDeptId());
        List<DeviceIdTypeDTO> deviceIdTypeDTOS = deviceClient.deviceInfoIdTypes(query);
        if (CollectionUtils.isEmpty(deviceIdTypeDTOS)) {
            return new ArrayList<>();
        }
        Set<Long> idList = deviceIdTypeDTOS.stream().map(DeviceIdTypeDTO::getId).collect(Collectors.toSet());
        Map<Long, Set<Long>> groupByIdTagMap = getGroupByIdTagMap(idList);
        deviceIdTypeDTOS.forEach(stallIdTypeDTO -> stallIdTypeDTO.setTagIds(groupByIdTagMap.get(stallIdTypeDTO.getId())));
        return deviceIdTypeDTOS;
    }

    private Map<Long, Set<Long>> getGroupByIdTagMap(Set<Long> idList) {
        return Optional.ofNullable(tagRefService.lambdaQuery()
                                           .in(TagRef::getRefId, idList)
                                           .list()).orElse(new ArrayList<>())
                .stream().collect(Collectors.groupingBy(TagRef::getRefId, Collectors.mapping(TagRef::getTagId, Collectors.toSet())));
    }

    @Override
    public List<ScenePointIdTypeDTO> tagRefScenePointIds(TagRefListRequest tagRefListRequest) {
        Set<Long> scenePointIds = null;
        if (CollectionUtils.isNotEmpty(tagRefListRequest.getTagIds())) {
            scenePointIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefListRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.SITE_POINT).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toSet());
            if (ListUtils.isEmpty(scenePointIds)) {
                return ListUtils.newArrayList();
            }
        }
        var query = new InspectScenePointQueryParam();
        query.setIds(scenePointIds);
        query.setPointName(tagRefListRequest.getName());
        query.setReceiveDepId(tagRefListRequest.getDeptId());
        List<ScenePointIdTypeDTO> scenePointIdTypes = scenePointClient.getScenePointIdTypes(query);
        if (CollectionUtils.isEmpty(scenePointIdTypes)) {
            return new ArrayList<>();
        }
        Set<Long> idList = scenePointIdTypes.stream().map(ScenePointIdTypeDTO::getId).collect(Collectors.toSet());
        Map<Long, Set<Long>> groupByIdTagMap = getGroupByIdTagMap(idList);
        scenePointIdTypes.forEach(stallIdTypeDTO -> stallIdTypeDTO.setTagIds(groupByIdTagMap.get(stallIdTypeDTO.getId())));
        return scenePointIdTypes;
    }

    @Override
    public List<PropertyDetailIdTypeDTO> tagRefPropertyDetailedIds(TagRefListRequest tagRefListRequest) {
        Set<Long> propertyDetailedIds = null;
        if (CollectionUtils.isNotEmpty(tagRefListRequest.getTagIds())) {
            propertyDetailedIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefListRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.PROPERTY).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toSet());
            if (ListUtils.isEmpty(propertyDetailedIds)) {
                return ListUtils.newArrayList();
            }
        }
        var query = new PropertyDetailedPageParam();
        query.setIds(propertyDetailedIds);
        query.setKeyword(tagRefListRequest.getName());
        query.setDeptId(tagRefListRequest.getDeptId());
        List<PropertyDetailIdTypeDTO> propertyDetailedIdTypes = propertyDetailedClient.getPropertyDetailedIdTypes(query);
        if (CollectionUtils.isEmpty(propertyDetailedIdTypes)) {
            return new ArrayList<>();
        }
        Set<Long> idList = propertyDetailedIdTypes.stream().map(PropertyDetailIdTypeDTO::getId).collect(Collectors.toSet());
        Map<Long, Set<Long>> groupByIdTagMap = getGroupByIdTagMap(idList);
        propertyDetailedIdTypes.forEach(stallIdTypeDTO -> stallIdTypeDTO.setTagIds(groupByIdTagMap.get(stallIdTypeDTO.getId())));
        return propertyDetailedIdTypes;
    }

    @Override
    public List<RiskSourceIdTypeVO> tagRefRiskSourceIds(TagRefListRequest tagRefListRequest) {
        Set<Long> riskSourceIds = null;
        if (CollectionUtils.isNotEmpty(tagRefListRequest.getTagIds())) {
            riskSourceIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefListRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.RISK_SOURCE).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toSet());
            if (ListUtils.isEmpty(riskSourceIds)) {
                return ListUtils.newArrayList();
            }
        }
        var query = new RiskSourceLedgerPageParam();
        query.setIds(riskSourceIds);
        query.setKeyword(tagRefListRequest.getName());
        query.setDeptId(tagRefListRequest.getDeptId());
        List<RiskSourceIdTypeVO> riskSourceIdTypes = riskSourceLedgerClient.getRiskSourceIdTypes(query);
        if (CollectionUtils.isEmpty(riskSourceIdTypes)) {
            return new ArrayList<>();
        }
        Set<Long> idList = riskSourceIdTypes.stream().map(RiskSourceIdTypeVO::getId).collect(Collectors.toSet());
        Map<Long, Set<Long>> groupByIdTagMap = getGroupByIdTagMap(idList);
        riskSourceIdTypes.forEach(stallIdTypeDTO -> stallIdTypeDTO.setTagIds(groupByIdTagMap.get(stallIdTypeDTO.getId())));
        return riskSourceIdTypes;
    }

    @Override
    public List<ProjectScheduleIdTypeDTO> tagRefProjectScheduleIds(TagRefListRequest tagRefListRequest) {
        List<Long> projectScheduleIds = null;
        if (CollectionUtils.isNotEmpty(tagRefListRequest.getTagIds())) {
            projectScheduleIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefListRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.SMALL_ENGINEERING).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toList());
            if (ListUtils.isEmpty(projectScheduleIds)) {
                return ListUtils.newArrayList();
            }
        }
        var query = new ProjectScheduleQuery();
        query.setIdList(projectScheduleIds);
        query.setKeyword(tagRefListRequest.getName());
        query.setDepId(tagRefListRequest.getDeptId());
        List<ProjectScheduleIdTypeDTO> projectScheduleIdTypes = projectScheduleClient.getProjectScheduleIdTypes(query);
        if (CollectionUtils.isEmpty(projectScheduleIdTypes)) {
            return new ArrayList<>();
        }
        Set<Long> idList = projectScheduleIdTypes.stream().map(ProjectScheduleIdTypeDTO::getId).collect(Collectors.toSet());
        Map<Long, Set<Long>> groupByIdTagMap = getGroupByIdTagMap(idList);
        projectScheduleIdTypes.forEach(stallIdTypeDTO -> stallIdTypeDTO.setTagIds(groupByIdTagMap.get(stallIdTypeDTO.getId())));
        return projectScheduleIdTypes;
    }

    @Override
    public List<PatrolLocationIdTypeDTO> tagRefPatrolPointIds(TagRefListRequest tagRefListRequest) {
        Set<Long> patrolLocationIds = null;
        if (CollectionUtils.isNotEmpty(tagRefListRequest.getTagIds())) {
            patrolLocationIds = tagRefService.lambdaQuery().in(TagRef::getTagId, tagRefListRequest.getTagIds())
                    .eq(TagRef::getRefType, TagRefTypeEnum.PATROL_POINT).select(TagRef::getRefId)
                    .list()
                    .stream()
                    .map(TagRef::getRefId)
                    .collect(Collectors.toSet());
            if (ListUtils.isEmpty(patrolLocationIds)) {
                return ListUtils.newArrayList();
            }
        }
        var query = new PatrolLocationPageParam();
        query.setIdSet(patrolLocationIds);
        query.setKeyword(tagRefListRequest.getName());
        List<PatrolLocationIdTypeDTO> patrolPointIdTypes = patrolLocationClient.getPatrolPointIdTypes(query);
        if (CollectionUtils.isEmpty(patrolPointIdTypes)) {
            return new ArrayList<>();
        }
        Set<Long> idList = patrolPointIdTypes.stream().map(PatrolLocationIdTypeDTO::getId).collect(Collectors.toSet());
        Map<Long, Set<Long>> groupByIdTagMap = getGroupByIdTagMap(idList);
        patrolPointIdTypes.forEach(stallIdTypeDTO -> stallIdTypeDTO.setTagIds(groupByIdTagMap.get(stallIdTypeDTO.getId())));
        return patrolPointIdTypes;
    }

    @Override
    public Map<Long, List<Long>> tagRefUser(TagRefListRequest tagRefListRequest) {
        return tagUserRefService.getTagRefUser(tagRefListRequest.getTagIds());
    }

    @Override
    public List<BaseTargetInfoResponse> tagRefData(TagRefListRequest tagRefListRequest) {
        if (CollectionUtils.isEmpty(tagRefListRequest.getTagIds()) && Objects.isNull(tagRefListRequest.getTargetType())) {
            return new ArrayList<>();
        }
        List<TagRef> tagRefList = tagRefService.lambdaQuery()
                .in(CollectionUtils.isNotEmpty(tagRefListRequest.getTagIds()), TagRef::getTagId, tagRefListRequest.getTagIds())
                .eq(Objects.nonNull(tagRefListRequest.getTargetType()), TagRef::getRefType, tagRefListRequest.getTargetType())
                .list();
        tagRefList = tagRefList.stream().filter(t -> Objects.nonNull(t.getRefId())).collect(Collectors.toList());
        Set<Long> tagIds = tagRefList.stream().map(TagRef::getTagId).collect(Collectors.toSet());
        Map<Long, String> tagBusinessMap;
        if (CollectionUtils.isNotEmpty(tagRefList)) {
            //获取标签关联的所有对象
            tagBusinessMap = this.lambdaQuery().in(TagBusiness::getId, tagIds)
                    .list()
                    .stream().collect(Collectors.toMap(TagBusiness::getId, TagBusiness::getName));

        } else {
            tagBusinessMap = new HashMap<>();
        }
        Map<Long, Set<Long>> refIdMap = tagRefList.stream()
                .collect(Collectors.groupingBy(TagRef::getRefId, Collectors.mapping(TagRef::getTagId, Collectors.toSet())));
        Map<TagRefTypeEnum, Set<Long>> groupByTargetTypeMap = tagRefList.stream()
                .collect(Collectors.groupingBy(TagRef::getRefType, Collectors.mapping(TagRef::getRefId, Collectors.toSet())));
        if (MapUtils.isEmpty(groupByTargetTypeMap) && Objects.isNull(tagRefListRequest.getTargetType())) {
            return new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(tagRefListRequest.getTagIds()) && Objects.nonNull(tagRefListRequest.getTargetType())) {
            groupByTargetTypeMap.clear();
            groupByTargetTypeMap.put(tagRefListRequest.getTargetType(), new HashSet<>());
        }

        //并行获取所有的数据
        List<BaseTargetInfoResponse> result = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        for (var targetType : groupByTargetTypeMap.entrySet()) {
            completableFutures.add(CompletableFuture.supplyAsync(() -> {
                try {
                    SessionContextHolder.requestTenant(tenant);
                    return targetType.getKey().getTargetInfo(targetType.getValue(), tagBusinessMap, refIdMap);
                } catch (Exception e) {
                    log.error("获取{}对象失败", targetType.getKey().getDescription(), e);
                }
                return null;
            }, executor).thenAccept(t -> {
                if (CollectionUtils.isNotEmpty(t)) {
                    result.addAll(t);
                }
            }));
        }
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).exceptionally(e -> {
            log.error("获取对象信息失败", e);
            return null;
        }).join();
        result.removeIf(Objects::isNull);
        return result;
    }

    @Override
    public List<TagBusinessResponse> tagRefList(TagRefListRequest tagRefListRequest) {
        List<TagBusiness> tagBusinessList = this.lambdaQuery()
                .in(CollectionUtils.isNotEmpty(tagRefListRequest.getTagIds()), TagBusiness::getId, tagRefListRequest.getTagIds())
                .list();
        if (CollectionUtils.isEmpty(tagBusinessList)) {
            return new ArrayList<>();
        }
        return tagBusinessList.stream().map(t -> {
            TagBusinessResponse response = new TagBusinessResponse();
            response.setTagId(t.getId());
            response.setTagName(t.getName());
            return response;
        }).collect(Collectors.toList());
    }

    private List<TagTreeResponse> buildTree(List<TagBusiness> tagBusinesses) {
        var tagIds = tagBusinesses.stream().map(TagBusiness::getId).collect(Collectors.toSet());
        var maxLevel = 100;
        Set<Long> notFoundParentIds;
        do {
            //收集未查到的父级标签
            notFoundParentIds = tagBusinesses.stream().filter(tag -> tag.getParentId() != 0 && !tagIds.contains(tag.getParentId()))
                    .map(TagBusiness::getParentId).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(notFoundParentIds)) {
                //查询父级标签
                var parentTagBusinesses = lambdaQuery().in(TagBusiness::getId, notFoundParentIds).list();
                tagBusinesses.addAll(parentTagBusinesses);
                tagIds.addAll(notFoundParentIds);
            }

        } while (maxLevel-- > 0 && CollectionUtils.isNotEmpty(notFoundParentIds));
        Map<Long, List<Long>> tagRefUser = tagUserRefService.getTagRefUser(new ArrayList<>(tagIds));
        List<UserDTO> userDTOS;
        if (MapUtils.isNotEmpty(tagRefUser)) {
            Set<Long> userIds = tagRefUser.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
            userDTOS = userClient.listUserByIds(userIds);
        } else {
            userDTOS = new ArrayList<>();
        }
        Set<Long> organizationIds = tagBusinesses.stream()
                .filter(t -> StringUtils.isNotBlank(t.getDepIds()))
                .map(t -> Arrays.stream(t.getDepIds().split(","))
                        .map(Long::valueOf).collect(Collectors.toSet()))
                .flatMap(Collection::stream).collect(Collectors.toSet());
        List<OrganizationDTO> organizationsByIds;
        if (CollectionUtils.isNotEmpty(organizationIds)) {
            organizationsByIds = organizationClient.getOrganizationsByIds(new ArrayList<>(organizationIds));
        } else {
            organizationsByIds = new ArrayList<>();
        }

        var notRootTagBusinesses = tagBusinesses.stream().filter(tag -> tag.getParentId() != 0).collect(Collectors.toList());
        var tagParentMap = MapUtils.listGroupToMap(notRootTagBusinesses, TagBusiness::getParentId);
        //获取一级
        var rootTagBusinesses = tagBusinesses
                .stream()
                .filter(tag -> tag.getParentId() == 0)
                .map(tag -> TagTreeResponse.convert(TagDetailResponse.convert(tag, tagRefUser, userDTOS, organizationsByIds), 1, null))
                .collect(Collectors.toList());
        findChild(rootTagBusinesses, 2, tagParentMap, tagRefUser, userDTOS, organizationsByIds);
        return rootTagBusinesses;
    }

    private void findChild(List<TagTreeResponse> rootTagBusinesses, long childLevel, Map<Long, List<TagBusiness>> tagParentMap,
                           Map<Long, List<Long>> tagRefUser, List<UserDTO> userDTOS, List<OrganizationDTO> organizationsByIds
                          ) {
        if (CollectionUtils.isEmpty(rootTagBusinesses)) {
            return;
        }
        for (TagTreeResponse tagTreeResponse : rootTagBusinesses) {
            var childList = tagParentMap.get(tagTreeResponse.getId());
            if (CollectionUtils.isNotEmpty(childList)) {
                tagTreeResponse.setChildList(childList.stream()
                                                     .map(tag -> TagTreeResponse.convert(TagDetailResponse.convert(tag, tagRefUser, userDTOS, organizationsByIds), childLevel, null))
                                                     .collect(Collectors.toList()));
                findChild(tagTreeResponse.getChildList(), childLevel + 1, tagParentMap, tagRefUser, userDTOS, organizationsByIds);
            }
        }
    }
}
