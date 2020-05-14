package vn.cuongph.mycustomview;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Ultils {
    public static void loadImageByURL(Context context, String url, ImageView imageView){
        try {
            Glide.with(context)
                    .load(url)
                    .into(imageView);
        } catch(IllegalArgumentException ex) {
            Log.wtf("Glide-tag", String.valueOf(imageView.getTag()));
        }
    }
}
