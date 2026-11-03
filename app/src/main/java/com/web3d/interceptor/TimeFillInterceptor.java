package com.web3d.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.time.LocalDateTime;
import java.util.Properties;


@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class TimeFillInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 1. 获取当前的 SQL 命令类型（INSERT, UPDATE, DELETE）
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = ms.getSqlCommandType();

        // 2. 获取你传给 Mapper 的参数对象（比如你的 MonitorErrorLog 实体类）
        Object parameter = invocation.getArgs()[1];

        // 3. 如果参数不为空，并且是 INSERT 或 UPDATE 操作
        if (parameter != null && (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE)) {

            // 使用 MyBatis 自带的 MetaObject 工具（强大的反射工具，免去手动写 Field）
            MetaObject metaObject = SystemMetaObject.forObject(parameter);

            // 如果是 INSERT，填充创建时间和更新时间
            if (sqlCommandType == SqlCommandType.INSERT) {
                if (metaObject.hasSetter("createTime")) {
                    metaObject.setValue("createTime", LocalDateTime.now());
                }
                if (metaObject.hasSetter("updateTime")) {
                    metaObject.setValue("updateTime", LocalDateTime.now());
                }
            }
            // 如果是 UPDATE，只填充更新时间
            else if (sqlCommandType == SqlCommandType.UPDATE) {
                if (metaObject.hasSetter("updateTime")) {
                    metaObject.setValue("updateTime", LocalDateTime.now());
                }
            }
        }

        // 4. 放行，让原 SQL 继续执行
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 无需配置
    }
}
