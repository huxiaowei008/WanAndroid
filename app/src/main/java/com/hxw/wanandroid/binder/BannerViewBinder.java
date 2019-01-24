package com.hxw.wanandroid.binder;

/**
 * @author hxw on 2018/8/12
 */
public class BannerViewBinder {

//    @NonNull
//    @Override
//    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
//        View root = inflater.inflate(R.layout.item_banner, parent, false);
//        return new ViewHolder(root);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull BannerListEntity banner) {
//        List<String> mBannerTitleList=new ArrayList<>();
//        List<String> bannerImageList = new ArrayList<>();
//        List<String> mBannerUrlList = new ArrayList<>();
//        for (BannerEntity bannerEntity:banner.getData()){
//            mBannerTitleList.add(bannerEntity.getTitle());
//            bannerImageList.add(bannerEntity.getImagePath());
//            mBannerUrlList.add(bannerEntity.getUrl());
//        }
//
//        //设置banner样式
//        holder.banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
//        //设置图片加载器
//        holder.banner.setImageLoader(new GlideImageLoader());
//        //设置图片集合
//        holder.banner.setImages(bannerImageList);
//        //设置banner动画效果
//        holder.banner.setBannerAnimation(Transformer.DepthPage);
//        //设置标题集合（当banner样式有显示title时）
//        holder.banner.setBannerTitles(mBannerTitleList);
//        //设置自动轮播，默认为true
//        holder.banner.isAutoPlay(true);
//        //设置轮播时间
//        holder.banner.setDelayTime(2000);
//        //设置指示器位置（当banner模式中有指示器时）
//        holder.banner.setIndicatorGravity(BannerConfig.CENTER);
//
//        holder.banner.setOnBannerListener(position -> AppUtils.showToast("position "+position));
//        //banner设置方法全部调用完毕时最后调用
//        holder.banner.start();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        Banner banner;
//
//        ViewHolder(View itemView) {
//            super(itemView);
//            banner = itemView.findViewById(R.id.banner);
//        }
//    }
}
