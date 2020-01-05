package com.softserve.easy.meta.field;

import com.softserve.easy.meta.MappingType;

import java.lang.annotation.Annotation;
import java.util.List;

public abstract class AbstractMetaField {
    protected MappingType mappingType;
    protected boolean transitionable;
    protected String nameOfField;
    protected List<Annotation> annotations;

    public AbstractMetaField(MappingType mappingType, boolean transitionable, String nameOfField, List<Annotation> annotations) {
        this.mappingType = mappingType;
        this.transitionable = transitionable;
        this.nameOfField = nameOfField;
        this.annotations = annotations;
    }

    public MappingType getMappingType() {
        return mappingType;
    }

    public void setMappingType(MappingType mappingType) {
        this.mappingType = mappingType;
    }

    public boolean isTransitionable() {
        return transitionable;
    }

    public void setTransitionable(boolean transitionable) {
        this.transitionable = transitionable;
    }

    public String getNameOfField() {
        return nameOfField;
    }

    public void setNameOfField(String nameOfField) {
        this.nameOfField = nameOfField;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }
}
