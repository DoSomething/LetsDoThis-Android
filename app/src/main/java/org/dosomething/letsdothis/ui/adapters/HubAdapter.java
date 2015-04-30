package org.dosomething.letsdothis.ui.adapters;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    public HubAdapter(User user)
    {
        super();
        this.hubList.add(user);
        hubList.add(CURRENTLY_DOING);
        hubList.add(BEEN_THERE_DONE_GOOD);
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
            profileViewHolder.name
                    .setText(String.format("%s %s.", user.first_name, user.last_name.charAt(0)));
        }
        else if(getItemViewType(position) == VIEW_TYPE_CURRENT_CAMPAIGN)
        {
            Campaign campaign = (Campaign) hubList.get(position);
            CurrentCampaignViewHolder currentCampaignViewHolder = (CurrentCampaignViewHolder) holder;
            currentCampaignViewHolder.title.setText(campaign.title);
            currentCampaignViewHolder.callToAction.setText(campaign.callToAction);
        }
        else
        {
            if(getItemViewType(position) == VIEW_TYPE_PAST_CAMPAIGN)
            {
                Campaign campaign = (Campaign) hubList.get(position);
                PastCampaignViewHolder pastCampaignViewHolder = (PastCampaignViewHolder) holder;
                //FIXME add image

                ColorMatrix cm = new ColorMatrix();
                cm.setSaturation(0);

                pastCampaignViewHolder.title.setText(campaign.title);
                pastCampaignViewHolder.campImage.setImageResource(R.mipmap.ic_launcher);
                pastCampaignViewHolder.campImage.setColorFilter(new ColorMatrixColorFilter(cm));
            }
            else if(getItemViewType(position) == VIEW_TYPE_EXPIRE)
            {
                ExpireViewHolder expireViewHolder = (ExpireViewHolder) holder;

                Long expire = (Long) hubList.get(position);
                List<String> timeUntilExpiration = TimeUtils.getTimeUntilExpiration(expire);
                expireViewHolder.days.setText(timeUntilExpiration.get(0));
                expireViewHolder.hours.setText(timeUntilExpiration.get(1));
                expireViewHolder.min.setText(timeUntilExpiration.get(2));
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
        Campaign campaign = objects.get(0);
        setExpirationView(campaign);
        int i = hubList.indexOf(BEEN_THERE_DONE_GOOD);
        hubList.addAll(i, objects);
        notifyItemRangeInserted(hubList.size() - objects.size(), hubList.size() - 1);
    }

    private void setExpirationView(Campaign campaign)
    {
        //        campaign.startTime  //FIXME get real expiration time

        Long l = System.currentTimeMillis();
        int i = hubList.indexOf(CURRENTLY_DOING);
        hubList.add(i + 1, l);
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
        protected final TextView title;
        protected final TextView callToAction;

        public CurrentCampaignViewHolder(View itemView)
        {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            callToAction = (TextView) itemView.findViewById(R.id.call_to_action);
        }
    }

    public static class PastCampaignViewHolder extends RecyclerView.ViewHolder
    {
        protected final ImageView campImage;
        protected final TextView  title;

        public PastCampaignViewHolder(View itemView)
        {
            super(itemView);
            campImage = (ImageView) itemView.findViewById(R.id.camp_image);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public static class ExpireViewHolder extends RecyclerView.ViewHolder
    {
        protected final TextView days;
        protected final TextView hours;
        protected final TextView min;

        public ExpireViewHolder(View itemView)
        {
            super(itemView);
            days = (TextView) itemView.findViewById(R.id.days);
            hours = (TextView) itemView.findViewById(R.id.hours);
            min = (TextView) itemView.findViewById(R.id.min);
        }
    }

}
