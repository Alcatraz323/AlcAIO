package org.edgeration.sdk.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.widget.TextView;

import org.edgeration.sdk.misc.Constants;

public class AnimateUtils {
    public static final int VALUE_ANIMATOR_FLAG_WIDTH = 0;
    public static final int VALUE_ANIMATOR_FLAG_HEIGHT = 1;
    public static final int VALUE_ANIMATOR_FLAG_BOTH = 2;

    public static void playRevealStart(final View target, SimpleAnimateInterface animateInterface) {
        target.setVisibility(View.VISIBLE);
        Animator circularReveal
                = ViewAnimationUtils.createCircularReveal(target,
                (int) target.getX(), (int) target.getY(), 0,
                (float) Math.hypot(target.getWidth(), target.getHeight()));
        circularReveal.setInterpolator(new AccelerateInterpolator());
        circularReveal.setDuration(Constants.DEF_ANIMATION_DURATION_FOR_REVEAL);
        circularReveal.start();
        circularReveal.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator p1) {
                target.setVisibility(View.VISIBLE);
                AnimationSet animationSet = new AnimationSet(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(Constants.DEF_ANIMATION_DURATION_FOR_REVEAL);
                animationSet.addAnimation(alphaAnimation);
                target.startAnimation(animationSet);
                animationSet.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation p1) {

                    }

                    @Override
                    public void onAnimationEnd(Animation p1) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation p1) {

                    }
                });
            }

            @Override
            public void onAnimationEnd(Animator p1) {
                target.setVisibility(View.VISIBLE);
                if (animateInterface != null)
                    animateInterface.onEnd();
            }

            @Override
            public void onAnimationCancel(Animator p1) {

            }

            @Override
            public void onAnimationRepeat(Animator p1) {

            }
        });
    }

    public static void playRevealCollapse(final View target) {
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(target,
                (int) target.getX(), (int) target.getY(),
                (float) Math.hypot(target.getWidth(), target.getHeight()), 0);
        circularReveal.setInterpolator(new AccelerateInterpolator());
        circularReveal.setDuration(Constants.DEF_ANIMATION_DURATION_FOR_REVEAL);
        circularReveal.start();
        circularReveal.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator p1) {

            }

            @Override
            public void onAnimationEnd(Animator p1) {
                target.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator p1) {

            }

            @Override
            public void onAnimationRepeat(Animator p1) {

            }
        });
    }

    public static void textChange(final TextView txv, final CharSequence tx) {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation fadeOutAnim = new AlphaAnimation(1, 0);
        fadeOutAnim.setDuration(Constants.DEF_ANIMATION_DURATION_FOR_FADE);
        animationSet.addAnimation(fadeOutAnim);
        txv.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation p1) {

            }

            @Override
            public void onAnimationEnd(Animation p1) {
                txv.setVisibility(View.GONE);
                txv.setText(tx);
                AnimationSet set = new AnimationSet(true);
                AlphaAnimation fadeInAnim = new AlphaAnimation(0, 1);
                fadeInAnim.setDuration(Constants.DEF_ANIMATION_DURATION_FOR_FADE);
                set.addAnimation(fadeInAnim);
                txv.startAnimation(set);
                set.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation p1) {

                    }

                    @Override
                    public void onAnimationEnd(Animation p1) {
                        txv.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation p1) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation p1) {

            }
        });

    }

    public static void fadeIn(View v, SimpleAnimateInterface animateInterface) {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation fadeInAnim = new AlphaAnimation(0, 1);
        fadeInAnim.setDuration(Constants.DEF_ANIMATION_DURATION_FOR_FADE);
        animationSet.addAnimation(fadeInAnim);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation p1) {
            }

            @Override
            public void onAnimationEnd(Animation p1) {
                v.setVisibility(View.VISIBLE);
                if (animateInterface != null)
                    animateInterface.onEnd();
            }

            @Override
            public void onAnimationRepeat(Animation p1) {

            }
        });
    }

    public static void fadeOut(View v, SimpleAnimateInterface animateInterface) {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation fadeOutAnim = new AlphaAnimation(1, 0);
        fadeOutAnim.setDuration(Constants.DEF_ANIMATION_DURATION_FOR_FADE);
        animationSet.addAnimation(fadeOutAnim);
        v.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation p1) {
            }

            @Override
            public void onAnimationEnd(Animation p1) {
                v.setVisibility(View.INVISIBLE);
                if (animateInterface != null)
                    animateInterface.onEnd();
            }

            @Override
            public void onAnimationRepeat(Animation p1) {

            }
        });
    }

    public static void doWidthHeightValueAnimator(View target, Interpolator interpolator, int duration,
                                                  int flag, int... targetVal) {
        ValueAnimator animator = ValueAnimator.ofInt(targetVal);
        animator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams animatingParams = target.getLayoutParams();
            if (flag == VALUE_ANIMATOR_FLAG_WIDTH || flag == VALUE_ANIMATOR_FLAG_BOTH) {
                animatingParams.width = (int) animation.getAnimatedValue();
            }
            if (flag == VALUE_ANIMATOR_FLAG_HEIGHT || flag == VALUE_ANIMATOR_FLAG_BOTH) {
                animatingParams.height = (int) animation.getAnimatedValue();
            }
            target.setLayoutParams(animatingParams);
        });
        animator.setInterpolator(interpolator);
        animator.setDuration(duration);
        animator.start();
    }

    public static void doTextSizeValueAnimator(TextView target, Interpolator interpolator, int duration,
                                               float... targetVal) {
        ValueAnimator animator = ValueAnimator.ofFloat(targetVal);
        animator.addUpdateListener(animation -> {
            target.setTextSize(TypedValue.COMPLEX_UNIT_PX, (Float) animation.getAnimatedValue());
        });
        animator.setInterpolator(interpolator);
        animator.setDuration(duration);
        animator.start();
    }

    public interface SimpleAnimateInterface {
        void onEnd();
    }
}