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
public abstract class ItemSquareImageView<T> extends FrameLayout {

    private ImageThumbnail mThumbnail;
    private Context mContext;

    public ItemSquareImageView(Context context) {
        super(context);
        mContext = context;
        mThumbnail = new ImageThumbnail(context);
        mThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.addView(mThumbnail);
    }

    public ItemSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mThumbnail = new ImageThumbnail(context);
        mThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.addView(mThumbnail);
    }

    public ItemSquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mThumbnail = new ImageThumbnail(context);
        mThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.addView(mThumbnail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //snap to width
    }

    public abstract void initialize(T item);

    protected abstract String getLabel(T item);

    protected static class ImageThumbnail extends ImageView {
        public ImageThumbnail(Context context) {
            super(context);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //snap to width
        }
    }

    protected ImageThumbnail getThumbnail() {
        return this.mThumbnail;
    }

    protected void setThumbnail(ImageThumbnail thumb) {
        this.mThumbnail = thumb;
    }

    public Context getmContext() {
        return this.mContext;
    }

    protected class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        public final ImageThumbnail mItemPicViewer;

        public DownloadImageTask(ImageThumbnail itemPicImage) {
            mItemPicViewer = itemPicImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(urls[0]).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("RHH", "Failed to download image");
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                mItemPicViewer.setImageBitmap(bitmap);
            }
        }
    }


}