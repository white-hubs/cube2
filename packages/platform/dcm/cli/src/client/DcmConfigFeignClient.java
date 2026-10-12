package com.guhecloud.platform.dcm.client;

import com.guhecloud.platform.dcm.param.DcmDeclarationParam;
import com.guhecloud.platform.dcm.param.RequestedConfigItemParam;
import feign.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import java.io.InputStream;
import java.util.List;

public interface DcmConfigFeignClient {
    String MEDIA_TYPE_YAML = "application/yaml";

    // 获取运行时申明
    @GetMapping(value = "/dcm/config?runtime=true",
                produces = { MEDIA_TYPE_YAML }
    )
    DcmDeclarationParam getRuntimeDcmDeclaration(@RequestParam("system") Boolean isSystem, @RequestParam("tenant") String tenant);

    @GetMapping(value = "/dcm/config?runtime=true",
                produces = { MEDIA_TYPE_YAML }
    )
    Response getRuntimeDcmDeclarationAsResponse(@RequestParam("system") Boolean isSystem, @RequestParam("tenant") String tenant);

    // 获取内置申明
    @GetMapping(value = "/dcm/config?runtime=false",
                produces = { MEDIA_TYPE_YAML }
    )
    DcmDeclarationParam getBuiltinDcmDeclaration();

    @GetMapping(value = "/dcm/config?runtime=false",
                produces = { MEDIA_TYPE_YAML }
    )
    Response getBuiltinDcmDeclarationAsResponse();

    // 获取内部差异
    @GetMapping(value = "/dcm/config/diff",
                produces = { MEDIA_TYPE_YAML }
    )
    List<RequestedConfigItemParam> internalDiff(@RequestParam("system") Boolean isSystem, @RequestParam("tenant") String tenant);

    @GetMapping(value = "/dcm/config/diff",
                produces = { MEDIA_TYPE_YAML }
    )
    Response internalDiffRaw(@RequestParam("system") Boolean isSystem, @RequestParam("tenant") String tenant);

    // 获取外部差异
    @PostMapping(value = "/dcm/config/diff",
                 produces = { MEDIA_TYPE_YAML },
                 consumes = { MEDIA_TYPE_YAML }
    )
    List<RequestedConfigItemParam> externalDiff(@RequestParam("system") Boolean isSystem, @RequestParam("tenant") String tenant,
                                                @Valid DcmDeclarationParam dcmDeclarationParam
                                               );

    // 应用申明式配置
    @PostMapping(value = "/dcm/config/diff",
                 produces = { MEDIA_TYPE_YAML },
                 consumes = { MEDIA_TYPE_YAML }
    )
    Response externalDiffRaw(@RequestParam("system") Boolean isSystem, @RequestParam("tenant") String tenant, InputStream bodyStream);

    @PostMapping(value = "/dcm/config/apply",
                 produces = { MEDIA_TYPE_YAML },
                 consumes = { MEDIA_TYPE_YAML }
    )
    List<RequestedConfigItemParam> applyDcmDeclaration(@RequestParam("system") Boolean isSystem, @RequestParam("tenant") String tenant,
                                                       DcmDeclarationParam dcmDeclarationParam
                                                      );

    @PostMapping(value = "/dcm/config/apply",
                 produces = { MEDIA_TYPE_YAML },
                 consumes = { MEDIA_TYPE_YAML }
    )
    Response applyRaw(@RequestParam("system") Boolean isSystem, @RequestParam("tenant") String tenant, InputStream declaration);

}
