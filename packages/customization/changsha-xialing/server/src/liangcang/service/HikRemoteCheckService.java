package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.guhecloud.cube.customization.xialing.server.liangcang.vo.HikRemoteCheckRequest;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.HikRemoteCheckResponse;
import org.springframework.web.multipart.MultipartFile;

/** 海康门禁远程核验回调接收业务。 */
public interface HikRemoteCheckService {

    /** 接收远程核验事件及现场图片。 */
    HikRemoteCheckResponse receive(Integer tenant, HikRemoteCheckRequest request, MultipartFile picture);
}
