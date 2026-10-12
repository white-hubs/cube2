package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.server.entity.Card;
import com.guhecloud.cube.biz.ouaa.server.param.CardModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.CardQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.CardService;
import com.guhecloud.cube.biz.ouaa.server.vo.CardVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 应用卡片管理接口
 *
 * @author lanchun
 * @date 2021/10/13 19:07
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAdmin()")
public class CardController {

    private final CardService cardService;

    private final FileClient fileClient;

    /**
     * 卡片保存
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/card")
    public Long save(@Valid @RequestBody CardModifyParam param) {
        return this.cardService.save(param);
    }

    /**
     * 卡片修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/card/{id}")
    public void update(@PathVariable("id") @Min(value = 1, message = "卡片主键不能小于1") Long id,
                       @Valid @RequestBody CardModifyParam param
                      ) {
        this.cardService.update(id, param);
    }

    /**
     * 卡片详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/card/{id}")
    public CardVO getCard(@PathVariable("id") @Min(value = 1, message = "卡片主键不能小于1") Long id) {
        Card card = this.cardService.getById(id);
        if (Objects.isNull(card)) {
            return null;
        }
        CardVO cardVO = ObjectUtils.copying(card, CardVO.class);
        List<FileVO> fileVOS = fileClient.getByBusinessKey(card.getId());
        if (!CollectionUtils.isEmpty(fileVOS)) {
            cardVO.setFileVO(fileVOS.get(0));
        }
        return cardVO;
    }

    /**
     * 卡片列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/cards")
    public List<CardVO> listCards(@Valid CardQueryParam param) {
        List<Card> cards = this.cardService.listCards(param);
        if (CollectionUtils.isEmpty(cards)) {
            return Collections.emptyList();
        }
        List<CardVO> cardVOS = ObjectUtils.copying(cards, CardVO.class);
        return cardVOS;
    }

    /**
     * 卡片删除
     *
     * @param id
     */
    @DeleteMapping("/card/{id}")
    public void remove(@PathVariable("id") @Min(value = 1, message = "卡片主键不能小于1") Long id) {
        this.cardService.remove(id);
    }

    /**
     * 删除访问端下的卡片
     *
     * @param terminalId
     * @param applicationId
     * @param code
     */
    @DeleteMapping("/card/{terminalId}/{applicationId}/{code}")
    public void removeByCardCode(@PathVariable("terminalId") @Min(value = 1, message = "卡片主键不能小于1") Long terminalId,
                                 @PathVariable("applicationId") @Min(value = 1, message = "应用主键不能小于1") Long applicationId,
                                 @PathVariable("code") String code
                                ) {
        this.cardService.remove(terminalId, applicationId, code);
    }
}
