package com.example.administrator.ding.module.nail;

import com.example.administrator.ding.model.entry.Nail;

import java.util.ArrayList;

/**
 * 获取钉帽View信息数组
 * Created by Administrator on 2018/8/25.
 */

public interface OnGetInitNailListListener {

    void onSuccess(ArrayList<Nail> nailList);
}
