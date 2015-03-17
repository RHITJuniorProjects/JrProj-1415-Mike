package rhit.jrProj.henry.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

import rhit.jrProj.henry.R;
import rhit.jrProj.henry.firebase.Trophy;

/**
 * Created by johnsoaa on 3/14/2015.
 */
public class TrophySquareImageView extends FrameLayout {

    private ImageThumbnail mThumbnail;
    private AsyncTask<String, Void, Bitmap> mTask;
    private View mOfDoom;

    public TrophySquareImageView(Context context) {
        super(context);
        mOfDoom = LayoutInflater.from(context).inflate(R.layout.view_trophy_text, this, false);
        mThumbnail = new ImageThumbnail(context);
        mThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.addView(mThumbnail);
        this.addView(mOfDoom);
    }

    public TrophySquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mOfDoom = LayoutInflater.from(context).inflate(R.layout.view_trophy_text, this, false);

        mThumbnail = new ImageThumbnail(context);
        mThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.addView(mThumbnail);
        this.addView(mOfDoom);
    }

    public TrophySquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mOfDoom = LayoutInflater.from(context).inflate(R.layout.view_trophy_text, this, false);
        mThumbnail = new ImageThumbnail(context);
        mThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.addView(mThumbnail);
        this.addView(mOfDoom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //snap to width
    }

    public void initialize(Trophy trophy) {
        setLayoutParams(new AbsListView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setPadding(20, 20, 20, 20);
        if (!trophy.getImage().equals("") && mTask == null || (mTask != null && mTask.getStatus() == AsyncTask.Status.FINISHED)) {
            mTask = new DownloadImageTask(mThumbnail).execute(trophy.getImage());
        }
        ((TextView) mOfDoom.findViewById(R.id.trophy_name_of_doom)).setText(getLabel(trophy));
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED) {
            mTask.cancel(true);
        }
    }

    protected String getLabel(Trophy trophy) {
        return trophy.getName();
    }

    private static class ImageThumbnail extends ImageView {
        public ImageThumbnail(Context context) {
            super(context);
            setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //snap to width
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        public final ImageThumbnail mTrophyPicViewer;

        public DownloadImageTask(ImageThumbnail trophyPicImage) {
            mTrophyPicViewer = trophyPicImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(urls[0]).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("RHH", "Failed to download image", e);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                mTrophyPicViewer.setImageBitmap(bitmap);
            }
        }
    }

}
