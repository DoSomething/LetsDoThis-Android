package org.dosomething.letsdothis.tasks;
import android.content.Context;

import com.j256.ormlite.dao.Dao;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;

import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 6/23/15.
 */
public class InterestGroupCampaignListTask extends BaseNetworkErrorHandlerTask
{
    public int            interestGroupId;
    public List<Campaign> campaigns;

    public InterestGroupCampaignListTask(int interestGroupId)
    {
        this.interestGroupId = interestGroupId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        ResponseCampaignList response = NetworkHelper.getDoSomethingAPIService()
                .campaignList(interestGroupId);
        campaigns = ResponseCampaignList.getCampaigns(response);

        Dao<Campaign, String> dao = DatabaseHelper.getInstance(context).getCampDao();
        for(Campaign c : campaigns)
        {
            c.interestGroup = interestGroupId;
            dao.createOrUpdate(c);
        }
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        super.handleError(context, throwable);
        return false;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }

}
