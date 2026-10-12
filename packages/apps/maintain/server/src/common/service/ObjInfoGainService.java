package com.guhecloud.cube.srv.gen.maintain.server.common.service;

import com.guhecloud.cube.srv.gen.maintain.server.common.dto.ObjInfoDTO;

/**
 * @author wql
 * @describe 获取对象信息
 * @date 2023/11/8 15:02
 */
public interface ObjInfoGainService {
    ObjInfoDTO getObjInfo(long ObjId, String objType);
}
