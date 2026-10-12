package com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeEntityStyleMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeStyleMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeStyleVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeStylePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.query.QrcodeQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author bq
 * @date 2022/7/21
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeStyleQueryService extends ServiceImpl<QrcodeStyleMapper, QrcodeStylePO> {
    private final QrcodeEntityStyleMapper qrcodeEntityStyleMapper;

    public List<QrcodeStyleVO> getList(QrcodeQueryParam param) {
        List<QrcodeStylePO> qrcodeStylePOS = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), QrcodeStylePO::getName, param.getKeyword()).list();
        return ObjectUtils.copying(qrcodeStylePOS, QrcodeStyleVO.class);
    }

    public Pagination<QrcodeStyleVO> getPage(QrcodeQueryParam param) {
        Page<QrcodeStylePO> page = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), QrcodeStylePO::getName, param.getKeyword())
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, QrcodeStyleVO.class);
    }

    /**
     * 获取实体类型的默认样式
     *
     * @param entityType 实体类型
     *
     * @return 默认样式id
     */
    public Long getDefaultStyleIdByEntityType(String entityType) {
        return qrcodeEntityStyleMapper.getDefaultStyleIdByEntityType(entityType);
    }
}
