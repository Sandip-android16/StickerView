package vn.cuongph.mycustomview;

import android.widget.EditText;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import java.util.ArrayList;
import java.util.WeakHashMap;


public class TextViewEditor extends android.support.v7.widget.AppCompatEditText {
    static Context c;
    private WeakHashMap<String, Pair<Canvas, Bitmap>> canvasStore;
    int color1;
    int color2;
    int color3;
    private Drawable foregroundDrawable;
    private boolean frozen;
    InnerShadow innerShadow;
    private ArrayList<Shadow> innerShadows;
    InnerStroke innerStroke;
    private int[] lockedCompoundPadding;
    OuterStroke outerStroke;
    Shadow shadow;
    private Integer strokeColor;
    private float strokeWidth;
    private Bitmap tempBitmap;
    private Canvas tempCanvas;

    public static class InnerShadow {
        int color;
        float dx;
        float dy;
        float r;

        public InnerShadow(float r, float dx, float dy, int color) {
            this.r = r;
            this.dx = dx;
            this.dy = dy;
            this.color = color;
        }
    }

    public static class InnerStroke {
        int color;

        public InnerStroke(int color) {
            this.color = color;
        }
    }

    public static class OuterStroke {
        int color;

        public OuterStroke(int color) {
            this.color = color;
        }
    }

    public static class Shadow {
        int color;
        float dx;
        float dy;
        float r;

        public Shadow(float r, float dx, float dy, int color) {
            this.r = r;
            this.dx = dx;
            this.dy = dy;
            this.color = color;
        }
    }

    public TextViewEditor(Context context) {
        super(context);
        this.color1 = 0;
        this.color2 = 0;
        this.color3 = 0;
        this.frozen = false;
        c = context;
        init(null);
    }

    public TextViewEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.color1 = 0;
        this.color2 = 0;
        this.color3 = 0;
        this.frozen = false;
        c = context;
        init(attrs);
    }

    public TextViewEditor(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.color1 = 0;
        this.color2 = 0;
        this.color3 = 0;
        this.frozen = false;
        c = context;
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        this.innerShadows = new ArrayList();
        if (this.canvasStore == null) {
            this.canvasStore = new WeakHashMap();
        }
        /*if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MagicTextView);
            if (a.getString(8) != null) {
                setTypeface(Typeface.createFromAsset(getContext().getAssets(), String.format("fonts/%s.ttf", new Object[]{"fonts/%s.otf", typefaceName})));
            }
            if (a.hasValue(9)) {
                Drawable foreground = a.getDrawable(9);
                if (foreground != null) {
                    setForegroundDrawable(foreground);
                } else {
                    setTextColor(a.getColor(9, ViewCompat.MEASURED_STATE_MASK));
                }
            }
            if (a.hasValue(10)) {
                Drawable background = a.getDrawable(10);
                if (background != null) {
                    setBackgroundDrawable(background);
                } else {
                    setBackgroundColor(a.getColor(10, ViewCompat.MEASURED_STATE_MASK));
                }
            }
            if (a.hasValue(0)) {
                addInnerShadow((float) a.getDimensionPixelSize(1, 0), (float) a.getDimensionPixelOffset(2, 0), (float) a.getDimensionPixelOffset(3, 0), a.getColor(0, ViewCompat.MEASURED_STATE_MASK));
            }
            if (a.hasValue(4)) {
                addOuterShadow((float) a.getDimensionPixelSize(5, 0), (float) a.getDimensionPixelOffset(6, 0), (float) a.getDimensionPixelOffset(7, 0), a.getColor(4, ViewCompat.MEASURED_STATE_MASK));
            }
            if (a.hasValue(13)) {
                float Width = (float) a.getDimensionPixelSize(11, 1);
                int strokeColor = a.getColor(13, ViewCompat.MEASURED_STATE_MASK);
                float strokeMiter = (float) a.getDimensionPixelSize(12, 10);
                Join strokeJoin = null;
                switch (a.getInt(14, 0)) {
                    case MetaData.DEFAULT_SECONDS_BETWEEN_ADS *//*0*//*:
                        strokeJoin = Join.MITER;
                        break;
                    case MetaData.DEFAULT_ACTIVITIES_BETWEEN_ADS *//*1*//*:
                        strokeJoin = Join.BEVEL;
                        break;
                    case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED *//*2*//*:
                        strokeJoin = Join.ROUND;
                        break;
                }
                setStroke(this.strokeWidth, strokeColor, strokeJoin, strokeMiter);
            }
        }*/
        if (VERSION.SDK_INT < 11) {
            return;
        }
        if (this.innerShadows.size() > 0 || this.foregroundDrawable != null) {
            setLayerType(1, null);
        }
    }

    public void setStroke(float width, int color, Join join, float miter) {
        this.strokeWidth = width;
        this.strokeColor = color == 0 ? null : Integer.valueOf(color);
    }

    public void setOuterStroke(float width, int color) {
        setStroke(width, color, Join.MITER, 10.0f);
        this.strokeWidth = width;
        this.outerStroke = new OuterStroke(color);
        invalidate();
    }

    public void setinnerstroke(float width, int color) {
        setStroke(width, color, Join.MITER, 10.0f);
        this.strokeWidth = width;
        this.innerStroke = new InnerStroke(color);
        invalidate();
    }

    public void removeinnerstroke() {
        this.innerStroke = null;
        invalidate();
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setGradient(int c1, int c2, int c3) {
        this.color1 = c1;
        this.color2 = c2;
        this.color3 = c3;
    }

    public void addOuterShadow(float r, float dx, float dy, int color) {
        if (r == 0.0f) {
        }
        this.shadow = new Shadow(10.0f, dx, dy, color);
        invalidate();
    }

    public void addInnerShadow(float r, float dx, float dy, int color) {
        if (r == 0.0f) {
        }
        this.innerShadow = new InnerShadow(5.0f, dx, dy, color);
        invalidate();
    }

    public void clearInnerShadows() {
        this.innerShadow = null;
        invalidate();
    }

    public void clearOuterShadows() {
        this.shadow = null;
    }

    public void setForegroundDrawable(Drawable d) {
        this.foregroundDrawable = d;
    }

    public Drawable getForeground() {
        if (this.foregroundDrawable == null) {
            return this.foregroundDrawable;
        }
        return new ColorDrawable(getCurrentTextColor());
    }

    @SuppressLint({"NewApi"})
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        freeze();
        Drawable restoreBackground = getBackground();
        Drawable[] restoreDrawables = getCompoundDrawables();
        int restoreColor = getCurrentTextColor();
        setCompoundDrawables(null, null, null, null);
        if (this.shadow != null) {
            if (VERSION.SDK_INT >= 19) {
                super.setLayerType(1, null);
            }
            setShadowLayer(this.shadow.r, this.shadow.dx, this.shadow.dy, this.shadow.color);
            getPaint().setShader(null);
            super.onDraw(canvas);
        } else {
            setShadowLayer(0.0f, 0.0f, 0.0f, 0);
            super.onDraw(canvas);
        }
        if (this.color1 == 0 && this.color2 == 0 && this.color3 == 0) {
            setPaintFlags(4);
            getPaint().setShader(null);
            setPaintFlags(1);
            setTextColor(restoreColor);
            super.onDraw(canvas);
        } else {
            setPaintFlags(4);
            Shader textShader = new LinearGradient(0.0f, 0.0f, 0.0f, (float) getLineHeight(), new int[]{this.color1, this.color2, this.color3}, new float[]{0.4f, 0.6f, 1.0f}, TileMode.REPEAT);
            setPaintFlags(1);
            getPaint().clearShadowLayer();
            getPaint().setShader(textShader);
            super.onDraw(canvas);
        }
        if (this.outerStroke != null) {
            Canvas tempCanvas = new Canvas();
            Paint paintStrock = new Paint();
            paintStrock.setColor(this.outerStroke.color);
            paintStrock.setStyle(Style.STROKE);
            paintStrock.setStrokeWidth(this.strokeWidth + 5.0f);
            paintStrock.setFlags(1);
            paintStrock.setTypeface(getTypeface());
            paintStrock.setTextSize(getTextSize());
            for (int i = 0; i < getLineCount(); i++) {
                String str = getText().toString().substring(getLayout().getLineStart(i), getLayout().getLineEnd(i));
                Rect bounds = new Rect();
                getLineBounds(i, bounds);
                FontMetrics fm = paintStrock.getFontMetrics();
                if (i != getLineCount() - 1) {
                    canvas.drawText(str, getLayout().getLineLeft(i), (((float) bounds.bottom) + fm.leading) - fm.descent, paintStrock);
                } else {
                    canvas.drawText(str, getLayout().getLineLeft(i), (((float) bounds.bottom) + fm.leading) - fm.bottom, paintStrock);
                }
                super.onDraw(tempCanvas);
            }
        }
        if (this.innerShadow != null) {
            generateTempCanvas();
            TextPaint paint = getPaint();
            setTextColor(this.innerShadow.color);
            super.onDraw(this.tempCanvas);
            setTextColor(ViewCompat.MEASURED_STATE_MASK);
            paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
            paint.setMaskFilter(new BlurMaskFilter(this.innerShadow.r, Blur.NORMAL));
            this.tempCanvas.save();
            this.tempCanvas.translate(this.innerShadow.dx, this.innerShadow.dy);
            super.onDraw(this.tempCanvas);
            this.tempCanvas.restore();
            canvas.drawBitmap(this.tempBitmap, 0.0f, 0.0f, null);
            this.tempCanvas.drawColor(0, Mode.CLEAR);
            paint.setXfermode(null);
            paint.setMaskFilter(null);
            setTextColor(restoreColor);
            setShadowLayer(0.0f, 0.0f, 0.0f, 0);
        }
        if (restoreDrawables != null) {
            setCompoundDrawablesWithIntrinsicBounds(restoreDrawables[0], restoreDrawables[1], restoreDrawables[2], restoreDrawables[3]);
        }
        unfreeze();
    }

    public void setTextColor(int color) {
        getPaint().clearShadowLayer();
        super.setTextColor(color);
        Log.d("color", BuildConfig.FLAVOR + color);
    }

    private void generateTempCanvas() {
        String key = String.format("%dx%d", new Object[]{Integer.valueOf(getWidth()), Integer.valueOf(getHeight())});
        Pair<Canvas, Bitmap> stored = (Pair) this.canvasStore.get(key);
        if (stored != null) {
            this.tempCanvas = (Canvas) stored.first;
            this.tempBitmap = (Bitmap) stored.second;
            return;
        }
        this.tempCanvas = new Canvas();
        this.tempBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        this.tempCanvas.setBitmap(this.tempBitmap);
        this.canvasStore.put(key, new Pair(this.tempCanvas, this.tempBitmap));
    }

    public void freeze() {
        this.lockedCompoundPadding = new int[]{getCompoundPaddingLeft(), getCompoundPaddingRight(), getCompoundPaddingTop(), getCompoundPaddingBottom()};
        this.frozen = true;
    }

    public void unfreeze() {
        this.frozen = false;
    }

    public void requestLayout() {
        if (!this.frozen) {
            super.requestLayout();
        }
    }

    public void postInvalidate() {
        if (!this.frozen) {
            super.postInvalidate();
        }
    }

    public void postInvalidate(int left, int top, int right, int bottom) {
        if (!this.frozen) {
            super.postInvalidate(left, top, right, bottom);
        }
    }

    public void invalidate() {
        if (!this.frozen) {
            super.invalidate();
        }
    }

    public void invalidate(Rect rect) {
        if (!this.frozen) {
            super.invalidate(rect);
        }
    }

    public void invalidate(int l, int t, int r, int b) {
        if (!this.frozen) {
            super.invalidate(l, t, r, b);
        }
    }

    public int getCompoundPaddingLeft() {
        return !this.frozen ? super.getCompoundPaddingLeft() : this.lockedCompoundPadding[0];
    }

    public int getCompoundPaddingRight() {
        return !this.frozen ? super.getCompoundPaddingRight() : this.lockedCompoundPadding[1];
    }

    public int getCompoundPaddingTop() {
        return !this.frozen ? super.getCompoundPaddingTop() : this.lockedCompoundPadding[2];
    }

    public int getCompoundPaddingBottom() {
        return !this.frozen ? super.getCompoundPaddingBottom() : this.lockedCompoundPadding[3];
    }

}
