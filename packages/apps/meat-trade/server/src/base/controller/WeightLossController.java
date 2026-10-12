package com.guhecloud.cube.biz.meattrade.server.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.base.entity.WeightLoss;
import com.guhecloud.cube.biz.meattrade.server.base.param.WeightLossCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.WeightLossUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.WeightLossService;
import com.guhecloud.cube.biz.meattrade.server.base.vo.WeightLossPageVO;
import com.guhecloud.cube.biz.meattrade.server.base.vo.WeightLossVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
import java.util.Objects;

/**
 * 重量抽检接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeightLossController {

    private final WeightLossService weightLossService;

    /**
     * 重量损耗新增
     *
     * @param param 重量损耗新增参数
     */
    @PostMapping("/weight-loss")
    public void save(@Valid @RequestBody WeightLossCreateParam param) {
        this.weightLossService.save(param);
    }

    /**
     * 重量损耗编辑
     *
     * @param param 重量损耗编辑参数
     */
    @PutMapping("/weight-loss")
    public void update(@Valid @RequestBody WeightLossUpdateParam param) {
        this.weightLossService.update(param);
    }

    /**
     * 重量损耗ID查询
     *
     * @param id 重量损耗主键
     *
     * @return 重量损耗信息
     */
    @GetMapping("/weight-loss/{id}")
    public WeightLossVO getById(@NotNull(message = "重量损耗不能为空") @Min(value = 1, message = "重量损耗不能小于1")
                                @PathVariable("id") Long id
                               ) {
        WeightLoss weightLoss = this.weightLossService.getById(id);
        return ObjectUtils.copying(weightLoss, WeightLossVO.class);
    }

    /**
     * 重量损耗分页
     *
     * @return 重量损耗分页信息
     */
    @GetMapping("/weight-loss")
    public Pagination<WeightLossPageVO> page(Paging paging) {
        Page<WeightLoss> page = weightLossService.page(paging);
        List<WeightLossPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), WeightLossPageVO.class);
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 根据时间查询重量损耗
     *
     * @param time 时间
     *
     * @return 重量损耗
     */
    @GetMapping("/weight-loss/time")
    public WeightLossVO getByTime(@RequestParam("time") long time) {
        WeightLoss weightLoss = weightLossService.getByTime(time);
        if (Objects.isNull(weightLoss)) {
            // 获取损耗降序排列
            List<WeightLoss> weightLosses =
                    weightLossService.lambdaQuery().orderByDesc(WeightLoss::getStartTime).list();
            if (weightLosses.size() == 0) {
                return null;
            }
            if (weightLosses.get(0).getStartTime() > time) {
                return ObjectUtils.copying(weightLosses.get(0), WeightLossVO.class);
            }
            if (weightLosses.get(weightLosses.size() - 1).getEndTime() < time) {
                return ObjectUtils.copying(weightLosses.get(weightLosses.size() - 1), WeightLossVO.class);
            }
        }
        return ObjectUtils.copying(weightLoss, WeightLossVO.class);
    }

    /**
     * 重量损耗删除
     *
     * @param id 重量损耗主键
     */
    @DeleteMapping("/weight-loss/{id}")
    public void remove(@PathVariable("id") Long id) {
        weightLossService.remove(id);
    }

}
