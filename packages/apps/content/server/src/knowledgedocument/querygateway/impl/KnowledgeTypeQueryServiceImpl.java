package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao.KnowledgeTypeMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KnowledgeTypeTreeVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.KnowledgeType;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.KnowledgeTypeQueryService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query.KnowledgeTypeQuery;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.database.util.EntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 知识文档类型查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KnowledgeTypeQueryServiceImpl extends ServiceImpl<KnowledgeTypeMapper, KnowledgeType> implements KnowledgeTypeQueryService {

    private final KnowledgeTypeMapper knowledgeTypeMapper;

    /**
     * 树形类型列表
     *
     * @param query 条件
     *
     * @return 树形列表
     */
    @Override
    public List<KnowledgeTypeTreeVO> getKnowledgeTypeTree(KnowledgeTypeQuery query) {
        // 根据条件查询数据
        List<KnowledgeType> source = knowledgeTypeMapper.selectList(Wrappers.<KnowledgeType>lambdaQuery()
                                                                            .like(StringUtils.isNotBlank(query.getName()), KnowledgeType::getName, query.getName())
                                                                   );
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        /* 获取上级 */
        // 获取所有序列号
        List<String> sequences = EntityUtils.getAllSequences(source);
        // 获取所包含上级的数据
        List<KnowledgeType> list = knowledgeTypeMapper.getBySequences(sequences);

        // 转换返回对象
        List<KnowledgeTypeTreeVO> voList = ObjectUtils.copying(list, KnowledgeTypeTreeVO.class);
        // 返回树形
        return TreeUtils.assembleTrees(voList);

    }
}
