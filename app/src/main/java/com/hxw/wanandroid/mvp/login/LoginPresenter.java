package com.hxw.wanandroid.mvp.login;

import com.hxw.core.AbstractErrorSubscriber;
import com.hxw.core.di.ActivityScope;
import com.hxw.core.mvp.BasePresenter;
import com.hxw.core.utils.AppUtils;
import com.hxw.wanandroid.WanApi;
import com.hxw.wanandroid.entity.BaseEntity;
import com.hxw.wanandroid.entity.HotKeyEntity;
import com.hxw.wanandroid.entity.UserEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hxw on 2018/6/22.
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<LoginView> {

    @Inject
    WanApi wanApi;

    @Inject
    LoginPresenter() {

    }

    public void login(String username, String password) {
        wanApi.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(this.<BaseEntity<UserEntity>>bindLifecycle())
                .subscribe(new AbstractErrorSubscriber<BaseEntity<UserEntity>>() {

                    @Override
                    public void onNext(BaseEntity<UserEntity> userEntity) {
                        if (userEntity.getErrorCode() == 0) {
                            AppUtils.showToast("登陆成功");
                        } else {
                            AppUtils.showToast("登陆失败->" + userEntity.getErrorMsg());
                        }
                    }

                });

    }

    public void register(String username, String password, String repassword) {
        wanApi.register(username, password, repassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(this.<BaseEntity<UserEntity>>bindLifecycle())
                .subscribe(new AbstractErrorSubscriber<BaseEntity<UserEntity>>() {

                    @Override
                    public void onNext(BaseEntity<UserEntity> userEntity) {
                        if (userEntity.getErrorCode() == 0) {
                            AppUtils.showToast("注册成功");
                        } else {
                            AppUtils.showToast("注册失败->" + userEntity.getErrorMsg());
                        }
                    }
                });
    }

    public void getHotKey() {
        wanApi.getHotKey()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(this.<BaseEntity<List<HotKeyEntity>>>bindLifecycle())
                .subscribe(new AbstractErrorSubscriber<BaseEntity<List<HotKeyEntity>>>() {
                    @Override
                    public void onNext(BaseEntity<List<HotKeyEntity>> listBaseEntity) {
                        if (listBaseEntity.getErrorCode()==0){
                            AppUtils.showToast("热词成功");
                        }
                    }
                });
    }
}
