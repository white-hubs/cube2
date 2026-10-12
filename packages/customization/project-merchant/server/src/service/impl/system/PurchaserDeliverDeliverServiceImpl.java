package com.guhecloud.cube.deliver.project.merchant.service.impl.system;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.dto.system.PurchaserDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.system.PurchaserDeliver;
import com.guhecloud.cube.deliver.project.merchant.mapper.system.PurchaserDeliverMapper;
import com.guhecloud.cube.deliver.project.merchant.service.system.PurchaserDeliverService;
import com.guhecloud.platform.base.util.PinYinUtils;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商户端-采购商 服务实现类
 * </p>
 *
 * @author bq
 * @since 2021-12-28
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PurchaserDeliverDeliverServiceImpl extends ServiceImpl<PurchaserDeliverMapper, PurchaserDeliver> implements PurchaserDeliverService {

    private final FileClient fileClient;

    @Override
    public void update(PurchaserDTO dto) {
        this.updateAvatarImage(dto);
        PurchaserDeliver purchaserDeliver = dto.convert();
        purchaserDeliver.setPinyin(PinYinUtils.getHead(dto.getNickname()));
        purchaserDeliver.setMobile(null);
        purchaserDeliver.setUsername(null);
        this.updateById(purchaserDeliver);
    }

    private void updateAvatarImage(PurchaserDTO dto) {
        if (StringUtils.isNotEmpty(dto.getAvatar())) {
            List<FileVO> file = fileClient.getByBusinessKey(dto.getId());
            //            if (file == null) {
            //                fileService.lambdaUpdate().eq(File::getPath, dto.getAvatar()).set(File::getBusinessType, UserTypeEnum.USER_TYPE_PURCHASER.getCode()).set(File::getBusinessKey, dto.getId()).set(File::getIsTemp, false).update();
            //            } else if (!file.getPath().equals(dto.getAvatar())) {
            //                fileService.remove(file.getId());
            //                fileService.lambdaUpdate().eq(File::getPath, dto.getAvatar()).set(File::getBusinessType, UserTypeEnum.USER_TYPE_PURCHASER.getCode()).set(File::getBusinessKey, dto.getId()).set(File::getIsTemp, false).update();
            //            }
        }
    }

}
