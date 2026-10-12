package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.entity.SafetyReport;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * <p>
 * 安全月报 服务类
 * </p>
 *
 * @author zwb
 * @since 2021-06-24
 */
public interface SafetyReportService extends IService<SafetyReport> {

    /**
     * 定时任务生成月报记录
     */
    void generatorCrontab();

    void download(String id, HttpServletRequest request, HttpServletResponse response);

    void upload(String id, InputStream inputStream, String nickname);

    void upload(String id, InputStream inputStream, LoginUser loginUser);
}
