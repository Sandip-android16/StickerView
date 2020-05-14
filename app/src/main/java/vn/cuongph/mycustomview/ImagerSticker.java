package vn.cuongph.mycustomview;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;



public class ImagerSticker extends RelativeLayout {

    private int mBaseHight;
    private int mBaseWidth;
    private int mBaseX;
    private int mBaseY;

    private Button mButtonRemove;
    private Button mButtonRotate;
    private Button mButtonScale;
    private Button mButtonRound;
    private ImageView mImagView;
    private RelativeLayout mRelativeLayoutBackground;
    private RelativeLayout mRelativeLayoutGroup;

    public LayoutInflater mInflater;
    private Activity mActivity;

    private DoubleTapListener mDoubleTaplistener;
    private boolean isFree;
    private boolean isShadow;

    private int mMarginLeft;
    private int mMarginTop;
    private float mOpacity;
    private Bitmap originalBitmap;
    private int pivx;
    private int pivy;
    private Bitmap shadowBitmap;
    private int[] size;
    private float startDegree;

    public interface DoubleTapListener {
        void onDoubleTap();
    }

    public ImagerSticker(Activity context, String s) {
        super(context);
        this.isFree = false;
        this.mOpacity = 1.0f;
        this.mActivity = context;
        this.size = getScreenSizeInPixels();
        this.mRelativeLayoutGroup = this;
        this.mBaseX = 0;
        this.mBaseY = 0;
        this.pivx = 0;
        this.pivy = 0;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mInflater.inflate(R.layout.image_art, this, true);
        this.mRelativeLayoutGroup.setLayoutParams(new LayoutParams(this.size[0] / 4, this.size[0] / 4));
        this.mImagView = (ImageView) findViewById(R.id.imageview_art);
        this.mImagView.setTag(Integer.valueOf(0));
        this.mButtonRemove = (Button) findViewById(R.id.close);
        this.mButtonRotate = (Button) findViewById(R.id.rotate);
        this.mButtonScale = (Button) findViewById(R.id.zoom);
        this.mButtonRound = (Button) findViewById(R.id.outring);
        Ultils.loadImageByURL(context, s, this.mImagView);
        this.mImagView.setOnTouchListener(new OnTouchListener() {
            final GestureDetector gestureDetector;

            {
                this.gestureDetector = new GestureDetector(ImagerSticker.this.mActivity, new SimpleOnGestureListener() {
                    public boolean onDoubleTap(MotionEvent e) {
                        return false;
                    }
                });
            }

            public boolean onTouch(View v, MotionEvent event) {
                if (ImagerSticker.this.isFree) {
                    return ImagerSticker.this.isFree;
                }
                LayoutParams layoutParams = (LayoutParams) ImagerSticker.this.mRelativeLayoutGroup.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN/*0*/:
                        ImagerSticker.this.mRelativeLayoutGroup.performClick();
                        ImagerSticker.this.mButtonRemove.setVisibility(VISIBLE);
                        ImagerSticker.this.mButtonRotate.setVisibility(VISIBLE);
                        ImagerSticker.this.mButtonScale.setVisibility(VISIBLE);
                        ImagerSticker.this.mButtonRound.setVisibility(VISIBLE);
                        ImagerSticker.this.mBaseX = (int) (event.getRawX() - ((float) layoutParams.leftMargin));
                        ImagerSticker.this.mBaseY = (int) (event.getRawY() - ((float) layoutParams.topMargin));
                        break;
                    case MotionEvent.ACTION_MOVE /*2*/:
                        int x_cord = (int) event.getRawX();
                        int y_cord = (int) event.getRawY();
                        ImagerSticker.this.mRelativeLayoutBackground = (RelativeLayout) ImagerSticker.this.getParent();
                        if (x_cord - ImagerSticker.this.mBaseX > (-((ImagerSticker.this.mRelativeLayoutGroup.getWidth() * 1) / 2)) && x_cord - ImagerSticker.this.mBaseX < ImagerSticker.this.mRelativeLayoutBackground.getWidth() - (ImagerSticker.this.mRelativeLayoutBackground.getWidth() / 2)) {
                            layoutParams.leftMargin = x_cord - ImagerSticker.this.mBaseX;
                        }
                        if (y_cord - ImagerSticker.this.mBaseY > (-((ImagerSticker.this.mRelativeLayoutGroup.getHeight() * 1) / 2)) && y_cord - ImagerSticker.this.mBaseY < ImagerSticker.this.mRelativeLayoutBackground.getHeight() - (ImagerSticker.this.mRelativeLayoutBackground.getHeight() / 2)) {
                            layoutParams.topMargin = y_cord - ImagerSticker.this.mBaseY;
                        }
                        layoutParams.rightMargin = -1000;
                        layoutParams.bottomMargin = -1000;
                        ImagerSticker.this.mRelativeLayoutGroup.setLayoutParams(layoutParams);
                        break;
                }
                ImagerSticker.this.mRelativeLayoutGroup.invalidate();
                this.gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        this.mButtonScale.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (ImagerSticker.this.isFree) {
                    return ImagerSticker.this.isFree;
                }
                int x_cord = (int) event.getRawX();
                int y_cord = (int) event.getRawY();
                LayoutParams layoutParams = (LayoutParams) ImagerSticker.this.mRelativeLayoutGroup.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN /*0*/:
                        ImagerSticker.this.mBaseX = x_cord;
                        ImagerSticker.this.mBaseY = y_cord;
                        ImagerSticker.this.mBaseWidth = ImagerSticker.this.mRelativeLayoutGroup.getWidth();
                        ImagerSticker.this.mBaseHight = ImagerSticker.this.mRelativeLayoutGroup.getHeight();
                        ImagerSticker.this.mRelativeLayoutGroup.getLocationOnScreen(new int[2]);
                        ImagerSticker.this.mMarginLeft = layoutParams.leftMargin;
                        ImagerSticker.this.mMarginTop = layoutParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP /*1*/:
                    case MotionEvent.ACTION_CANCEL /*3*/:
                    case MotionEvent.ACTION_POINTER_UP /*6*/:
                        ImagerSticker.this.mRelativeLayoutGroup.performLongClick();
                        break;
                    case MotionEvent.ACTION_MOVE /*2*/:
                        int currx = x_cord;
                        int curry = y_cord;
                        float angle = (float) Math.toDegrees(Math.atan2((double) (curry - ImagerSticker.this.mBaseY), (double) (currx - ImagerSticker.this.mBaseX)));
                        if (angle < 0.0f) {
                            angle += 360.0f;
                        }
                        int changesW = currx - ImagerSticker.this.mBaseX;
                        int changesH = curry - ImagerSticker.this.mBaseY;
                        changesW = (int) (Math.sqrt((double) ((changesW * changesW) + (changesH * changesH))) * Math.cos(Math.toRadians((double) (angle - ImagerSticker.this.mRelativeLayoutGroup.getRotation()))));
                        changesH = (int) (Math.sqrt((double) ((changesW * changesW) + (changesH * changesH))) * Math.sin(Math.toRadians((double) (angle - ImagerSticker.this.mRelativeLayoutGroup.getRotation()))));
                        int width = (changesW * 2) + ImagerSticker.this.mBaseWidth;
                        int height = (changesH * 2) + ImagerSticker.this.mBaseHight;
//                        if (width > ImagerSticker.this.size[0] / 6 && width < (ImagerSticker.this.size[0] / 2) + (ImagerSticker.this.size[0] / 4)) {
//                            layoutParams.width = width;
//                            layoutParams.leftMargin = ImagerSticker.this.mMarginLeft - changesW;
//                        }
//                        if (height > ImagerSticker.this.size[0] / 6 && height < (ImagerSticker.this.size[0] / 2) + (ImagerSticker.this.size[0] / 4)) {
//                            layoutParams.height = height;
//                            layoutParams.topMargin = ImagerSticker.this.mMarginTop - changesH;
//                        }

                        if (width > 150) {
                            layoutParams.width = width;
                            layoutParams.leftMargin = (int) (ImagerSticker.this.mMarginLeft - ((float) changesW));
                        }
                        if (height > 150) {
                            layoutParams.height = height;
                            layoutParams.topMargin = (int) (ImagerSticker.this.mMarginTop - ((float) changesH));
                        }

                        ImagerSticker.this.mRelativeLayoutGroup.setLayoutParams(layoutParams);
                        break;
                }
                ImagerSticker.this.mRelativeLayoutGroup.invalidate();
                return true;
            }
        });

        this.mButtonRotate.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (ImagerSticker.this.isFree) {
                    return ImagerSticker.this.isFree;
                }
                LayoutParams layoutParams = (LayoutParams) ImagerSticker.this.mRelativeLayoutGroup.getLayoutParams();
                ImagerSticker.this.mRelativeLayoutBackground = (RelativeLayout) ImagerSticker.this.getParent();
                int[] pos = new int[2];
                ImagerSticker.this.mRelativeLayoutBackground.getLocationOnScreen(pos);
                int x_cord = ((int) event.getRawX()) - pos[0];
                int y_cord = ((int) event.getRawY()) - pos[1];
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN /*0*/:
                        ImagerSticker.this.startDegree = ImagerSticker.this.mRelativeLayoutGroup.getRotation();
                        ImagerSticker.this.pivx = layoutParams.leftMargin + (ImagerSticker.this.getWidth() / 2);
                        ImagerSticker.this.pivy = layoutParams.topMargin + (ImagerSticker.this.getHeight() / 2);
                        ImagerSticker.this.mBaseX = x_cord - ImagerSticker.this.pivx;
                        ImagerSticker.this.mBaseY = ImagerSticker.this.pivy - y_cord;
                        break;
                    case MotionEvent.ACTION_MOVE /*2*/:
                        int curry = ImagerSticker.this.pivy - y_cord;
                        int angle = (int) (Math.toDegrees(Math.atan2((double) ImagerSticker.this.mBaseY, (double) ImagerSticker.this.mBaseX)) - Math.toDegrees(Math.atan2((double) curry, (double) (x_cord - ImagerSticker.this.pivx))));
                        ImagerSticker.this.mRelativeLayoutGroup.setLayerType(2, null);
                        ImagerSticker.this.mRelativeLayoutGroup.setRotation((ImagerSticker.this.startDegree + ((float) angle)) % 360.0f);
                        break;
                }
                ImagerSticker.this.mRelativeLayoutGroup.invalidate();
                return true;
            }
        });
        this.mButtonRemove.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!ImagerSticker.this.isFree) {
                    ImagerSticker.this.mRelativeLayoutBackground = (RelativeLayout) ImagerSticker.this.getParent();
                    ImagerSticker.this.mRelativeLayoutBackground.performClick();
                    ImagerSticker.this.mRelativeLayoutBackground.removeView(ImagerSticker.this.mRelativeLayoutGroup);
                }
            }
        });
    }

    private int[] getScreenSizeInPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        this.mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return new int[]{dm.widthPixels, dm.heightPixels};
    }


    public void setLocation() {
        this.mRelativeLayoutBackground = (RelativeLayout) getParent();
        LayoutParams layoutParams = (LayoutParams) this.mRelativeLayoutGroup.getLayoutParams();
        layoutParams.topMargin = (int) (Math.random() * ((double) (this.mRelativeLayoutBackground.getHeight() - 400)));
        layoutParams.leftMargin = (int) (Math.random() * ((double) (this.mRelativeLayoutBackground.getWidth() - 400)));
        this.mRelativeLayoutGroup.setLayoutParams(layoutParams);
    }

    public void setImageId() {
//        this.image.setId(this.mRelativeLayoutGroup.getId() + 10000);
    }

    public void disableAll() {
        this.mButtonRemove.setVisibility(INVISIBLE);
        this.mButtonRotate.setVisibility(INVISIBLE);
        this.mButtonScale.setVisibility(INVISIBLE);
        this.mButtonRound.setVisibility(INVISIBLE);
    }

    public void setColor(int color) {
        this.mImagView.getDrawable().setColorFilter(null);
        this.mImagView.getDrawable().setColorFilter(new ColorMatrixColorFilter(new float[]{0.33f, 0.33f, 0.33f, 0.0f, (float) Color.red(color), 0.33f, 0.33f, 0.33f, 0.0f, (float) Color.green(color), 0.33f, 0.33f, 0.33f, 0.0f, (float) Color.blue(color), 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.mImagView.setTag(Integer.valueOf(color));
        this.mRelativeLayoutGroup.performLongClick();
    }

    public void setImage(Bitmap bm) {
        if (this.originalBitmap == null) {
            this.originalBitmap = bm;
            this.shadowBitmap = shadowImage(this.originalBitmap);
        }
        if (this.isShadow) {
            this.mImagView.setImageBitmap(this.originalBitmap);
        } else {
            this.mImagView.setImageBitmap(this.originalBitmap);
            this.mImagView.setBackgroundResource(0);
        }
        this.mImagView.setAlpha(this.mOpacity);
        setColor(((Integer) this.mImagView.getTag()).intValue());
        this.mRelativeLayoutGroup.performLongClick();
    }

    public void setFree(boolean b) {
        this.isFree = b;
    }

    public void showShadow(boolean isShadow) {
        this.isShadow = isShadow;
        setImage(this.originalBitmap);
        this.mRelativeLayoutGroup.performLongClick();
    }

    public Bitmap shadowImage(Bitmap src) {
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOut);
        int[] offsetXY = new int[2];
        Bitmap bmAlpha = src.extractAlpha(new Paint(), offsetXY);
        Paint ptAlphaColor = new Paint();
        ptAlphaColor.setColor(-11184811);
        canvas.drawBitmap(Bitmap.createScaledBitmap(bmAlpha, bmAlpha.getWidth(), bmAlpha.getHeight(), false), (float) offsetXY[0], (float) offsetXY[1], ptAlphaColor);
        bmAlpha.recycle();
        return bmOut;
    }

    public void adjustOpacity(float opacity) {
        this.mOpacity = opacity;
        setImage(this.originalBitmap);
    }

    public void resetImage() {
        this.originalBitmap = null;
        this.mRelativeLayoutGroup.performLongClick();
    }

    public void changeImageView(String str) {
        Ultils.loadImageByURL(mActivity, str, this.mImagView);
    }

    public void setOnDoubleTapListener(DoubleTapListener doubleTapListener) {
        this.mDoubleTaplistener = doubleTapListener;
    }

    public float getmOpacity() {
        return this.mImagView.getAlpha();
    }

}
