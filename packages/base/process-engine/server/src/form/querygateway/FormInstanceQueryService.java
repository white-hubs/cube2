package com.guhecloud.cube.base.flow.form.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.form.dao.FormInstanceDataMapper;
import com.guhecloud.cube.base.flow.form.dao.FormInstanceMapper;
import com.guhecloud.cube.base.flow.form.interfaces.vo.FormDefinitionVO;
import com.guhecloud.cube.base.flow.form.interfaces.vo.FormInstanceDataVO;
import com.guhecloud.cube.base.flow.form.interfaces.vo.FormInstanceDetailVO;
import com.guhecloud.cube.base.flow.form.po.FormInstanceDataPO;
import com.guhecloud.cube.base.flow.form.po.FormInstancePO;
import com.guhecloud.cube.base.flow.form.querygateway.query.FormDefinitionQueryParam;
import com.guhecloud.cube.base.flow.form.querygateway.query.FormInstanceQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author bq
 * @date 2022/09/08
 * @description 表单实例查询
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class FormInstanceQueryService extends ServiceImpl<FormInstanceMapper, FormInstancePO> {

    private final FormInstanceDataMapper formInstanceDataMapper;
    private final FormDefinitionQueryService formDefinitionQueryService;

    /**
     * ID查询
     *
     * @param id 表单实例ID
     *
     * @return
     */
    public FormInstanceDetailVO get(Long id) {
        long startTime = System.currentTimeMillis();
        FormInstancePO formInstancePO = this.getById(id);
        Assert.isNotFound(formInstancePO, "表单实例不存在");
        FormInstanceDetailVO detailVO = ObjectUtils.copying(formInstancePO, FormInstanceDetailVO.class);
        this.extend(detailVO);
        log.info("表单实例查询耗时:{}", System.currentTimeMillis() - startTime);
        return detailVO;
    }

    /**
     * 列表查询
     *
     * @param param 查询参数
     *
     * @return
     */
    public List<FormInstanceDetailVO> getList(FormInstanceQueryParam param) {
        List<FormInstancePO> formInstancePOS = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getBusinessType()), FormInstancePO::getBusinessType, param.getBusinessType())
                .eq(StringUtils.isNotEmpty(param.getBusinessKey()), FormInstancePO::getBusinessKey, param.getBusinessKey())
                .list();
        return ObjectUtils.copying(formInstancePOS, FormInstanceDetailVO.class);
    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     *
     * @return
     */
    public Pagination<FormInstanceDetailVO> getPage(FormInstanceQueryParam param) {
        Page<FormInstancePO> page = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getBusinessType()), FormInstancePO::getBusinessType, param.getBusinessType())
                .eq(StringUtils.isNotEmpty(param.getBusinessKey()), FormInstancePO::getBusinessKey, param.getBusinessKey())
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, FormInstanceDetailVO.class);
    }

    /**
     * 根据业务ID查询绑定的表单信息
     *
     * @param businessType 业务类型
     * @param businessKey  业务key
     *
     * @return
     */
    public FormInstanceDetailVO getByBusiness(String businessType, String businessKey) {
        FormInstancePO instancePO = this.lambdaQuery().eq(FormInstancePO::getBusinessType, businessType)
                .eq(FormInstancePO::getBusinessKey, businessKey).one();
        Assert.isNotFound(instancePO, "未查询到相关的表单信息");
        FormInstanceDetailVO instanceDetailVO = ObjectUtils.copying(instancePO, FormInstanceDetailVO.class);
        this.extend(instanceDetailVO);
        return instanceDetailVO;
    }

    private void extend(FormInstanceDetailVO detailVO) {
        // 查询表单定义
        List<FormDefinitionVO> formDefinitionVOS = formDefinitionQueryService.get(new FormDefinitionQueryParam(detailVO.getFormKey(), detailVO.getVersion()));
        detailVO.setFormDefinitionVO(!formDefinitionVOS.isEmpty() ? formDefinitionVOS.getFirst() : new FormDefinitionVO());
        // 查询表单实例的数据
        FormInstanceDataPO instanceDataPO = formInstanceDataMapper.selectOne(Wrappers.<FormInstanceDataPO>lambdaQuery()
                                                                                     .eq(FormInstanceDataPO::getFormInstanceId, detailVO.getId()));
        detailVO.setFormInstanceDataVO(ObjectUtils.copying(instanceDataPO, FormInstanceDataVO.class));
    }

    /**
     * 根据实例ID查询定义ID
     *
     * @param id
     *
     * @return
     */
    public Long getDefinitionId(Long id) {
        FormInstancePO instancePO = this.getById(id);
        if (Objects.nonNull(instancePO)) {
            List<FormDefinitionVO> formDefinitionVOS = formDefinitionQueryService.get(new FormDefinitionQueryParam(instancePO.getFormKey(), instancePO.getVersion()));
            return !CollectionUtils.isEmpty(formDefinitionVOS) ? formDefinitionVOS.get(0).getId() : null;
        }
        return null;
    }

}
