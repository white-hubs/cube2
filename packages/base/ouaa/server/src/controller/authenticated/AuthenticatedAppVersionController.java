package com.guhecloud.cube.biz.ouaa.server.controller.authenticated;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.server.entity.AppVersion;
import com.guhecloud.cube.biz.ouaa.server.service.AppVersionService;
import com.guhecloud.cube.biz.ouaa.server.vo.AppVersionVO;
import com.guhecloud.platform.base.model.LegacyResponse;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticatedAppVersionController {

    private final AppVersionService appVersionService;

    private final FileClient fileClient;

    /**
     * 获取最新一条的版本信息
     *
     * @param appType
     *
     * @return
     */
    @GetMapping("/authenticated/app-version/new")
    public LegacyResponse<AppVersionVO> getNewAppVersion(@RequestParam(value = "appType") String appType) {
        AppVersion appVersion = this.appVersionService.getNewAppVersion(appType);
        if (Objects.isNull(appVersion)) {
            return null;
        }
        AppVersionVO appVersionVO = ObjectUtils.copying(appVersion, AppVersionVO.class);
        List<FileVO> fileVOList = this.fileClient.getByBusinessKey(appVersionVO.getId());
        if (!CollectionUtils.isEmpty(fileVOList)) {
            appVersionVO.setFileVO(fileVOList.get(0));
        }
        return LegacyResponse.success(appVersionVO);
    }
}
