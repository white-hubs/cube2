package com.guhecloud.cube.base.flow.form.querygateway;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.form.dao.FormDefinitionMapper;
import com.guhecloud.cube.base.flow.form.interfaces.vo.FormDefinitionVO;
import com.guhecloud.cube.base.flow.form.po.FormDefinitionPO;
import com.guhecloud.cube.base.flow.form.querygateway.query.FormDefinitionQueryParam;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/10/18
 * @description 表单定义查询
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormDefinitionQueryService extends ServiceImpl<FormDefinitionMapper, FormDefinitionPO> {

    /**
     * 根据表单定义key查询
     * 不指定版本时，查询所有
     *
     * @param queryParam 查询参数
     *
     * @return
     */
    public List<FormDefinitionVO> get(FormDefinitionQueryParam queryParam) {
        List<FormDefinitionPO> definitionPOS = this.lambdaQuery().eq(FormDefinitionPO::getKey, queryParam.getKey())
                .eq(null != queryParam.getVersion(), FormDefinitionPO::getVersion, queryParam.getVersion())
                .orderByDesc(FormDefinitionPO::getVersion)
                .list();
        return ObjectUtils.copying(definitionPOS, FormDefinitionVO.class);
    }

    /**
     * 根据表单定义key查询最新的表单定义json
     *
     * @param formKey 表单定义key
     *
     * @return
     */
    public FormDefinitionVO getLastVersionByKey(String formKey) {
        FormDefinitionVO formDefinitionVO = new FormDefinitionVO();
        if (StringUtils.isEmpty(formKey)) {
            return formDefinitionVO;
        }
        List<FormDefinitionPO> definitionPOS = this.lambdaQuery().eq(FormDefinitionPO::getKey, formKey)
                .orderByDesc(FormDefinitionPO::getVersion)
                .last("limit 1")
                .list();
        if (!CollectionUtils.isEmpty(definitionPOS)) {
            formDefinitionVO.setId(definitionPOS.get(0).getId());
            formDefinitionVO.setJsonSchema(definitionPOS.get(0).getJsonSchema());
            formDefinitionVO.setVersion(definitionPOS.get(0).getVersion());
        }
        return formDefinitionVO;
    }

    public List<FormDefinitionVO> getLastVersionByKeys(List<String> formKeys) {
        return ObjectUtils.copying(this.lambdaQuery().in(FormDefinitionPO::getKey, formKeys)
                                           .orderByDesc(FormDefinitionPO::getVersion).list()
                                           .stream().collect(Collectors.groupingBy(
                        FormDefinitionPO::getKey, // 分组依据key
                        Collectors.maxBy(Comparator.comparing(FormDefinitionPO::getVersion)) // 对每个key保留版本最高的对象
                                                                                  ))
                                           .values() // 获取每个key对应的最大版本对象
                                           .stream() // 转换为Stream
                                           .filter(Optional::isPresent) // 过滤掉没有最大版本的对象（Optional.empty）
                                           .map(Optional::get) // 提取Optional中的值
                                           .collect(Collectors.toList()), FormDefinitionVO.class);
    }
}
