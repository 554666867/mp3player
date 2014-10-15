package cn.wuenqiang.app.service;

import java.io.File;

import cn.wuenqiang.app.AppConstant;
import cn.wuenqiang.downoad.HttpDownloader;
import cn.wuenqiang.model.Mp3Info;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

public class DownloadService extends Service {

	//Service启动时调用（每次点击ListActivity当中的一个条目的时候）
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//从Intent从将mp3Info取出
		Mp3Info mp3Info = (Mp3Info)intent.getSerializableExtra("mp3Info") ;
		//生成一个下载线程
		DownloadThread downloadThread = new DownloadThread(mp3Info);
		Thread thread = new Thread(downloadThread);
		thread.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	//内部类，用于下载mp3Info歌曲歌词
	class DownloadThread implements Runnable{
		private Mp3Info mp3Info;
		public DownloadThread(Mp3Info mp3Info){
			this.mp3Info = mp3Info;
		}
		@Override
		public void run() {
			//下载地址http://192.168.1.107:8081/mp3/a1.mp3
			//根据MP3文件的名字，生成下载地址
			String mp3Url = AppConstant.URL.BASE_URL + mp3Info.getMp3Name();
			String lrcUrl = AppConstant.URL.BASE_URL + mp3Info.getLrcName();
			//生成下载文件所用的对象
			HttpDownloader httpDownloader = new HttpDownloader();
			//将文件下载下来，并存储到SDCard当中
			int mp3Result = httpDownloader.downFile(mp3Url, "mp3/", mp3Info.getMp3Name());
			int lrcResult = httpDownloader.downFile(lrcUrl, "mp3/", mp3Info.getLrcName());
		}
		//使用Notification提示客户下载结果
	}
	
	
}
