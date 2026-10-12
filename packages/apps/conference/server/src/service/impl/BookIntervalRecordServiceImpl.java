package com.guhecloud.cube.srv.gen.conferencemanage.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.BookIntervalRecordMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookIntervalRecord;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.BookIntervalRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookIntervalRecordServiceImpl extends ServiceImpl<BookIntervalRecordMapper, BookIntervalRecord> implements BookIntervalRecordService {
}
