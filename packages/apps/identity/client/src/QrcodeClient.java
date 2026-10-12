package com.guhecloud.cube.srv.gen.identity.client;

import com.guhecloud.cube.srv.gen.identity.client.vo.QrcodeEntityVO;
import com.guhecloud.cube.srv.gen.identity.client.vo.QrcodeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author bq
 * @date 2022/7/28 10:52
 * @description 二维码
 */
@FeignClient("identity")
public interface QrcodeClient {
    /**
     * 根据code查询二维码绑定的实体
     *
     * @param code 二维码code
     *
     * @return
     */
    @GetMapping("/qrcode/code")
    QrcodeEntityVO getEntityByCode(@RequestParam(value = "code") String code);

    /**
     * 根据实体ID查询二维码信息
     *
     * @param entityId 实体ID
     *
     * @return
     */
    @GetMapping("/qrcode/entity")
    QrcodeVO getQrcodeByEntityId(@RequestParam(value = "entityId") String entityId);

    /**
     * 批量查询实体关联的二维码 code。
     *
     * @param entityIds 实体 ID 集合
     *
     * @return 仅保证 entityId 与 code 有值的二维码信息
     */
    @PostMapping("/qrcode/entity-codes")
    List<QrcodeVO> getQrcodeCodesByEntityIds(@RequestBody List<Long> entityIds);

}
