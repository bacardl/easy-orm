package com.softserve.easy.meta.metasql;

import java.util.List;

public interface ObjectQuery {
    int executeUpdate();
    List<?> list();
    ObjectQuery setMaxResult(int rowno);
    ObjectQuery setParameter(String parameterName, Object value);
    ObjectQuery setParameter(int position, Object value);
}
