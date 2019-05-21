package com.fine.vegetables.net;

import com.android.volley.Request;
import com.fine.httplib.ApiParams;

public class Client {

    private static final int POST = Request.Method.POST;

    /**
     * /user/appVersion/getUpdateVersion.do
     * GET
     * 获取App版本更新情况
     * app版本情况
     * platform  0 android
     *
     * @return
     */
    public static API updateVersion() {
        return new API("/user/appVersion/queryForceVersion.do", new ApiParams()
                .put("platform", 0)
//                .put("pCode", API.getPCode())
//                .put("sign", API.getSign())
        ) ;
    }
}
