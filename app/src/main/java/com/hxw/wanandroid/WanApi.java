package com.hxw.wanandroid;

import com.hxw.wanandroid.entity.BaseEntity;
import com.hxw.wanandroid.entity.UserEntity;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * WanAndroid的接口api
 *
 * @author hxw on 2018/6/2.
 */
public interface WanApi {
    String BASEURL = "http://www.wanandroid.com/";

    /**
     * 登陆
     */
    @POST("user/login")
    Observable<BaseEntity<UserEntity>> login(
            @Query("username") String username,
            @Query("password") String password
    );

    /**
     * 注册
     */
    @POST("user/register")
    Observable<BaseEntity<UserEntity>> register(
            @Query("username") String username,
            @Query("password") String password,
            @Query("repassword") String repassword
    );


}
