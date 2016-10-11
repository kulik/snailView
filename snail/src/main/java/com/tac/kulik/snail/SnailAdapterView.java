package com.tac.kulik.snail;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.LinkedList;

/**
 * Created by kulik on 29.07.16.
 */
public class SnailAdapterView extends AdapterView<BaseAdapter> {

    float[] posX = new float[]{
            0,
            0.4f,
            0,
            0,
            0.4f * 0.6f,
            0.4f * 0.6f + 0.4f * 0.4f * 0.4f,
            0.4f * 0.6f,
            0.4f * 0.6f

    };
    float[] posY7 = new float[]{
            0,
            0.6f,
            0.6f + 0.4f * 0.4f,
            0.6f,
            0.6f,
            0.6f + 0.4f * 0.4f * 0.6f,
            0.6f + 0.4f * 0.4f * 0.6f,
            0.6f + 0.4f * 0.4f * 0.6f
    };
    float[] posY8 = new float[]{
            0,
            0.6f,
            0.6f + 0.4f * 0.4f,
            0.6f,
            0.6f,
            0.6f + 0.4f * 0.4f * 0.6f,
            0.6f + 0.4f * 0.4f * 0.6f,
            0.6f + 0.4f * 0.4f * 0.6f + 0.4f * 0.4f * 0.4f * 0.6f,
    };

    private static final String TAG = SnailAdapterView.class.getSimpleName();
    private static final int DEFAULT_SHAIL_SIZE = 7;
    private BaseAdapter mAdapter;
    private int mMaxVisibleItemsQuantity;
    private int mMaxAnimatableItemsQuantity;
    private int mItemPadding;
    private Direction mDirection = Direction.FORWARD;

    public int getPosition() {
        return mPosition;
    }

    private int mPosition;
    //    private final LinkedList<View> mItemViews = new LinkedList<View>();
    private Context mCtx;

    private volatile AnimatorSet mMoveAnimation;
    private ObjectAnimator mLayoutAnimator = new ObjectAnimator();
    //    private ObjectAnimator mInvalidateAnimator = new ObjectAnimator();


    private static Interpolator sOversotInterpolator = new OvershootInterpolator(3f);
    private static Interpolator sMoveInterpolator = new LinearInterpolator();
    //            private static Interpolator sMoveInterpolator = new OvershootInterpolator(3f);
    private static Interpolator sFinishingInterpolator = new DecelerateInterpolator();
    private boolean isAnimateInfininy = false;
    private View mInfinitieView;
    private LinkedList<View> mAnimatedObjectsList;
    private OnScrollListener mScrollListener;

    private class LayoutParams extends ViewGroup.LayoutParams {
        private ObjectAnimator mExpandDirX = new ObjectAnimator();
        private ObjectAnimator mExpandDirY = new ObjectAnimator();
        private ObjectAnimator mWidthAnimator = new ObjectAnimator();
        private ObjectAnimator mHeightAnimator = new ObjectAnimator();

//        private int mPosition;

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
//TODO refactor it to ViewHolerProperty...
            mWidthAnimator.setTarget(this);
            mWidthAnimator.setPropertyName("widtha");
            mHeightAnimator.setTarget(this);
            mHeightAnimator.setPropertyName("heighta");
            mExpandDirX.setProperty(View.X);
            mExpandDirY.setProperty(View.Y);
        }

        public void setWidtha(float w) {
            width = (int) w;
        }

        public void setHeighta(float h) {
            height = (int) h;
        }

        public float getWidtha() {
            return width;
        }

        public float getHeighta() {
            return height;
        }

    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(super.generateLayoutParams(attrs));
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(super.generateLayoutParams(p));
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p);
    }

    private DataSetObserver mDataSetObserver;

    public SnailAdapterView(Context context) {
        super(context);
    }

    public SnailAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SnailAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SnailAdapterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attributeSet) {
        mCtx = context;
        TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.SnailAdapterView);
        mMaxVisibleItemsQuantity = attr.getInt(R.styleable.SnailAdapterView_snail_deep, DEFAULT_SHAIL_SIZE);
        mItemPadding = attr.getDimensionPixelSize(R.styleable.SnailAdapterView_snail_padding, mCtx.getResources().getDimensionPixelSize(R.dimen.defoultPadding));
        mItemPadding = attr.getDimensionPixelSize(R.styleable.SnailAdapterView_snail_padding, mCtx.getResources().getDimensionPixelSize(R.dimen.defoultPadding));
        int resourceId = attr.getResourceId(R.styleable.SnailAdapterView_snail_infinity_layout, -1);
        if (resourceId != -1) {
            mInfinitieView = (LayoutInflater.from(context)).inflate(resourceId, null);
        }
        attr.recycle();
        mMaxAnimatableItemsQuantity = (!isAnimateInfininy) ? mMaxVisibleItemsQuantity - 1 : mMaxVisibleItemsQuantity;
        mAnimatedObjectsList = new LinkedList<View>();

        mLayoutAnimator.setPropertyName("request");
        mLayoutAnimator.setFloatValues(0, 1f);
        mLayoutAnimator.setTarget(this);
        mLayoutAnimator.addListener(mAnimationListener);
    }

    public void setRequest(float w) {
        measureItems();
        requestLayout();
    }

    private Animator.AnimatorListener mAnimationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (mDirection == Direction.FORWARD) {
                mPosition++;
            } else {
                mPosition--;
            }
            if (mPosition == mAdapter.getCount()) {
                mPosition = 0;
            }
            if (mPosition == -1) {
                mPosition = mAdapter.getCount()-1;
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {

//            mLayoutAnimator.removeAllListeners();
            post(new Runnable() {
                @Override
                public void run() {
                    reinitChild();
                    if (mPosition != mScrollTo) {
                        if (mDirection == Direction.FORWARD) {
                            setSelection(mScrollTo);
                        } else {
                            setSelectionBack(mScrollTo);
                        }
                    } else {
                        if (mScrollListener != null) {
                            mScrollListener.onScrolledToPos(mPosition, mAnimatedObjectsList.get(0));
                        }
                    }
                }
            });
        }


        @Override
        public void onAnimationCancel(Animator animation) {
            Log.d(TAG, "onAnimationCancel");
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            Log.d(TAG, "OnAnimationRepeat");

        }
    };

    private void reinitChild() {
            final int i;
        View v;
        if (mDirection == Direction.FORWARD) {
            i = (mPosition + mMaxAnimatableItemsQuantity - 1) % mAdapter.getCount();
            Log.d(TAG, "add new item " + i);
            View convertView = mAnimatedObjectsList.pop();
            v = mAdapter.getView(i, convertView, null);
            reMeasureChild(v, mMaxAnimatableItemsQuantity - 1);
            mAnimatedObjectsList.add(v);
        } else {
            i = mPosition % mAdapter.getCount();
            Log.d(TAG, "add new item " + i);
            View convertView = mAnimatedObjectsList.pollLast();
            v = mAdapter.getView(i, convertView, null);
            reMeasureChild(v, 0);
            mAnimatedObjectsList.addFirst(v);
        }
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemClickListener onItemClickListener = getOnItemClickListener();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(SnailAdapterView.this, v, i, -1);
                }
            }
        });
        requestLayout();
    }

    @Override
    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(BaseAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mAdapter = adapter;
        initAdapter();

        if (mDataSetObserver == null) {
            mDataSetObserver = new DataSetObserver() {
                @Override
                public void onChanged() {
                    onDataChange();
                }

                @Override
                public void onInvalidated() {
                    onDataInvalidated();
                }
            };
        }
        mAdapter.registerDataSetObserver(mDataSetObserver);
    }

    protected void initAdapter() {
        removeAllViewsInLayout();
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mAdapter == null) {
            return;
        }

        if (changed) {
            mAdapter.notifyDataSetChanged();
            fillList();
        }
        layoutsItems();
    }

    private void measureItems() {
        for (int i = 0; i < mMaxAnimatableItemsQuantity; i++) {
            View child = mAnimatedObjectsList.pollLast();
            reMeasureChild(child, i);
            mAnimatedObjectsList.addFirst(child);
        }
    }

    @Override
    public View getSelectedView() {
        return mAnimatedObjectsList.get(mPosition);
    }

    int mScrollTo = 0;

    @Override
    public void setSelection(int position) {
        //TODO start scrolling
        mScrollTo = position;
        mDirection = Direction.FORWARD;
        if (mMoveAnimation != null) {
            if (mMoveAnimation.isRunning()) {
                mMoveAnimation.end();
                mMoveAnimation.cancel();
                mMoveAnimation = null;
            }
        }
        mMoveAnimation = new AnimatorSet();
        mMoveAnimation.setInterpolator(sMoveInterpolator);
        if (Math.abs(mPosition - mScrollTo) == 1) {
            mMoveAnimation.setInterpolator(sFinishingInterpolator);
            mMoveAnimation.setDuration(600);
        } else {
            mMoveAnimation.setDuration(400);
        }

        LinkedList al = new LinkedList();
        for (int i = 0; i < mMaxAnimatableItemsQuantity; i++) {
            View witch = mAnimatedObjectsList.pollFirst();
            View to = mAnimatedObjectsList.getLast();
            animate(to, al, witch);
            mAnimatedObjectsList.addLast(witch);
        }

//
//        View to = mAnimatedObjectsList.get(0);
//        for (int i = 1; i < mMaxAnimatableItemsQuantity; i++) {
//            View witch = mAnimatedObjectsList.get(i);
//            animate(to, al, witch);
//            to = witch;
//        }
//        View witch = mAnimatedObjectsList.get(0);
//        animate(to, al, witch);

        al.add(mLayoutAnimator);
        mMoveAnimation.playTogether(al);
        mMoveAnimation.start();
    }

    public enum Direction {
        FORWARD,
        BACKWARD
    }

    public void setSelectionBack(int position) {
        //TODO start scrolling
        mScrollTo = position;
        mDirection = Direction.BACKWARD;
        if (mMoveAnimation != null) {
            if (mMoveAnimation.isRunning()) {
                mMoveAnimation.end();
                mMoveAnimation.cancel();
                mMoveAnimation = null;
            }
        }
        mMoveAnimation = new AnimatorSet();
        mMoveAnimation.setInterpolator(sMoveInterpolator);
        if (Math.abs(mPosition - mScrollTo) == 1) {
            mMoveAnimation.setInterpolator(sFinishingInterpolator);
            mMoveAnimation.setDuration(400);
        } else {
            mMoveAnimation.setDuration(250);
        }
        LinkedList al = new LinkedList();


        for (int i = 0; i < mMaxAnimatableItemsQuantity; i++) {
            View witch = mAnimatedObjectsList.pollLast();
            View to = mAnimatedObjectsList.getFirst();
            animate(to, al, witch);
            mAnimatedObjectsList.addFirst(witch);
        }


//
//
//        for (int i = 1; i < mMaxAnimatableItemsQuantity; i++) {
//            View witch = mAnimatedObjectsList.get(i);
//            animate(to, al, witch);
//            to = witch;
//        }
//        View witch = mAnimatedObjectsList.get(0);
//        animate(to, al, witch);

        al.add(mLayoutAnimator);
        mMoveAnimation.playTogether(al);
        mMoveAnimation.start();
    }

    public void setSelectionSilent(int position) {
        //TODO start scrolling
        mScrollTo = position;
        mPosition = position;
        onDataChange();
    }

    private void animate(View to, LinkedList al, View witch) {
        LayoutParams layoutParams;
        layoutParams = (LayoutParams) witch.getLayoutParams();
        layoutParams.mExpandDirX.setFloatValues(witch.getX(), to.getX());
        layoutParams.mExpandDirY.setFloatValues(witch.getY(), to.getY());
        layoutParams.mHeightAnimator.setFloatValues(witch.getLayoutParams().height, to.getLayoutParams().height);
        layoutParams.mWidthAnimator.setFloatValues(witch.getLayoutParams().width, to.getLayoutParams().width);

        layoutParams.mExpandDirX.setTarget(witch);
        layoutParams.mExpandDirY.setTarget(witch);

        al.add(layoutParams.mExpandDirX);
        al.add(layoutParams.mExpandDirY);
        al.add(layoutParams.mWidthAnimator);
        al.add(layoutParams.mHeightAnimator);

    }

    public void setOnScrellListener(OnScrollListener listener) {
        mScrollListener = listener;
    }

    private void fillList() {
//        Log.i(TAG, "fillList()");
        View newChild = null;
        removeAllViewsInLayout();
        mAnimatedObjectsList.clear();

        int needAdd = Math.min(mAdapter.getCount(), mMaxAnimatableItemsQuantity);

        for (int i = 0; i < needAdd; i++) {
            newChild = mAdapter.getView(i, null, this);
            mAnimatedObjectsList.add(newChild);

            ViewGroup.LayoutParams layoutParams = newChild.getLayoutParams();
            LayoutParams params;
            if (layoutParams == null) {
                params = (LayoutParams) generateDefaultLayoutParams();
            } else {
                params = (LayoutParams) generateLayoutParams(layoutParams);
            }
            newChild.setLayoutParams(params);

            final int finalI = i;
            newChild.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnItemClickListener onItemClickListener = getOnItemClickListener();
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(SnailAdapterView.this, v, finalI, mAdapter.getItemId(mPosition));
                    }
                }
            });
            addChildInLayout(newChild);
            measureChild(newChild, i);
        }
        if (mInfinitieView != null) {
            addViewInLayout(mInfinitieView, getChildCount(), generateDefaultLayoutParams());
            measureChild(mInfinitieView, mMaxVisibleItemsQuantity - 1);
        }
    }

    @Override
    public int getPositionForView(View view) {
        return super.getPositionForView(view);
    }

    private void addChildInLayout(final View child) {
        LayoutParams params = (LayoutParams) child.getLayoutParams();
        if (params == null) {
            params = (LayoutParams) generateDefaultLayoutParams();
        }
        addViewInLayout(child, -1, params);
    }

    private void measureChild(final View child, int i) {
//        Log.v(TAG, "measureChild()");
        float w = getWidthByIndex(i, mMaxVisibleItemsQuantity) * getWidth() - mItemPadding;
        float h = getHeightByIndex(i, mMaxVisibleItemsQuantity) * getHeight() - mItemPadding;
        int widthMeashure = MeasureSpec.makeMeasureSpec((int) w, MeasureSpec.EXACTLY);
        int heightMeashure = MeasureSpec.makeMeasureSpec((int) h, MeasureSpec.EXACTLY);
        if (child != null) {
            child.measure(widthMeashure, heightMeashure);
            child.getLayoutParams().height = (int) h;
            child.getLayoutParams().width = (int) w;
        }
    }

    private void reMeasureChild(final View child, int i) {
//        Log.v(TAG, "measureChild()");
        float h = child.getLayoutParams().height;
        float w = child.getLayoutParams().width;
        int widthMeashure = MeasureSpec.makeMeasureSpec((int) w, MeasureSpec.EXACTLY);
        int heightMeashure = MeasureSpec.makeMeasureSpec((int) h, MeasureSpec.EXACTLY);
        if (child != null) {
            child.measure(widthMeashure, heightMeashure);
        }
    }

    public static float getWidthByIndex(int i, int n) {
        return (float) (Math.pow(0.4f, i / 2) * (((i + 1) % 2 == 0 && i < n - 1) ? 0.6f : 1f));
    }

    public static float getHeightByIndex(int i, int n) {
        float v = (float) (Math.pow(0.4f, (i + 1) / 2) * (((i + 4) % 2 == 0 && i < n - 1) ? 0.6f : 1f));
        //FIX ME it is anoying but tests should pass
        return v;
//        } else{
//                return v * ((i == 6 && n == 7) ? 0.6f : 1f);
//            }
    }

    private void layoutsItems() {
//        Log.v(TAG, "layoutsItems()");
        for (int i = 0, a = getChildCount(); i < a; i++) {
            final View child = getChildAt(i);
            float r = child.getLayoutParams().width;
            float b = child.getLayoutParams().height;
            float l = (posX[i] * getWidth()) + mItemPadding / 2;
            float[] y = (mMaxVisibleItemsQuantity == 7) ? posY7 : posY8;
            float t = (y[i] * getHeight()) + mItemPadding / 2;
            child.layout((int) l, (int) t, (int) (l + r), (int) (t + b));
        }
    }

    private void onDataInvalidated() {
        Log.v(TAG, "onDataInvalidated()");
        removeAllViewsInLayout();

//        mItemViews.clear();
        requestLayout();
    }

    private void onDataChange() {
        Log.v(TAG, "onDataChange()");
        for (int i = 0, a = mAnimatedObjectsList.size(); i < a; i++) {
            View child = mAnimatedObjectsList.get(i);
            child = mAdapter.getView(mPosition + i, child, null);
            final int finalPos = i + mPosition;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnItemClickListener onItemClickListener = getOnItemClickListener();
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(SnailAdapterView.this, v, finalPos, -1);
                    }
                }
            });
            reMeasureChild(child, i);
        }
        requestLayout();
    }

}
