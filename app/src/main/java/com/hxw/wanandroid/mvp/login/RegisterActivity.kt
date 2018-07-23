package com.hxw.wanandroid.mvp.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import com.hxw.core.base.AbstractActivity
import com.hxw.core.utils.AppUtils
import com.hxw.wanandroid.R
import kotlinx.android.synthetic.main.activity_register.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.jxinject.jx

/**
 * @author hxw on 2018/6/6.
 *
 */
class RegisterActivity : AbstractActivity(), LoginView, KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val mPresenter: LoginPresenter by lazy { kodein.jx.newInstance<LoginPresenter>() }

    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun init(savedInstanceState: Bundle?) {
        mPresenter.takeView(this)
        fa_btn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                animateRevealClose()
            } else {
                finishActivity()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            showEnterAnimation()
        }

        btn_register.setOnClickListener {
            val username = et_username.text.toString()
            val password = et_password.text.toString()
            val repassword = et_repeat_password.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty() && repassword.isNotEmpty()) {
                mPresenter.register(username, password, repassword)
            } else {
                AppUtils.showToast("信息未填完整")
            }

        }
    }

    override fun onResume() {
        super.onResume()
    }

    /**
     * 若要在完成第二个活动时反转场景转换动画,请调用Activity.finishAfterTransition()方法
     * 而不是Activity.finish(),注意版本5.0
     */
    private fun finishActivity() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
        }
    }

    /**
     * 制作展示进入的动画
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun showEnterAnimation() {


        val transition = TransitionInflater.from(this).inflateTransition(R.transition.fab_transition)

        window.sharedElementEnterTransition = transition
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                card_register.visibility = View.GONE
            }


            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                animateRevealShow()
            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }


        })

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun animateRevealShow() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(card_register, card_register.width / 2,
                0, (fa_btn.width / 2).toFloat(), card_register.height.toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                card_register.visibility = View.VISIBLE
            }
        })
        mAnimator.start()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun animateRevealClose() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(card_register, card_register.width / 2,
                0, card_register.height.toFloat(), (fa_btn.width / 2).toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                card_register.visibility = View.INVISIBLE
                finishActivity()
            }
        })
        mAnimator.start()
    }

    override fun onBackPressed() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateRevealClose()
        } else {
            super.onBackPressed()
        }
    }
}