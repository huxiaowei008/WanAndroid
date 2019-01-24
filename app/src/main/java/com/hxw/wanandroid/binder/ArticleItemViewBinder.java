package com.hxw.wanandroid.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hxw.wanandroid.R;
import com.hxw.wanandroid.entity.ArticleData;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author hxw on 2018/7/28
 */
public class ArticleItemViewBinder {


//    @NonNull
//    @Override
//    protected ArticleHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
//        View root = inflater.inflate(R.layout.item_article_list, parent, false);
//
//        return new ArticleHolder(root);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull ArticleHolder holder, @NonNull ArticleData item) {
//        holder.tvauthor.setText(item.getAuthor());
//        holder.tvtitle.setText(item.getTitle());
//        String classification = item.getSuperChapterName() + "/" + item.getChapterName();
//        holder.tvClassification.setText(classification);
//        holder.tvtime.setText(item.getNiceDate());
//    }
//
//    static class ArticleHolder extends RecyclerView.ViewHolder {
//
//        private TextView tvauthor;
//        private TextView tvClassification;
//        private TextView tvtitle;
//        private TextView tvtime;
//        private ImageView ivfavorite;
//
//        ArticleHolder(@NonNull View itemView) {
//            super(itemView);
//            this.ivfavorite = itemView.findViewById(R.id.iv_favorite);
//            this.tvtime = itemView.findViewById(R.id.tv_time);
//            this.tvtitle = itemView.findViewById(R.id.tv_title);
//            this.tvClassification = itemView.findViewById(R.id.tv_classification);
//            this.tvauthor = itemView.findViewById(R.id.tv_author);
//        }
//    }
}
