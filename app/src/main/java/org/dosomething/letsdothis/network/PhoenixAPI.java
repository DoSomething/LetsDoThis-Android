package org.dosomething.letsdothis.network;

import org.dosomething.letsdothis.network.models.ResponseCampaignList;
import org.dosomething.letsdothis.network.models.ResponseCampaignWrapper;
import org.dosomething.letsdothis.network.models.ResponseReportBack;
import org.dosomething.letsdothis.network.models.ResponseReportBackList;

import co.touchlab.android.threading.errorcontrol.NetworkException;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;


/**
 * Provides the Phoenix API interface.
 * @see <a href="https://github.com/DoSomething/phoenix/wiki/API">API Reference</a>
 * @author izzyoji
 * @author NearChaos
 */
public interface PhoenixAPI {
    String PRODUCTION_URL = "https://www.dosomething.org/api/v1/";
    String THOR_URL = "https://thor.dosomething.org/api/v1";
    String QA_URL = "https://staging.dosomething.org/api/v1";

    @Headers("Content-Type: application/json")
    @GET("/campaigns/{id}.json")
    ResponseCampaignWrapper campaign(@Path("id") int id) throws NetworkException;

    @Headers("Content-Type: application/json")
    @GET("/reportback-items.json?load_user=true")
    ResponseReportBackList reportBackList(
            @Query("status") String status,
            @Query("campaigns") String campaignIds,
            @Query("count") int count,
            @Query("random") boolean random,
            @Query("page") int page) throws NetworkException;

    @Headers("Content-Type: application/json")
    @GET("/reportback-items/{id}.json?load_user=true")
    ResponseReportBack reportBack(@Path("id") int id) throws NetworkException;

    @Headers("Content-Type: application/json")
    @GET("/campaigns.json")
    ResponseCampaignList campaignListByCause(
            @Query("term_ids") int causeId,
            @Query("page") int page) throws NetworkException;
}
