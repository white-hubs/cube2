package com.guhecloud.cube.base.file.server.controller;

import com.guhecloud.cube.base.file.server.dto.FileExpirationPolicyDTO;
import com.guhecloud.cube.base.file.server.param.FileExpirationPolicyPageParam;
import com.guhecloud.cube.base.file.server.service.FileExpirationPolicyService;
import com.guhecloud.cube.base.file.server.vo.FileExpirationPolicyVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件清理策略管理
 *
 * @Author: lijie
 * @CreateTime: 2024-12-12  16:33
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileExpirationPolicyController {
    private final FileExpirationPolicyService fileExpirationPolicyService;

    /**
     * 查询文件清理策略
     */
    @GetMapping("/get/expiration/policy/page")
    public Pagination<FileExpirationPolicyVO> getExpirationPolicyPage(@Valid FileExpirationPolicyPageParam param) {
        return fileExpirationPolicyService.getExpirationPolicyPage(param);
    }

    /**
     * 新增文件清理策略
     */
    @PostMapping("/add/expiration/policy")
    public Boolean addExpirationPolicy(@Valid @RequestBody FileExpirationPolicyDTO param) {
        return fileExpirationPolicyService.addExpirationPolicy(param);
    }

    /**
     * 编辑文件清理策略
     */
    @PutMapping("/update/expiration/policy")
    public Boolean updateExpirationPolicy(@Valid @RequestBody FileExpirationPolicyDTO param) {
        return fileExpirationPolicyService.updateExpirationPolicy(param);
    }

    @DeleteMapping("/delete/expiration/policy/{id}")
    public Boolean deleteExpirationPolicy(@PathVariable("id") Long id) {
        return fileExpirationPolicyService.deleteExpirationPolicy(id);
    }
}
