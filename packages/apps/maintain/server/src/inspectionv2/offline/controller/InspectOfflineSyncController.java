package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.param.InspectOfflineCreateSyncSessionParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.result.InspectOfflineSyncSessionResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.service.InspectOfflineSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日常巡检离线同步入口。
 * <p>
 * 该控制器只负责离线同步协议的 HTTP 封装，具体快照构建、分片读取和权限校验由
 * {@link InspectOfflineSyncService} 处理。
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectOfflineSyncController {
    private final InspectOfflineSyncService syncService;

    /**
     * 创建离线同步会话。
     * <p>
     * 接口会快速返回 sessionId，快照构建在后台异步执行，避免 App 请求被大数据组装阻塞到超时。
     */
    @PostMapping("/inspect-offline/sync-sessions")
    public InspectOfflineSyncSessionResult createSession(@RequestBody InspectOfflineCreateSyncSessionParam param) {
        return syncService.createSession(param);
    }

    /**
     * 查询离线同步会话进度和 manifest。
     * <p>
     * App 通过该接口轮询 BUILDING/READY/FAILED 状态，并在 READY 后按 chunks 下载分片。
     */
    @GetMapping("/inspect-offline/sync-sessions/{sessionId}")
    public InspectOfflineSyncSessionResult getSession(@PathVariable("sessionId") String sessionId) {
        return syncService.getSession(sessionId);
    }

    /**
     * 下载离线同步分片。
     * <p>
     * 返回内容是 gzip 压缩后的 JSON，HTTP 头显式声明 Content-Encoding，客户端可自动解压。
     */
    @GetMapping("/inspect-offline/sync-sessions/{sessionId}/chunks/{chunkId}")
    public ResponseEntity<byte[]> getChunk(@PathVariable("sessionId") String sessionId, @PathVariable("chunkId") String chunkId) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_ENCODING, "gzip")
                .contentType(MediaType.APPLICATION_JSON)
                .body(syncService.getChunk(sessionId, chunkId));
    }
}
