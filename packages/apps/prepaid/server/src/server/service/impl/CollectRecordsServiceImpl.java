package com.guhecloud.cube.srv.prepaid.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.prepaid.server.dao.CollectRecordsMapper;
import com.guhecloud.cube.srv.prepaid.server.param.CollectRecordsPageParam;
import com.guhecloud.cube.srv.prepaid.server.po.CollectRecordsPO;
import com.guhecloud.cube.srv.prepaid.server.result.CollectRecordsResult;
import com.guhecloud.cube.srv.prepaid.server.service.CollectRecordsService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 抄表记录服务
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectRecordsServiceImpl extends ServiceImpl<CollectRecordsMapper, CollectRecordsPO> implements CollectRecordsService {

    /**
     * 获取电表抄表记录分页列表
     *
     * @param param 条件对象
     *
     * @return 分页列表
     */
    @Override
    public Pagination<CollectRecordsResult> getCollectRecordsPage(CollectRecordsPageParam param) {

        Page<CollectRecordsPO> page = this.page(PagingUtils.convert(param), Wrappers.<CollectRecordsPO>lambdaQuery()
                                                        .eq(CollectRecordsPO::getMeterId, param.getMeterId())
                                                        .orderByDesc(CollectRecordsPO::getCollectTime)
                                               );

        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords().stream().map(po -> {
            CollectRecordsResult recordsResult = new CollectRecordsResult();
            BeanUtils.copyProperties(po, recordsResult);
            return recordsResult;
        }).collect(Collectors.toList()));
    }
}
