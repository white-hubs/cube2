package com.guhecloud.cube.base.flow.form.querygateway.query;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.form.dao.FormControlBusinessMapper;
import com.guhecloud.cube.base.flow.form.po.FormControlBusinessPO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bq
 * @date 2022/10/28
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormControlBusinessQueryService extends ServiceImpl<FormControlBusinessMapper, FormControlBusinessPO> {

}
