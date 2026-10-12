package com.guhecloud.cube.srv.gen.work.permit.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.work.permit.server.dao.WorkPermitAssociatedItemMapper;
import com.guhecloud.cube.srv.gen.work.permit.server.entity.WorkPermitAssociatedItem;
import com.guhecloud.cube.srv.gen.work.permit.server.service.WorkPermitAssociatedItemService;
import org.springframework.stereotype.Service;

/**
 * 作业关联项服务实现。
 */
@Service
public class WorkPermitAssociatedItemServiceImpl extends ServiceImpl<WorkPermitAssociatedItemMapper, WorkPermitAssociatedItem>
        implements WorkPermitAssociatedItemService {
}
