package cn.wuenqiang.app;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
/**
 * 
 * @author Administrator
 * 将远程歌曲列表和本地歌曲列表的Activity组织在一起
 */
public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		
		//得到TabHost对象，针对TabActivity的操作通常都由这个对象完成
		TabHost tabHost = this.getTabHost();
		//生成一个Intent对象，该对象指向Mp3ListActivity（由于一个页面是由多个Activity组成的，所以需要用Intent启动另外一些Activity）
		Intent remoteIntent = new Intent();
		remoteIntent.setClass(this, RmoteActivity.class);
		//生成一个TabSpec对象，这个对象代表了一个选项页面
		TabHost.TabSpec remoteSpec = tabHost.newTabSpec("Remote");
		//设置该选项页面的图标和标题，通过系统得到自带图片
		Resources res = this.getResources();
		remoteSpec.setIndicator("Remote",res.getDrawable(android.R.drawable.stat_sys_download));
		//设置该选项页面的内容
		remoteSpec.setContent(remoteIntent);
		//将设置好的TabSpec添加到TabHost
		tabHost.addTab(remoteSpec);
		
		Intent localIntent = new Intent();
		localIntent.setClass(this, LocalActivity.class);
		//生成一个TabSpec对象，这个对象代表了一个选项页面
		TabHost.TabSpec localSpec = tabHost.newTabSpec("Local");
		localSpec.setIndicator("Local",res.getDrawable(android.R.drawable.stat_sys_upload));
		//设置该选项页面的内容
		localSpec.setContent(localIntent);
		//将设置好的TabSpec添加到TabHost
		tabHost.addTab(localSpec);
		
	}

}
