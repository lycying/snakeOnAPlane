package info.u250.snakeonaplane;

import info.u250.c2d.engine.Engine;
import info.u250.c2d.graphic.AdControl;
import info.u250.snakeonaplane.R;
import info.u250.snakeonaplane.SnakeOnAPlane;
import info.u250.snakeonaplane.scene.PlatformFiles;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.ResolutionStrategy;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class SnakeOnAPlaneAndroidActivity extends AndroidApplication {
	RelativeLayout layout;
	private LoadingProgressAndroid loadingView;

	public LoadingProgressAndroid getLoadingView() {
		return loadingView;
	}
	protected AdView adView;
	private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
	protected Handler handler = new Handler()  {
	        @Override
	        public void handleMessage(Message msg) {
	            switch(msg.what) {
	                case SHOW_ADS:
	                {
	                    adView.setVisibility(View.VISIBLE);
	                    break;
	                }
	                case HIDE_ADS:
	                {
	                    adView.setVisibility(View.GONE);
	                    break;
	                }
	            }
	        }
	    };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		layout = new RelativeLayout(this);

		loadingView = new LoadingProgressAndroid(this);
		// Create and setup the AdMob view
        adView = new AdView(this, AdSize.BANNER, "a15052e9c1bc823"); // Put in your secret key here
        adView.loadAd(new AdRequest());

        SnakeOnAPlane snake =  new SnakeOnAPlane(new AdControl() {
			@Override
			public void show() {
				handler.sendEmptyMessage(SHOW_ADS );
			}
			
			@Override
			public void hide() {
				handler.sendEmptyMessage( HIDE_ADS);
			}
			
		},new PlatformFiles() {
			
			@Override
			public void showLevelDialog(final LevelDialogEvents callback) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						final AlertDialog dlg = new AlertDialog.Builder(SnakeOnAPlaneAndroidActivity.this).setCancelable(false).create();
						dlg.show();
						final Window window = dlg.getWindow();
						window.setContentView(R.layout.level_dialog);
						ImageView pre = (ImageView) window.findViewById(R.id.level_pre);
						ImageView menu = (ImageView) window.findViewById(R.id.level_menu);
						ImageView next = (ImageView) window.findViewById(R.id.ilevel_next);
						
						pre.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								callback.pre();
								dlg.dismiss();
							}
						});
						
						next.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								callback.next();
								dlg.dismiss();
							}
						});
						
						menu.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								callback.menu();
								dlg.dismiss();
							}
						});
					}
				});
				
			}
		},new LoadingFeedbackAndroid(this));
        AndroidApplicationConfiguration config=new AndroidApplicationConfiguration();
		config.useGL20=Engine.getEngineConfig().useGL20;
		config.useAccelerometer=false;
		config.useCompass=false;
		config.useWakelock=true;
		config.resolutionStrategy = new ResolutionStrategy() {
			@Override
			public MeasuredDimension calcMeasures(int widthMeasureSpec,
					int heightMeasureSpec) {
				int width = View.MeasureSpec.getSize(widthMeasureSpec);
				int height = View.MeasureSpec.getSize(heightMeasureSpec);
				
				final float eWidth = Engine.getEngineConfig().width;
				final float eHeight = Engine.getEngineConfig().height;
				//480/800 > 560/960
				if((float)width/(float)height<eWidth/eHeight){
					height = (int)(eHeight*width/eWidth);
				}else if(width/height>eWidth/eHeight){
					width = (int)(eWidth*height/eHeight);
				}
				return new MeasuredDimension(width, height);
			}
		};
		
		View game = initializeForView(snake, config);

		layout.addView(game);
		
		RelativeLayout.LayoutParams adParams = 
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        layout.addView(adView, adParams);
        
		setContentView(layout);
		loadingView.showLoading();
		handler.sendEmptyMessage( HIDE_ADS);
		super.onCreate(savedInstanceState);
	}

	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				Gdx.input.getInputProcessor().keyDown(Keys.BACK);
			} catch (Exception ex) {
			}
			return true;
		}
		return false;
	}
}