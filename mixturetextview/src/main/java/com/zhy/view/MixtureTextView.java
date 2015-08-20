package com.zhy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by zhy on 15/8/20.
 */
public class MixtureTextView extends RelativeLayout
{

    String text = "8月12日，天津港的惊天巨响hello world how are you ，炸碎了很多人的hello world how are you心，人们在感动于救人的同时，禁不住愤怒地责问，为什么一次次重特大hello world how are you事故中生命的代价换不来应有的平hello world how are you安?下个血淋淋的场面将会在哪里出现?是的，我们已经有点伤不起了，因为世间没有什么比生命更宝贵。为了生命的美好，在生产中投入多大的hello world how are you安全成本都不算昂贵。更何况2013年6月，针对全国多个地区接连发生多起重特大安全生产事故，习近平总书记就已强调：“重特大安全生产事故，造成重大人员伤亡和财产损失，必须引起hello world how are you高度重视。人命关天，发展决不能以牺牲人的生命为代价。这必须作为一条不可逾越的红线。”时隔不久，为什么又一次以决堤式的hello world how are you程度来冲毁这条红线，谁该hello world how are you为之买单?";

    private Layout layout = null;

    public MixtureTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setWillNotDraw(false);
    }

    private List<List<Rect>> mDestRects = new ArrayList<List<Rect>>();
    private List<Integer> mYs = new ArrayList<Integer>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        TextPaint paint = new TextPaint();
        paint.setTextSize(30);
        layout = new StaticLayout("中", paint, 100, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false);
        int lineHeight = layout.getLineDescent(0) - layout.getLineAscent(0);
        lineHeight = layout.getLineBottom(0) - layout.getLineTop(0);

        Log.e("TAG", "lineHeight = " + lineHeight);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View v = getChildAt(i);
            v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            int height = v.getMeasuredHeight();

            height = resetValueBaseLineHeight(lineHeight, height);

            MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
            lp.height = height;
            lp.leftMargin = resetValueBaseLineHeight(lineHeight, lp.leftMargin);
            lp.rightMargin = resetValueBaseLineHeight(lineHeight, lp.rightMargin);
            lp.bottomMargin = resetValueBaseLineHeight(lineHeight, lp.bottomMargin);
            lp.topMargin = resetValueBaseLineHeight(lineHeight, lp.topMargin);
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private int resetValueBaseLineHeight(int lineHeight, int height)
    {
        int rest = height % lineHeight;
        if (rest * 1.0f / lineHeight > 0.5)
        {
            height = (height / lineHeight + 1) * lineHeight;
        } else
        {
            height = (height / lineHeight) * lineHeight;
        }
        return height;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        //clear datas
        mDestRects.clear();
        mYs.clear();

        //get all y cors
        int cCount = getChildCount();
        Rect rect = null;
        for (int i = 0; i < cCount; i++)
        {
            View c = getChildAt(i);
            if (c.getVisibility() == View.GONE) continue;
            mYs.add(c.getTop());
            mYs.add(c.getBottom());
        }
        mYs.add(getHeight());

        //去除相同的数字
        HashSet<Integer> tmp = new HashSet<>(mYs);
        mYs = new ArrayList<>(tmp);

        Collections.sort(mYs);

        //add view's top y
        int first = mYs.get(0);
        if (first != 0)
        {
            Rect top = new Rect(0, 0, getWidth(), first);
            mDestRects.add(Arrays.asList(top));
        }


        //just log
        for (Integer r : mYs)
        {
            Log.e("TAG", "after y =>" + r);
        }

        //find rect between y1 and y2
        List<Rect> tmps = null;
        for (int i = 0; i < mYs.size() - 1; i++)
        {
            int y1 = mYs.get(i);
            int y2 = mYs.get(i + 1);

            Log.e("TAG", "y1 = " + y1 + " , y2 = " + y2);
            tmps = new ArrayList<Rect>();
            List<Rect> rs = caculateViewYBetween(y1, y2);
            Log.e("TAG", " get " + rs.size() + " views ");
            if (rs.size() == 0)
            {
                tmps.add(new Rect(0, y1, getWidth(), y2));
                for (Rect r : tmps)
                {
                    Log.e("TAG", r.toShortString());//l t r b
                }
                mDestRects.add(tmps);
                continue;
            }
            if (rs.size() == 1)
            {
                Rect leftFirst = rs.get(0);
                if (leftFirst.left > 0)
                {
                    tmps.add(new Rect(0, y1, leftFirst.left, y2));
                    if (leftFirst.right < getWidth())
                        tmps.add(new Rect(leftFirst.right, y1, getWidth(), y2));
                } else
                {
                    tmps.add(new Rect(leftFirst.right, y1, getWidth(), y2));
                }
                for (Rect r : tmps)
                {
                    Log.e("TAG", r.toShortString());//l t r b
                }
                mDestRects.add(tmps);
                continue;
            }

            Rect leftFirst = rs.get(0);
            if (leftFirst.left > 0)
            {
                tmps.add(new Rect(0, y1, leftFirst.left, y2));
            }

            for (int j = 0; j < rs.size() - 1; j++)
            {
                Rect ra = rs.get(j);
                Rect rb = rs.get(j + 1);
                tmps.add(new Rect(ra.right, y1, rb.left, y2));
            }

            Rect lastRect = rs.get(rs.size() - 1);
            if (lastRect.right < getWidth())
            {
                tmps.add(new Rect(lastRect.right, y1, getWidth(), y2));
            }

            for (Rect r : tmps)
            {
                Log.e("TAG", r.toShortString());//l t r b
            }

            mDestRects.add(tmps);
        }
        //==finish mDestRects init
        Log.e("TAG", " mDestRects.size = " + mDestRects.size());


        TextPaint paint = new TextPaint();
        paint.setTextSize(30);
        layout = new StaticLayout("中", paint, 100, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false);
        int lineHeight = layout.getLineDescent(0) - layout.getLineAscent(0);
        lineHeight = layout.getLineBottom(0) - layout.getLineTop(0);

        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < getHeight(); i++)
        {
            //canvas.drawRect(0, i * lineHeight, getWidth(), i * lineHeight + lineHeight, p);
        }


        List<List<Rect>> bak = new ArrayList<List<Rect>>(mDestRects);
        int inc = 0;
        for (int i = 0; i < mDestRects.size(); i++)
        {
            List<Rect> rs = mDestRects.get(i);

            if (rs.size() > 1)
            {
                int index = inc + i;
                bak.remove(rs);
                inc--;
                Rect rect1 = rs.get(0);
                int lh = rect1.height() / lineHeight ;
                for (int k = 0; k < lh; k++)
                {
                    for (int j = 0; j < rs.size(); j++)
                    {
                        inc++;
                        bak.add(index++, Arrays.asList(new Rect(
                                rs.get(j).left,
                                rect1.top + lineHeight * k,
                                rs.get(j).right,
                                rect1.top + lineHeight * k + lineHeight)));
                    }
                }

            }
        }

        Log.e("TAG", "bak.size = " + bak.size());

        mDestRects = bak;
        Log.e("TAG", "mDestRects bak.size = " + bak.size());

        int start = 0;
        boolean newLine = false;
        int top = 0;
        int dy = 0;
        for (int i = 0; i < mDestRects.size(); i++)
        {
            List<Rect> rs = mDestRects.get(i);
            Rect r = null;
            Log.e("TAG", " rs.size = " + rs.size());
            if (rs.size() == 1)
            {
                r = rs.get(0);
                layout =
                        new StaticLayout(text.substring(start),
                                paint, r.width(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false);

                int lineCount = r.height() % lineHeight == 0 ? r.height() / lineHeight : (r.height() / lineHeight) + 1;

                //ajust lineCount
                if (i == mDestRects.size() - 1)
                    lineCount = layout.getLineCount() < lineCount ? layout.getLineCount() : lineCount;

                //int end = layout.getLineBottom(lineCount - 1) - layout.getLineTop(0) + dy ;

                //boolean isLineCountDel = justifyLineCountDel(end, r);
                //if (isLineCountDel)
                {
                    // Log.e("TAG", "isLineCountAdd");
                    //  lineCount--;
                }


                Log.e("TAG", "lineCount = " + lineCount);
                canvas.save();

                if (newLine)
                {
                    dy += top - r.top;
                }

                canvas.translate(r.left, r.top + dy);
                canvas.clipRect(0, 0, r.width(), layout.getLineBottom(lineCount - 1) - layout.getLineTop(0));
                layout.draw(canvas);
                canvas.restore();
                start += layout.getLineEnd(lineCount - 1);

                newLine = false;
                if (i < mDestRects.size() - 1)
                {
                    Rect r2 = mDestRects.get(i + 1).get(0);
                    if (r.top != r2.top)
                    {
                        Log.e("TAG", "newLine");
                        newLine = true;
                        top = layout.getLineBottom(lineCount - 1) - layout.getLineTop(0) + r.top;
                    }

                }
            }
        }


    }



    private List<Rect> caculateViewYBetween(int y1, int y2)
    {
        List<Rect> rs = new ArrayList<>();
        Rect tmp = null;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View v = getChildAt(i);
            if (v.getTop() <= y1 && v.getBottom() >= y2)
            {
                tmp = new Rect(v.getLeft(), y1, v.getRight(), y2);
                rs.add(tmp);
            }
        }
        return rs;
    }
}
