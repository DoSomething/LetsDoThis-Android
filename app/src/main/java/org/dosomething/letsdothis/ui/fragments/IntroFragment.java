package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.IntroActivity;
import org.dosomething.letsdothis.ui.views.SlantedBackgroundDrawable;

/**
 * Created by toidiu on 4/21/15.
 */
public class IntroFragment extends BaseIntroFragment
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String TAG          = IntroFragment.class.getSimpleName();
    public static final String SLANTED_LEFT = "SLANTED_LEFT";
    public static final String INTRO_TEXT   = "INTRO_TEXT";
    public static final String IMAGE_RES    = "IMAGE_RES";
    public static final String SHOW_PREV    = "SHOW_PREV";

    public static IntroFragment newInstance(boolean showPrev, IntroActivity.IntroFragmentExtraHolder introFragmentExtraHolder)
    {
        Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_PREV, showPrev);
        bundle.putBoolean(SLANTED_LEFT, introFragmentExtraHolder.slantedLeft);
        bundle.putString(INTRO_TEXT, introFragmentExtraHolder.text);

        bundle.putString(IMAGE_RES, introFragmentExtraHolder.imageResource);
        IntroFragment introFragment = new IntroFragment();
        introFragment.setArguments(bundle);

        return introFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initIntroNavigation(view);

        boolean slatedLeft = getArguments().getBoolean(SLANTED_LEFT);
        String text = getArguments().getString(INTRO_TEXT);
        boolean showPrev = getArguments().getBoolean(SHOW_PREV);

        View prev = view.findViewById(R.id.prev);
        if(! showPrev)
        {
            prev.setVisibility(View.GONE);
        }

        TextView introText = (TextView) view.findViewById(R.id.intro_text);
        introText.setText(text);

        View slantedBg = view.findViewById(R.id.slanted_bg);
        slantedBg.setBackground(new SlantedBackgroundDrawable(slatedLeft));
        slantedBg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

}
