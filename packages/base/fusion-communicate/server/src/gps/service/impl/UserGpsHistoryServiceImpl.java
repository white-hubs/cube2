package com.guhecloud.cube.biz.fusion.communicate.server.gps.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.mapper.UserGpsHistoryMapper;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.param.UploadGpsParam;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.param.UserGpsHistoriesQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.po.UserGpsHistoryPO;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.service.UserGpsHistoryService;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.vo.UserGpsHistoriesVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserGpsHistoryServiceImpl extends ServiceImpl<UserGpsHistoryMapper, UserGpsHistoryPO> implements UserGpsHistoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long uploadUserGps(UploadGpsParam param) {
        LocalDateTime uploadTime = DateUtils.now();
        UserGpsHistoryPO userLastGpsHistory = this.baseMapper
                .selectUserLastGpsHistory(SecurityContextHolder.getPrincipal().getId());
        //是否为连续坐标点
        if (isContinuousGps(param, userLastGpsHistory)) {
            userLastGpsHistory.setLastUploadTime(uploadTime);
            this.baseMapper.updateById(userLastGpsHistory);
            return 0L;
        }
        //直接存入
        UserGpsHistoryPO insertUserGpsHistory = ObjectUtils.copying(param, UserGpsHistoryPO.class);
        insertUserGpsHistory.setUploadTime(uploadTime);
        insertUserGpsHistory.setUserId(SecurityContextHolder.getPrincipal().getId());
        insertUserGpsHistory.setLastUploadTime(uploadTime);
        this.baseMapper.insert(insertUserGpsHistory);
        return insertUserGpsHistory.getId();
    }

    @Override
    public UserGpsHistoriesVO getUserLastGpsHistory(Long userId) {
        UserGpsHistoryPO userGpsHistoryPO = this.baseMapper
                .selectUserLastGpsHistory(userId);
        return ObjectUtils.copying(userGpsHistoryPO, UserGpsHistoriesVO.class);
    }

    @Override
    public List<UserGpsHistoriesVO> getUserLastGpsHistories(List<Long> userIds) {
        List<UserGpsHistoryPO> userGpsHistoryPOS = this.baseMapper.selectUserLastGpsHistories(userIds);
        return ObjectUtils.copying(userGpsHistoryPOS, UserGpsHistoriesVO.class);
    }

    @Override
    public Pagination<UserGpsHistoriesVO> getGpsHistories(UserGpsHistoriesQueryParam param) {
        Page<UserGpsHistoryPO> userGpsHistoriesVOPage = this.baseMapper
                .selectPage(PagingUtils.convert(param), Wrappers.<UserGpsHistoryPO>lambdaQuery()
                        .eq(Objects.nonNull(param.getUserId()), UserGpsHistoryPO::getUserId, param.getUserId())
                        .between(Objects.nonNull(param.getPeriod()), UserGpsHistoryPO::getUploadTime, ObjectUtils
                                .ifNull(param.getPeriod(), Period::getBegin), ObjectUtils
                                         .ifNull(param.getPeriod(), Period::getEnd))
                        .orderByDesc(UserGpsHistoryPO::getUploadTime));
        return PagingUtils.convert(userGpsHistoriesVOPage, UserGpsHistoriesVO.class);
    }

    public boolean isContinuousGps(UploadGpsParam param, UserGpsHistoryPO userLastGpsHistory) {
        if (Objects.isNull(userLastGpsHistory)) {
            return false;
        }
        //过滤掉连续的重复坐标数据(后续替换成计算2个坐标点的直线距离进行过滤)
        return userLastGpsHistory.getLat().equals(param.getLat())
               && userLastGpsHistory.getLng().equals(param.getLng());
    }
}
