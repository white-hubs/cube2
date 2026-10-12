package com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway;

import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.aggregates.ThirdFusionAccount;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.infrastructure.repository.ThirdFusionAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdFusionAccountCommandService {

    private final ThirdFusionAccountRepository thirdFusionAccountRepository;

    public void batchIncreaseThirdFusionAccount(List<ThirdFusionAccount> thirdFusionAccounts) {
        this.thirdFusionAccountRepository.batchSave(thirdFusionAccounts);
    }
}
