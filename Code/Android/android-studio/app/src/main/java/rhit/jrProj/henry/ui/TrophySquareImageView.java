package rhit.jrProj.henry.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import rhit.jrProj.henry.firebase.Trophy;

/**
 * Created by johnsoaa on 3/14/2015.
 */
public class TrophySquareImageView extends FrameLayout {

    private ImageThumbnail mThumbnail;

    public TrophySquareImageView(Context context) {
        super(context);
    }

    public TrophySquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrophySquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //snap to width
    }

    private void initialize(Context context, Trophy trophy) {
        setLayoutParams(new AbsListView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        //set padding in future
        mThumbnail = new ImageThumbnail(context);
        mThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.addView(mThumbnail);
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
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
