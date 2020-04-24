package com.hxw.wanandroid

import com.hxw.wanandroid.entity.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * WanAndroid的接口api
 *
 * @author hxw
 * @date 2018/6/2
 */
interface WanApi {
    /**
     * 1. 首页相关
     * 1.1 首页文章列表
     *
     * @param page 页码,从0开始
     * @return 首页文章列表数据
     */
    @GET("article/list/{page}/json")
    suspend fun getHomeArticle(
        @Path("page") page: Int
    ): BaseEntity<ArticleListEntity<ArticleEntity>>

    /**
     * 1.2 首页banner
     *
     * @return Banners数据
     */
    @GET("banner/json")
    suspend fun getBanner(): BaseListEntity<BannerEntity>

    /**
     * 1.3 常用网站
     *
     * @return 常用网站列表数据
     */
    @GET("friend/json")
    suspend fun getFriend(): BaseEntity<List<FriendEntity>>

    /**
     * 1.4 搜索热词
     *
     * @return 热词列表数据
     */
    @GET("hotkey/json")
    suspend fun getHotKey(): BaseEntity<List<HotKeyEntity>>

    /**
     * 1.5 最新项目
     *
     * @param page 页码,从0开始
     * @return 最新项目列表数据
     */
    @GET("article/listproject/{page}/json")
    suspend fun getLatestProject(
        @Path("page") page: Int
    ): BaseEntity<ArticleListEntity<ArticleEntity>>

    /**
     * 2. 体系
     * 2.1 体系数据
     *
     * @return 体系导航的树状结构数据
     */
    @GET("tree/json")
    suspend fun getTree(): BaseListEntity<TreeEntity>

    /**
     * 2.2 知识体系下的文章
     *
     * @param page 页码,从0开始
     * @param cid  分类的id,上述二级目录的id
     * @return 文章列表数据
     */
    @GET("article/list/{page}/json")
    suspend fun getTreeArticle(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseEntity<ArticleListEntity<ArticleEntity>>

    /**
     * 3. 导航
     * 3.1 导航数据
     *
     * @return 导航列表数据
     */
    @GET("navi/json")
    suspend fun getNavi(): BaseListEntity<NaviEntity>

    /**
     * 4. 项目
     * 4.1 项目分类
     *
     * @return 项目的分类数据
     */
    @GET("project/tree/json")
    suspend fun getProjectTree(): BaseListEntity<TreeEntity>

    /**
     * 4.2 项目列表数据
     *
     * @param page 页码,从1开始
     * @param cid  分类的id,上面项目分类接口
     * @return 项目列表数据
     */
    @GET("project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseEntity<ArticleListEntity<ArticleEntity>>

    /**
     * 5. 登录与注册
     * 5.1 登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息数据
     */
    @POST("user/login")
    suspend fun login(
        @Query("username") username: String?,
        @Query("password") password: String?
    ): BaseEntity<UserEntity>

    @POST("http://172.29.6.149:28182/engineering/address/getProjectId")
    suspend fun loginDeferred(
        @Query("loginToken") token: String?,
        @Query("projectId") projectId: String?
    ): BaseEntity<Any>

    /**
     * 5.2 注册
     *
     * @param username   用户名
     * @param password   密码
     * @param repassword 再次确认密码
     * @return 用户信息数据
     */
    @POST("user/register")
    suspend fun register(
        @Query("username") username: String?,
        @Query("password") password: String?,
        @Query("repassword") repassword: String?
    ): BaseEntity<UserEntity>

    /**
     * 5.3 退出登录
     *
     * @return 退出成功与否
     */
    @GET("user/logout/json")
    suspend fun loginOut(): BaseEntity<Any>

    /**
     * 6. 收藏
     * 6.1 收藏文章列表
     *
     * @param page 页码,从0开始
     * @return 收藏的文章列表数据
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectList(
        @Path("page") page: Int
    ): BaseEntity<ArticleListEntity<CollectEntity>>

    /**
     * 6.2 收藏站内文章
     *
     * @param id 文章id
     * @return 基础格式, 用于判断收藏成功与否
     */
    @POST("lg/collect/{id}/json")
    suspend fun collect(
        @Path("id") id: Int
    ): BaseEntity<*>

    /**
     * 6.3 收藏站外文章
     *
     * @param title  文章标题
     * @param author 作者
     * @param link   链接
     * @return 基础格式, 用于判断收藏成功与否
     */
    @POST("lg/collect/add/json")
    suspend fun collect(
        @Query("title") title: String?,
        @Query("author") author: String?,
        @Query("link") link: String?
    ): BaseEntity<*>

    /**
     * 6.4 取消收藏
     * 6.4.1 文章列表
     *
     * @param id 列表中文章的id
     * @return 基础格式, 用于判断取消收藏成功与否
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollect(
        @Path("id") id: Int
    ): BaseEntity<*>

    /**
     * 6.4.2 我的收藏页面（该页面包含自己录入的内容）
     *
     * @param id       列表中文章的id
     * @param originId 代表的是你收藏之前的那篇文章本身的id;但是收藏支持主动添加,这种情况下,没有originId则为-1
     * @return 基础格式, 用于判断取消收藏成功与否
     */
    @POST("lg/uncollect_{originId}/{id}/json")
    suspend fun unCollect(
        @Path("id") id: Int,
        @Path("originId") originId: Int
    ): BaseEntity<*>

    /**
     * 6.5 收藏网站列表
     *
     * @return 收藏的网站列表数据
     */
    @GET("lg/collect/usertools/json")
    suspend fun getCollectWeb(): BaseEntity<List<WebEntity>>

    /**
     * 6.6 收藏网址
     *
     * @param name 名称
     * @param link 网址
     * @return 基础格式, 用于判断收藏成功与否
     */
    @POST("lg/collect/addtool/json")
    suspend fun collectWeb(
        @Query("name") name: String?,
        @Query("link") link: String?
    ): BaseEntity<WebEntity>

    /**
     * 6.7 编辑收藏网站
     *
     * @param id   收藏的网站id
     * @param name 修改后的名称
     * @param link 修改后的网址
     * @return 基础格式, 用于判断编辑成功与否
     */
    @POST("lg/collect/updatetool/json")
    suspend fun updateWeb(
        @Query("id") id: Int,
        @Query("name") name: String?,
        @Query("link") link: String?
    ): BaseEntity<WebEntity>

    /**
     * 6.8 删除收藏网站
     *
     * @param id 收藏的网站id
     * @return 基础格式, 用于判断删除成功与否
     */
    @POST("lg/collect/deletetool/json")
    suspend fun deleteWeb(
        @Query("id") id: Int
    ): BaseEntity<*>

    /**
     * 7. 搜索
     * 7.1 搜索
     *
     * @param key  搜索关键词,支持多个关键词,用空格隔开
     * @param page 页码,从0开始
     * @return 文章列表数据
     */
    @POST("article/query/{page}/json")
    suspend fun search(
        @Query("k") key: String?,
        @Path("page") page: Int
    ): BaseEntity<ArticleListEntity<ArticleEntity>>

    /**
     * 9. 微信公众号
     * 9.1 获取公众号列表
     *
     * @return 微信公众号列表数据
     */
    @GET("wxarticle/chapters/json")
    suspend fun getWXPublic(): BaseListEntity<TreeEntity>

    /**
     * 9.2 获取公众号文章
     *
     * @param id   公众号ID
     * @param page 页码,从1开始
     * @param key  搜索关键字
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWxArticle(
        @Path("id") id: Int,
        @Path("page") page: Int,
        @Query("k") key: String?
    ): BaseEntity<ArticleListEntity<ArticleEntity>>

    companion object {
        const val BASEURL = "https://www.wanandroid.com/"
    }
}