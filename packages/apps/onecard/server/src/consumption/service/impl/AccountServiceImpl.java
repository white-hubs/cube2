package com.guhecloud.cube.onecard.server.consumption.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.onecard.server.consumption.dao.AccountMapper;
import com.guhecloud.cube.onecard.server.consumption.entity.Account;
import com.guhecloud.cube.onecard.server.consumption.entity.AccountDetails;
import com.guhecloud.cube.onecard.server.consumption.entity.AmountChangeRecord;
import com.guhecloud.cube.onecard.server.consumption.entity.CopiesChangeRecord;
import com.guhecloud.cube.onecard.server.consumption.enumeration.AccountStatusEnum;
import com.guhecloud.cube.onecard.server.consumption.param.AccountInfoQueryParam;
import com.guhecloud.cube.onecard.server.consumption.param.AmountRecord;
import com.guhecloud.cube.onecard.server.consumption.param.CopiesRecord;
import com.guhecloud.cube.onecard.server.consumption.service.AccountDetailsService;
import com.guhecloud.cube.onecard.server.consumption.service.AccountService;
import com.guhecloud.cube.onecard.server.consumption.service.AmountChangeRecordService;
import com.guhecloud.cube.onecard.server.consumption.service.CopiesChangeRecordService;
import com.guhecloud.cube.onecard.server.consumption.vo.AccountDetailsVO2;
import com.guhecloud.cube.onecard.server.consumption.vo.AccountDetailsVo;
import com.guhecloud.cube.onecard.server.consumption.vo.AccountVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    private final AccountDetailsService accountDetailsService;
    private final AmountChangeRecordService amountChangeRecordService;
    private final CopiesChangeRecordService copiesChangeRecordService;
    private final UserClient userClient;

    @Override
    public Pagination<AccountVo> getConsumptionInfo(AccountInfoQueryParam param) {

        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(param.getAccountName())) {
            queryWrapper.eq("account_name", param.getAccountName());
        }
        if (StringUtils.isNotBlank(param.getEnterParam())) {
            queryWrapper.and(query -> query.like("user_no", param.getEnterParam())
                                     .or().like("user_serial", param.getEnterParam())
                                     .or().like("user_card", param.getEnterParam())
                                     .or().like("user_name", param.getEnterParam())
                            );
        }
        queryWrapper.orderByDesc("user_card");
        Page<Account> page = this.page(PagingUtils.convert(param), queryWrapper);
        if (page != null && page.getRecords().size() > 0) {
            List<Account> accountList = page.getRecords().stream().map(a -> {
                Account account = new Account();
                ObjectUtils.copying(a, account);
                if (StringUtils.isNotBlank(a.getAccountStatus())) {
                    account.setAccountStatus(AccountStatusEnum.getValue(a.getAccountStatus()));
                }
                return account;
            }).collect(Collectors.toList());
            page.setRecords(accountList);
        }
        return PagingUtils.convert(page, AccountVo.class);

    }

    @Override
    public AccountDetailsVo getConsumptionDetails(long id) {

        AccountDetailsVo accountDetailsVo = new AccountDetailsVo();
        List<Account> list = this.list(Wrappers.<Account>lambdaQuery().eq(Account::getId, id));
        for (Account account : list) {
            accountDetailsVo.setUserCard(account.getUserCard());
            accountDetailsVo.setAccountBalance(account.getAccountBalance());
            accountDetailsVo.setUserName(account.getUserName());
            accountDetailsVo.setUserNo(account.getUserNo());
            accountDetailsVo.setUserSerial(account.getUserSerial());

            List<AccountDetails> accountDetailsList = accountDetailsService.list(Wrappers.<AccountDetails>lambdaQuery()
                                                                                         .eq(AccountDetails::getUserCard, account.getUserCard()));
            for (AccountDetails accountDetails : accountDetailsList) {
                accountDetailsVo.setCashBalance(accountDetails.getCashBalance());
                accountDetailsVo.setSubsidyBalance(accountDetails.getSubsidyBalance());
            }

            List<AmountChangeRecord> amountChangeRecordsList = amountChangeRecordService.list(Wrappers.<AmountChangeRecord>lambdaQuery()
                                                                                                      .eq(AmountChangeRecord::getUserCard, account.getUserCard())
                                                                                                      .orderByDesc(AmountChangeRecord::getTransactionTime)
                                                                                                      .last("limit 5"));
            List<AmountRecord> amountRecordList = ObjectUtils.copying(amountChangeRecordsList, AmountRecord.class);
            accountDetailsVo.setAmountRecord(amountRecordList);

            List<CopiesChangeRecord> copiesChangeRecordList = copiesChangeRecordService.list(Wrappers.<CopiesChangeRecord>lambdaQuery()
                                                                                                     .eq(CopiesChangeRecord::getUserCard, account.getUserCard())
                                                                                                     .orderByDesc(CopiesChangeRecord::getTransactionTime)
                                                                                                     .last("limit 5"));
            List<CopiesRecord> copiesRecordList = ObjectUtils.copying(copiesChangeRecordList, CopiesRecord.class);
            accountDetailsVo.setCopiesRecord(copiesRecordList);
        }
        return accountDetailsVo;
    }

    @Override
    public AccountDetailsVO2 getAccountInfo(Long id) {
        AccountDetails accountDetails = accountDetailsService.getById(id);
        if (Objects.isNull(accountDetails)) {
            return null;
        }
        AccountDetailsVO2 accountDetailsVO2 = ObjectUtils.copying(accountDetails, AccountDetailsVO2.class);
        UserDTO user = userClient.getUser(id);
        accountDetailsVO2.setUserName(Objects.nonNull(user) ? user.getName() : "");
        return accountDetailsVO2;
    }
}
