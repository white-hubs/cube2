package com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.dao.BedFileMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.entity.BedFile;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.service.BedFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BedFileServiceImpl extends ServiceImpl<BedFileMapper, BedFile> implements BedFileService {
}
