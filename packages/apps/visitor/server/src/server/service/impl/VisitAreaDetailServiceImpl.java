package com.guhecloud.cube.srv.gen.visitor.management.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.VisitAreaDetailMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitAreaDetailPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitAreaDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 区域详情
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitAreaDetailServiceImpl extends ServiceImpl<VisitAreaDetailMapper, VisitAreaDetailPO> implements VisitAreaDetailService {
}
