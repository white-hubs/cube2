package com.guhecloud.cube.biz.door.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.door.server.entity.Card;
import com.guhecloud.cube.biz.door.server.vo.CardStatByStatusVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("door-card-mapper")
public interface CardMapper extends BaseMapper<Card> {
    @InterceptorIgnore(illegalSql = "true", tenantLine = "true")
    void insertOrUpdateBatch(List<Card> dataList);

    /**
     * 按使用状态统计数量
     *
     * @return
     */
    CardStatByStatusVO getCardStatByStatus();
}
