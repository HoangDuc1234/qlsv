package prj.hoangduc1234first

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import prj.hoangduc1234first.R

class CustomItemAnimator : DefaultItemAnimator() {

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.alpha = 0f
        holder.itemView.translationY = holder.itemView.height.toFloat() / 4

        val fadeIn = ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 0f, 1f)
        fadeIn.duration = 300

        val translateUp = ObjectAnimator.ofFloat(holder.itemView, View.TRANSLATION_Y, holder.itemView.height.toFloat() / 4, 0f)
        translateUp.duration = 300

        translateUp.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                dispatchAddFinished(holder)
            }
        })

        fadeIn.start()
        translateUp.start()

        return true
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        val fadeOut = ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 1f, 0f)
        fadeOut.duration = 300

        val translateDown = ObjectAnimator.ofFloat(holder.itemView, View.TRANSLATION_Y, 0f, holder.itemView.height.toFloat() / 4)
        translateDown.duration = 300

        translateDown.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                dispatchRemoveFinished(holder)
            }
        })

        fadeOut.start()
        translateDown.start()

        return true
    }
}
