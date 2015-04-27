package org.dosomething.letsdothis.ui.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.ReportBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class CampaignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int VIEW_TYPE_CAMPAIGN          = 0;
    public static final int VIEW_TYPE_CAMPAIGN_EXPANDED = 1;
    public static final int VIEW_TYPE_CAMPAIGN_FOOTER   = 2;
    public static final int VIEW_TYPE_REPORT_BACK       = 3;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ArrayList<Object> campaigns = new ArrayList<>();
    private CampaignClickListener campaignClickListener;
    private int selectedPosition = - 1;

    public interface CampaignClickListener
    {
        void onCampaignClicked(int campaignId);

        void onCampaignExpanded(int position);
    }

    public CampaignAdapter(List<Campaign> campaigns, CampaignClickListener campaignClickListener)
    {
        super();
        this.campaigns.addAll(campaigns);
        this.campaignClickListener = campaignClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
            case VIEW_TYPE_CAMPAIGN_FOOTER:
                View footerLayout = LayoutInflater.from(parent.getContext())
                                                      .inflate(R.layout.item_campaign_footer,
                                                               parent, false);
                return new CampaignFooterViewHolder((TextView) footerLayout);
            case VIEW_TYPE_REPORT_BACK:
                View reportBackLayout = LayoutInflater.from(parent.getContext())
                                                      .inflate(R.layout.item_report_back_square,
                                                               parent, false);
                return new ReportBackViewHolder((ImageView) reportBackLayout);
            case VIEW_TYPE_CAMPAIGN_EXPANDED:
                View bigLayout = LayoutInflater.from(parent.getContext())
                                               .inflate(R.layout.item_campaign_large, parent,
                                                        false);
                return new ExpandedCampaignViewHolder(bigLayout);
            default:
                View smallLayout = LayoutInflater.from(parent.getContext())
                                                 .inflate(R.layout.item_campaign, parent, false);
                return new CampaignViewHolder(smallLayout);

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN)
        {
            final Campaign campaign = (Campaign) campaigns.get(position);
            CampaignViewHolder campaignViewHolder = (CampaignViewHolder) holder;
            campaignViewHolder.title.setText(campaign.title);
            campaignViewHolder.callToAction.setText(campaign.callToAction);
            Picasso.with(campaignViewHolder.imageView.getContext()).load(campaign.imagePath)
                   .into(campaignViewHolder.imageView);
            campaignViewHolder.root.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectedPosition = position;
                    campaignClickListener.onCampaignExpanded(position);
                    notifyItemChanged(position);
                }
            });
        }
        else if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN_EXPANDED)
        {
            final Campaign campaign = (Campaign) campaigns.get(position);
            ExpandedCampaignViewHolder expandedCampaignViewHolder = (ExpandedCampaignViewHolder) holder;
            expandedCampaignViewHolder.title.setText(campaign.title);
            expandedCampaignViewHolder.callToAction.setText(campaign.callToAction);
            Picasso.with(expandedCampaignViewHolder.imageView.getContext()).load(campaign.imagePath)
                   .into(expandedCampaignViewHolder.imageView);
            expandedCampaignViewHolder.imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectedPosition = - 1;
                    notifyItemChanged(position);
                }
            });
            expandedCampaignViewHolder.campaignDetails.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    campaignClickListener.onCampaignClicked(campaign.id);
                }
            });

            expandedCampaignViewHolder.problemFact.setText(campaign.problemFact);
        }
        else if(getItemViewType(position) == VIEW_TYPE_REPORT_BACK)
        {
            final ReportBack reportBack = (ReportBack) campaigns.get(position);
            ReportBackViewHolder reportBackViewHolder = (ReportBackViewHolder) holder;

            Picasso.with(reportBackViewHolder.root.getContext()).load(reportBack.getImagePath())
                   .into(reportBackViewHolder.root);
        }


    }

    @Override
    public int getItemViewType(int position)
    {
        Object currentObject = campaigns.get(position);
        if(currentObject instanceof Campaign)
        {
            if(position == selectedPosition)
            {
                return VIEW_TYPE_CAMPAIGN_EXPANDED;
            }
            else
            {
                return VIEW_TYPE_CAMPAIGN;
            }
        }
        else if (currentObject instanceof String)
        {
            return VIEW_TYPE_CAMPAIGN_FOOTER;
        }
        else
        {
            return VIEW_TYPE_REPORT_BACK;
        }
    }

    public void addItem(Object o)
    {
        campaigns.add(o);
        notifyItemInserted(campaigns.size() - 1);
    }

    public void addAll(List<ReportBack> objects)
    {
        campaigns.addAll(objects);
        notifyItemRangeInserted(campaigns.size() - objects.size(), campaigns.size() - 1);
    }
    @Override
    public int getItemCount()
    {
        return campaigns.size();
    }

    public static class CampaignViewHolder extends RecyclerView.ViewHolder
    {
        protected View      root;
        protected ImageView imageView;
        protected TextView  title;
        protected TextView  callToAction;

        public CampaignViewHolder(View itemView)
        {
            super(itemView);
            this.root = itemView;
            this.imageView = (ImageView) itemView.findViewById(R.id.image);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.callToAction = (TextView) itemView.findViewById(R.id.call_to_action);
        }
    }

    public static class ExpandedCampaignViewHolder extends CampaignViewHolder
    {
        private TextView problemFact;
        private View     campaignDetails;

        public ExpandedCampaignViewHolder(View itemView)
        {
            super(itemView);
            problemFact = (TextView) itemView.findViewById(R.id.problemFact);
            campaignDetails = itemView.findViewById(R.id.campaign_details);
        }
    }

    public static class ReportBackViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView root;

        public ReportBackViewHolder(ImageView itemView)
        {
            super(itemView);
            this.root = itemView;
        }
    }

    public static class CampaignFooterViewHolder extends RecyclerView.ViewHolder
    {
        public CampaignFooterViewHolder(TextView itemView)
        {
            super(itemView);
        }
    }
}