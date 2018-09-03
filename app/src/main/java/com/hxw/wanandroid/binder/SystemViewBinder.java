package com.hxw.wanandroid.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxw.wanandroid.R;
import com.hxw.wanandroid.entity.TreeEntity;

import me.drakeet.multitype.ItemViewBinder;

/**
 * @author hxw on 2018/8/25
 */
public class SystemViewBinder extends ItemViewBinder<TreeEntity, SystemViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_system, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TreeEntity item) {
        holder.tvTitle.setText(item.getName());
        if (item.getChildren() != null) {
            StringBuilder content = new StringBuilder();
            for (TreeEntity data : item.getChildren()) {
                content.append(data.getName()).append("   ");
            }

            holder.tvContent.setText(content);
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_system_title);
            tvContent = itemView.findViewById(R.id.tv_system_content);
        }
    }
}
