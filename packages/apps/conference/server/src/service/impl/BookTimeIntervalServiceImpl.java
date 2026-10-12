package com.guhecloud.cube.srv.gen.conferencemanage.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.BookTimeIntervalMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookTimeInterval;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.BookTimeIntervalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookTimeIntervalServiceImpl extends ServiceImpl<BookTimeIntervalMapper, BookTimeInterval> implements BookTimeIntervalService {
}
