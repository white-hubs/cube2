package com.guhecloud.cube.base.bracing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.bracing.dao.MessageAssemblyNoticeObjMapper;
import com.guhecloud.cube.base.bracing.po.MessageAssemblyNoticeObj;
import com.guhecloud.cube.base.bracing.service.MessageAssemblyNoticeObjService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wql
 * @describe 通知对象
 * @date 2023/9/1 17:51
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageMessageAssemblyNoticeObjServiceImpl extends ServiceImpl<MessageAssemblyNoticeObjMapper, MessageAssemblyNoticeObj> implements MessageAssemblyNoticeObjService {

}
