package com.softserve.easy.action;

import com.softserve.easy.helper.ExecutableList;

public class ActionQueue {
    private ExecutableList<EntityInsertAction> insertions;
    private ExecutableList<EntityDeleteAction> deletions;
    private ExecutableList<EntityUpdateAction> updates;
}
