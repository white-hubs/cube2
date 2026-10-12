package com.guhecloud.cube.biz.linkage.server.linkage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.linkage.dao.LinkageApplicationMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.LinkageApplication;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LinkageApplicationServiceImpl extends ServiceImpl<LinkageApplicationMapper, LinkageApplication> implements LinkageApplicationService {
}
