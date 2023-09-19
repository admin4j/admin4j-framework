package com.admin4j.framework.mp.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 注意！！ 必须开启映射注解
 * *
 * * @TableName(autoResultMap = true)
 * *
 * * 以下两种类型处理器，二选一 也可以同时存在
 * *
 * * 注意！！选择对应的 JSON 处理器也必须存在对应 JSON 解析依赖包
 * 使用xml是需要自己设置
 * ```xml
 * <resultMap id="myResultMap" type="com.example.MyObject">
 * <result column="data" property="data" typeHandler="com.example.JacksonTypeHandler"/>
 * </resultMap>
 * ```
 *
 * @author andanyang
 * @since 2023/9/19 9:06
 */
public abstract class AbstractEncryptTypeHandler<T> extends BaseTypeHandler<T> {

    /**
     * 加密方法
     *
     * @param plaintext 明文
     * @return
     */
    protected abstract String encrypt(T plaintext);

    /**
     * 解密方法
     *
     * @param ciphertext 密文
     * @return
     */
    protected abstract T decrypt(String ciphertext);

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T parameter, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, encrypt(parameter));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return decrypt(resultSet.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return decrypt(resultSet.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return decrypt(callableStatement.getString(columnIndex));
    }
}
