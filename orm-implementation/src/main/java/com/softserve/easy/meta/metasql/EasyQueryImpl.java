package com.softserve.easy.meta.metasql;

import java.util.List;

public class EasyQueryImpl implements EasyQuery{
    private String string;

    public EasyQueryImpl(String query) {

    }

    @Override
    public int executeUpdate() {
        return 0;
    }

    @Override
    public List<?> list() {
        return null;
    }

    @Override
    public ObjectQuery setMaxResult(int rowNumber) {
        return null;
    }

    @Override
    public ObjectQuery setParameter(String parameterName, Object value) {
        return null;
    }

    @Override
    public ObjectQuery setParameter(int position, Object value) {
        return null;
    }
}
