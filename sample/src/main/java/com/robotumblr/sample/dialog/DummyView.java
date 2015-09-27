package com.robotumblr.sample.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.robotumblr.sample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexander Sokol
 * on 17.09.15 12:43.
 */
public class DummyView extends FrameLayout {


    private static final int INITIAL_DURATION = 250;
    private static final int MIN_DURATION = 500;
    private static final int MAX_DURATION = 5000;

    private List<MovementAnimator> mAnimators = new ArrayList<>();

    public DummyView(Context context) {
        super(context);
        init();
    }

    public DummyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DummyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int minCount = getContext().getResources().getInteger(R.integer.min_circle_count);
        int maxCount = getContext().getResources().getInteger(R.integer.max_circle_count);
        int minSize = getContext().getResources().getDimensionPixelSize(R.dimen.min_circle_size);
        int maxSize = getContext().getResources().getDimensionPixelSize(R.dimen.max_circle_size);

        int count = randomInt(minCount, maxCount);
        for (int i = 0; i < count; i++) {
            View view = new View(getContext());
            view.setBackgroundResource(getRandomBg());
            int size = randomInt(minSize, maxSize);
            addView(view, size, size);
            mAnimators.add(new MovementAnimator(view));
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);

                for (MovementAnimator animator : mAnimators) {
                    int centerX = (getWidth() / 2) - (animator.view.getWidth() / 2);
                    int centerY = (getHeight() / 2) - (animator.view.getHeight() / 2);
                    animator.view.setTranslationX(centerX);
                    animator.view.setTranslationY(centerY);
                    animator.run(true);
                }
            }
        });
    }


    private int randomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    private int getRandomBg() {
        Random random = new Random();
        int rand = random.nextInt(5);
        switch (rand) {
            case 0:
                return R.drawable.shape_blue;
            case 1:
                return R.drawable.shape_green;
            case 2:
                return R.drawable.shape_purple;
            case 3:
                return R.drawable.shape_red;
            case 4:
                return R.drawable.shape_yellow;
            default:
                return R.drawable.shape_default;
        }
    }


    private class MovementAnimator implements Animator.AnimatorListener {

        View view;
        Animator animatorX;
        Animator animatorY;

        public MovementAnimator(View view){
            this.view = view;
        }

        public void run(boolean initial){
            int left = view.getWidth() / 2;
            int top = view.getHeight() / 2;
            int right = getWidth() - left;
            int bottom = getHeight() - top;

            int toX = randomInt(left, right);
            int toY = randomInt(top, bottom);

            animatorX = ObjectAnimator.ofFloat(view, "translationX", toX);
            animatorY = ObjectAnimator.ofFloat(view, "translationY", toY);
            animatorX.removeAllListeners();

            long duration = initial ? randomInt(INITIAL_DURATION, MIN_DURATION) : randomInt(MIN_DURATION, MAX_DURATION);

            if(initial){
                animatorX.setInterpolator(new DecelerateInterpolator());
                animatorY.setInterpolator(new DecelerateInterpolator());
            }

            animatorX.setDuration(duration);
            animatorY.setDuration(duration);

            animatorX.addListener(this);

            animatorX.start();
            animatorY.start();
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            animatorY.cancel();
            run(false);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}

