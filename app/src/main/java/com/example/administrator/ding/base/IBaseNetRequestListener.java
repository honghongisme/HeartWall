package com.example.administrator.ding.base;

public interface IBaseNetRequestListener {

    /**
     * 请求成功
     */
    void onSuccess();

    /**
     * 网络异常等错误
     */
    void onFailed();

}
