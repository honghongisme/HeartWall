package com.example.administrator.ding.listener;

import com.example.administrator.ding.bean.Nail;

import java.util.List;

/**
 * 获取钉帽View信息数组
 * Created by Administrator on 2018/8/25.
 */

public interface OnGetNailListListener {

    void onSuccess(List<Nail> nailList);
}
