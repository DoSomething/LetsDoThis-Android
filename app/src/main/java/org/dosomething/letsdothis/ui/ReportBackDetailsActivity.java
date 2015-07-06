package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.tasks.ReportBackDetailsTask;
import org.dosomething.letsdothis.tasks.SubmitKudosTask;
import org.dosomething.letsdothis.ui.adapters.KudoClickListener;
import org.dosomething.letsdothis.utils.TimeUtils;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/27/15.
 */
public class ReportBackDetailsActivity extends BaseActivity
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String EXTRA_REPORT_BACK_ID = "report_back_id";

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private ImageView image;
    private TextView  timestamp;
    private TextView  caption;
    private TextView  name;
    private ViewGroup kudos;
    private boolean   listenersAdded;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_back_details);

        image = (ImageView) findViewById(R.id.image);
        timestamp = (TextView) findViewById(R.id.timestamp);
        caption = (TextView) findViewById(R.id.caption);
        name = (TextView) findViewById(R.id.name);
        kudos = (ViewGroup) findViewById(R.id.kudos_bar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TaskQueue.loadQueueDefault(this).execute(
                new ReportBackDetailsTask(getIntent().getIntExtra(EXTRA_REPORT_BACK_ID, - 1)));
    }

    public static Intent getLaunchIntent(Context context, int reportBackId)
    {
        return new Intent(context, ReportBackDetailsActivity.class)
                .putExtra(EXTRA_REPORT_BACK_ID, reportBackId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(SubmitKudosTask task)
    {
        if(BuildConfig.DEBUG)
        {
            Toast.makeText(this, "kudos submitted", Toast.LENGTH_SHORT).show();
        }
        TaskQueue.loadQueueDefault(this).execute(
                new ReportBackDetailsTask(getIntent().getIntExtra(EXTRA_REPORT_BACK_ID, - 1)));
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(ReportBackDetailsTask task)
    {
        if(task.reportBack != null)
        {
            final ReportBack reportBack = task.reportBack;
            User user = task.user;

            Picasso.with(this).load(reportBack.getImagePath()).resize(image.getWidth(), 0)
                    .into(image);
            timestamp.setText(TimeUtils.getTimeSince(this, reportBack.createdAt * 1000));
            caption.setText(reportBack.caption);
            if(user != null)
            {
                name.setText(String.format("%s %s.", user.first_name, user.last_name.charAt(0)));
            }
            else
            {
                name.setText(reportBack.user.id);
            }
            toolbar.setTitle(reportBack.campaign.title);

            //FIXME add user's avatar
            if(!listenersAdded)
            {
                listenersAdded = true;
                int count = kudos.getChildCount();
                for(int i = 0; i < count; i++)
                {
                    View kudoView = kudos.getChildAt(i);
                    kudoView.setOnClickListener(new KudoClickListener(i)
                    {
                        @Override
                        public void onClick(View v)
                        {

                            //fixme get drupal id
                            TaskQueue.loadQueueDefault(ReportBackDetailsActivity.this)
                                     .execute(new SubmitKudosTask(kudo.id, reportBack.id, null));

                        }
                    });
                }
            }

        }
        else
        {
            Toast.makeText(this, "report back data failed", Toast.LENGTH_SHORT).show();
        }
    }
}
