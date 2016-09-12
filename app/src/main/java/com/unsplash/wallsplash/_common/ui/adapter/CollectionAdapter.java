package com.unsplash.wallsplash._common.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.data.data.Collection;
import com.unsplash.wallsplash._common.ui.widget.FreedomImageView;
import com.unsplash.wallsplash._common.ui.widget.MaterialProgressBar;
import com.unsplash.wallsplash._common.ui.widget.ShortTimeView;
import com.unsplash.wallsplash._common.utils.AnimUtils;
import com.unsplash.wallsplash._common.utils.ColorUtils;
import com.unsplash.wallsplash._common.utils.ObservableColorMatrix;
import com.unsplash.wallsplash._common.utils.TypefaceUtils;
import com.unsplash.wallsplash._common.utils.ValueUtils;
import com.unsplash.wallsplash.collection.view.activity.CollectionActivity;
import com.unsplash.wallsplash.me.view.activity.MeActivity;

import java.util.List;

/**
 * Collection adapter. (Recycler view)
 */

public class CollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // widget
    private Context mContext;
    private List<Collection> itemList;

    // data
    private boolean own = false;
    private Animation animation;
    private int mDuration = 500;
    private Interpolator mInterpolator = new LinearInterpolator();
    protected int lastPosition = -1;

    /**
     * <br> life cycle.
     */

    public CollectionAdapter(Context context, List<Collection> list) {
        this.mContext = context;
        this.itemList = list;
        animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ObjectAnimator anim = ObjectAnimator.ofFloat(viewToAnimate, "translationY", viewToAnimate.getMeasuredHeight(), 0);
            anim.setInterpolator(mInterpolator);
            anim.setDuration(mDuration).start();
            lastPosition = position;
        } else {
            ViewCompat.setTranslationY(viewToAnimate, 0);
        }
    }

    /**
     * <br> UI.
     */

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection, parent, false);
        return new ViewHolder(v, viewType);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        setAnimation(holder.itemView, position);
        ((ViewHolder) holder).title.setText("");
        ((ViewHolder) holder).subtitle.setText("");
        ((ViewHolder) holder).image.setShowShadow(false);
        if (itemList.get(position).cover_photo != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Glide.with(mContext)
                        .load(itemList.get(position).cover_photo.urls.regular)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model,
                                                           Target<GlideDrawable> target,
                                                           boolean isFromMemoryCache, boolean isFirstResource) {
                                ((ViewHolder) holder).progressBar.setVisibility(View.GONE);
                                ((ViewHolder) holder).title.setText(itemList.get(position).title.toUpperCase());
                                int photoNum = itemList.get(position).total_photos;
                                ((ViewHolder) holder).subtitle.setText(photoNum + (photoNum > 1 ? " photos" : " photo"));
                                ((ViewHolder) holder).image.setShowShadow(true);
                                return false;
                            }

                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(((ViewHolder) holder).image);
            } else {
                Glide.with(mContext)
                        .load(itemList.get(position).cover_photo.urls.regular)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model,
                                                           Target<GlideDrawable> target,
                                                           boolean isFromMemoryCache, boolean isFirstResource) {
                                ((ViewHolder) holder).progressBar.setVisibility(View.GONE);
                                if (!itemList.get(position).cover_photo.hasFadeIn) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        ((ViewHolder) holder).image.setHasTransientState(true);
                                    }
                                    final ObservableColorMatrix matrix = new ObservableColorMatrix();
                                    final ObjectAnimator saturation = ObjectAnimator.ofFloat(
                                            matrix, ObservableColorMatrix.SATURATION, 0f, 1f);
                                    saturation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener
                                            () {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                            // just animating the color matrix does not invalidate the
                                            // drawable so need this update listener.  Also have to create mContext
                                            // new CMCF as the matrix is immutable :(
                                            ((ViewHolder) holder).image.setColorFilter(new ColorMatrixColorFilter(matrix));
                                        }
                                    });
                                    saturation.setDuration(2000L);
                                    saturation.setInterpolator(AnimUtils.getFastOutSlowInInterpolator(mContext));
                                    saturation.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            ((ViewHolder) holder).image.clearColorFilter();
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                ((ViewHolder) holder).image.setHasTransientState(false);
                                            }
                                        }
                                    });
                                    saturation.start();
                                    itemList.get(position).cover_photo.hasFadeIn = true;
                                }

                                ((ViewHolder) holder).title.setText(itemList.get(position).title.toUpperCase());
                                int photoNum = itemList.get(position).total_photos;
                                ((ViewHolder) holder).subtitle.setText(photoNum + (photoNum > 1 ? " photos" : " photo"));
                                ((ViewHolder) holder).image.setShowShadow(true);
                                return false;
                            }

                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(((ViewHolder) holder).image);
            }
            ((ViewHolder) holder).background.setBackgroundColor(
                    ColorUtils.calcCardBackgroundColor(
                            mContext,
                            itemList.get(position).cover_photo.color));
            ((ViewHolder) holder).tvTime.setTime(ValueUtils.getFormattedDate(itemList.get(position).published_at));
            setOnClick((ViewHolder) holder, position);
        } else {
            ((ViewHolder) holder).image.setImageResource(R.color.colorTextContent_light);
            ((ViewHolder) holder).title.setText(itemList.get(position).title.toUpperCase());
            int photoNum = itemList.get(position).total_photos;
            ((ViewHolder) holder).subtitle.setText(photoNum + (photoNum > 1 ? " photos" : " photo"));
        }
    }

    private void setOnClick(ViewHolder holder, final int position) {
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof Activity) {
                    Collection c = itemList.get(position);
                    WallSplashApplication.getInstance().setCollection(c);
                    WallSplashApplication.getInstance().setMyOwnCollection(own);

                    Intent intent = new Intent(mContext, CollectionActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeScaleUpAnimation(
                                    view,
                                    (int) view.getX(), (int) view.getY(),
                                    view.getMeasuredWidth(), view.getMeasuredHeight());
                    if (own) {
                        ActivityCompat.startActivityForResult(
                                (Activity) mContext,
                                intent,
                                MeActivity.COLLECTION_ACTIVITY,
                                options.toBundle());
                    } else {
                        ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());
                    }
                }
            }
        });
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        Glide.clear(((ViewHolder) holder).image);
    }

    public void setActivity(Activity a) {
        this.mContext = a;
    }

    /**
     * <br> data.
     */

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public void insertItem(Collection c, int position) {
        itemList.add(position, c);
        notifyItemInserted(position);
    }

    public void removeItem(Collection c) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).id == c.id) {
                itemList.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    public void changeItem(Collection c) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).id == c.id) {
                itemList.remove(i);
                itemList.add(i, c);
                notifyItemChanged(i);
                return;
            }
        }
        insertItem(c, 0);
    }

    public void clearItem() {
        itemList.clear();
        notifyDataSetChanged();
    }

    public int getRealItemCount() {
        return itemList.size();
    }

    public void setOwn(boolean own) {
        this.own = own;
    }

    /**
     * <br> inner class.
     */

    // view holder.

    public class ViewHolder extends RecyclerView.ViewHolder {
        // widget
        public RelativeLayout background;
        public FreedomImageView image;
        public TextView title;
        public TextView subtitle;
        private MaterialProgressBar progressBar;
        private ShortTimeView tvTime;

        public ViewHolder(View itemView, int position) {
            super(itemView);

            this.background = (RelativeLayout) itemView.findViewById(R.id.item_collection_background);

            this.image = (FreedomImageView) itemView.findViewById(R.id.item_collection_cover);
            if (itemList.get(position).cover_photo != null) {
                image.setSize(itemList.get(position).cover_photo.width, itemList.get(position).cover_photo.height);
            }

            this.title = (TextView) itemView.findViewById(R.id.item_collection_title);

            this.subtitle = (TextView) itemView.findViewById(R.id.item_collection_subtitle);
            TypefaceUtils.setTypeface(itemView.getContext(), subtitle);
            this.progressBar = (MaterialProgressBar) itemView.findViewById(R.id.progress);
            this.tvTime = (ShortTimeView) itemView.findViewById(R.id.tvTime);
            TypefaceUtils.setTypeface(itemView.getContext(), tvTime);
        }

    }
}