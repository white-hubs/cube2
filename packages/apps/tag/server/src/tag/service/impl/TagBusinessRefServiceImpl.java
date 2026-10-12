package com.guhecloud.cube.srv.gen.tag.tag.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.tag.tag.dao.TagBusinessRefMapper;
import com.guhecloud.cube.srv.gen.tag.tag.entity.TagRef;
import com.guhecloud.cube.srv.gen.tag.tag.service.TagBusinessRefService;
import org.springframework.stereotype.Service;

@Service
public class TagBusinessRefServiceImpl extends ServiceImpl<TagBusinessRefMapper, TagRef> implements TagBusinessRefService {
}
