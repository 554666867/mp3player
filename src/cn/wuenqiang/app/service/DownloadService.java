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

	//Service����ʱ���ã�ÿ�ε��ListActivity���е�һ����Ŀ��ʱ��
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//��Intent�ӽ�mp3Infoȡ��
		Mp3Info mp3Info = (Mp3Info)intent.getSerializableExtra("mp3Info") ;
		//����һ�������߳�
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

	//�ڲ��࣬��������mp3Info�������
	class DownloadThread implements Runnable{
		private Mp3Info mp3Info;
		public DownloadThread(Mp3Info mp3Info){
			this.mp3Info = mp3Info;
		}
		@Override
		public void run() {
			//���ص�ַhttp://192.168.1.107:8081/mp3/a1.mp3
			//����MP3�ļ������֣��������ص�ַ
			String mp3Url = AppConstant.URL.BASE_URL + mp3Info.getMp3Name();
			String lrcUrl = AppConstant.URL.BASE_URL + mp3Info.getLrcName();
			//���������ļ����õĶ���
			HttpDownloader httpDownloader = new HttpDownloader();
			//���ļ��������������洢��SDCard����
			int mp3Result = httpDownloader.downFile(mp3Url, "mp3/", mp3Info.getMp3Name());
			int lrcResult = httpDownloader.downFile(lrcUrl, "mp3/", mp3Info.getLrcName());
		}
		//ʹ��Notification��ʾ�ͻ����ؽ��
	}
	
	
}
