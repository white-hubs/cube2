package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.facade;

import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.AreaOptionVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MdataAreaFacadeService {
    private final AreaClient areaClient;

    public AreaDTO getBySequence(String areaSequence) {
        Assert.isParameterError(StringUtils.isBlank(areaSequence), "区域不能为空");
        AreaDTO area = areaClient.getAreaBySequence(areaSequence.trim());
        Assert.isParameterError(area == null, "区域不存在");
        Assert.isParameterError(Boolean.FALSE.equals(area.getEnabled()), "区域已停用");
        return area;
    }

    public List<AreaOptionVO> listOptions(String keyword) {
        AreaQueryParam queryParam = new AreaQueryParam();
        queryParam.setKeyword(keyword);
        List<AreaDTO> areas = areaClient.getAreas(queryParam);
        return ObjectUtils.copying(areas, AreaOptionVO.class);
    }
}
