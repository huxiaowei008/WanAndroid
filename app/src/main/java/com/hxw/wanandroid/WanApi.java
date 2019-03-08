package com.hxw.wanandroid;

import com.hxw.wanandroid.entity.ArticleEntity;
import com.hxw.wanandroid.entity.ArticleListEntity;
import com.hxw.wanandroid.entity.BannerEntity;
import com.hxw.wanandroid.entity.BaseEntity;
import com.hxw.wanandroid.entity.BaseListEntity;
import com.hxw.wanandroid.entity.CollectEntity;
import com.hxw.wanandroid.entity.FriendEntity;
import com.hxw.wanandroid.entity.HotKeyEntity;
import com.hxw.wanandroid.entity.NaviEntity;
import com.hxw.wanandroid.entity.TreeEntity;
import com.hxw.wanandroid.entity.UserEntity;
import com.hxw.wanandroid.entity.WebEntity;

import java.util.List;

import kotlinx.coroutines.Deferred;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * WanAndroid的接口api
 *
 * @author hxw
 * @date 2018/6/2
 */
public interface WanApi {
    String BASEURL = "http://www.wanandroid.com/";

    /**
     * 1. 首页相关
     * 1.1 首页文章列表
     *
     * @param page 页码,从0开始
     * @return 首页文章列表数据
     */
    @GET("article/list/{page}/json")
    Deferred<BaseEntity<ArticleListEntity<ArticleEntity>>> getHomeArticle(
            @Path("page") int page
    );

    /**
     * 1.2 首页banner
     *
     * @return Banners数据
     */
    @GET("banner/json")
    Deferred<BaseListEntity<BannerEntity>> getBanner();

    /**
     * 1.3 常用网站
     *
     * @return 常用网站列表数据
     */
    @GET("friend/json")
    Deferred<BaseEntity<List<FriendEntity>>> getFriend();

    /**
     * 1.4 搜索热词
     *
     * @return 热词列表数据
     */
    @GET("hotkey/json")
    Deferred<BaseEntity<List<HotKeyEntity>>> getHotKey();

    /**
     * 1.5 最新项目
     *
     * @param page 页码,从0开始
     * @return 最新项目列表数据
     */
    @GET("article/listproject/{page}/json")
    Deferred<BaseEntity<ArticleListEntity<ArticleEntity>>> getLatestProject(
            @Path("page") int page
    );

    /**
     * 2. 体系
     * 2.1 体系数据
     *
     * @return 体系导航的树状结构数据
     */
    @GET("tree/json")
    Deferred<BaseListEntity<TreeEntity>> getTree();

    /**
     * 2.2 知识体系下的文章
     *
     * @param page 页码,从0开始
     * @param cid  分类的id,上述二级目录的id
     * @return 文章列表数据
     */
    @GET("article/list/{page}/json")
    Deferred<BaseEntity<ArticleListEntity<ArticleEntity>>> getTreeArticle(
            @Path("page") int page,
            @Query("cid") int cid

    );

    /**
     * 3. 导航
     * 3.1 导航数据
     *
     * @return 导航列表数据
     */
    @GET("navi/json")
    Deferred<BaseListEntity<NaviEntity>> getNavi();

    /**
     * 4. 项目
     * 4.1 项目分类
     *
     * @return 项目的分类数据
     */
    @GET("project/tree/json")
    Deferred<BaseListEntity<TreeEntity>> getProjectTree();

    /**
     * 4.2 项目列表数据
     *
     * @param page 页码,从1开始
     * @param cid  分类的id,上面项目分类接口
     * @return 项目列表数据
     */
    @GET("project/list/{page}/json")
    Deferred<BaseEntity<ArticleListEntity<ArticleEntity>>> getProjectList(
            @Path("page") int page,
            @Query("cid") int cid
    );

    /**
     * 5. 登录与注册
     * 5.1 登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息数据
     */
    @POST("user/login")
    Deferred<BaseEntity<UserEntity>> login(
            @Query("username") String username,
            @Query("password") String password
    );

    @POST("http://172.29.6.149:28182/engineering/address/getProjectId")
    Deferred<BaseEntity<Object>> loginDeferred(
            @Query("loginToken") String token,
            @Query("projectId") String projectId
    );

    /**
     * 5.2 注册
     *
     * @param username   用户名
     * @param password   密码
     * @param repassword 再次确认密码
     * @return 用户信息数据
     */
    @POST("user/register")
    Deferred<BaseEntity<UserEntity>> register(
            @Query("username") String username,
            @Query("password") String password,
            @Query("repassword") String repassword
    );

    /**
     * 5.3 退出登录
     *
     * @return 退出成功与否
     */
    @GET("user/logout/json")
    Deferred<BaseEntity<Object>> loginOut();

    /**
     * 6. 收藏
     * 6.1 收藏文章列表
     *
     * @param page 页码,从0开始
     * @return 收藏的文章列表数据
     */
    @GET("lg/collect/list/{page}/json")
    Deferred<BaseEntity<ArticleListEntity<CollectEntity>>> getCollectList(
            @Path("page") int page
    );

    /**
     * 6.2 收藏站内文章
     *
     * @param id 文章id
     * @return 基础格式, 用于判断收藏成功与否
     */
    @POST("lg/collect/{id}/json")
    Deferred<BaseEntity> collect(
            @Path("id") int id
    );

    /**
     * 6.3 收藏站外文章
     *
     * @param title  文章标题
     * @param author 作者
     * @param link   链接
     * @return 基础格式, 用于判断收藏成功与否
     */
    @POST("lg/collect/add/json")
    Deferred<BaseEntity> collect(
            @Query("title") String title,
            @Query("author") String author,
            @Query("link") String link
    );

    /**
     * 6.4 取消收藏
     * 6.4.1 文章列表
     *
     * @param id 列表中文章的id
     * @return 基础格式, 用于判断取消收藏成功与否
     */
    @POST("lg/uncollect_originId/{id}/json")
    Deferred<BaseEntity> unCollect(
            @Path("id") int id
    );

    /**
     * 6.4.2 我的收藏页面（该页面包含自己录入的内容）
     *
     * @param id       列表中文章的id
     * @param originId 代表的是你收藏之前的那篇文章本身的id;但是收藏支持主动添加,这种情况下,没有originId则为-1
     * @return 基础格式, 用于判断取消收藏成功与否
     */
    @POST("lg/uncollect_{originId}/{id}/json")
    Deferred<BaseEntity> unCollect(
            @Path("id") int id,
            @Path("originId") int originId
    );

    /**
     * 6.5 收藏网站列表
     *
     * @return 收藏的网站列表数据
     */
    @GET("lg/collect/usertools/json")
    Deferred<BaseEntity<List<WebEntity>>> getCollectWeb();

    /**
     * 6.6 收藏网址
     *
     * @param name 名称
     * @param link 网址
     * @return 基础格式, 用于判断收藏成功与否
     */
    @POST("lg/collect/addtool/json")
    Deferred<BaseEntity<WebEntity>> collectWeb(
            @Query("name") String name,
            @Query("link") String link
    );

    /**
     * 6.7 编辑收藏网站
     *
     * @param id   收藏的网站id
     * @param name 修改后的名称
     * @param link 修改后的网址
     * @return 基础格式, 用于判断编辑成功与否
     */
    @POST("lg/collect/updatetool/json")
    Deferred<BaseEntity<WebEntity>> updateWeb(
            @Query("id") int id,
            @Query("name") String name,
            @Query("link") String link
    );

    /**
     * 6.8 删除收藏网站
     *
     * @param id 收藏的网站id
     * @return 基础格式, 用于判断删除成功与否
     */
    @POST("lg/collect/deletetool/json")
    Deferred<BaseEntity> deleteWeb(
            @Query("id") int id
    );

    /**
     * 7. 搜索
     * 7.1 搜索
     *
     * @param key  搜索关键词,支持多个关键词,用空格隔开
     * @param page 页码,从0开始
     * @return 文章列表数据
     */
    @POST("article/query/{page}/json")
    Deferred<BaseEntity<ArticleListEntity<ArticleEntity>>> search(
            @Query("k") String key,
            @Path("page") int page
    );

    /**
     * 9. 微信公众号
     * 9.1 获取公众号列表
     *
     * @return 微信公众号列表数据
     */
    @GET("wxarticle/chapters/json")
    Deferred<BaseListEntity<TreeEntity>> getWXPublic();

    /**
     * 9.2 获取公众号文章
     *
     * @param id   公众号ID
     * @param page 页码,从1开始
     * @param key  搜索关键字
     */
    @GET("wxarticle/list/{id}/{page}/json")
    Deferred<BaseEntity<ArticleListEntity<ArticleEntity>>> getWxArticle(
            @Path("id") int id,
            @Path("page") int page,
            @Query("k") String key
    );

}
