package com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces;

import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.AnnouncementCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command.ModelTypeCommand;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.ModelTypeVo;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.po.ModelType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description 类型管理
 * @Author lrj
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ModelTypeController {

    private final AnnouncementCommandService announcementCommandService;

    /**
     * @Description 类型新增
     * @Author lrj
     */
    @PostMapping("/addModelTypeCommand")
    public void add(@RequestBody ModelTypeCommand modelTypeCommand) {
        announcementCommandService.add(modelTypeCommand);
    }

    /**
     * @Description 类型修改
     * @Author lrj
     */
    @PutMapping("/updateModelTypeCommand/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody ModelTypeCommand modelTypeCommand) {
        announcementCommandService.updateModelType(id, modelTypeCommand);
    }

    /**
     * @Description 类型删除
     * @Author lrj
     */
    @DeleteMapping("/removeModelType/{id}")
    public void removeModelType(@PathVariable("id") Long id) {
        announcementCommandService.removeModelType(id);
    }

    /**
     * @Description 类型列表
     * @Author lrj
     */
    @GetMapping("/getList/{modelType}")
    public List<ModelTypeVo> getList(@PathVariable String modelType, @RequestParam(value = "keyword", required = false) String keyword) {
        return announcementCommandService.getList(modelType, keyword);
    }

    /**
     * @param modelType
     *
     * @return
     *
     * @Description 获取类型级别
     */
    @GetMapping("/getLevel/{modelType}")
    public List<ModelType> getLevel(@PathVariable String modelType) {
        return announcementCommandService.getLevel(modelType);
    }

}
