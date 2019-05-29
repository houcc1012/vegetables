package com.fine.vegetables.net;

import com.android.volley.Request;
import com.fine.httplib.ApiParams;
import com.fine.vegetables.Preference;
import com.fine.vegetables.model.SubmitConfirmOrder;
import com.fine.vegetables.model.SubmitOrder;
import com.google.gson.Gson;

import java.io.Serializable;

public class Client {

    private static final int POST = Request.Method.POST;

    public static final int PAGE_SIZE = 20;

    /**
     * /api/user/login.do
     */
    public static API login(String account, String password) {
        return new API(POST, "/api/user/login.do", new ApiParams()
                .put("account", account)
                .put("password", password)
                .put("deviceId", Preference.get().getPushClientId())
        );
    }

    /**
     * /api/user/logout.do
     */
    public static API logout() {
        return new API(POST, "/api/user/logout.do", new ApiParams());
    }

    /**
     * 查询用户信息
     * /api/user/userInfo.do
     */
    public static API getUserInfo() {
        return new API("/api/user/userInfo.do", new ApiParams());
    }

    /**
     * 商品分类列表 api/vegetable/group/list.do
     */

    public static API getCategoryList() {
        return new API("/api/vegetable/group/list.do", new ApiParams());
    }

    /**
     * 商品列表
     * /api/vegetable/search.do
     */

    public static API getCommodityList(String name, int page, int pageSize) {
        return new API("/api/vegetable/search.do", new ApiParams()
                .put("name", name)
                .put("page", page)
                .put("pageSize", pageSize));
    }

    public static API getCommodityList(int groupId, int page, int pageSize) {
        return new API("/api/vegetable/search.do", new ApiParams()
                .put("groupId", groupId)
                .put("page", page)
                .put("pageSize", pageSize));
    }


    /**
     * 提交订单
     *
     * @param
     */
    public static API submitOrder(SubmitOrder submitOrder) {
        return new API(POST, "/api/order/entrust.do", new Gson().toJson(submitOrder));

    }

    /**
     * 用户订单列表
     * api/order/list.do
     *
     * @param type 不穿查全部  1是待送达 2 已完成
     */
    public static API getUserOrderList(int type, int page, int pageSize) {
        return new API("/api/order/list.do", new ApiParams()
                .put("type", type)
                .put("page", page)
                .put("pageSize", pageSize));
    }

    /**
     * 订单详情
     * /api/order/orderDetail.do
     */

    public static API getUserOrderDetail(String orderId) {
        return new API("/api/order/orderDetail.do", new ApiParams()
                .put("orderId", orderId));
    }

    /**
     * 卖家订单列表
     *
     * @param type 1.代送订单 2:历史订单
     */
    public static API getSellerOrderList(int type, int page, int pageSize) {
        return new API("/api/order/list.do", new ApiParams()
                .put("type", type)
                .put("page", page)
                .put("pageSize", pageSize));
    }

    /**
     * 确认订单
     */

    public static API confirmOrder(String orderId, String actualMoney) {
        return new API(POST, "/api/order/confirm.do", new ApiParams()
                .put("orderId", orderId)
                .put("actualMoney", actualMoney));
    }

    /**
     * 订单详情
     * /api/order/orderDetail.do
     */

    public static API getSupplierOrderDetail(String orderId) {
        return new API("/api/order/orderDetail.do", new ApiParams()
                .put("orderId", orderId));
    }

    /**
     * /api/user/concat.do
     */
    public static API getSupplierPhone() {
        return new API("/api/user/concat.do", new ApiParams());
    }

    /**
     * 提交订单
     *
     * @param
     */
    public static API comfirmOrder(SubmitConfirmOrder submitOrder) {
        return new API(POST, "/api/order/modify.do", new Gson().toJson(submitOrder));

    }

}
