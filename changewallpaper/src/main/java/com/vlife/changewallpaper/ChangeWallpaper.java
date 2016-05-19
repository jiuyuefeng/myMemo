package com.vlife.changewallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/5/19 0019.
 */
public class ChangeWallpaper extends WallpaperService {
    //定义需要加载的图片数组
    public Bitmap []bitmaps;
    //实现WallPaperService必须实现的抽象方法
    @Override
    public Engine onCreateEngine() {

        //加载图片
        bitmaps=new Bitmap[]{
                BitmapFactory.decodeResource(getResources(),R.mipmap.pic1),
                BitmapFactory.decodeResource(getResources(),R.mipmap.pic2),
                BitmapFactory.decodeResource(getResources(),R.mipmap.pic3),
                BitmapFactory.decodeResource(getResources(),R.mipmap.pic4),
                BitmapFactory.decodeResource(getResources(),R.mipmap.pic5),
                BitmapFactory.decodeResource(getResources(),R.mipmap.pic6),
        };
        //返回自定义的Engine
        return new MyEngine();
    }

    //自定义Engine
    class MyEngine extends Engine{

        //记录程序界面是否可见
        private  boolean mVisible;
        //记录切换次数
        private int count=1;
        //定义画笔
        private Paint mPaint=new Paint();
        //定义一个Handler，用于定时更新UI
        private Handler mHandler = new Handler();
        //定义一个周期性执行的任务
        private Runnable myRunnable= new Runnable() {
            public void run() {
                //动态画图
                changePicture();
            }
        };
        @Override
        public void onCreate(SurfaceHolder surfaceHolder){
            super.onCreate(surfaceHolder);
            //初始化画笔
//            mPaint.setARGB(76,0,0,255);
//            mPaint.setAntiAlias(true);
//            mPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        public void onDestroy(){
            super.onDestroy();
            //删除回调
            mHandler.removeCallbacks(myRunnable);
        }
        @Override
        public void onVisibilityChanged(boolean visible){
            mVisible=visible;
            //当界面可见的时候，显示壁纸
            if(visible){
                changePicture();
            }
            else{
                //如果界面不可见，删除回调
                mHandler.removeCallbacks(myRunnable);
            }
        }

        //缩放图片
        private Bitmap resizeBitmap(Bitmap bitmap,int newWidth,int newHeight){
            if(bitmap!=null){
                int oldWidth=bitmap.getWidth();
                int oldHeight=bitmap.getHeight();
                //计算宽高和缩放率
                float zoomWidth = (float)newWidth/oldWidth;
                float zoomHeight = (float)newHeight/oldHeight;
                //创建操作图片用的matrix对象Matrix
                Matrix matrix = new Matrix();
                //缩放图片动作
                matrix.postScale(zoomWidth,zoomHeight);
                //创建新的图片Bitmap
                return Bitmap.createBitmap(bitmap,0,0,oldWidth,oldHeight,matrix,true);
            }else{
                return null;
            }
        }

        //定义图片切换的工具方法
        private void changePicture(){

            //获取手机屏幕大小
            WindowManager windowManager= (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            //int width= windowManager.getDefaultDisplay().getWidth();
            //int height=windowManager.getDefaultDisplay().getHeight();
            Display display = windowManager.getDefaultDisplay();
            Point size=new Point();
            display.getSize(size);
            int width=size.x;
            int height=size.y;
            Log.d("TAG","bitmap.size"+width+","+height);

            //获取该壁纸的SurfaceHolder
            final SurfaceHolder holder=getSurfaceHolder();
            Canvas c=null;
            try{
                c=holder.lockCanvas();
                if(c!=null){
                    //绘制背景色
                    //c.drawColor(0xffffffff);
                    //设置透明度
                    //mPaint.setAlpha(255);
                    //设置壁纸图片
                    for(int i=0;i<count;i++){
                        Bitmap tempBitmap=resizeBitmap(bitmaps[i],width,height);
                        c.drawBitmap(tempBitmap,0,0,null);
                    }
                }
            } finally {
                if(c!=null){
                    holder.unlockCanvasAndPost(c);
                }
            }
            //删除回调
            mHandler.removeCallbacks(myRunnable);
            if(mVisible){
                count++;
                if(count>=6){
                    count=1;
                    try{
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //指定5秒后切换一次
                mHandler.postDelayed(myRunnable,5000);
            }
        }
    }

}
