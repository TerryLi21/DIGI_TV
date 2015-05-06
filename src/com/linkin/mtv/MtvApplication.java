/**
 * @desc
 * 
 * @author fire@ipmacro.com
 * @since 2015-4-15
 */
package com.linkin.mtv;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MtvApplication extends Application {

	public static MtvApplication mApp;

	@Override
	public void onCreate() {
		super.onCreate();
		mApp = this;
		initImageLoader();
	}

	private void initImageLoader() {
		// 创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);
	}

}
