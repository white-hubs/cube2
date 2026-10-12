package com.guhecloud.cube.base.message2.client;

import com.guhecloud.cube.base.message2.client.dto.MsgRecordDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(name = "message2")
public interface MsgRecordClient {

    /**
     * 根据业务主键批量删除消息
     *
     * @param businessIds 业务主键集合
     */
    @DeleteMapping("/msg-record/business/batch")
    void removeBatchByBusinessIds(@RequestBody Set<String> businessIds);

    /**
     * 消息详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/msg-record/{id}")
    MsgRecordDTO getMsgRecord(@PathVariable("id") Long id);
}
