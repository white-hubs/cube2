package com.guhecloud.platform.database.config;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Aspect
@EnableTransactionManagement
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionConfig {

    private static final String AOP_POINTCUT_EXPRESSION = "@within(org.springframework.stereotype.Service) || @within(org.springframework.stereotype.Repository)";

    private final TransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        Map<String, TransactionAttribute> txMap = new HashMap<>();
        txMap.put("save*", requiredTx);
        txMap.put("remove*", requiredTx);
        txMap.put("update*", requiredTx);
        txMap.put("batch*", requiredTx);
        txMap.put("clear*", requiredTx);
        txMap.put("add*", requiredTx);
        txMap.put("append*", requiredTx);
        txMap.put("modify*", requiredTx);
        txMap.put("edit*", requiredTx);
        txMap.put("insert*", requiredTx);
        txMap.put("delete*", requiredTx);
        txMap.put("do*", requiredTx);
        txMap.put("create*", requiredTx);
        txMap.put("select*", readOnlyTx);
        //		txMap.put("get*", readOnlyTx);
        txMap.put("valid*", readOnlyTx);
        txMap.put("list*", readOnlyTx);
        txMap.put("count*", readOnlyTx);
        txMap.put("find*", readOnlyTx);
        txMap.put("load*", readOnlyTx);
        txMap.put("search*", readOnlyTx);
        //		txMap.put("*", requiredTx);
        source.setNameMap(txMap);
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);
        return txAdvice;
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }

}
