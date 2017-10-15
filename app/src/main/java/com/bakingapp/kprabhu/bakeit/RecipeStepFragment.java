package com.bakingapp.kprabhu.bakeit;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bakingapp.kprabhu.bakeit.model.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kprabhu on 9/17/17.
 */

class RecipeStepFragment extends LifecycleFragment {
    private static final String TAG = "RecipeStepFragment";

    private static final java.lang.String SELECTED_POSITION = "position";

    @BindView(R.id.recipe_step_description)
    TextView recipeStepDesc;

    @BindView(R.id.recipe_step_video)
    SimpleExoPlayerView exoPlayerView;


    SimpleExoPlayer exoPlayer;
    RecipeStepViewModel recipeStepModel;

    long position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        position = C.TIME_UNSET;
        if (savedInstanceState != null) {
            //...your code...
            position = savedInstanceState.getLong(SELECTED_POSITION, C.TIME_UNSET);
        }
        return inflater.inflate(R.layout.recipe_step_info, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recipeStepModel = ViewModelProviders.of(getActivity()).get(RecipeStepViewModel.class);
        observeViewModel();
    }

    private void observeViewModel() {
        recipeStepModel.getStep().observe(this, new Observer<Step>() {
            @Override
            public void onChanged(@Nullable Step step) {
                Log.i(TAG, "observeViewModel: step" + step);
                recipeStepDesc.setText(step.description);
                if (step.videoURL.isEmpty()) {
                    exoPlayerView.setVisibility(View.GONE);
                    return;
                }
                exoPlayerView.setVisibility(View.VISIBLE);
                setVideo(step);
            }

        });
    }

    private void setVideo(Step step) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        AdaptiveTrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        exoPlayerView.setPlayer(exoPlayer);

        Uri mp4VideoUri = Uri.parse(step.videoURL);

        BandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), "BakingApp", (TransferListener<? super DataSource>) bandwidthMeterA);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri,
                dataSourceFactory, extractorsFactory, null, null);
        if (position != C.TIME_UNSET) exoPlayer.seekTo(position);
        exoPlayer.prepare(videoSource);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            recipeStepDesc.setVisibility(View.GONE);
        } else {

            recipeStepDesc.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            position = exoPlayer.getCurrentPosition();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(SELECTED_POSITION,position);
    }
}
