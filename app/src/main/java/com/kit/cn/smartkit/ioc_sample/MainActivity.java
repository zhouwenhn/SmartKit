package com.kit.cn.smartkit.ioc_sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;


import com.kit.cn.library.ioc.annotations.field.InjectChildView;
import com.kit.cn.library.ioc.annotations.field.InjectContentView;
import com.kit.cn.library.utils.log.L;
import com.kit.cn.smartkit.BaseComponentFragment;
import com.kit.cn.smartkit.BizComponentFragment;
import com.kit.cn.smartkit.CommonFrameFragment;
import com.kit.cn.smartkit.CommonWidgetFragment;
import com.kit.cn.smartkit.R;
import com.kit.cn.smartkit.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

@InjectContentView(value = R.layout.fragment_main)
public class MainActivity extends BaseActivity {

    private SparseArray<Fragment> mFragmentArray = new SparseArray<>();

    private List<String> mFragmentTitle = new ArrayList<>();

    @InjectChildView(R.id.container)
    private ViewPager mViewPager;

    @InjectChildView(R.id.toolbar)
    private Toolbar toolbar;

//    @InjectChildView(R.id.tabs)
//    private TabLayout tabLayout;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d("chowen>>>>");
        initFragments();
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
//        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initFragments() {
//        mFragmentArray.append(0, new DBFragment());
//        mFragmentArray.append(1, new HttpFragment());
//        mFragmentArray.append(2, new DownloadFragment());
//        mFragmentArray.append(3, new ViewAnnotationFragment());
//        mFragmentArray.append(4, new WidgetFragment());
//        mFragmentArray.append(5, new CacheFragment());
//        mFragmentArray.append(6, new MessageFragment());
//        mFragmentArray.append(7, new UtilsFragment());
//        mFragmentArray.append(8, new CommonFrameFragment());


//        mFragmentTitle.add("数据库");
//        mFragmentTitle.add("网络");
//        mFragmentTitle.add("下载");
//        mFragmentTitle.add("注解");
//        mFragmentTitle.add("控件");
//        mFragmentTitle.add("缓存");
//        mFragmentTitle.add("消息");
//        mFragmentTitle.add("Utils");
//        mFragmentTitle.add("框架");

        mFragmentArray.append(0, new BaseComponentFragment());
        mFragmentArray.append(1, new BizComponentFragment());
        mFragmentArray.append(2, new CommonWidgetFragment());
        mFragmentArray.append(3, new CommonFrameFragment());

        mFragmentTitle.add("基础组件");
        mFragmentTitle.add("业务组件");
        mFragmentTitle.add("通用控件");
        mFragmentTitle.add("通用框架");
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentArray.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentArray.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitle.get(position);
        }
    }
}
