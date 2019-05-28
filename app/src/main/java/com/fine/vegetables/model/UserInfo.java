package com.fine.vegetables.model;

import java.io.Serializable;


/**
 * 登录后的用户信息数据
 */
public class UserInfo implements Serializable {

    public static final int TYPE_BUYER = 1;
    public static final int TYPE_SUPPLIER = 2;

    private String account;
    private String addr;
    private String concatName;
    private String name;
    private String phone;
    /**
     * 1 商户 2 配送
     */
    private int type;

    public int getType() {
        return type;
    }

    public String getAccount() {
        return account;
    }

    public String getAddr() {
        return addr;
    }

    public String getConcatName() {
        return concatName;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void setConcatName(String concatName) {
        this.concatName = concatName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(int type) {
        this.type = type;
    }
}


