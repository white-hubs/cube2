package com.guhecloud.cube.srv.gen.park.management.server.querygateway.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.srv.gen.park.management.server.dao.RoomMapper;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomDeletedVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomEnterpriseVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomPO;
import com.guhecloud.cube.srv.gen.park.management.server.po.TypeDataPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.EnterpriseQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.RoomQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.TypeDataQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.RoomQuery;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomQueryServiceImpl extends ServiceImpl<RoomMapper, RoomPO> implements RoomQueryService {

    private final TypeDataQueryService typeDataQueryService;
    private final FileClient fileClient;
    private final AreaClient areaClient;
    private final RoomMapper roomMapper;
    private final EnterpriseQueryService enterpriseQueryService;

    @Override
    public RoomVO getVoById(Long id) {
        RoomPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的房间");
        RoomVO vo = ObjectUtils.copying(po, RoomVO.class);
        // 查询 类型名称
        this.setRoomTypeName(vo);
        // 查询 文件
        this.extendFileVOS(vo);

        return vo;
    }

    @Override
    public List<RoomVO> getVOList(RoomQuery query) {
        List<RoomPO> pos = this.getQueryChainWrapper(query).list();
        List<RoomVO> vos = ObjectUtils.copying(pos, RoomVO.class);

        return vos;
    }

    @Override
    public List<RoomVO> tenantRoom() {

        return roomMapper.tenantRoom(SessionContextHolder.getRequester().getTenant());
    }

    @Override
    public Pagination<RoomVO> getVOPage(RoomQuery query) {
        Page<RoomPO> page = this.getQueryChainWrapper(query).page(PagingUtils.convert(query));
        List<RoomPO> pos = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList());
        List<RoomVO> vos = ObjectUtils.copying(pos, RoomVO.class);
        vos.parallelStream().forEach(this::setRoomTypeName);
        vos.forEach(s -> {
            if (s.getAreaSequence() != null) {
                String[] split = s.getAreaSequence().split(",");
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < split.length; i++) {
                    String areaNameById = areaClient.getAreaNameById(Long.valueOf(split[(split.length - 1) - i]), SessionContextHolder
                            .getRequester().getTenant());
                    if ((split.length) > i && i != 0) {
                        stringBuilder.append("/").append(areaNameById);
                    } else {
                        stringBuilder.append(areaNameById);
                    }
                }

                s.setAreaName(stringBuilder.toString());
            }

        });
        return PagingUtils.convert(page, vos);
    }

    @Override
    public RoomEnterpriseVO getRoomEnterpriseVO(Long id, String name) {

        Assert.isParameterError((null == id && StringUtils.isEmpty(name)), "id或name，至少有一个不能为空");

        RoomPO po = this.lambdaQuery()
                .eq(null != id, RoomPO::getId, id)
                .eq(StringUtils.isNotEmpty(name), RoomPO::getName, name)
                .one();

        Assert.isNotFound(po, "查不到房间信息");

        RoomEnterpriseVO vo = ObjectUtils.copying(po, RoomEnterpriseVO.class);

        // 查询 类型名称
        this.setRoomTypeName(vo);
        // 查询 文件
        this.extendFileVOS(vo);

        // 查询所关联的企业信息
        this.extendEnterpriseVO(vo);

        return vo;
    }

    @Override
    public List<RoomDeletedVO> tenantRoomByIds(List<Long> ids) {

        return roomMapper.tenantRoomByIds(SessionContextHolder.getRequester().getTenant(), ids);
    }

    @Override
    public List<RoomDeletedVO> tenantStopRoomById(List<Long> ids) {
        return roomMapper.tenantStopRoomById(SessionContextHolder.getRequester().getTenant(), ids);
    }

    private LambdaQueryChainWrapper<RoomPO> getQueryChainWrapper(RoomQuery query) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getKeyword()), RoomPO::getName, query.getKeyword())
                .eq(null != query.getAreaId(), RoomPO::getAreaId, query.getAreaId())
                .eq(null != query.getRoomTypeId(), RoomPO::getRoomTypeId, query.getRoomTypeId())
                .eq(StringUtils.isNotEmpty(query.getStatus()), RoomPO::getStatus, query.getStatus())
                .gt(null != query.getRent(), RoomPO::getEnterpriseId, query.getRent())
                .between(query.getPeriod() !=
                         null, BasicEntity::getCreateTime, ObjectUtils.ifNull(query.getPeriod(), Period::getBegin), ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .orderByDesc(BasicEntity::getCreateTime);
    }

    private void setRoomTypeName(RoomVO vo) {
        if (vo != null) {
            TypeDataPO typeDataPO = typeDataQueryService.getById(vo.getRoomTypeId());
            vo.setRoomTypeName(typeDataPO != null ? typeDataPO.getName() : "");
        }
    }

    private void extendFileVOS(RoomVO vo) {
        try {
            vo.setFileVOS(fileClient.getByBusinessKey(vo.getId()));
        } catch (Exception e) {
            log.error("查询文件异常", e);
        }

    }

    private void extendEnterpriseVO(RoomEnterpriseVO vo) {
        if (null != vo.getEnterpriseId()) {
            vo.setEnterpriseVO(enterpriseQueryService.getVoById(vo.getEnterpriseId()));
        }
    }

}
