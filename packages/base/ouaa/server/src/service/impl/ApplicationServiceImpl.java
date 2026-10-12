package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.ApplicationMapper;
import com.guhecloud.cube.biz.ouaa.server.dao.TenantApplicationMapper;
import com.guhecloud.cube.biz.ouaa.server.dto.ApplicationDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.ApplicationMarketDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationExtend;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationGroup;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantApplication;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationBatchModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationExtendInfo;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationExtendService;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationGroupService;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationIterationRecordService;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.ProductApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalApplicationService;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationVO;
import com.guhecloud.cube.biz.ouaa.server.vo.BaseInfo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements ApplicationService {

    private final TerminalApplicationService terminalApplicationService;
    private final ProductApplicationService productApplicationService;
    private final ApplicationGroupService applicationGroupService;
    private final ApplicationExtendService applicationExtendService;
    private final ApplicationIterationRecordService applicationIterationRecordService;
    private final TenantApplicationMapper tenantApplicationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull ApplicationModifyParam param) {
        Long id = IdWorker.getId();
        Application application = ObjectUtils.copying(param, Application.class);
        application.setId(id);
        this.baseMapper.insert(application);
        // 保存扩展信息
        applicationExtendService.saveOrUpdate(id, param.getExtendParam());
        //保存应用和端的绑定关系
        terminalApplicationService.saveOrUpdateBatch(id, param.getTerminalIdList());
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull ApplicationModifyParam param) {
        Application application = this.getById(id);
        Assert.isNotFound(application, "应用不存在");
        ObjectUtils.copying(param, application);
        applicationExtendService.saveOrUpdate(id, param.getExtendParam());
        terminalApplicationService.saveOrUpdateBatch(id, param.getTerminalIdList());
        this.baseMapper.updateById(application);
    }

    @Override
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
        DatabaseContextHolder.softDelete(applicationExtendService, id);
        // 删除访问端应用关联
        terminalApplicationService.removeByApplication(id);
        // 删除产品应用关联
        productApplicationService.removeByApplication(id);
    }

    @Override
    public Application getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public Application getByCode(@NonNull String code) {
        return this.baseMapper.selectOne(Wrappers.<Application>lambdaQuery().eq(Application::getCode, code));
    }

    @Override
    public List<Application> listByIds(@NonNull Collection<? extends Serializable> idList) {
        return idList.isEmpty() ? Collections.emptyList() : this.baseMapper.selectBatchIds(idList);
    }

    @Override
    public List<Application> listByIds(@NonNull Collection<? extends Serializable> idList, String keyword) {
        return idList.isEmpty() ? Collections.emptyList() : this.lambdaQuery().in(Application::getId, idList)
                .like(StringUtils.isNotEmpty(keyword), Application::getName, keyword)
                .list();
    }

    @Override
    public Page<Application> listByPage(@NonNull ApplicationQueryParam param) {
        return this.lambdaQuery().eq(Objects.nonNull(param.getEnabled()), Application::getEnabled, param.getEnabled())
                .eq(Objects.nonNull(param.getApplicationGroupId()), Application::getApplicationGroupId, param.getApplicationGroupId())
                .like(!StringUtils.isEmpty(param.getKeyword()), Application::getName, param.getKeyword())
                .in(CollectionUtils.isNotEmpty(param.getApplicationIdList()), Application::getId, param.getApplicationIdList())
                .orderByDesc(Application::getCreateTime).page(PagingUtils.convert(param));
    }

    @Override
    public ApplicationDTO getApplicationByCode(String code) {
        Application one = this.getOne(Wrappers.<Application>lambdaQuery()
                                              .eq(Application::getCode, code));
        return ObjectUtils.copying(one, ApplicationDTO.class);
    }

    @Override
    public List<ApplicationDTO> getApplicationsByCodes(List<String> codes) {
        List<Application> list = this.list(Wrappers.<Application>lambdaQuery()
                                                   .in(Application::getCode, codes));
        return ObjectUtils.copying(list, ApplicationDTO.class);
    }

    @Override
    public List<ApplicationDTO> getApplications() {
        List<Application> list = this.baseMapper.selectList(null);
        return ObjectUtils.copying(list, ApplicationDTO.class);
    }

    @Override
    public ApplicationVO getApplicationDetailById(Long id) {
        Application application = this.getById(id);
        Assert.isNotFound(application, "不存在应用信息");
        ApplicationVO applicationVO = ObjectUtils.copying(application, ApplicationVO.class);
        // 查询产品线信息
        ApplicationGroup applicationGroup = this.applicationGroupService.getById(applicationVO.getApplicationGroupId());
        if (Objects.nonNull(applicationGroup)) {
            applicationVO.setApplicationGroupName(applicationGroup.getName());
        }
        ApplicationExtend applicationExtend = applicationExtendService.getById(application.getId());
        if (Objects.nonNull(applicationExtend)) {
            applicationVO.setApplicationExtendInfo(ObjectUtils.copying(applicationExtend, ApplicationExtendInfo.class));
        }
        //查询端
        applicationVO.setTerminalList(terminalApplicationService.getTerminalList(id));
        // 查询依赖应用
        if (StringUtils.isNotBlank(application.getDependAppIds())) {
            applicationVO.setDependAppList(this.lambdaQuery()
                                                   .in(Application::getId, Arrays.stream(application.getDependAppIds().split(","))
                                                           .collect(Collectors.toList()))
                                                   .list()
                                                   .stream().map(app -> ObjectUtils.copying(app, BaseInfo.class))
                                                   .collect(Collectors.toList()));
        }

        return applicationVO;
    }

    @Override
    public Pagination<ApplicationPageVO> getApplicationPage(ApplicationQueryParam param) {
        Page<Application> page = this.listByPage(param);
        List<ApplicationPageVO> applications = ObjectUtils.copying(page.getRecords(), ApplicationPageVO.class);
        if (CollectionUtils.isEmpty(applications)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        // 关联应用组信息
        Set<Long> applicationGroupIds = page.getRecords().stream().map(Application::getApplicationGroupId).filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<Long> applicationIds = page.getRecords().stream().map(Application::getId).filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Long, ApplicationGroup> applicationGroupMap = this.applicationGroupService.listByIds(applicationGroupIds).stream()
                .collect(Collectors.toMap(ApplicationGroup::getId, obj -> obj, (o, n) -> n));
        Map<Long, String> applicationVersionMap = applicationIterationRecordService.getApplicationVersion(applicationIds);
        applications.forEach(application -> {
            ApplicationGroup applicationGroup = applicationGroupMap.get(application.getApplicationGroupId());
            if (Objects.nonNull(applicationGroup)) {
                application.setApplicationGroupName(applicationGroup.getName());
            }
            application.setVersion(applicationVersionMap.get(application.getId()));
        });
        return PagingUtils.convert(page, applications);
    }

    @Override
    public List<BaseInfo> getList(ApplicationQueryParam param) {
        List<Application> applicationList = getApplicationList(param);
        if (CollectionUtils.isEmpty(applicationList)) {
            return null;
        }
        List<Long> applicationIds = applicationList.stream().map(Application::getId).distinct().collect(Collectors.toList());
        Map<Long, String> applicationVersionMap = applicationIterationRecordService.getApplicationVersion(applicationIds);
        return applicationList.stream().map(t -> {
            BaseInfo info = new BaseInfo();
            info.setId(t.getId());
            info.setName(t.getName());
            info.setVersion(applicationVersionMap.get(t.getId()));
            return info;
        }).collect(Collectors.toList());
    }

    private List<Application> getApplicationList(ApplicationQueryParam param) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(param.getApplicationGroupId()), Application::getApplicationGroupId, param.getApplicationGroupId())
                .like(StringUtils.isNotBlank(param.getKeyword()), Application::getName, param.getKeyword())
                .eq(Objects.nonNull(param.getEnabled()), Application::getEnabled, param.getEnabled())
                .eq(Objects.nonNull(param.getPublicAccess()), Application::getPublicAccess, param.getPublicAccess())
                .list();
    }

    @Override
    public List<ApplicationMarketDTO> getApplicationMarketList(ApplicationQueryParam param) {
        List<Application> applicationList = getApplicationList(param);
        if (CollectionUtils.isEmpty(applicationList)) {
            return Collections.emptyList();
        }
        return getApplicationMarkets(applicationList);
    }

    private List<ApplicationMarketDTO> getApplicationMarkets(List<Application> applicationList) {
        return applicationList.stream().map(t -> {
            ApplicationMarketDTO applicationMarketDTO = ObjectUtils.copying(t, ApplicationMarketDTO.class);
            applicationMarketDTO.setDescription(t.getRemark());
            return applicationMarketDTO;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(ApplicationBatchModifyParam param) {
        if (CollectionUtils.isEmpty(param.getApplicationIds()) || (Objects.isNull(param.getEnabled())
                                                                   && Objects.isNull(param.getApplicationGroupId()) &&
                                                                   Objects.isNull(param.getPublicAccess()))) {
            return;
        }
        this.lambdaUpdate().set(Objects.nonNull(param.getEnabled()), Application::getEnabled, param.getEnabled())
                .set(Objects.nonNull(param.getPublicAccess()), Application::getPublicAccess, param.getPublicAccess())
                .set(Objects.nonNull(param.getApplicationGroupId()), Application::getApplicationGroupId, param.getApplicationGroupId())
                .set(Application::getUpdateTime, new Date())
                .in(Application::getId, param.getApplicationIds())
                .update();

    }

    @Override
    public List<ApplicationMarketDTO> getRecommendApplication(ApplicationQueryParam param) {
        List<Application> applicationList = getApplicationList(param);
        if (CollectionUtils.isEmpty(applicationList)) {
            return Collections.emptyList();
        }
        return getApplicationMarkets(this.GetRandomThreeInfoList(applicationList, 3));
    }

    @Override
    public Pagination<ApplicationPageVO> getMyApplication(ApplicationQueryParam param) {
        //租户下的应用
        List<TenantApplication> tenantApplications = this.tenantApplicationMapper.selectList(Wrappers.<TenantApplication>lambdaQuery()
                                                                                                     .eq(TenantApplication::getTenant, SessionContextHolder
                                                                                                             .getRequester().getTenant()));
        if (CollectionUtils.isEmpty(tenantApplications)) {
            return new Pagination<>();
        }
        param.setApplicationIdList(tenantApplications.stream().map(TenantApplication::getApplicationId).collect(Collectors.toList()));
        return getApplicationPage(param);
    }

    public <T> List<T> GetRandomThreeInfoList(List<T> list, int count) {
        List<T> olist = new ArrayList<>();
        if (list.size() <= count) {
            return list;
        } else {
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                int intRandom = random.nextInt(list.size() - 1);
                olist.add(list.get(intRandom));
                list.remove(list.get(intRandom));
            }
            return olist;
        }
    }
}
