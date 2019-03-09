package com.example.administrator.ding.presenter;

import com.example.administrator.ding.model.entities.Nail;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取钉帽View信息数组
 * Created by Administrator on 2018/8/25.
 */

public interface OnGetInitNailListListener {

    void onSuccess(ArrayList<Nail> nailList);
}
