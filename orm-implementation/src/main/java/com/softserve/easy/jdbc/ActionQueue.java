package com.softserve.easy.jdbc;

import com.softserve.easy.action.EntityDeleteAction;
import com.softserve.easy.action.EntityInsertAction;
import com.softserve.easy.action.EntityUpdateAction;
import com.softserve.easy.helper.ExecutableList;

public class ActionQueue {
    private ExecutableList<EntityInsertAction> insertions;
    private ExecutableList<EntityDeleteAction> deletions;
    private ExecutableList<EntityUpdateAction> updates;
}
