package com.guhecloud.cube.base.bracing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.bracing.dao.MessageAssemblyNoticeWayMapper;
import com.guhecloud.cube.base.bracing.po.MessageAssemblyNoticeWay;
import com.guhecloud.cube.base.bracing.service.MessageAssemblyNoticeWayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wql
 * @describe TODO
 * @date 2023/9/1 17:53
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageMessageAssemblyNoticeWayServiceImpl extends ServiceImpl<MessageAssemblyNoticeWayMapper, MessageAssemblyNoticeWay> implements MessageAssemblyNoticeWayService {

}
