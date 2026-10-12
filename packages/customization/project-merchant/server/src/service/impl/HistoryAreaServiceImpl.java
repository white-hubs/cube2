package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.HistoryArea;
import com.guhecloud.cube.deliver.project.merchant.mapper.HistoryAreaMapper;
import com.guhecloud.cube.deliver.project.merchant.service.HistoryAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 空间、区域历史面积 服务实现类
 * </p>
 *
 * @author bq
 * @since 2021-07-14
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HistoryAreaServiceImpl extends ServiceImpl<HistoryAreaMapper, HistoryArea> implements HistoryAreaService {
}
