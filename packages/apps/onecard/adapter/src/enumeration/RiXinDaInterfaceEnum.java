package com.guhecloud.cube.srv.gen.adapter.onecard.server.enumeration;

import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.AmountChangeParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.ConsumeRecordOutVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.RiXinDaConsumeRecordOutVO;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/17 10:51
 * @Description: 日新达接口枚举
 */
@Getter
public enum RiXinDaInterfaceEnum {

    ACCOUNT_INFO("/biz/account/page", "账户信息") {
        @Override
        public Map<String, Object> getQueryParam(AmountChangeParamQuery param) {
            Map<String, Object> map = new HashMap<>();
            map.put("startUpdateTime", param.getBeginDate());
            map.put("endUpdateTime", param.getEndDate());
            return map;
        }

        @Override
        public void extendInfoTransfer(RiXinDaConsumeRecordOutVO record, ConsumeRecordOutVO consumeRecordOut) {

        }
    },
    CONSUMER_RECORD("/biz/consumptionrecord/page", "消费记录") {
        @Override
        public Map<String, Object> getQueryParam(AmountChangeParamQuery param) {
            Map<String, Object> map = new HashMap<>();
            map.put("startConsumptionDate", param.getBeginDate());
            map.put("endConsumptionDate", param.getEndDate());
            return map;
        }

        @Override
        public void extendInfoTransfer(RiXinDaConsumeRecordOutVO record, ConsumeRecordOutVO consumeRecordOut) {
            consumeRecordOut.setTradeAmount(record.getAmount());
            consumeRecordOut.setSubsidyAmount(record.getSubsidy());
            consumeRecordOut.setTradeTime(record.getConsumptionDate());
            consumeRecordOut.setType(CombinationTypeEnum.getEnumDesc(record.getType()));
        }

    },
    CONSUMER_TIMES_RECORD("/biz/consumptiontimesrecord/page", "消次记录") {
        @Override
        public Map<String, Object> getQueryParam(AmountChangeParamQuery param) {
            Map<String, Object> map = new HashMap<>();
            map.put("startConsumptionDate", param.getBeginDate());
            map.put("endConsumptionDate", param.getEndDate());
            return map;
        }

        @Override
        public void extendInfoTransfer(RiXinDaConsumeRecordOutVO record, ConsumeRecordOutVO consumeRecordOut) {
            consumeRecordOut.setTimes(record.getTimes());
            consumeRecordOut.setTradeTime(record.getConsumptionDate());
            consumeRecordOut.setType(CardTimesTypeEnum.getEnumDesc(record.getType()));
        }
    },
    REFUND_RECORD("/biz/refundrecord/page", "退款记录") {
        @Override
        public Map<String, Object> getQueryParam(AmountChangeParamQuery param) {
            Map<String, Object> map = new HashMap<>();
            map.put("startRefundDate", param.getBeginDate());
            map.put("endRefundDate", param.getEndDate());
            return map;
        }

        @Override
        public void extendInfoTransfer(RiXinDaConsumeRecordOutVO record, ConsumeRecordOutVO consumeRecordOut) {
            consumeRecordOut.setTradeAmount(record.getCash());
            consumeRecordOut.setSubsidyAmount(record.getSubsidy());
            consumeRecordOut.setTradeTime(record.getRefundDate());
            consumeRecordOut.setType(CombinationTypeEnum.getEnumDesc(record.getType()));
        }

    },
    REFUND_TIMES_RECORD("/biz/refundtimesrecord/page", "退次记录") {
        @Override
        public Map<String, Object> getQueryParam(AmountChangeParamQuery param) {
            Map<String, Object> map = new HashMap<>();
            map.put("startRefundDate", param.getBeginDate());
            map.put("endRefundDate", param.getEndDate());
            return map;
        }

        @Override
        public void extendInfoTransfer(RiXinDaConsumeRecordOutVO record, ConsumeRecordOutVO consumeRecordOut) {
            consumeRecordOut.setTimes(record.getTimes());
            consumeRecordOut.setTradeTime(record.getRefundDate());
            consumeRecordOut.setType(CardTimesTypeEnum.getEnumDesc(record.getType()));
        }
    },
    RECHARGE_RECORD("/biz/rechargerecord/page", "充减值记录") {
        @Override
        public Map<String, Object> getQueryParam(AmountChangeParamQuery param) {
            Map<String, Object> map = new HashMap<>();
            map.put("startRechargeDate", param.getBeginDate());
            map.put("endRechargeDate", param.getEndDate());
            return map;
        }

        @Override
        public void extendInfoTransfer(RiXinDaConsumeRecordOutVO record, ConsumeRecordOutVO consumeRecordOut) {
            if (1 == record.getType()) {
                consumeRecordOut.setTradeAmount(record.getAmount());
            } else if (2 == record.getType()) {
                consumeRecordOut.setSubsidyAmount(record.getAmount());
            }
            consumeRecordOut.setTradeTime(record.getRechargeDate());
            consumeRecordOut.setType(CashTypeEnum.getEnumDesc(record.getType()));
        }
    },
    RECHARGE_TIMES_RECORD("/biz/rechargetimesrecord/page", "充减次记录") {
        @Override
        public Map<String, Object> getQueryParam(AmountChangeParamQuery param) {
            Map<String, Object> map = new HashMap<>();
            map.put("startRechargeDate", param.getBeginDate());
            map.put("endRechargeDate", param.getEndDate());
            return map;
        }

        @Override
        public void extendInfoTransfer(RiXinDaConsumeRecordOutVO record, ConsumeRecordOutVO consumeRecordOut) {
            consumeRecordOut.setTimes(record.getTimes());
            consumeRecordOut.setTradeTime(record.getRechargeDate());
            consumeRecordOut.setType(CardTimesTypeEnum.getEnumDesc(record.getType()));
        }
    },
    ;

    private final String url;

    private final String desc;

    public abstract Map<String, Object> getQueryParam(AmountChangeParamQuery param);

    public abstract void extendInfoTransfer(RiXinDaConsumeRecordOutVO record, ConsumeRecordOutVO consumeRecordOut);

    RiXinDaInterfaceEnum(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }
}
