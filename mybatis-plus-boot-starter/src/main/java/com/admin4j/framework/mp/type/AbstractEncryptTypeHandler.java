package com.admin4j.framework.mp.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
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
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T s, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, encrypt(s));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return decrypt(resultSet.getString(s));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return decrypt(resultSet.getString(1));
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return decrypt(callableStatement.getString(1));
    }
}
