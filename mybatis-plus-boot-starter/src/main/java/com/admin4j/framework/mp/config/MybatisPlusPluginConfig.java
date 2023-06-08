package com.admin4j.framework.mp.config;

import com.admin4j.common.service.ILoginTenantInfoService;
import com.admin4j.framework.mp.properties.MpProperties;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

/**
 * Mybatis plus 配置
 *
 * @author andanyang
 */
@RefreshScope
@RequiredArgsConstructor
@EnableConfigurationProperties(MpProperties.class)
public class MybatisPlusPluginConfig {

    private final MpProperties mpProperties;

    private final ILoginTenantInfoService loginTenantInfoService;

    /**
     * 分页插件
     *
     * @return MP 插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //多租户
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {

                Long tenantId = loginTenantInfoService.getTenantId();
                if (null == tenantId) {
                    tenantId = 0L;
                }

                return new LongValue(tenantId);
            }

            @Override
            public boolean ignoreTable(String tableName) {

                Long tenantId = loginTenantInfoService.getTenantId();
                //没有登录，可关闭 租户
                if (ObjectUtils.isEmpty(tenantId) || Objects.equals(0L, tenantId)) {
                    return true;
                }

                if (null == mpProperties.getIgnoreTenantTable()) {
                    return false;
                }
                return ArrayUtils.contains(mpProperties.getIgnoreTenantTable(), tableName);
            }
        }));

        //分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        //乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        //防止全表更新与删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        //动态表名
        //interceptor.addInnerInterceptor(new DynamicTableNameInnerInterceptor());

        //sql 性能规范
        if (mpProperties.isIllegalSql()) {
            interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        }
        return interceptor;
    }
}
