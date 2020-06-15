package com.hxw.wanandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_load_image.*

class LoadImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_image)
        iv_show.load("http://39.108.148.248:10002/engineering/pointPosition/pointPosition_19c0f83d35a945f89080ba02c0d22ccb.mp4")
    }
}
