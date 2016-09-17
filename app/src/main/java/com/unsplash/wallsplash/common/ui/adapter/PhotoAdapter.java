package com.unsplash.wallsplash.common.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.collection.view.activity.CollectionActivity;
import com.unsplash.wallsplash.common.data.data.ChangeCollectionPhotoResult;
import com.unsplash.wallsplash.common.data.data.Collection;
import com.unsplash.wallsplash.common.data.data.LikePhotoResult;
import com.unsplash.wallsplash.common.data.data.Photo;
import com.unsplash.wallsplash.common.data.data.User;
import com.unsplash.wallsplash.common.data.service.PhotoService;
import com.unsplash.wallsplash.common.data.tools.AuthManager;
import com.unsplash.wallsplash.common.ui.activity.LoginActivity;
import com.unsplash.wallsplash.common.ui.dialog.DeleteCollectionPhotoDialog;
import com.unsplash.wallsplash.common.ui.dialog.RateLimitDialog;
import com.unsplash.wallsplash.common.ui.dialog.SelectCollectionPhotoDialog;
import com.unsplash.wallsplash.common.ui.widget.FreedomImageView;
import com.unsplash.wallsplash.common.ui.widget.LikeImageButton;
import com.unsplash.wallsplash.common.ui.widget.MaterialProgressBar;
import com.unsplash.wallsplash.common.ui.widget.ShortTimeView;
import com.unsplash.wallsplash.common.utils.AnimUtils;
import com.unsplash.wallsplash.common.utils.ColorUtils;
import com.unsplash.wallsplash.common.utils.ObservableColorMatrix;
import com.unsplash.wallsplash.common.utils.TypefaceUtils;
import com.unsplash.wallsplash.common.utils.ValueUtils;
import com.unsplash.wallsplash.me.view.activity.MeActivity;
import com.unsplash.wallsplash.photo.view.activity.PhotoActivity;
import com.unsplash.wallsplash.user.view.activity.UserActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Photos adapter. (Recycler view)
 */

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements SelectCollectionPhotoDialog.OnCollectionsChangedListener, DeleteCollectionPhotoDialog.OnDeleteCollectionListener {
    // widget
    private Context mContext;
    private List<Photo> itemList;
    private PhotoService service;
    private static final String TAG = PhotoAdapter.class.getSimpleName();
    // data
    private boolean own = false;
    private boolean inMyCollection = false;

    /**
     * <br> life cycle.
     */

    public PhotoAdapter(Context context, List<Photo> list) {
        this.mContext = context;
        this.itemList = list;
    }

    /**
     * <br> UI.
     */

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(v, viewType);
    }


    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).title.setText("");
        ((ViewHolder) holder).image.setShowShadow(false);
        Glide.with(mContext).load(itemList.get(position).user.profile_image.large)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(((ViewHolder) holder).avatar);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Glide.with(mContext)
                    .load(itemList.get(position).urls.regular)
                    .placeholder(R.drawable.bg_blur)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {
                            itemList.get(position).loadPhotoSuccess = true;
                            ((ViewHolder) holder).image.setShowShadow(true);
                            ((ViewHolder) holder).progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onException(Exception e, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(((ViewHolder) holder).image);
        } else {
            Glide.with(mContext)
                    .load(itemList.get(position).urls.regular)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {
                            ((ViewHolder) holder).progress.setVisibility(View.GONE);
                            itemList.get(position).loadPhotoSuccess = true;
                            if (!itemList.get(position).hasFadedIn) {
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
                                        // drawable so need this update listener.  Also have to create a
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
                                itemList.get(position).hasFadedIn = true;
                            }

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
        String titleTxt = "By " + itemList.get(position).user.name;
        ((ViewHolder) holder).title.setText(titleTxt);
        ((ViewHolder) holder).tvTime.setTime(ValueUtils.getFormattedDate(itemList.get(position).created_at));
        ((ViewHolder) holder).tvNumberLike.setText(String.valueOf(itemList.get(position).likes));

        if (inMyCollection) {
            ((ViewHolder) holder).deleteButton.setVisibility(View.VISIBLE);
        } else {
            ((ViewHolder) holder).deleteButton.setVisibility(View.GONE);
        }

        ((ViewHolder) holder).likeButton.initLikeState(itemList.get(position).liked_by_user);

        ((ViewHolder) holder).background.setBackgroundColor(
                ColorUtils.calcCardBackgroundColor(
                        mContext,
                        itemList.get(position).color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((ViewHolder) holder).image.setTransitionName(itemList.get(position).id);
        }

        setOnClick((ViewHolder) holder, position);
    }

    private void setOnClick(final ViewHolder holder, final int position) {
        holder.likeButton.setOnLikeListener(new LikeImageButton.OnLikeListener() {
            @Override
            public void onClickLikeButton(boolean newLikeState) {
                setLikeForAPhoto(newLikeState, position);
            }
        });


        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof Activity) {
                    Photo p = itemList.get(position);
                    WallSplashApplication.getInstance().setPhoto(p);
                    Log.d(TAG, "onClick: " + p.id);
                    if (itemList.get(position).loadPhotoSuccess) {
                        View imageView = ((RelativeLayout) view).getChildAt(0);
                        Drawable d = ((FreedomImageView) imageView).getDrawable();
                        WallSplashApplication.getInstance().setDrawable(d);
                    } else {
                        WallSplashApplication.getInstance().setDrawable(null);
                    }

                    Intent intent = new Intent(mContext, PhotoActivity.class);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat options = ActivityOptionsCompat
                                .makeScaleUpAnimation(
                                        view,
                                        (int) view.getX(), (int) view.getY(),
                                        view.getMeasuredWidth(), view.getMeasuredHeight());
                        ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());
                    } else {
                        View imageView = ((RelativeLayout) view).getChildAt(0);
                        ActivityOptionsCompat options = ActivityOptionsCompat
                                .makeSceneTransitionAnimation(
                                        (Activity) mContext,
                                        Pair.create(imageView, mContext.getString(R.string.transition_photo_image)),
                                        Pair.create(imageView, mContext.getString(R.string.transition_photo_background)));
                        ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());
                    }
                }
            }
        });

        holder.addCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof Activity) {
                    if (!AuthManager.getInstance().isAuthorized()) {
                        Intent i = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(i);
                    } else {
                        SelectCollectionPhotoDialog dialog = new SelectCollectionPhotoDialog();
                        dialog.setPhoto(itemList.get(position));
                        if (own) {
                            dialog.setOnCollectionsChangedListener(PhotoAdapter.this);
                        }
                        dialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(), null);
                    }
                }
            }
        });

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User u = User.buildUser(itemList.get(position));
                WallSplashApplication.getInstance().setUser(u);
                Intent intent = new Intent(mContext, UserActivity.class);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    mContext.startActivity(intent);
                } else {
                    View v = holder.avatar;
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(
                                    (Activity) mContext,
                                    Pair.create(v, mContext.getString(R.string.transition_user_avatar)));
                    ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof CollectionActivity) {
                    DeleteCollectionPhotoDialog dialog = new DeleteCollectionPhotoDialog();
                    dialog.setDeleteInfo(
                            ((CollectionActivity) mContext).getCollection(),
                            itemList.get(position),
                            position);
                    dialog.setOnDeleteCollectionListener(PhotoAdapter.this);
                    dialog.show(((CollectionActivity) mContext).getSupportFragmentManager(), null);
                }
            }
        });
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        Glide.clear((((ViewHolder) holder).image));
    }

    public void setActivity(Activity activity) {
        this.mContext = activity;
    }

    /**
     * <br> data.
     */

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public void insertItem(Photo item) {
        itemList.add(item);
        notifyItemInserted(itemList.size() - 1);
    }

    public void clearItem() {
        itemList.clear();
        notifyDataSetChanged();
    }

    private void setLikeForAPhoto(boolean like, int position) {
        if (service == null) {
            service = PhotoService.getService();
        }
        service.setLikeForAPhoto(
                itemList.get(position).id,
                like,
                new OnSetLikeListener(itemList.get(position).id, like, position));
    }

    public void cancelService() {
        if (service != null) {
            service.cancel();
        }
    }

    public int getRealItemCount() {
        return itemList.size();
    }

    public void setOwn(boolean own) {
        this.own = own;
    }

    @Override
    public void onDeletePhotoSuccess(ChangeCollectionPhotoResult result, int position) {
        if (itemList.size() > position && itemList.get(position).id.equals(result.photo.id)) {
            itemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void setInMyCollection(boolean in) {
        this.inMyCollection = in;
    }

    /**
     * <br> interface.
     */

    // on set like listener.

    private class OnSetLikeListener implements PhotoService.OnSetLikeListener {
        // data
        private String id;
        private boolean like;
        private int position;

        public OnSetLikeListener(String id, boolean like, int position) {
            this.id = id;
            this.like = like;
            this.position = position;
        }

        @Override
        public void onSetLikeSuccess(Call<LikePhotoResult> call, Response<LikePhotoResult> response) {
            if (response.isSuccessful() && response.body() != null) {
                if (itemList.size() >= position
                        && itemList.get(position).id.equals(response.body().photo.id)) {
                    itemList.get(position).liked_by_user = response.body().photo.liked_by_user;
                    itemList.get(position).likes = response.body().photo.likes;
                }
            } else if (Integer.parseInt(response.headers().get("X-Ratelimit-Remaining")) < 0) {
                RateLimitDialog dialog = new RateLimitDialog();
                dialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(), null);
            } else {
                service.setLikeForAPhoto(
                        id,
                        like,
                        this);
            }
        }

        @Override
        public void onSetLikeFailed(Call<LikePhotoResult> call, Throwable t) {
            service.setLikeForAPhoto(
                    id,
                    like,
                    this);
        }
    }

    // on collections changed listener.

    @Override
    public void onAddCollection(Collection c) {
        ((MeActivity) mContext).addCollection(c);
    }

    @Override
    public void onAddPhotoToCollection(Collection c, ChangeCollectionPhotoResult.User user) {
        ((MeActivity) mContext).changeCollection(c);
    }

    /**
     * <br> inner class.
     */

    // view holder.

    public class ViewHolder extends RecyclerView.ViewHolder {
        // widget
        public RelativeLayout background;
        public FreedomImageView image;
        private TextView title, tvNumberLike;
        private LikeImageButton likeButton;
        private CircularImageView avatar;
        private ShortTimeView tvTime;
        private MaterialProgressBar progress;
        public ImageButton deleteButton, addCollection;

        public ViewHolder(View itemView, int position) {
            super(itemView);

            this.background = (RelativeLayout) itemView.findViewById(R.id.item_photo_background);

            this.image = (FreedomImageView) itemView.findViewById(R.id.item_photo_image);
            image.setSize(itemList.get(position).width, itemList.get(position).height);

            this.title = (TextView) itemView.findViewById(R.id.item_photo_title);
            TypefaceUtils.setTypeface(itemView.getContext(), title);

            this.tvNumberLike = (TextView) itemView.findViewById(R.id.tvNumberLike);
            TypefaceUtils.setTypeface(itemView.getContext(), tvNumberLike);

            this.likeButton = (LikeImageButton) itemView.findViewById(R.id.item_photo_likeButton);

            this.addCollection = (ImageButton) itemView.findViewById(R.id.item_photo_collectionButton);

            avatar = (CircularImageView) itemView.findViewById(R.id.avatar);

            this.tvTime = (ShortTimeView) itemView.findViewById(R.id.tvTime);

            this.progress = (MaterialProgressBar) itemView.findViewById(R.id.progress);

            this.deleteButton = (ImageButton) itemView.findViewById(R.id.item_photo_deleteButton);
        }


    }

}
