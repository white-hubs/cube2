package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerCategoryMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerCategory;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.HiddenDangerCategoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lzy
 * @date 2022/4/26
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerCategoryQueryServiceImpl extends ServiceImpl<HiddenDangerCategoryMapper, HiddenDangerCategory> implements HiddenDangerCategoryQueryService {

}
