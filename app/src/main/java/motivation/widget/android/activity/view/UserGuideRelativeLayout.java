package motivation.widget.android.activity.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import motivation.widget.android.R;


public class UserGuideRelativeLayout extends RelativeLayout {

    private Paint transparentPaint;
    private float favouriteCircleCenterX;
    private float favouriteCircleY;
    private float circleRadius;

    public UserGuideRelativeLayout(Context context) {
        super(context);
    }

    public UserGuideRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
        init();
    }

    private void init() {
        transparentPaint = new Paint();
        transparentPaint.setColor(Color.TRANSPARENT);
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public UserGuideRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UserGuideRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        canvas.drawCircle(favouriteCircleCenterX, favouriteCircleY, circleRadius, transparentPaint);
    }

    public void setFavouriteCircleCenter(float centerX, float centerY) {
        float circleDiameter = getResources().getDimensionPixelSize(R.dimen.favourite_circle_diameter);
        this.circleRadius = circleDiameter / 2.0f;
        this.favouriteCircleCenterX = centerX;
        this.favouriteCircleY = centerY;
    }
}
