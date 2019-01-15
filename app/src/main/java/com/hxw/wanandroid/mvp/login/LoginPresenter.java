package com.hxw.wanandroid.mvp.login;

import com.hxw.core.mvp.BasePresenter;
import com.hxw.core.utils.AppUtils;
import com.hxw.wanandroid.Constant;
import com.hxw.wanandroid.WanApi;
import com.hxw.wanandroid.entity.BaseEntity;
import com.hxw.wanandroid.entity.HotKeyEntity;
import com.hxw.wanandroid.entity.UserEntity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hxw on 2018/6/22.
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    private WanApi wanApi;

    LoginPresenter(WanApi api) {
        this.wanApi = api;
    }


    public void login(String username, String password) {
        wanApi.login(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .as(this.<BaseEntity<UserEntity>>bindLifecycle())
                .subscribe(userEntity -> {
                    if (userEntity.getErrorCode() == Constant.NET_SUCCESS) {
                        AppUtils.showToast("登陆成功");
                        getMView().loginOrRegisterSuccess();
                    } else {
                        AppUtils.showToast("登陆失败->" + userEntity.getErrorMsg());
                    }
                });

    }

    public void register(String username, String password, String repassword) {
        wanApi.register(username, password, repassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(this.<BaseEntity<UserEntity>>bindLifecycle())
                .subscribe(userEntity -> {
                    if (userEntity.getErrorCode() == Constant.NET_SUCCESS) {
                        AppUtils.showToast("注册成功");
                    } else {
                        AppUtils.showToast("注册失败->" + userEntity.getErrorMsg());
                    }
                });
    }

    public void getHotKey() {
        wanApi.getHotKey()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(this.<BaseEntity<List<HotKeyEntity>>>bindLifecycle())
                .subscribe(listBaseEntity -> {
                    if (listBaseEntity.getErrorCode() == Constant.NET_SUCCESS) {
                        AppUtils.showToast("热词成功");
                    }
                });
    }
}
