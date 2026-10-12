package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo;

import lombok.Data;

/**
 * 获取公钥返回
 * 例子：{
 * "success": true,
 * "data": {
 * "publicKey": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLGfxJ9PHyApfToQBa2sMZhiR0JSxADFCK1zJysbSgu88KgLUsf3QdKoa1ABZTYjWd1z+y5aK4KFtoEzvAcc94fzaeeLkaPKPmPjLFTlZe4sCDor0+onMrPu1X2uwiCPycCa+baW/4rFQE13GILWGQHeR1awUzOvlbhHzipzUgsQIDAQAB"
 * },
 * "code": "0",
 * "errMsg": ""
 * }
 */
@Data
public class DaHuaPublicKeyVO {
    private String publicKey;
}
