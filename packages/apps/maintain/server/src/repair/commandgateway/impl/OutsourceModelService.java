package com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.repair.dao.OutsourceModelMapper;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceModelPO;
import org.springframework.stereotype.Service;

/**
 * @author pengjian
 * @date 2022/6/30 10:20
 * @description
 */
@Service
public class OutsourceModelService
        extends ServiceImpl<OutsourceModelMapper, OutsourceModelPO>
        implements IService<OutsourceModelPO> {
}
