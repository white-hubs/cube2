package com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.repair.dao.OutsourceCompanyMapper;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceCompanyPO;
import org.springframework.stereotype.Service;

/**
 * @author pengjian
 * @date 2022/6/30 10:22
 * @description
 */
@Service
public class OutsourceCompanyService
        extends ServiceImpl<OutsourceCompanyMapper, OutsourceCompanyPO>
        implements IService<OutsourceCompanyPO> {
}
