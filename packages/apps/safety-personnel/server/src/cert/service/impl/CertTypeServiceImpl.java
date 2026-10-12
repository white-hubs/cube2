package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.CertCategory;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.CertType;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.EmployeeCert;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.mapper.CertTypeMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.mapper.EmployeeCertMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.CertTypeModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.CertTypeQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.CertCategoryService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.CertTypeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo.CertTypeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 证书类型服务实现。
 */
@Service("safetyPersonnelCertTypeService")
@RequiredArgsConstructor
public class CertTypeServiceImpl extends ServiceImpl<CertTypeMapper, CertType> implements CertTypeService {

    private final CertCategoryService certCategoryService;
    private final EmployeeCertMapper employeeCertMapper;

    @Override
    public Pagination<CertTypeVO> pageTypes(CertTypeQueryParam param) {
        Page<CertType> page = lambdaQuery()
                .eq(Objects.nonNull(param.getCategoryId()), CertType::getCategoryId, param.getCategoryId())
                .like(StringUtils.isNotBlank(param.getKeyword()), CertType::getTypeName, param.getKeyword())
                .orderByDesc(CertType::getCreateTime)
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, page.getRecords().stream().map(this::toVO).toList());
    }

    private CertTypeVO toVO(CertType type) {
        CertTypeVO vo = CertTypeVO.of(type);
        CertCategory category = certCategoryService.getById(type.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }
        return vo;
    }

    @Override
    public CertTypeVO detail(Long id) {
        return toVO(getRequired(id));
    }

    @Override
    @Transactional
    public CertTypeVO create(CertTypeModifyParam param) {
        checkCategory(param.getCategoryId());
        String typeName = normalizeName(param.getTypeName());
        assertNameNotExists(param.getCategoryId(), typeName, null);
        CertType type = new CertType();
        type.setId(IdWorker.getId());
        fill(type, param, typeName);
        save(type);
        return toVO(type);
    }

    @Override
    @Transactional
    public CertTypeVO updateType(Long id, CertTypeModifyParam param) {
        CertType type = getRequired(id);
        checkCategory(param.getCategoryId());
        String typeName = normalizeName(param.getTypeName());
        assertNameNotExists(param.getCategoryId(), typeName, id);
        fill(type, param, typeName);
        updateById(type);
        return toVO(type);
    }

    @Override
    @Transactional
    public void deleteType(Long id) {
        CertType type = getRequired(id);
        boolean hasCert = employeeCertMapper.exists(Wrappers.<EmployeeCert>lambdaQuery().eq(EmployeeCert::getTypeId, id));
        Assert.isConflict(hasCert, "证书类型已被引用，不允许删除");
        DatabaseContextHolder.softDelete(this, type);
    }

    private void fill(CertType type, CertTypeModifyParam param, String typeName) {
        type.setCategoryId(param.getCategoryId());
        type.setTypeName(typeName);
        type.setDefinition(param.getDefinition());
        type.setValidPeriod(param.getValidPeriod());
        type.setValidPeriodUnit(param.getValidPeriodUnit());
        type.setWarnDays(param.getWarnDays());
        type.setNeedReview(Boolean.TRUE.equals(param.getNeedReview()));
        type.setReviewPeriod(param.getReviewPeriod());
        type.setReviewPeriodUnit(param.getReviewPeriodUnit());
    }

    private void checkCategory(Long categoryId) {
        CertCategory category = certCategoryService.getById(categoryId);
        Assert.isNotFound(category, "证书分类不存在");
    }

    private CertType getRequired(Long id) {
        CertType type = getById(id);
        Assert.isNotFound(type, "证书类型不存在");
        return type;
    }

    private void assertNameNotExists(Long categoryId, String typeName, Long excludeId) {
        boolean exists = lambdaQuery()
                .eq(CertType::getCategoryId, categoryId)
                .eq(CertType::getTypeName, typeName)
                .ne(Objects.nonNull(excludeId), CertType::getId, excludeId)
                .exists();
        Assert.isConflict(exists, "同一分类下证书类型名称已存在");
    }

    private String normalizeName(String name) {
        String normalized = name == null ? null : name.trim();
        Assert.isParameterError(normalized == null || normalized.isBlank(), "证书类型名称不能为空");
        return normalized;
    }
}
