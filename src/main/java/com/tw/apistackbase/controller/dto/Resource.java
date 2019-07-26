package com.tw.apistackbase.controller.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.xml.bind.annotation.XmlAnyElement;

public class Resource<T> {

    private T content;

    public Resource() {
    }

    public Resource(T content) {
        this.content = content;
    }

    @JsonUnwrapped
    @XmlAnyElement
    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

}
