package com.guhecloud.cube.srv.gen.safety.management.controller.riskIndex;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.WeightAllotQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.WeightAllotVO;
import com.guhecloud.cube.srv.gen.safety.management.service.WeightAllotService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 权重分配
 *
 * @author wql
 * @describe 权重分配
 * @date 2023/10/12 19:55
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeightAllotController {
    private final WeightAllotService weightAllotService;

    /**
     * 保存权重分配
     *
     * @Author wql
     * @Description 保存权重分配
     * @Date 11:47 2023/10/13
     **/
    @PutMapping("/weightAllot/save")
    public void saveWeightAllot(@Valid @RequestBody WeightAllotVO weightAllotVO) {
        weightAllotService.saveWeightAllot(weightAllotVO);
    }

    /**
     * 删除权重配置
     *
     * @Author wql
     * @Description 删除权重配置
     * @Date 11:56 2023/10/13
     **/
    @DeleteMapping("/weightAllot/delete/{id}")
    public void deleteWeightAllot(@PathVariable("id") Long id) {
        weightAllotService.deleteWeightAllot(id);
    }

    /**
     * 权重配置列表
     *
     * @Author wql
     * @Description 权重配置列表
     * @Date 13:34 2023/10/13
     **/
    @GetMapping("/weightAllot/list")
    public Pagination<WeightAllotVO> weightAllotList(WeightAllotQuery query) {
        return weightAllotService.weightAllotList(query);
    }

    /**
     * 权重配置详情
     *
     * @Author wql
     * @Description 权重配置详情
     * @Date 13:47 2023/10/13
     **/
    @GetMapping("/weightAllot/{id}")
    public WeightAllotVO weightAllotDetail(@PathVariable("id") Long id) {
        return weightAllotService.weightAllotDetail(id);
    }

}
