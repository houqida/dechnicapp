package com.dechnic.omsdcapp.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * Created by Administrator on 2017/3/22.
 */

public abstract class BaseFragment<T> extends Fragment {
    private View view ;
    private LayoutInflater inflater;
    private ViewGroup container ;

    public View setContentView(int resourceId) {
        view = inflater.inflate(resourceId, container, false);
        return view;
    }
    public View getContentView(){
        return  this.view ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater ;
        this.container = container ;
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    /**
     * 获取控件id
     * @param id
     * @return
     */
    public View findViewById(int id){
        return view.findViewById(id) ;
    }
}
