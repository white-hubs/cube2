package com.guhecloud.cube.srv.gen.park.management.server.querygateway.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.srv.gen.park.management.server.dao.RoomRentRecordMapper;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomRentRecordVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomRentRecordPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.RoomRentRecordQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.RoomRentRecordQuery;
import com.guhecloud.cube.srv.gen.park.management.server.service.RoomRentRecordRoomService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomRentRecordQueryServiceImpl extends ServiceImpl<RoomRentRecordMapper, RoomRentRecordPO> implements RoomRentRecordQueryService {

    private final RoomRentRecordRoomService rentRecordRoomService;

    private final FileClient fileClient;

    @Override
    public RoomRentRecordVO getVoById(Long id) {

        RoomRentRecordPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的租赁信息");

        RoomRentRecordVO vo = ObjectUtils.copying(po, RoomRentRecordVO.class);
        // 查询 租赁房间
        vo.setRentRooms(rentRecordRoomService.getParamByRentRecordId(id));
        // 查询 文件
        this.extendFileVOS(vo);

        return vo;
    }

    @Override
    public List<RoomRentRecordVO> getVoList(RoomRentRecordQuery query) {
        List<RoomRentRecordPO> pos = this.getQueryChainWrapper(query).list();
        List<RoomRentRecordVO> vos = ObjectUtils.copying(pos, RoomRentRecordVO.class);

        return vos;
    }

    @Override
    public Pagination<RoomRentRecordVO> getVoPage(RoomRentRecordQuery query) {
        Page<RoomRentRecordPO> page = this.getQueryChainWrapper(query).page(PagingUtils.convert(query));

        List<RoomRentRecordPO> pos = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList());
        List<RoomRentRecordVO> vos = ObjectUtils.copying(pos, RoomRentRecordVO.class);

        return PagingUtils.convert(page, vos);
    }

    private LambdaQueryChainWrapper<RoomRentRecordPO> getQueryChainWrapper(RoomRentRecordQuery query) {
        String signTimeStart = "";
        String signTimeEnd = "";
        String endTimeStart = "";
        String endTimeEnd = "";
        if (StringUtils.isNotEmpty(query.getSignTimePeriod())) {
            String[] split = query.getSignTimePeriod().split(",");
            signTimeStart = split[0];
            signTimeEnd = split[1];
        }
        if (StringUtils.isNotEmpty(query.getEndTimePeriod())) {
            String[] split = query.getSignTimePeriod().split(",");
            endTimeStart = split[0];
            endTimeEnd = split[1];
        }
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getKeyword()), RoomRentRecordPO::getRoomNames, query.getKeyword())
                .eq(null != query.getEnterpriseId(), RoomRentRecordPO::getEnterpriseId, query.getEnterpriseId())
                .between(StringUtils.isNotEmpty(query.getSignTimePeriod()), RoomRentRecordPO::getSignTime, signTimeStart, signTimeEnd)
                .between(StringUtils.isNotEmpty(query.getEndTimePeriod()),
                         RoomRentRecordPO::getEndTime, endTimeStart
                        ,
                         endTimeEnd
                        )
                .orderByDesc(RoomRentRecordPO::getSignTime);
    }

    private void extendFileVOS(RoomRentRecordVO vo) {
        try {
            vo.setFileVOS(fileClient.getByBusinessKey(vo.getId()));
        } catch (Exception e) {
            log.error("查询文件异常", e);
        }

    }
}
