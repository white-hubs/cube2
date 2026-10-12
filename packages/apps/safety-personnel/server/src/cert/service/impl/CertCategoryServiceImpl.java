package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.CertCategory;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.CertType;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.mapper.CertCategoryMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.mapper.CertTypeMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.CertCategoryModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.CertCategoryService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo.CertCategoryTreeVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 证书分类服务实现。
 */
@Service("safetyPersonnelCertCategoryService")
@RequiredArgsConstructor
public class CertCategoryServiceImpl extends ServiceImpl<CertCategoryMapper, CertCategory> implements CertCategoryService {

    private static final int DEFAULT_SORT_NO = 0;

    private final CertTypeMapper certTypeMapper;

    @Override
    public List<CertCategoryTreeVO> tree() {
        List<CertCategory> categories = lambdaQuery()
                .orderByAsc(CertCategory::getLevel)
                .orderByAsc(CertCategory::getSortNo)
                .orderByAsc(CertCategory::getId)
                .list();
        return TreeUtils.assembleTrees(categories.stream().map(CertCategoryTreeVO::of).toList());
    }

    @Override
    @Transactional
    public CertCategoryTreeVO createRoot(CertCategoryModifyParam param) {
        return CertCategoryTreeVO.of(create(null, param));
    }

    @Override
    @Transactional
    public CertCategoryTreeVO createChild(Long parentId, CertCategoryModifyParam param) {
        CertCategory parent = getRequired(parentId);
        return CertCategoryTreeVO.of(create(parent.getId(), param));
    }

    @Override
    @Transactional
    public CertCategoryTreeVO updateCategory(Long id, CertCategoryModifyParam param) {
        CertCategory category = getRequired(id);
        String name = normalizeName(param.getName());
        assertNameNotExists(category.getParentId(), name, id);
        category.setName(name);
        category.setSortNo(Objects.requireNonNullElse(param.getSortNo(), DEFAULT_SORT_NO));
        updateById(category);
        return CertCategoryTreeVO.of(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        CertCategory category = getRequired(id);
        boolean hasChild = lambdaQuery().eq(CertCategory::getParentId, category.getId()).exists();
        Assert.isConflict(hasChild, "存在子分类，不允许删除");
        boolean hasType = certTypeMapper.exists(Wrappers.<CertType>lambdaQuery().eq(CertType::getCategoryId, category.getId()));
        Assert.isConflict(hasType, "分类下存在证书类型，不允许删除");
        DatabaseContextHolder.softDelete(this, category);
    }

    private CertCategory create(Long parentId, CertCategoryModifyParam param) {
        String name = normalizeName(param.getName());
        assertNameNotExists(parentId, name, null);
        CertCategory category = new CertCategory();
        category.setId(IdWorker.getId());
        category.setParentId(parentId);
        category.setName(name);
        category.setSortNo(Objects.requireNonNullElse(param.getSortNo(), DEFAULT_SORT_NO));
        save(category);
        return category;
    }

    private CertCategory getRequired(Long id) {
        CertCategory category = getById(id);
        Assert.isNotFound(category, "证书分类不存在");
        return category;
    }

    private void assertNameNotExists(Long parentId, String name, Long excludeId) {
        boolean exists = lambdaQuery()
                .isNull(Objects.isNull(parentId), CertCategory::getParentId)
                .eq(Objects.nonNull(parentId), CertCategory::getParentId, parentId)
                .eq(CertCategory::getName, name)
                .ne(Objects.nonNull(excludeId), CertCategory::getId, excludeId)
                .exists();
        Assert.isConflict(exists, "同级证书分类名称已存在");
    }

    private String normalizeName(String name) {
        String normalized = name == null ? null : name.trim();
        Assert.isParameterError(normalized == null || normalized.isBlank(), "证书分类名称不能为空");
        return normalized;
    }
}
