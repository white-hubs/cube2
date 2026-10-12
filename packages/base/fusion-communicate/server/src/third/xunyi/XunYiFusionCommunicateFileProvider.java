package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.param.command.IncreaseThirdFusionFileFacadeCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.common.AbstractCommonFusionCommunicateFile;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.common.service.IThirdFusionFileService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.FusionFileVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.XunYiApiService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.response.XunYiFileResponseDTO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class XunYiFusionCommunicateFileProvider extends AbstractCommonFusionCommunicateFile {

    private final UserClient userClient;

    private final XunYiApiService xunYiApiService;

    private final IThirdFusionFileService thirdFusionFileService;

    private final XunYiFusionCommunicateAccountProvider xunYiFusionCommunicateAccountProvider;

    @Autowired
    public XunYiFusionCommunicateFileProvider(IThirdFusionFileService thirdFusionFileService,
                                              XunYiApiService xunYiApiService,
                                              UserClient userClient,
                                              XunYiFusionCommunicateAccountProvider xunYiFusionCommunicateAccountProvider
                                             ) {
        super(thirdFusionFileService);
        this.xunYiApiService = xunYiApiService;
        this.userClient = userClient;
        this.thirdFusionFileService = thirdFusionFileService;
        this.xunYiFusionCommunicateAccountProvider = xunYiFusionCommunicateAccountProvider;
    }

    @Override
    public String getProviderCode() {
        return "xunYi";
    }

    @Override
    public FusionFileVO saveFileById(String id) {
        XunYiFileResponseDTO xunYiFile = this.xunYiApiService.getFileRecordById(id);
        //转换为系统用户
        Long callerUserId = this.xunYiFusionCommunicateAccountProvider
                .getSysUserByFusionCommunicateAccount(xunYiFile.getCaller());
        Long calleeUserId = this.xunYiFusionCommunicateAccountProvider
                .getSysUserByFusionCommunicateAccount(xunYiFile.getCalled());
        Map<Long, UserDTO> userDTOMap = this.userClient.listUserByIds(Arrays.asList(callerUserId, calleeUserId)).stream()
                .collect(Collectors.toMap(UserDTO::getId, user -> user, (o, n) -> n));
        //存储文件
        FusionFileVO file = buildFileVO(xunYiFile, callerUserId, calleeUserId, userDTOMap);
        IncreaseThirdFusionFileFacadeCommand thirdFusionFileCommand = ObjectUtils
                .copying(file, IncreaseThirdFusionFileFacadeCommand.class);
        this.thirdFusionFileService.saveThirdFusionFile(thirdFusionFileCommand);
        return file;
    }

    public FusionFileVO buildFileVO(XunYiFileResponseDTO xunYiFile, Long callerUserId, Long calleeUserId,
                                    Map<Long, UserDTO> userDTOMap
                                   ) {
        return FusionFileVO.builder()
                .id(IdWorker.getId())
                .time(xunYiFile.getCreateTime())
                .type(xunYiFile.getRecordName().split(".")[1])
                .suffix(xunYiFile.getRecordName().split(".")[1])
                .url(xunYiFile.getSubPath() + xunYiFile.getRecordName())
                .callerUserId(callerUserId)
                .callerName(userDTOMap.get(callerUserId).getName())
                .calleeUserId(calleeUserId)
                .calleeName(userDTOMap.get(calleeUserId).getName())
                .fileTimeLength(Long.valueOf(xunYiFile.getTimeLength()))
                .fileCreateTime(xunYiFile.getCreateTime())
                .fileEndTime(xunYiFile.getReleaseTime())
                .build();
    }
}
