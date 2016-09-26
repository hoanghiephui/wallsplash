package com.unsplash.wallsplash.common.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.data.data.User;
import com.unsplash.wallsplash.common.data.tools.AuthManager;
import com.unsplash.wallsplash.common.utils.LinkUtils;
import com.unsplash.wallsplash.common.utils.ThemeUtils;
import com.unsplash.wallsplash.common.utils.TypefaceUtils;
import com.unsplash.wallsplash.me.view.activity.MeActivity;
import com.unsplash.wallsplash.user.view.activity.UserActivity;

import java.util.List;

/**
 * Created by Hoang Hiep on 9/25/2016.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    // widget
    private Context a;
    private List<User> itemList;

    /**
     * <br> life cycle.
     */

    public UserAdapter(Context a, List<User> list) {
        this.a = a;
        this.itemList = list;
    }

    /**
     * <br> UI.
     */

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.title.setText(itemList.get(position).name);
        if (TextUtils.isEmpty(itemList.get(position).bio)) {
            holder.subtitle.setText(
                    itemList.get(position).total_photos + " Photos, "
                            + itemList.get(position).total_collections + " Collections, "
                            + itemList.get(position).total_likes + " Likes");
        } else {
            holder.subtitle.setText(itemList.get(position).bio);
        }

        if (TextUtils.isEmpty(itemList.get(position).portfolio_url)) {
            holder.portfolioBtn.setVisibility(View.GONE);
        } else {
            holder.portfolioBtn.setVisibility(View.VISIBLE);
        }

        if (ThemeUtils.getInstance(a).isLightTheme()) {
            holder.portfolioBtn.setImageResource(R.drawable.ic_item_earth_light);
        } else {
            holder.portfolioBtn.setImageResource(R.drawable.ic_item_earth_dark);
        }

        if (itemList.get(position).profile_image != null) {
            Glide.with(a)
                    .load(itemList.get(position).profile_image.large)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .override(128, 128)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.avatar);
        } else {
            Glide.with(a)
                    .load(R.drawable.ic_avatar)
                    .override(128, 128)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.avatar);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.avatar.setTransitionName(itemList.get(position).username);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) a;
                if (AuthManager.getInstance().isAuthorized()
                        && !TextUtils.isEmpty(AuthManager.getInstance().getUsername())
                        && itemList.get(position).username.equals(AuthManager.getInstance().getUsername())) {
                    Intent intent = new Intent(activity, MeActivity.class);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.activity_in, 0);
                    } else {
                        View v = holder.avatar;
                        ActivityOptionsCompat options = ActivityOptionsCompat
                                .makeSceneTransitionAnimation(
                                        activity,
                                        Pair.create(v, activity.getString(R.string.transition_me_avatar)));
                        ActivityCompat.startActivity(activity, intent, options.toBundle());
                    }
                } else {
                    User u = itemList.get(position);
                    WallSplashApplication.getInstance().setUser(u);
                    Intent intent = new Intent(activity, UserActivity.class);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.activity_in, 0);
                    } else {
                        View v = holder.avatar;
                        ActivityOptionsCompat options = ActivityOptionsCompat
                                .makeSceneTransitionAnimation(
                                        activity,
                                        Pair.create(v, activity.getString(R.string.transition_user_avatar)));
                        ActivityCompat.startActivity(activity, intent, options.toBundle());
                    }
                }
            }
        });

        holder.portfolioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(itemList.get(position).portfolio_url)) {
                    LinkUtils.accessLink(a, itemList.get(position).portfolio_url);
                }
            }
        });
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        Glide.clear(holder.avatar);
    }

    public void setActivity(Activity a) {
        this.a = a;
    }

    /**
     * <br> data.
     */

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void insertItem(User u, int position) {
        itemList.add(position, u);
        notifyItemInserted(position);
    }

    public void clearItem() {
        itemList.clear();
        notifyDataSetChanged();
    }

    public int getRealItemCount() {
        return itemList.size();
    }

    /**
     * <br> inner class.
     */

    // view holder.

    class ViewHolder extends RecyclerView.ViewHolder {
        // widget
        CircularImageView avatar;
        ImageButton portfolioBtn;

        public TextView title;
        public TextView subtitle;

        ViewHolder(View itemView) {
            super(itemView);
            this.avatar = (CircularImageView) itemView.findViewById(R.id.item_user_avatar);

            this.portfolioBtn = (ImageButton) itemView.findViewById(R.id.item_user_portfolio);

            this.title = (TextView) itemView.findViewById(R.id.item_user_title);

            this.subtitle = (TextView) itemView.findViewById(R.id.item_user_subtitle);
            TypefaceUtils.setTypeface(itemView.getContext(), subtitle);
        }


    }
}
