package vn.cuongph.mycustomview;

import android.widget.RelativeLayout;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class TextSticker extends RelativeLayout {

    private int mBaseHeight;
    private int mBaseWidth;
    private int mBaseX;
    private int mBaseY;

    private Button mButtonRemove;
    private Button mButtonRotate;
    private Button mButtonScale;
    private Button mButtonRound;
    private RelativeLayout mRelativeLayoutBackground;
    private RelativeLayout mRelativeLayoutGroup;

    private Context mActivity;

    private OnCloseListener mOnCloseListener;

    private int color1;
    private int color2;
    private int color3;

    private DoubleTapListener mDoubleTaplistener;

    private boolean isFree;

    private boolean innerstroke;
    public LayoutInflater mInflater;

    private float mMarginLeft;
    private float mMarginTop;

    private int pivx;
    private int pivy;

    private float startDegree;
    private int textColor;
    private int textInnerShadowColor;
    private int textShadowColor;
    private int textShadowXoffset;
    private int textShadowYoffset;
    private int textStrokeColor;
    private int textStrokeColorinner;
    private float textStrokeWidth;
    public AutoResizeTextView textViewArt;

    public interface DoubleTapListener {
        void onDoubleTap();
    }

    public interface OnCloseListener {
        void close();
    }

    public TextSticker(Context context) {
        super(context);
        this.isFree = false;
        this.textStrokeWidth = 1.0f;
        this.textColor = ViewCompat.MEASURED_STATE_MASK;
        this.textShadowColor = 0;
        this.textStrokeColor = 0;
        this.textStrokeColorinner = 0;
        this.textInnerShadowColor = 0;
        this.textShadowXoffset = 5;
        this.textShadowYoffset = 5;
        this.color1 = 0;
        this.color2 = 0;
        this.color3 = 0;
        this.mActivity = context;
        this.mRelativeLayoutGroup = this;
        this.mBaseX = 0;
        this.mBaseY = 0;
        this.pivx = 0;
        this.pivy = 0;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mInflater.inflate(R.layout.text_art, this, true);
        this.mRelativeLayoutGroup.setLayoutParams(new LayoutParams(400, 400));
        this.textViewArt = (AutoResizeTextView) findViewById(R.id.textviewart);
        this.textViewArt.setText(this.mActivity.getResources().getString(R.string.txt_double_tap_to_edit));
        this.textViewArt.setTextColor(this.textColor);
        this.textViewArt.setGravity(17);
        this.textViewArt.setEnableSizeCache(true);
        this.textViewArt.setTextSize(200.0f);
        this.mButtonRemove = (Button) findViewById(R.id.close);
        this.mButtonRotate = (Button) findViewById(R.id.rotate);
        this.mButtonScale = (Button) findViewById(R.id.zoom);
        this.mButtonRound = (Button) findViewById(R.id.outring);
        this.textViewArt.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        this.textViewArt.setOnTouchListener(new OnTouchListener() {
            final GestureDetector gestureDetector;

            {
                this.gestureDetector = new GestureDetector(TextSticker.this.mActivity, new SimpleOnGestureListener() {
                    public boolean onDoubleTap(MotionEvent e) {
                        TextSticker.this.textViewArt.setFocusable(true);
                        TextSticker.this.textViewArt.setFocusableInTouchMode(true);
                        TextSticker.this.textViewArt.setClickable(true);
                        TextSticker.this.textViewArt.setSelected(true);
                        TextSticker.this.textViewArt.setCursorVisible(true);
                        if (TextSticker.this.textViewArt.getText().toString().equals(TextSticker.this.mActivity.getResources().getString(R.string.txt_double_tap_to_edit))) {
                            TextSticker.this.textViewArt.setText(BuildConfig.FLAVOR);
                        }
                        return false;
                    }
                });
            }

            public boolean onTouch(View v, MotionEvent event) {
                if (TextSticker.this.isFree) {
                    return TextSticker.this.isFree;
                }
                LayoutParams layoutParams = (LayoutParams) TextSticker.this.mRelativeLayoutGroup.getLayoutParams();
                if (TextSticker.this.textViewArt.isFocused()) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN/*0*/:
                        TextSticker.this.mRelativeLayoutGroup.performClick();
                        TextSticker.this.mButtonRemove.setVisibility(VISIBLE);
                        TextSticker.this.mButtonRotate.setVisibility(VISIBLE);
                        TextSticker.this.mButtonScale.setVisibility(VISIBLE);
                        TextSticker.this.mButtonRound.setVisibility(VISIBLE);
                        TextSticker.this.mBaseX = (int) (event.getRawX() - ((float) layoutParams.leftMargin));
                        TextSticker.this.mBaseY = (int) (event.getRawY() - ((float) layoutParams.topMargin));
                        break;
                    case MotionEvent.ACTION_MOVE /*2*/:
                        int x_cord = (int) event.getRawX();
                        int y_cord = (int) event.getRawY();
                        TextSticker.this.mRelativeLayoutBackground = (RelativeLayout) TextSticker.this.getParent();
                        if (x_cord - TextSticker.this.mBaseX > (-((TextSticker.this.mRelativeLayoutGroup.getWidth() * 2) / 3)) && x_cord - TextSticker.this.mBaseX < TextSticker.this.mRelativeLayoutBackground.getWidth() - (TextSticker.this.mRelativeLayoutGroup.getWidth() / 3)) {
                            layoutParams.leftMargin = x_cord - TextSticker.this.mBaseX;
                        }
                        if (y_cord - TextSticker.this.mBaseY > (-((TextSticker.this.mRelativeLayoutGroup.getHeight() * 2) / 3)) && y_cord - TextSticker.this.mBaseY < TextSticker.this.mRelativeLayoutBackground.getHeight() - (TextSticker.this.mRelativeLayoutGroup.getHeight() / 3)) {
                            layoutParams.topMargin = y_cord - TextSticker.this.mBaseY;
                        }
                        layoutParams.rightMargin = -1000;
                        layoutParams.bottomMargin = -1000;
                        TextSticker.this.mRelativeLayoutGroup.setLayoutParams(layoutParams);
                        break;
                }
                TextSticker.this.mRelativeLayoutGroup.invalidate();
                return this.gestureDetector.onTouchEvent(event);
            }
        });

        this.mButtonScale.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (TextSticker.this.isFree) {
                    return TextSticker.this.isFree;
                }
                int x_cord = (int) event.getRawX();
                int y_cord = (int) event.getRawY();
                LayoutParams layoutParams = (LayoutParams) TextSticker.this.mRelativeLayoutGroup.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN /*0*/:
                        TextSticker.this.mBaseX = x_cord;
                        TextSticker.this.mBaseY = y_cord;
                        TextSticker.this.mBaseWidth = TextSticker.this.mRelativeLayoutGroup.getWidth();
                        TextSticker.this.mBaseHeight = TextSticker.this.mRelativeLayoutGroup.getHeight();
                        TextSticker.this.mRelativeLayoutGroup.getLocationOnScreen(new int[2]);
                        TextSticker.this.mMarginLeft = (float) layoutParams.leftMargin;
                        TextSticker.this.mMarginTop = (float) layoutParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP /*1*/:
                    case MotionEvent.ACTION_CANCEL /*3*/:
                    case MotionEvent.ACTION_POINTER_UP /*6*/:
                        TextSticker.this.mRelativeLayoutGroup.performLongClick();
                        break;
                    case MotionEvent.ACTION_MOVE /*2*/:
                        int currx = x_cord;
                        int curry = y_cord;
                        float angle = (float) Math.toDegrees(Math.atan2((double) (curry - TextSticker.this.mBaseY), (double) (currx - TextSticker.this.mBaseX)));
                        if (angle < 0.0f) {
                            angle += 360.0f;
                        }
                        int changesW = currx - TextSticker.this.mBaseX;
                        int changesH = curry - TextSticker.this.mBaseY;
                        changesW = (int) (Math.sqrt((double) ((changesW * changesW) + (changesH * changesH))) * Math.cos(Math.toRadians((double) (angle - TextSticker.this.mRelativeLayoutGroup.getRotation()))));
                        changesH = (int) (Math.sqrt((double) ((changesW * changesW) + (changesH * changesH))) * Math.sin(Math.toRadians((double) (angle - TextSticker.this.mRelativeLayoutGroup.getRotation()))));
                        int width = (changesW * 2) + TextSticker.this.mBaseWidth;
                        int height = (changesH * 2) + TextSticker.this.mBaseHeight;
                        if (width > 150) {
                            layoutParams.width = width;
                            layoutParams.leftMargin = (int) (TextSticker.this.mMarginLeft - ((float) changesW));
                        }
                        if (height > 150) {
                            layoutParams.height = height;
                            layoutParams.topMargin = (int) (TextSticker.this.mMarginTop - ((float) changesH));
                        }
                        TextSticker.this.mRelativeLayoutGroup.setLayoutParams(layoutParams);
                        TextSticker.this.mRelativeLayoutGroup.invalidate();
                        break;
                }
                TextSticker.this.mRelativeLayoutGroup.invalidate();
                TextSticker.this.textViewArt.reAdjust();
                TextSticker.this.textViewArt.invalidate();
                TextSticker.this.invalidate();
                return true;
            }
        });
        this.mButtonRotate.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (TextSticker.this.isFree) {
                    return TextSticker.this.isFree;
                }
                LayoutParams layoutParams = (LayoutParams) TextSticker.this.mRelativeLayoutGroup.getLayoutParams();
                TextSticker.this.mRelativeLayoutBackground = (RelativeLayout) TextSticker.this.getParent();
                int[] pos = new int[2];
                TextSticker.this.mRelativeLayoutBackground.getLocationOnScreen(pos);
                int x_cord = ((int) event.getRawX()) - pos[0];
                int y_cord = ((int) event.getRawY()) - pos[1];
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN/*0*/:
                        TextSticker.this.startDegree = TextSticker.this.mRelativeLayoutGroup.getRotation();
                        TextSticker.this.pivx = layoutParams.leftMargin + (TextSticker.this.getWidth() / 2);
                        TextSticker.this.pivy = layoutParams.topMargin + (TextSticker.this.getHeight() / 2);
                        TextSticker.this.mBaseX = x_cord - TextSticker.this.pivx;
                        TextSticker.this.mBaseY = TextSticker.this.pivy - y_cord;
                        break;
                    case MotionEvent.ACTION_MOVE /*2*/:
                        int curry = TextSticker.this.pivy - y_cord;
                        int angle = (int) (Math.toDegrees(Math.atan2((double) TextSticker.this.mBaseY, (double) TextSticker.this.mBaseX)) - Math.toDegrees(Math.atan2((double) curry, (double) (x_cord - TextSticker.this.pivx))));
//                        if (angle < 0) {
//                            angle += MetaData.DEFAULT_PERIODIC_METADATA_INTERVAL;
//                        }
                        TextSticker.this.mRelativeLayoutGroup.setLayerType(2, null);
                        TextSticker.this.mRelativeLayoutGroup.setRotation((TextSticker.this.startDegree + ((float) angle)) % 360.0f);
                        break;
                }
                TextSticker.this.mRelativeLayoutGroup.invalidate();
                TextSticker.this.textViewArt.invalidate();
                TextSticker.this.textViewArt.reAdjust();
                return true;
            }
        });
        this.mButtonRemove.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!TextSticker.this.isFree) {
                    if (TextSticker.this.mOnCloseListener != null) {
                        TextSticker.this.mOnCloseListener.close();
                    }
                    TextSticker.this.mRelativeLayoutBackground = (RelativeLayout) TextSticker.this.getParent();
                    TextSticker.this.mRelativeLayoutBackground.performClick();
                    TextSticker.this.mRelativeLayoutBackground.removeView(TextSticker.this.mRelativeLayoutGroup);
                }
            }
        });
    }

    public void setOnCloseListner(OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    public void setLocation() {
        this.mRelativeLayoutBackground = (RelativeLayout) getParent();
        Log.e("size", "h" + this.mRelativeLayoutBackground.getHeight() + " w" + this.mRelativeLayoutBackground.getWidth());
        LayoutParams layoutParams = (LayoutParams) this.mRelativeLayoutGroup.getLayoutParams();
        layoutParams.topMargin = (int) (Math.random() * ((double) (this.mRelativeLayoutBackground.getHeight() - 400)));
        layoutParams.leftMargin = (int) (Math.random() * ((double) (this.mRelativeLayoutBackground.getWidth() - 400)));
        this.mRelativeLayoutGroup.setLayoutParams(layoutParams);
    }

    public void setTextViewId() {
//        this.textViewArt.setId(this.mRelativeLayoutGroup.getId() + 10000);
    }

    public void disableAll() {
        this.mButtonRemove.setVisibility(INVISIBLE);
        this.mButtonRotate.setVisibility(INVISIBLE);
        this.mButtonScale.setVisibility(INVISIBLE);
        this.mButtonRound.setVisibility(INVISIBLE);
        this.textViewArt.setFocusable(false);
        this.textViewArt.setFocusableInTouchMode(false);
        this.textViewArt.setClickable(false);
        this.textViewArt.setSelected(false);
        this.textViewArt.setCursorVisible(false);
    }

    public void setFree(boolean b) {
        this.isFree = b;
    }

    public void adjustOpacity(float opacity) {
        this.textViewArt.setAlpha(opacity);
    }

    public float getOpacity() {
        return this.textViewArt.getAlpha();
    }

    public void setTextGravity(int g) {
        this.textViewArt.setGravity(g | 16);
        this.mRelativeLayoutGroup.performLongClick();
    }

    public void setFont(Typeface tf) {
        this.textViewArt.setTypeface(tf);
        this.textViewArt.reAdjust();
        this.mRelativeLayoutGroup.performLongClick();
    }

    public void setColorText(int color) {
        this.textColor = color;
        this.color1 = 0;
        this.color2 = 0;
        this.color3 = 0;
        settextEffects();
        this.textViewArt.setTextColor(this.textColor);
    }

    public TextPaint getPaint() {
        return this.textViewArt.getPaint();
    }

    public void setGradient(int color1, int color2, int color3) {
        settextEffects();
        this.textColor = 0;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        settextEffects();
    }

    public void setGradientTop(int color1) {
        settextEffects();
        this.textColor = 0;
        this.color1 = color1;
        settextEffects();
    }

    public void setGradientCenter(int color2) {
        settextEffects();
        this.textColor = 0;
        this.color2 = color2;
        settextEffects();
    }

    public void setGradientBottom(int color3) {
        settextEffects();
        this.textColor = 0;
        this.color3 = color3;
        settextEffects();
    }

    public void setStrokeSize(float size) {
        this.textStrokeWidth = size;
        settextEffects();
    }

    public void setStrokeEffect(int color, float size) {
        this.textStrokeWidth = size;
        this.textStrokeColor = color;
        settextEffects();
    }

    public void setStrokeColor(int color) {
        this.textStrokeColor = color;
        settextEffects();
    }

    public void removeinnerstroke() {
        this.innerstroke = false;
        this.textViewArt.removeinnerstroke();
    }

    public float getStrokeWidth() {
        return this.textViewArt.getStrokeWidth();
    }

    public void setShadowColor(int color) {
        this.textShadowColor = color;
        settextEffects();
    }

    public void setInnerShadowColor(int color) {
        if (color == 0) {
            this.textViewArt.clearInnerShadows();
        } else {
            this.textViewArt.addInnerShadow(5.0f, (float) this.textShadowXoffset, (float) this.textShadowYoffset, color);
        }
        this.textInnerShadowColor = color;
    }

    public void setInnerShadowColor() {
        if (this.textInnerShadowColor == 0) {
            this.textViewArt.clearInnerShadows();
        } else {
            this.textViewArt.addInnerShadow(5.0f, (float) this.textShadowXoffset, (float) this.textShadowYoffset, this.textInnerShadowColor);
        }
    }

    public void setOffSet(int xoffset, int yoffset) {
        this.textShadowXoffset = xoffset;
        this.textShadowYoffset = yoffset;
        settextEffects();
    }

    public void setOnDoubleTapListener(DoubleTapListener doubleTapListener) {
        this.mDoubleTaplistener = doubleTapListener;
    }

    public void settextEffects() {
        this.textViewArt.setGradient(this.color1, this.color2, this.color3);
        if (this.textShadowColor != 0) {
            this.textViewArt.addOuterShadow(10.0f, (float) this.textShadowXoffset, (float) this.textShadowYoffset, this.textShadowColor);
        } else {
            this.textViewArt.clearOuterShadows();
        }
        this.textViewArt.setOuterStroke(this.textStrokeWidth, this.textStrokeColor);
        this.mRelativeLayoutGroup.invalidate();
        this.textViewArt.invalidate();
        this.textViewArt.reAdjust();
        invalidate();
//        this.mRelativeLayoutGroup.performLongClick();
    }

}
