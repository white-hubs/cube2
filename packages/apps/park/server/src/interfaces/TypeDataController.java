package com.guhecloud.cube.srv.gen.park.management.server.interfaces;

import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.TypeDataCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.TypeDataParam;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.TypeDataVO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.TypeDataQueryService;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * <p>
 * 类型数据 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-09-16
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TypeDataController {

    private final TypeDataQueryService queryService;
    private final TypeDataCommandService commandService;

    /**
     * 查询类型列表
     *
     * @param tag 类别标识
     *            INDUSTRY-行业、ROOM-房间、APPEAL-诉求、INTERVIEW-走访
     *
     * @return 类型列表
     */
    @GetMapping("/type-data/{tag}/list")
    public List<TypeDataVO> list(@PathVariable("tag") String tag, @RequestParam(value = "keyword", required = false) String keyword) {
        return queryService.getListByTag(tag, keyword);
    }

    /**
     * 新增类型
     *
     * @param tag   类别标识
     * @param param 参数
     */
    @PostMapping("/type-data/{tag}")
    public void save(@PathVariable("tag") String tag, @Valid @RequestBody TypeDataParam param) {
        commandService.save(tag, param);
    }

    /**
     * 编辑类型
     *
     * @param id    ID
     * @param param 参数
     */
    @PutMapping("/type-data/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody TypeDataParam param) {
        commandService.update(id, param);
    }

    /**
     * 删除
     *
     * @param id ID
     */
    @DeleteMapping("/type-data/{id}")
    public void remove(@PathVariable("id") Long id) {
        commandService.remove(id);
    }

    /**
     * 批量删除
     *
     * @param ids ID
     */
    @DeleteMapping("/type-data/batch")
    public void removeBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") String ids) {
        commandService.removeBatch(ids);
    }

}
