package com.vlife.changewallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

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
            //Log.i("my","开始运行onCreate");

            //初始化画笔
            mPaint.setARGB(76,0,0,255);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            //Log.d("my","运行了onCreate");
        }

        @Override
        public void onDestroy(){
            super.onDestroy();
            //删除回调
            mHandler.removeCallbacks(myRunnable);
            //Log.i("my","运行了onDestroy");
        }
        @Override
        public void onVisibilityChanged(boolean visible){
            mVisible=visible;
            //Log.i("my","运行了onVisibility");
            //当界面可见的时候，显示壁纸
            if(visible){
                changePicture();
            }
            else{
                //如果界面不可见，删除回调
                mHandler.removeCallbacks(myRunnable);
            }
        }
        //定义图片切换的工具方法
        private void changePicture(){
            //Log.i("my","运行了changePicture");
            //获取该壁纸的SurfaceHolder
            final SurfaceHolder holder=getSurfaceHolder();
            Canvas c=null;
            try{
                c=holder.lockCanvas();
                if(c!=null){
                    //绘制背景色
                    c.drawColor(0xffffffff);
                    mPaint.setAlpha(255);
                    //设置背景图片
                    for(int i=0;i<count;i++){
                        //c.scale(c.getWidth(),c.getHeight());
                        c.drawBitmap(bitmaps[i],0,0,mPaint);
                        //c.drawRect(0,0,150,75,mPaint);//测试是否有画图
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
