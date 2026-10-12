package com.guhecloud.cube.srv.gen.stall.client;

import com.guhecloud.cube.srv.gen.stall.vo.GenStallListVo;
import com.guhecloud.cube.srv.gen.stall.vo.GenStallVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("stallManagement")
public interface GenStallClient {
    @PostMapping({ "/gen-stall/ids" })
    List<GenStallListVo> getStallByIds(@RequestBody List<Long> ids);

    @GetMapping("/gen-stall/byId")
    GenStallVo getByIdInner(@RequestParam("id") Long id);
}
