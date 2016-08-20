package com.kit.cn.smartkit.db_sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.kit.cn.library.db.ORM.BuilderOrmInstance;
import com.kit.cn.library.db.ORM.extra.Extra;
import com.kit.cn.library.ioc.annotations.field.InjectChildView;
import com.kit.cn.library.ioc.annotations.field.InjectContentView;
import com.kit.cn.library.widget.toast.ToastUtil;
import com.kit.cn.library.utils.log.Logger;
import com.kit.cn.smartkit.R;
import com.kit.cn.smartkit.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouwen on 16/8/12.
 */

@InjectContentView(value = R.layout.fragment_db)
public class ORMFragment extends BaseFragment {

    private List<OrmInfo> mList = new ArrayList<>();

    @InjectChildView(value = R.id.insert, listener = View.OnClickListener.class)
    private Button mBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData("zhouwen", "female", "alibaba");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData(String name, String sex, String company) {
        OrmInfo ormInfo;
        for (int i = 0; i < 10; i++) {
            ormInfo = new OrmInfo();
            ormInfo.setCompany(company + i);
            ormInfo.setName(name + i);
            ormInfo.setSex(sex + i);
            mList.add(ormInfo);
        }
    }

    public static Extra getExtra() {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append("orm_db");
        keyBuilder.append("type");
        return new Extra(String.valueOf(1234), keyBuilder.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert:
                BuilderOrmInstance.getORMSQLite(getActivity()).insert(getExtra(), mList);
                break;
            case R.id.update:
                initData("chowen", "male", "baidu");
                BuilderOrmInstance.getORMSQLite(getActivity()).update(getExtra(), mList);
                break;
            case R.id.remove:
                // BuilderOrmInstance.getORMSQLite().delete(OrmInfo.class);
                break;
            case R.id.query:
                Logger.e("query>>>>" + BuilderOrmInstance.getORMSQLite(getActivity()).select(getExtra(), OrmInfo.class).toString());
                List<OrmInfo> lists = BuilderOrmInstance.getORMSQLite(getActivity()).select(getExtra(), OrmInfo.class);
                if (lists != null) {
                    OrmInfo info = lists.get(0);
                    ToastUtil.showToast(getContext(),
                            "name>" + info.name + ">sex>" + info.sex + ">company>" + info.company);
                }

                break;
        }
        super.onClick(v);
    }
}
