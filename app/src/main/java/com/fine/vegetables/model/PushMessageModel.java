package com.fine.vegetables.model;


/**
 * Created by ${houcc} on 2017/5/4.
 */

public class PushMessageModel {

    public static final int TYPE_HOME = 1;
    public static final int TYPE_ORDER_DETAIL = 2;

    private int id;
    private int classify;//1 系统 2 组推 3 单推
    private String dataId;
    private int userId;
    private String content;
    private String title;
    private int type;//1 首页 2 订单详情

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
