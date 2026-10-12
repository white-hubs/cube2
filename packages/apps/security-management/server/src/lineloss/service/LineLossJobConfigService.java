package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossJobConfig;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossJobConfigParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossJobConfigVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface LineLossJobConfigService extends IService<LineLossJobConfig> {
    Boolean saveOrUpdateConfig(@RequestBody @Valid LineLossJobConfigParam param);

    LineLossJobConfigVO detail();
}
