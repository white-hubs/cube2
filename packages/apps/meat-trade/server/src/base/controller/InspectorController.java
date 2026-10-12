package com.guhecloud.cube.biz.meattrade.server.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Inspector;
import com.guhecloud.cube.biz.meattrade.server.base.param.InspectorCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.InspectorQueryParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.InspectorUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.InspectorService;
import com.guhecloud.cube.biz.meattrade.server.base.vo.InspectorPageVO;
import com.guhecloud.cube.biz.meattrade.server.base.vo.InspectorVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.UserTenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 抽检员接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectorController {

    private final InspectorService inspectorService;

    private final UserClient userClient;
    private final UserTenantClient userTenantClient;

    /**
     * 抽检员新增
     *
     * @param param 抽检员新增信息
     */
    @PostMapping("/inspector")
    public void save(@Valid @RequestBody InspectorCreateParam param) {
        this.inspectorService.save(param);
    }

    /**
     * 抽检员更新
     *
     * @param param 抽检员更新信息
     */
    @PutMapping("/inspector")
    public void update(@Valid @RequestBody InspectorUpdateParam param) {
        this.inspectorService.update(param);
    }

    /**
     * 抽检员ID查询
     *
     * @param id 抽检员ID
     *
     * @return 抽检员信息
     */
    @GetMapping("/inspector/{id}")
    public InspectorVO getById(@NotNull(message = "抽检员主键不能为空") @Min(value = 1, message = "抽检员主键不能小于1")
                               @PathVariable("id") Long id
                              ) {
        Inspector inspector = this.inspectorService.getById(id);
        return ObjectUtils.copying(inspector, InspectorVO.class);
    }

    /**
     * 抽检员分页查询
     *
     * @param queryParam 查询参数
     *
     * @return 抽检员分页信息
     */
    @GetMapping("/inspector")
    public Pagination<InspectorPageVO> page(@Valid InspectorQueryParam queryParam) {
        Page<Inspector> page = this.inspectorService.page(queryParam);
        List<InspectorPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), InspectorPageVO.class);
        return PagingUtils.convert(page, pageVOS);
    }

    @GetMapping("/inspector/user/page")
    public Pagination<UserDTO> listInspectorUserPage(InspectorQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setPageNumber(queryParam.getPageNumber());
        userQueryParam.setPageSize(queryParam.getPageSize());
        return this.userClient.listUserByTenant(tenant, userQueryParam);
    }

    /**
     * 抽检员列表查询
     *
     * @return 抽检员列表集合
     */
    @GetMapping("/inspector/list")
    public List<InspectorPageVO> list(@Valid InspectorQueryParam queryParam) {
        List<Inspector> inspectors = this.inspectorService.lambdaQuery()
                .eq(Objects.nonNull(queryParam.getIsTrusted()), Inspector::getEnabled, queryParam.getIsTrusted()).list();
        return ObjectUtils.copying(inspectors, InspectorPageVO.class);
    }

    /**
     * 抽检员删除
     *
     * @param id 抽检员主键
     */
    @DeleteMapping("/inspector/{id}")
    public void remove(@NotNull(message = "抽检员主键不能为空") @Min(value = 1, message = "抽检员主键不能小于1")
                       @PathVariable("id") Long id
                      ) {
        this.inspectorService.remove(id);
    }

}
