package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.OuaaFileSceneCodeEnum;
import com.guhecloud.cube.biz.ouaa.server.dao.AppVersionMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.AppVersion;
import com.guhecloud.cube.biz.ouaa.server.param.AppVersionModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.AppVersionQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.AppVersionService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DigestUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements AppVersionService {

    private final FileRefClient fileRefClient;

    private final FileClient fileClient;

    private final RestTemplate restTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull AppVersionModifyParam param) {
        AppVersion appVersion = ObjectUtils.copying(param, AppVersion.class);
        //文件字节数组
        FileVO fileVO = fileClient.getById(param.getFileId());
        String apkSecret = getAppApkFileSecret(fileVO);
        appVersion.setApkSecret(apkSecret);
        this.baseMapper.insert(appVersion);
        if (Objects.nonNull(param.getFileId())) {
            FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setFileId(param.getFileId());
            fileRefCreateParam.setBusinessKey(appVersion.getId());
            fileRefCreateParam.setSceneCode(OuaaFileSceneCodeEnum.OUAA_APP_VERSION.getCode());
            this.fileRefClient.refInduce(fileRefCreateParam);
        }
        return appVersion.getId();
    }

    private String getAppApkFileSecret(FileVO fileVO) {
        try {
            // 获取文件输入流
            String fileUrl = fileVO.getEndpointPath();
            ResponseEntity<Resource> responseEntity = restTemplate.exchange(
                    fileUrl,
                    HttpMethod.GET,
                    null,
                    Resource.class
                                                                           );
            Resource resource = responseEntity.getBody();
            InputStream inputStream = resource.getInputStream();
            // 计算文件的MD5哈希值
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
            byte[] mdBytes = md.digest();
            return bytesToHex(mdBytes);
        } catch (NoSuchAlgorithmException | IOException e) {
            // 处理异常
            throw new RuntimeException("Error processing file", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull AppVersionModifyParam param) {
        AppVersion appVersion = this.getById(id);
        Assert.isNotFound(appVersion, "版本记录不存在");
        ObjectUtils.copying(param, appVersion);
        FileVO fileVO = fileClient.getById(param.getFileId());
        try {
            appVersion.setApkSecret(DigestUtils.md5Hex(new FileInputStream(fileVO.getPath())));
        } catch (FileNotFoundException e) {
            throw new ServiceException("文件不存在或已被删除");
        } catch (IOException e) {
            throw new ServiceException("加密失败");
        }
        this.baseMapper.updateById(appVersion);
        if (Objects.nonNull(param.getFileId())) {
            FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setFileId(param.getFileId());
            fileRefCreateParam.setBusinessKey(appVersion.getId());
            fileRefCreateParam.setSceneCode(OuaaFileSceneCodeEnum.OUAA_APP_VERSION.getCode());
            this.fileRefClient.refInduce(fileRefCreateParam);
        }
    }

    @Override
    public AppVersion getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public AppVersion getNewAppVersion(@NonNull String appType) {
        Page<AppVersion> appVersions = this.lambdaQuery()
                .eq(AppVersion::getTerminal, SessionContextHolder.getRequester().getTerminal())
                .eq(AppVersion::getType, appType)
                .orderByDesc(AppVersion::getSort)
                .page(new Page(0, 1));
        if (!CollectionUtils.isEmpty(appVersions.getRecords())) {
            return appVersions.getRecords().get(0);
        }
        return null;
    }

    @Override
    public Page<AppVersion> getPage(AppVersionQueryParam param) {
        return this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getTerminal()), AppVersion::getTerminal, param.getTerminal())
                .eq(StringUtils.isNotEmpty(param.getType()), AppVersion::getType, param.getType())
                .orderByDesc(AppVersion::getCreateTime)
                .page(PagingUtils.convert(param));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
