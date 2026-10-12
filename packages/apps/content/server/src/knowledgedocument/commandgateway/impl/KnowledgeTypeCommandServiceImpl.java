package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.KnowledgeTypeCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command.KnowledgeTypeCommand;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao.KnowledgeBaseMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao.KnowledgeTypeMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.KnowledgeBase;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.KnowledgeType;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 知识库类型
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KnowledgeTypeCommandServiceImpl extends ServiceImpl<KnowledgeTypeMapper, KnowledgeType> implements KnowledgeTypeCommandService {

    private final KnowledgeBaseMapper knowledgeBaseMapper;

    /**
     * 新增
     *
     * @param command 文档类型
     *
     * @return id
     */
    @Override
    public Long insert(KnowledgeTypeCommand command) {
        KnowledgeType po = ObjectUtils.copying(command, KnowledgeType.class);
        po.setId(IdWorker.getId());
        this.save(po);
        return po.getId();
    }

    /**
     * 编辑
     *
     * @param command 类型
     *
     * @return 主键
     */
    @Override
    public Long update(KnowledgeTypeCommand command) {
        Assert.isNotFound(command.getId());
        KnowledgeType po = this.getById(command.getId());
        Assert.isNotFound(po, "类型不存在，请确认是否被删除！");
        this.update(Wrappers.<KnowledgeType>lambdaUpdate()
                            .set(KnowledgeType::getName, command.getName())
                            .set(KnowledgeType::getParentId, command.getParentId())
                            .set(KnowledgeType::getDescription, command.getDescription())
                            .set(KnowledgeType::getUpdateTime, DateUtils.now())
                            .set(KnowledgeType::getUpdateBy, SecurityContextHolder.getPrincipal().getAccount())
                            .eq(KnowledgeType::getId, command.getId())
                   );
        return po.getId();
    }

    /**
     * 删除类型
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @Override
    public Boolean remove(Long id) {
        // 查找是否有子集
        KnowledgeType child = this.getOne(Wrappers.<KnowledgeType>lambdaQuery().eq(KnowledgeType::getParentId, id).last(" limit 1"));
        Assert.isNotAcceptable(child != null, "该类型还有子类型，请先清空子类型后再试。");
        StringBuffer sb = new StringBuffer();
        List<KnowledgeBase> knowledgeList = knowledgeBaseMapper.selectList(Wrappers.<KnowledgeBase>lambdaQuery()
                                                                                   .eq(KnowledgeBase::getTypeId, id));
        Optional.ofNullable(knowledgeList).orElse(new ArrayList<>())
                .forEach(knowledgeBase -> sb.append(",").append(knowledgeBase.getName()));
        Assert.isNotAcceptable(CollectionUtils.isNotEmpty(knowledgeList), "该类型还在使用" + sb);
        DatabaseContextHolder.softDelete(this, id);
        return true;
    }
}
