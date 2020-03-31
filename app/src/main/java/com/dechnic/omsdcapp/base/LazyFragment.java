package com.dechnic.omsdcapp.base;

/**
 * Created by Administrator on 2017/3/24.
 * 懒加载
 */

public abstract class LazyFragment extends BaseFragment{
    protected boolean isVisible;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = CCApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }

    protected abstract void lazyLoad();

}
