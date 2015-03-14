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
    private Context mContext;

    public TrophySquareImageView(Context context) {
        super(context);
        mContext = context;
    }

    public TrophySquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public TrophySquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //snap to width
    }

    public void initialize(Trophy trophy) {
        setLayoutParams(new AbsListView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setPadding(20, 20, 20, 20);
        mThumbnail = new ImageThumbnail(mContext);
        new DownloadImageTask(mThumbnail).execute(trophy.getImage());
        mThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
        View ofDoom = LayoutInflater.from(mContext).inflate(R.layout.view_trophy_text, this, false);
        ((TextView) ofDoom.findViewById(R.id.trophy_name_of_doom)).setText(trophy.getName());
        this.addView(mThumbnail);
        this.addView(ofDoom);
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
                Log.e("RHH", "Failed to download image");
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
