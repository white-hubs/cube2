package com.guhecloud.cube.srv.gen.park.management.server.querygateway.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaListDTO;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.cube.srv.gen.park.management.server.dao.RoomScadaResourcesMapper;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.AreaListVo;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomScadaResourcesVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomScadaResourcesPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.RoomScadaResourcesQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.RoomScadaResourcesQuery;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomScadaResourcesQueryServiceImpl extends ServiceImpl<RoomScadaResourcesMapper, RoomScadaResourcesPO> implements RoomScadaResourcesQueryService {

    private final AreaClient areaClient;

    @Override
    public RoomScadaResourcesVO getVoById(Long id) {
        RoomScadaResourcesPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的房间");
        return ObjectUtils.copying(po, RoomScadaResourcesVO.class);
    }

    @Override
    public List<RoomScadaResourcesVO> getVOList(RoomScadaResourcesQuery query) {
        List<RoomScadaResourcesPO> pos = this.getQueryChainWrapper(query).list();

        List<RoomScadaResourcesVO> vos = ObjectUtils.copying(pos, RoomScadaResourcesVO.class);

        return vos;
    }

    @Override
    public List<AreaListVo> getVOTree(RoomScadaResourcesQuery query) {
        List<RoomScadaResourcesPO> pos = this.getQueryChainWrapper(query)
                .list();

        AreaQueryParam areaQueryParam = new AreaQueryParam();
        List<AreaListDTO> areaListDTOS = areaClient.listInnerArea(areaQueryParam);
        List<AreaListVo> copying = ObjectUtils.copying(areaListDTOS, AreaListVo.class);
        copying.forEach(s -> {
            pos.forEach(a -> {
                if (s.getId().equals(a.getAreaId())) {
                    s.setRoomScadaResourcesId(a.getId());
                    s.setUrl(a.getUrl());
                    s.setEnabled(true);
                }
            });
        });

        List<AreaListVo> areaListVos = TreeUtils.assembleTrees(copying);
        return areaListVos;
    }

    private LambdaQueryChainWrapper<RoomScadaResourcesPO> getQueryChainWrapper(RoomScadaResourcesQuery query) {
        return this.lambdaQuery()
                .eq(null != query.getAreaId(), RoomScadaResourcesPO::getAreaId, query.getAreaId());
    }

}
