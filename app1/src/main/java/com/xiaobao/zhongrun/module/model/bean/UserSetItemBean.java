package com.xiaobao.zhongrun.module.model.bean;

public class UserSetItemBean {

    private String itemName;
    private String itemValue;

    public UserSetItemBean() {
    }

    public UserSetItemBean(String itemName, String itemValue) {
        this.itemName = itemName;
        this.itemValue = itemValue;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
}
