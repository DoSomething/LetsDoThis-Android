package org.dosomething.letsdothis.ui.adapters;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toidiu on 4/17/15.
 */
public class HubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int    VIEW_TYPE_PROFILE          = 0;
    public static final int    VIEW_TYPE_SECTION_TITLE    = 1;
    public static final int    VIEW_TYPE_CURRENT_CAMPAIGN = 2;
    public static final int    VIEW_TYPE_PAST_CAMPAIGN    = 3;
    public static final int    VIEW_TYPE_EXPIRE           = 4;
    public static final String CURRENTLY_DOING            = "currently doing";
    public static final String BEEN_THERE_DONE_GOOD       = "been there, done good";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ArrayList<Object> hubList = new ArrayList<>();
    private User                    user;
    private HubAdapterClickListener hubAdapterClickListener;
    private boolean isPublic = false;
    private Campaign clickedCampaign;

    public HubAdapter(HubAdapterClickListener hubAdapterClickListener, boolean isPublic)
    {
        super();
        this.hubAdapterClickListener = hubAdapterClickListener;
        addUser(new User(null, "", "", null));
        hubList.add(CURRENTLY_DOING);
        hubList.add(BEEN_THERE_DONE_GOOD);
        this.isPublic = isPublic;
    }

    public void addUser(User user)
    {
        this.user = user;
        if(! hubList.isEmpty() && hubList.get(0) instanceof User)
        {
            hubList.set(0, user);
        }
        else
        {
            hubList.add(0, user);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
            case VIEW_TYPE_SECTION_TITLE:
                View sectionLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign_footer, parent, false);
                return new SectionTitleViewHolder((TextView) sectionLayout);
            case VIEW_TYPE_PROFILE:
                View profileLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_profile, parent, false);
                return new ProfileViewHolder(profileLayout);
            case VIEW_TYPE_CURRENT_CAMPAIGN:
                View currentLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_current_campaign, parent, false);
                return new CurrentCampaignViewHolder(currentLayout);
            case VIEW_TYPE_PAST_CAMPAIGN:
                View pastLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_past_campaign, parent, false);
                return new PastCampaignViewHolder(pastLayout);
            case VIEW_TYPE_EXPIRE:
                View expireLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_expire, parent, false);
                return new ExpireViewHolder(expireLayout);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        if(getItemViewType(position) == VIEW_TYPE_SECTION_TITLE)
        {
            String s = (String) hubList.get(position);
            SectionTitleViewHolder sectionTitleViewHolder = (SectionTitleViewHolder) holder;
            sectionTitleViewHolder.textView.setText(s);
        }
        else if(getItemViewType(position) == VIEW_TYPE_PROFILE)
        {
            User user = (User) hubList.get(position);
            ProfileViewHolder profileViewHolder = (ProfileViewHolder) holder;

            if(user != null && user.avatarPath != null)
            {
                Picasso.with(((ProfileViewHolder) holder).userImage.getContext())
                        .load(user.avatarPath)
                        .resizeDimen(R.dimen.hub_avatar_height, R.dimen.hub_avatar_height)
                        .into(profileViewHolder.userImage);
            }


            String first = user.first_name;
            String last = "";
            if(user.last_name != null && user.last_name.length() > 0)
            {
                last = user.last_name.charAt(0) + ".";
            }

            String displayName = String.format("%s %s", first, last);
            profileViewHolder.name.setText(displayName);
        }
        else if(getItemViewType(position) == VIEW_TYPE_CURRENT_CAMPAIGN)
        {
            final Campaign campaign = (Campaign) hubList.get(position);
            CurrentCampaignViewHolder viewHolder = (CurrentCampaignViewHolder) holder;
            viewHolder.title.setText(campaign.title);
            viewHolder.callToAction.setText(campaign.callToAction);
            viewHolder.count.setText(campaign.count);

            Resources res = viewHolder.title.getResources();
            if(isPublic)
            {
                viewHolder.actionButtons.setVisibility(View.GONE);
            }

            Context context = viewHolder.image.getContext();
            int height = context.getResources().getDimensionPixelSize(R.dimen.campaign_height);
            Picasso.with(context).load(campaign.imagePath).resize(0, height).into(viewHolder.image);

            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);


            int size = campaign.group.size();

            if(campaign.showShare == Campaign.UploadShare.SHARE)
            {
                viewHolder.proveShare.setText(res.getString(R.string.share_photo));
            }
            else if(campaign.showShare == Campaign.UploadShare.SHOW_OFF)
            {
                viewHolder.proveShare.setText(res.getString(R.string.show_off));
            }
            else if(campaign.showShare == Campaign.UploadShare.UPLOADING)
            {
                viewHolder.proveShare.setText(res.getString(R.string.uploading));
            }

            viewHolder.proveShare.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(campaign.showShare == Campaign.UploadShare.SHARE)
                    {
                        hubAdapterClickListener.onShareClicked(campaign);
                        clickedCampaign = campaign;
                    }
                    else if(campaign.showShare == Campaign.UploadShare.SHOW_OFF)
                    {
                        hubAdapterClickListener.onProveClicked(campaign);
                        clickedCampaign = campaign;
                    }
                }
            });

            viewHolder.invite.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    hubAdapterClickListener.onInviteClicked(campaign);
                }
            });

            if(size > 0)
            {
                int friendSize = context.getResources()
                        .getDimensionPixelSize(R.dimen.friend_avatar);
                viewHolder.friends.setVisibility(View.VISIBLE);
                viewHolder.friendsCount.setText(Integer.toString(size));
                viewHolder.friendsCount.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        hubAdapterClickListener.groupClicked(campaign.id, user.id);
                    }
                });

                for(int i = 0; i < 4; i++)
                {
                    FrameLayout childAt = (FrameLayout) viewHolder.friendsContainer.getChildAt(i);

                    ImageView imageView = (ImageView) childAt.getChildAt(0);

                    if(campaign.group.size() > i)
                    {
                        User friend = campaign.group.get(i);
                        Picasso.with(context).load(friend.avatarPath).resize(friendSize, 0)
                                .into(imageView);
                        childAt.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                hubAdapterClickListener.friendClicked(user.id);
                            }
                        });
                    }
                    else
                    {
                        imageView.setImageDrawable(null);
                    }
                }
            }
            else
            {
                viewHolder.friends.setVisibility(View.GONE);
            }
        }
        else if(getItemViewType(position) == VIEW_TYPE_PAST_CAMPAIGN)
        {
            Campaign campaign = (Campaign) hubList.get(position);
            PastCampaignViewHolder pastCampaignViewHolder = (PastCampaignViewHolder) holder;
            Context context = pastCampaignViewHolder.image.getContext();
            int height = context.getResources().getDimensionPixelSize(R.dimen.campaign_height);
            Picasso.with(context).load(campaign.imagePath).resize(0, height)
                    .into(pastCampaignViewHolder.image);
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            pastCampaignViewHolder.image.setColorFilter(new ColorMatrixColorFilter(cm));

            pastCampaignViewHolder.title.setText(campaign.title);
        }
        else if(getItemViewType(position) == VIEW_TYPE_EXPIRE)
        {
            ExpireViewHolder viewHolder = (ExpireViewHolder) holder;

            Long expire = (Long) hubList.get(position);
            List<String> campExpTime = TimeUtils.getTimeUntilExpiration(expire);
            int dayInt = Integer.parseInt(campExpTime.get(0));
            int hourInt = Integer.parseInt(campExpTime.get(1));
            int minInt = Integer.parseInt(campExpTime.get(2));

            Resources resources = viewHolder.itemView.getContext().getResources();
            viewHolder.daysLabel.setText(resources.getQuantityString(R.plurals.days, dayInt));
            viewHolder.hoursLabel.setText(resources.getQuantityString(R.plurals.hours, hourInt));
            viewHolder.minutesLabel.setText(resources.getQuantityString(R.plurals.minutes, minInt));

            viewHolder.expire_label.setVisibility(View.VISIBLE);
            viewHolder.expired.setVisibility(View.GONE);
            viewHolder.daysWrapper.setVisibility(View.GONE);
            viewHolder.hoursWrapper.setVisibility(View.GONE);
            viewHolder.minWrapper.setVisibility(View.GONE);
            if(dayInt > 0)
            {
                viewHolder.daysWrapper.setVisibility(View.VISIBLE);
                viewHolder.days.setText(String.valueOf(dayInt));
            }
            else if(hourInt > 0)
            {
                viewHolder.hoursWrapper.setVisibility(View.VISIBLE);
                viewHolder.hours.setText(String.valueOf(hourInt));
            }
            else if(minInt > 0)
            {
                viewHolder.minWrapper.setVisibility(View.VISIBLE);
                viewHolder.minutes.setText(String.valueOf(minInt));
            }
            else
            {
                viewHolder.expire_label.setVisibility(View.GONE);
                viewHolder.expired.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        Object currentObject = hubList.get(position);
        if(currentObject instanceof String)
        {
            return VIEW_TYPE_SECTION_TITLE;
        }
        else if(currentObject instanceof User)
        {
            return VIEW_TYPE_PROFILE;
        }
        else if(currentObject instanceof Campaign)
        {
            //FIXME properly get if campaign is past
            int posOfPastHeader = hubList.indexOf(BEEN_THERE_DONE_GOOD);
            if(position < posOfPastHeader)
            {
                return VIEW_TYPE_CURRENT_CAMPAIGN;
            }
            else
            {
                return VIEW_TYPE_PAST_CAMPAIGN;
            }
        }
        else if(currentObject instanceof Long)
        {
            return VIEW_TYPE_EXPIRE;
        }
        return 0;
    }

    public void addCurrentCampaign(List<Campaign> objects)
    {
        setExpirationView();
        if(hubList.isEmpty())
        {
            int i = hubList.indexOf(BEEN_THERE_DONE_GOOD);
            hubList.addAll(i, objects);
            notifyItemRangeInserted(hubList.size() - objects.size(), hubList.size() - 1);
        }
        else
        {
            for(int i = 0, j = 3; i < objects.size(); i++)
            {
                Object o = hubList.get(j);
                if(o instanceof Campaign)
                {
                    hubList.set(j, objects.get(i));
                    j++;
                }
                else
                {
                    hubList.add(j, objects.get(i));
                    j++;
                }
            }

            notifyItemRangeChanged(2, objects.size());
        }
    }

    private void setExpirationView()
    {
        if(! isPublic)
        {
            Long expire = TimeUtils.getExpirationTime();
            int i = hubList.indexOf(CURRENTLY_DOING);
            hubList.add(i + 1, expire);
        }
    }

    public void addPastCampaign(List<Campaign> objects)
    {
        hubList.addAll(objects);
        notifyItemRangeInserted(hubList.size() - objects.size(), hubList.size() - 1);
    }

    @Override
    public int getItemCount()
    {
        return hubList.size();
    }

    public void processingUpload()
    {
        for(int i = 0; i < hubList.size(); i++)
        {
            Object o = hubList.get(i);
            if(o instanceof Campaign)
            {
                Campaign campaign = (Campaign) o;
                if(campaign.id == clickedCampaign.id)
                {
                    campaign.showShare = Campaign.UploadShare.UPLOADING;
                    notifyItemChanged(i);
                }
            }
        }
    }

    public Campaign getClickedCampaign()
    {
        return clickedCampaign;
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView userImage;
        protected TextView  name;

        public ProfileViewHolder(View itemView)
        {
            super(itemView);
            this.userImage = (ImageView) itemView.findViewById(R.id.user_image);
            this.name = (TextView) itemView.findViewById(R.id.name);
        }
    }

    public static class CurrentCampaignViewHolder extends RecyclerView.ViewHolder
    {
        protected final TextView     title;
        protected final TextView     callToAction;
        protected final TextView     count;
        protected final ImageView    image;
        protected final View         friends;
        protected final TextView     friendsCount;
        protected final LinearLayout friendsContainer;
        protected final Button       proveShare;
        protected final Button       invite;
        protected final View         actionButtons;

        public CurrentCampaignViewHolder(View itemView)
        {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            callToAction = (TextView) itemView.findViewById(R.id.call_to_action);
            image = (ImageView) itemView.findViewById(R.id.image);
            count = (TextView) itemView.findViewById(R.id.count);
            friends = itemView.findViewById(R.id.friends);
            friendsCount = (TextView) itemView.findViewById(R.id.friends_count);
            friendsContainer = (LinearLayout) itemView.findViewById(R.id.friends_container);
            proveShare = (Button) itemView.findViewById(R.id.prove_share);
            invite = (Button) itemView.findViewById(R.id.invite);
            actionButtons = itemView.findViewById(R.id.action_buttons);
        }
    }

    public static class PastCampaignViewHolder extends RecyclerView.ViewHolder
    {
        protected final ImageView image;
        protected final TextView  title;

        public PastCampaignViewHolder(View itemView)
        {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public static class ExpireViewHolder extends RecyclerView.ViewHolder
    {
        public          TextView expired;
        public          TextView expire_label;
        protected final TextView days;
        protected final TextView hours;
        protected final TextView minutes;
        protected final TextView daysLabel;
        protected final TextView hoursLabel;
        protected final TextView minutesLabel;
        protected final View     daysWrapper;
        protected final View     hoursWrapper;
        protected final View     minWrapper;

        public ExpireViewHolder(View itemView)
        {
            super(itemView);
            expire_label = (TextView) itemView.findViewById(R.id.expire_label);
            expired = (TextView) itemView.findViewById(R.id.expired_already);
            daysWrapper = itemView.findViewById(R.id.days_wrapper);
            hoursWrapper = itemView.findViewById(R.id.hours_wrapper);
            minWrapper = itemView.findViewById(R.id.min_wrapper);
            days = (TextView) itemView.findViewById(R.id.days);
            hours = (TextView) itemView.findViewById(R.id.hours);
            minutes = (TextView) itemView.findViewById(R.id.min);
            daysLabel = (TextView) itemView.findViewById(R.id.days_label);
            hoursLabel = (TextView) itemView.findViewById(R.id.hours_label);
            minutesLabel = (TextView) itemView.findViewById(R.id.minutes_label);
        }
    }

    public interface HubAdapterClickListener
    {
        void friendClicked(String friendId);

        void groupClicked(int campaignId, String userId);

        void onShareClicked(Campaign campaign);

        void onProveClicked(Campaign campaign);

        void onInviteClicked(Campaign campaign);
    }
}
