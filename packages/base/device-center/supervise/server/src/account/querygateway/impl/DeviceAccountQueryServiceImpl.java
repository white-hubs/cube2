package com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.dao.DeviceAccountMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.interfaces.vo.DeviceAccountVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceAccountPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.DeviceAccountQueryService;
import com.guhecloud.cube.srv.gen.maintain.client.FaultClient;
import com.guhecloud.cube.srv.gen.maintain.vo.DevcieCountVo;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceAccountQueryServiceImpl extends ServiceImpl<DeviceAccountMapper, DeviceAccountPO> implements DeviceAccountQueryService {

    //    private final HistoricalDeviceInstanceClient historicalDeviceInstanceClient;

    //    private final DeviceInstanceClient deviceInstanceClient;
    private final FaultClient faultClient;

    /*@Override
    public DeviceAccountVO getVOById(Long id) {

        //查询设备台账
        DeviceAccountPO po = this.getById(id);

        //查询设备详情
        DeviceInstanceDetailVO deviceInstanceDetailVO = deviceInstanceClient.detail(id);
        Assert.requireResource(deviceInstanceDetailVO, "无法获取设备实例详情！");

        DeviceAccountVO vo = ObjectUtils.copying(deviceInstanceDetailVO, DeviceAccountVO.class);
        if (po != null) {
            vo.setStatus(po.getStatus());
        }

        this.extendMaintainCost(vo);
        return vo;
    }*/
/*
    @Override
    public Pagination<DeviceAccountPageVO> getPage(DeviceAccountQuery query) {
        // 校验 是否有筛选条件
        boolean hasCondition = this.hasCondition(query);
        // 分页查询设备信息
        Pagination<DeviceAccountPageVO> pageVOPagination = this.getPageVO(hasCondition, query);
        // 扩展查询
        this.extendMaintainCost(pageVOPagination.getRecords());
        return pageVOPagination;
    }*/

   /* @Override
    public List<DeviceAccountPageVO> getList(DeviceAccountQuery query) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        List<DeviceAccountVO> accountVOS = this.baseMapper.getList(userId, Wrappers.query()
                .eq(query.getCollected() != null, "b.user_id", userId)
                .orderByDesc(query.getCollected() == null, "a.create_time")
                .orderByDesc(query.getCollected() != null, "b.time")

        );

        Set<Long> deviceIds = accountVOS.stream().map(DeviceAccountVO::getId).collect(Collectors.toSet());
        query.setDeviceInstanceIds(deviceIds);
        return this.get(query, accountVOS).getRecords();
    }*/

    @Override
    public List<Long> getDeviceIds() {
        List<DeviceAccountPO> pos = this.lambdaQuery()
                .select(DeviceAccountPO::getId)
                .list();
        return pos.stream().map(DeviceAccountPO::getId).collect(Collectors.toList());
    }

    /*private Pagination<DeviceAccountPageVO> getPageVO(boolean hasCondition, DeviceAccountQuery query) {
        Pagination<DeviceAccountPageVO> pageVO;
        Long userId = SecurityContextHolder.getPrincipal().getId();


        // 由于 带条件和不带条件查询 ，分页数据有出入。目前满足当前版本使用所有查询都走 带条件查询。
//        pageVO = this.conditionPageVo(query, userId);

        *//*if (hasCondition) {
            pageVO = this.conditionPageVo(query, userId);
        } else {
            pageVO = this.nonConditionPageVo(query, userId);

        }*//*
        return pageVO;
    }*/

  /*  private Pagination<DeviceAccountPageVO> conditionPageVo(DeviceAccountQuery query, Long userId) {
        List<DeviceAccountVO> pos = this.baseMapper.getList(userId, Wrappers.query()
                .eq(query.getCollected() != null, "b.user_id", userId)
                .orderByDesc(query.getCollected() == null, "a.create_time")
                .orderByDesc(query.getCollected() != null, "b.time")
        );
        Set<Long> deviceIds = pos.stream().map(DeviceAccountVO::getId).collect(Collectors.toSet());
        query.setDeviceInstanceIds(deviceIds);

        return this.get(query, pos);
    }*/

    /*private Pagination<DeviceAccountPageVO> nonConditionPageVo(DeviceAccountQuery query, Long userId) {

        Page<DeviceAccountVO> page = this.baseMapper.getPage(PagingUtils.convert(query), userId, Wrappers.query()
                .eq(query.getCollected() != null, "b.user_id", userId)
                .orderByDesc(query.getCollected() == null, "a.create_time")
                .orderByDesc(query.getCollected() != null, "b.time")
        );
        List<DeviceAccountVO> records = page.getRecords();
        Set<Long> deviceIds = records.stream().map(DeviceAccountVO::getId).collect(Collectors.toSet());
        query.setDeviceInstanceIds(deviceIds);
        query.setPageNumber(1);
        List<DeviceAccountPageVO> vos = this.get(query, records).getRecords();
        return PagingUtils.convert(page, vos);
    }*/


  /*  private boolean hasCondition(DeviceAccountQuery query) {
        if (StringUtils.isNotEmpty(query.getKeyword())) return true;
        if (query.getAreaId() != null) return true;
        if (query.getGenreId() != null) return true;
        return false;
    }*/


   /* private Pagination<DeviceAccountPageVO> get(DeviceAccountQuery query, List<DeviceAccountVO> accountVOS) {
        if (CollectionUtils.isNotEmpty(query.getDeviceInstanceIds())) {

            Pagination<HistoricalDeviceInstanceDTO> instanceDevDTOPage = historicalDeviceInstanceClient.listByIds(query);


            // 设置 是否被收藏
            Map<Long, DeviceAccountVO> map = accountVOS.stream().collect(Collectors.toMap(DeviceAccountVO::getId, Function.identity()));

            List<DeviceAccountPageVO> pageVOS = instanceDevDTOPage.getRecords().stream().map(instanceRangeVO -> {
                DeviceAccountPageVO vo = ObjectUtils.copying(instanceRangeVO, DeviceAccountPageVO.class);
                DeviceAccountVO accountVO = map.getOrDefault(instanceRangeVO.getId(), null);
                if (accountVO != null) {
                    vo.setCreateTime(accountVO.getCreateTime());
                    vo.setCollected(accountVO.getCollected());
                    vo.setStatus(accountVO.getStatus());
                }
                return vo;
            }).filter(Objects::nonNull).collect(Collectors.toList());

            return new Pagination<>(instanceDevDTOPage.getTotal(), instanceDevDTOPage.getPages(), pageVOS);

        }


        return new Pagination<>();

    }*/

    /**
     * 扩充查询设备的 维护成本
     */
    private void extendMaintainCost(DeviceAccountVO vo) {
        Map<Long, Double> map = this.getMaintainCostByApi(Collections.singletonList(vo.getId()));
        if (MapUtils.isNotEmpty(map)) {
            vo.setMaintainCost(map.getOrDefault(vo.getId(), 0D));
        }

    }

    /**
     * 扩充查询设备的 维护成本
     */
   /* private void extendMaintainCost(List<DeviceAccountPageVO> vos) {
        Map<Long, Double> map = this.getMaintainCostByApi(vos.stream().map(HistoricalDeviceInstanceDTO::getId).collect(Collectors.toList()));
        if (MapUtils.isNotEmpty(map)) {
            vos.parallelStream().forEach(vo -> {
                vo.setMaintainCost(map.getOrDefault(vo.getId(), 0D));
            });
        }

    }*/

    /**
     * 扩充查询设备的 维护成本
     */
    private Map<Long, Double> getMaintainCostByApi(List<Long> deviceIds) {
        List<DevcieCountVo> deviceCount = null;
        try {
            deviceCount = faultClient.getDeviceCount(deviceIds);
        } catch (Exception e) {
            log.error("获取运维服务中维护成本异常", e);
        }

        return Optional.ofNullable(deviceCount).orElse(Collections.emptyList()).stream()
                .collect(Collectors.toMap(DevcieCountVo::getDeviceId, vo -> vo.getNum().doubleValue()));

    }

}
