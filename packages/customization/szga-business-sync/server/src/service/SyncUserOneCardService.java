package com.guhecloud.cube.srv.gen.business.info.sync.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.UserOneCard;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 15561
 * @description 针对表【user_one_card(一卡通开通表)】的数据库操作Service
 * @createDate 2024-12-17 16:11:56
 */
public interface SyncUserOneCardService extends IService<UserOneCard> {

    String userOneCardInit(Integer tenant, MultipartFile file);
}
