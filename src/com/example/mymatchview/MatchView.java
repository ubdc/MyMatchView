package com.example.mymatchview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class MatchView extends View {
	/**
	 * 闪电链动画完成一遍的时间
	 */
	private int LIGHTNING_CHAIN_INTERVAL;
	/**
	 * 闪电链尾巴的时间长度
	 */
	private int LIGHTNING_CHAIN_TAIL_LENGTH;
	/**
	 * 初始动画时长
	 */
	private int ANIMATION_IN_OUT_TIME;
	private int ANIMATION_IN_OUT_TIME_TAIL;
	private List<Line> lines;
	private Paint linePaint;
	private boolean isLightningChainBegined;
	private boolean isFirstLightningChain;
	/**
	 * 没有闪电链的线条的默认alpha
	 */
	private int defaultAlpha = 100;
	/**
	 * 第一次发生闪电链的时间
	 */
	private long firstLightningTime;
	
	private long animaInOutTime, currentAnimTime;
	private int[] randomDegree;
	private int[] randomTranslateX;
	private int[] randomTranslateY;
	
	private Handler h;
	
	private Runnable in = new Runnable() {
		@Override
		public void run() {
			if (animaInOutTime == 0) {
				animaInOutTime = System.currentTimeMillis();
			}
			
			currentAnimTime = System.currentTimeMillis() - animaInOutTime;
			if (currentAnimTime > ANIMATION_IN_OUT_TIME + ANIMATION_IN_OUT_TIME_TAIL) {
				isLightningChainBegined = true;
			} else {
				h.post(this);
			}
			invalidate();
		}
	};
	
	private Runnable out = new Runnable() {
		@Override
		public void run() {
			isLightningChainBegined = false;
			if (animaInOutTime == 0) {
				animaInOutTime = System.currentTimeMillis();
			}
			
			currentAnimTime = (ANIMATION_IN_OUT_TIME + ANIMATION_IN_OUT_TIME_TAIL) - (System.currentTimeMillis() - animaInOutTime);
			if (currentAnimTime <= 0) {
				currentAnimTime = 0;
				isLightningChainBegined = false;
			} else {
				h.post(this);
			}
			invalidate();
		}
	};
	
	public void animIn() {
		animaInOutTime = currentAnimTime = 0;
		isLightningChainBegined = false;
		isFirstLightningChain = true;
		firstLightningTime = 0;
		h.removeCallbacks(in);
		h.removeCallbacks(out);
		h.post(in);
	}
	
	public void animOut() {
		animaInOutTime = currentAnimTime = 0;
		h.removeCallbacks(in);
		h.removeCallbacks(out);
		h.post(out);
	}

	public MatchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MatchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MatchView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		h = new Handler(getContext().getMainLooper());
		linePaint = new Paint();
		linePaint.setStrokeWidth(3);
		linePaint.setAntiAlias(true);
		linePaint.setColor(0xffffffff);
		lines = new ArrayList<Line>();
	}
	
	public void setLines(List<Line> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		lines.clear();
		lines.addAll(list);
		
		currentAnimTime = 0;
		
		LIGHTNING_CHAIN_INTERVAL = lines.size() * 80;
		LIGHTNING_CHAIN_TAIL_LENGTH = LIGHTNING_CHAIN_INTERVAL / 5;
		
		ANIMATION_IN_OUT_TIME = lines.size() * 19;
		ANIMATION_IN_OUT_TIME_TAIL = 19 * 60;
		
		Random ran = new Random();
		randomDegree = new int[lines.size()];
		for (int i = 0; i < randomDegree.length; i++) {
			randomDegree[i] = (int) (180 + 360 * ran.nextFloat());
		}
		randomTranslateX = new int[lines.size()];
		for (int i = 0; i < randomTranslateX.length; i++) {
			randomTranslateX[i] = (int) (50 + 150 * ran.nextFloat());
		}
		randomTranslateY = new int[lines.size()];
		for (int i = 0; i < randomTranslateY.length; i++) {
			randomTranslateY[i] = (int) (0 + -50 * ran.nextFloat());
		}
		
		requestLayout();
		animIn();
	}
	
	/**
	 * @param progress [0..100]
	 */
	public void setAnimProgress(int progress) {
		h.removeCallbacks(in);
		h.removeCallbacks(out);
		currentAnimTime = (ANIMATION_IN_OUT_TIME + ANIMATION_IN_OUT_TIME_TAIL) * progress / 100;
		isLightningChainBegined = false;
		invalidate();
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		animIn();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.save();
		canvas.clipRect(getPaddingLeft() - 1, getPaddingTop() - 1, getWidth() - getPaddingRight() + 1, getHeight() - getPaddingBottom() + 1);
		if (isLightningChainBegined) {
			drawLightningChain(canvas);
		} else {
			animateInOut(canvas, currentAnimTime);
		}
		canvas.restore();
	}
	
	private void animateInOut(Canvas canvas, long currentAnimTime) {
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			int lineTime = ANIMATION_IN_OUT_TIME * i / lines.size();
			
			if (lineTime > currentAnimTime) {
				linePaint.setAlpha(0);
			} else {
				int dt = (int) (currentAnimTime - lineTime);
				if (dt > ANIMATION_IN_OUT_TIME_TAIL) {
					linePaint.setAlpha(defaultAlpha);
					canvas.drawLine(getPaddingLeft() + line.start.x, getPaddingTop() + line.start.y, getPaddingLeft() + line.end.x, getPaddingTop() + line.end.y, linePaint);
				} else {
					canvas.save();
					linePaint.setAlpha(defaultAlpha * dt / ANIMATION_IN_OUT_TIME_TAIL);
					canvas.translate(
							randomTranslateX[i] - randomTranslateX[i] * dt / ANIMATION_IN_OUT_TIME_TAIL, 
							randomTranslateY[i] - randomTranslateY[i] * dt / ANIMATION_IN_OUT_TIME_TAIL);
					canvas.rotate(
							randomDegree[i] - randomDegree[i] * dt / ANIMATION_IN_OUT_TIME_TAIL,
							getPaddingLeft() + (line.start.x + line.end.x) / 2, 
							getPaddingLeft() + (line.start.y + line.end.y) / 2);
					canvas.drawLine(
							getPaddingLeft() + line.start.x, 
							getPaddingTop() + line.start.y, 
							getPaddingLeft() + line.start.x + (line.end.x - line.start.x) * dt / ANIMATION_IN_OUT_TIME_TAIL, 
							getPaddingTop() + line.start.y + (line.end.y - line.start.y) * dt / ANIMATION_IN_OUT_TIME_TAIL, 
							linePaint);
					canvas.restore();
				}
			}
		}
		invalidate();
	}
	
	private void drawLightningChain(Canvas canvas) {
		if (firstLightningTime == 0) {
			firstLightningTime = System.currentTimeMillis();
		}
		
		long currentAnimTime = (System.currentTimeMillis() - firstLightningTime) % LIGHTNING_CHAIN_INTERVAL;
		
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			int lineTime = LIGHTNING_CHAIN_INTERVAL * i / lines.size();
			
			if (lineTime > currentAnimTime) {
				if (currentAnimTime >= LIGHTNING_CHAIN_TAIL_LENGTH || isFirstLightningChain) {
					linePaint.setAlpha(defaultAlpha);
				} else {
					lineTime -= LIGHTNING_CHAIN_INTERVAL;
					setLinePaintAlpha((int) (currentAnimTime - lineTime));
				}
			} else {
				setLinePaintAlpha((int) (currentAnimTime - lineTime));
			}
			canvas.drawLine(getPaddingLeft() + line.start.x, getPaddingTop() + line.start.y, getPaddingLeft() + line.end.x, getPaddingTop() + line.end.y, linePaint);
		}
		
		if (currentAnimTime > LIGHTNING_CHAIN_TAIL_LENGTH) {
			isFirstLightningChain = false;
		}
		
		invalidate();
	}
	
	private void setLinePaintAlpha(int dt) {
		if (dt > LIGHTNING_CHAIN_TAIL_LENGTH) {
			linePaint.setAlpha(defaultAlpha);
		} else {
			linePaint.setAlpha(255 - (255 - defaultAlpha) * dt / LIGHTNING_CHAIN_TAIL_LENGTH);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		int maxWidth = 0;
		int maxHeight = 0;
		
		if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
			for (int i = 0; i < lines.size(); i++) {
				Line line = lines.get(i);
				maxWidth = Math.max(maxWidth, line.start.x);
				maxWidth = Math.max(maxWidth, line.end.x);
				maxHeight = Math.max(maxHeight, line.start.y);
				maxHeight = Math.max(maxHeight, line.end.y);
			}
			maxWidth += getPaddingLeft() + getPaddingRight();
			maxHeight += getPaddingTop() + getPaddingBottom();
		}
		
		if (widthMode == MeasureSpec.EXACTLY) {
			maxWidth = width;
		}
		
		if (heightMode == MeasureSpec.EXACTLY) {
			maxHeight = height;
		}
		setMeasuredDimension(maxWidth, maxHeight);
	}
	
	public static class Line {
		public Point start = new Point();
		public Point end = new Point();
		
		public Line(Point start, Point end) {
			this.start = start;
			this.end = end;
		}
		
		public Line(int startX, int startY, int endX, int endY) {
			this.start.set(startX, startY);
			this.end.set(endX, endY);
		}
		
		public void offset(int x, int y) {
			start.offset(x, y);
			end.offset(x, y);
		}
		
		@Override
		public String toString() {
			return "{" + start + ", " + end + "}";
		}
	}
}
