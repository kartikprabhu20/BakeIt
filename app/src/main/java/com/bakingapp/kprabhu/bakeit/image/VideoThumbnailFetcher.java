package com.bakingapp.kprabhu.bakeit.image;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by kprabhu on 9/18/17.
 */

class VideoThumbnailFetcher implements DataFetcher<InputStream> {
    private VideoThumbnailUrl videoThumbnailUrl;

    VideoThumbnailFetcher(VideoThumbnailUrl videoThumbnailUrl) {
        this.videoThumbnailUrl = videoThumbnailUrl;
    }


    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
        Bitmap bitmap;
        MediaMetadataRetriever mediaMetadataRetriever = null;

        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoThumbnailUrl.getUrl(), new HashMap<String, String>());

            bitmap = mediaMetadataRetriever.getFrameAtTime();

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }

        if (bitmap == null) {
            callback.onLoadFailed(new Exception("Bitmap is null"));
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            ByteArrayInputStream bs = new ByteArrayInputStream(bos.toByteArray());

            callback.onDataReady(bs);
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @Override
    public DataSource getDataSource() {
        return com.bumptech.glide.load.DataSource.REMOTE;
    }

}
