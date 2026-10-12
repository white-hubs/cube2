package com.guhecloud.cube.base.file.server.client;

import com.guhecloud.cube.base.file.server.client.dto.FileTenantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-19  10:58
 * @Description:
 * @Version: 1.0
 */
@FeignClient(
        name = "ouaa"
)
public interface FileTenantClient {
    @GetMapping({ "/authenticated/tenant/list" })
    List<FileTenantDTO> listTenants(@RequestParam(value = "keyword", required = false) String var1);

    @GetMapping("/authenticated/tenant/category/list")
    List<FileTenantDTO> listTenantsByCategory(@RequestParam(value = "category") String category);
}
