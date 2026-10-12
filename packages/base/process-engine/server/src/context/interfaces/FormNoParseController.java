package com.guhecloud.cube.base.flow.context.interfaces;

import com.guhecloud.cube.base.flow.context.tool.FormNumberCreater;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormNoParseController {

    @Resource(name = "flowFormNumberCreater")
    private FormNumberCreater formNumberCreater;

    @GetMapping("/form/no")
    public String getFormNo(@RequestParam("prefix") String prefix) {
        return formNumberCreater.create(prefix);
    }

}
