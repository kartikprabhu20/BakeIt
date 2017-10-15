package com.bakingapp.kprabhu.bakeit.image;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

/**
 * Created by kprabhu on 9/17/17.
 */

class VideoThumbnailFactory implements com.bumptech.glide.load.model.ModelLoaderFactory<VideoThumbnailUrl, java.io.InputStream> {
    @Override
    public ModelLoader<VideoThumbnailUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
        return new VideoThumbnailLoader();
    }

    @Override
    public void teardown() {

    }
}
