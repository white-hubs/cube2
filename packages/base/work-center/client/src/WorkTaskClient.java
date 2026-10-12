package com.guhecloud.cube.biz.work.center.client;

import com.guhecloud.cube.biz.work.center.client.param.WorkHomePageBatchParam;
import com.guhecloud.cube.biz.work.center.client.param.WorkHomePageParam;
import com.guhecloud.cube.biz.work.center.client.param.WorkTagReferBusinessTypeModifyParam;
import com.guhecloud.cube.biz.work.center.client.param.WorkTaskQueryParam;
import com.guhecloud.cube.biz.work.center.client.vo.WorkHomePageVO;
import com.guhecloud.cube.biz.work.center.client.vo.WorkTaskPageVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = "workCenter")
public interface WorkTaskClient {

    /**
     * 根据业务主键批量删除工作任务
     *
     * @param businessIds 业务主键集合
     */
    @DeleteMapping("/work-task/business/batch")
    void removeBatchByBusinessIds(@RequestBody Set<String> businessIds);

    /**
     * 工作任务标签关联业务类型
     *
     * @param workTagCode
     * @param param
     *
     * @return
     */
    @PostMapping("/work-tags/{workTagCode}/refer/business-type")
    Long saveWorkTagReferBusinessType(@PathVariable("workTagCode") String workTagCode,
                                      @RequestBody WorkTagReferBusinessTypeModifyParam param
                                     );

    /**
     * 工作任务标签删除
     *
     * @param id
     */
    @DeleteMapping("/work-tags/{id}")
    void removeById(@PathVariable("id") Long id);

    /**
     * 工作中心-任务查询
     * (一般是内部调用)
     */
    @PostMapping("/work/center/task/query/post")
    Pagination<WorkTaskPageVO> taskQueryPost(@RequestBody WorkTaskQueryParam param);

    /**
     * 任务统计
     * (一般是内部调用)
     */
    @PostMapping("/work/center/task/num/post")
    WorkHomePageVO taskNumPost(@RequestBody WorkHomePageParam param);

    /**
     * 批量获取任务统计
     *
     * @param param 批量统计条件
     * @return key为业务分组id，value为任务统计
     */
    @PostMapping("/work/center/task/num/batch/post")
    Map<Long, WorkHomePageVO> taskNumBatchPost(@RequestBody WorkHomePageBatchParam param);

    @PostMapping("/work/center/task/hand/user")
    List<Long> getHandTaskUser(@RequestBody Set<Long> userIds);
}
