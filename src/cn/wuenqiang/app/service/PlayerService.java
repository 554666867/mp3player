package cn.wuenqiang.app.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Queue;

import cn.wuenqiang.app.AppConstant;
import cn.wuenqiang.lrc.LrcProcessor;
import cn.wuenqiang.model.Mp3Info;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;

public class PlayerService extends Service {

	private boolean isPlaying = false;
	private boolean isPause = false;
	private boolean isReleased = false;
	private MediaPlayer mediaPlayer = null;
	private Handler handler = new Handler();
	private UpdateTimeCallback updateTimeCallback = null;
	private long begin = 0;
	private long nextTimeMill = 0;
	private long currentTimeMill = 0;
	private String message = null;
	private long pauseTimeMills = 0;
	private ArrayList<Queue> queues = null;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	//Activityÿ�η���һ��Intent��ʱ�򶼻�ִ��һ�Σ�Ȼ����ݲ�ͬMSG��ִ�в�ͬ�ķ���
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Mp3Info mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
		int MSG = intent.getIntExtra("MSG", 0);
		if (mp3Info != null) {
			if(MSG == AppConstant.PlayerMsg.PLAY_MSG){
				play(mp3Info);
			}
		} else {
			if(MSG == AppConstant.PlayerMsg.PAUSE_MSG){
				pause();
			}
			else if(MSG == AppConstant.PlayerMsg.STOP_MSG){
				stop();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void play(Mp3Info mp3Info) {
            if(!isPlaying){
			String path = getMp3Path(mp3Info);
			mediaPlayer = MediaPlayer.create(this, Uri.parse("file://" + path));
			mediaPlayer.setLooping(false);
			mediaPlayer.start();
			prepareLrc(mp3Info.getLrcName());
			handler.postDelayed(updateTimeCallback,5);
			begin=System.currentTimeMillis();
			isPlaying = true;
			isReleased = false;
            }
	}

	private void pause() {
		if(isPlaying){
			mediaPlayer.pause();
			handler.removeCallbacks(updateTimeCallback);
			pauseTimeMills=System.currentTimeMillis();
		}else {
			mediaPlayer.start();
			handler.postDelayed(updateTimeCallback,5);
			begin=System.currentTimeMillis()-pauseTimeMills+begin;
		}
		isPlaying=isPlaying?false:true;
	}

	private void stop() {
		if (mediaPlayer != null) {
			if (isPlaying) {
				if (!isReleased) {
					handler.removeCallbacks(updateTimeCallback);
					mediaPlayer.stop();
					mediaPlayer.release();
					isReleased = true;
				}
				isPlaying = false;
			}
		}
	}

	private String getMp3Path(Mp3Info mp3Info) {
		String SDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		String path = SDCardRoot + File.separator + "mp3" + File.separator
				+ mp3Info.getMp3Name();
		return path;
	}
	
	//���ݸ���ļ������֣�����ȡ����ļ����е���Ϣ
	private void prepareLrc(String lrcName){
		try {
			InputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsoluteFile() +File.separator + "mp3/" + lrcName);
			LrcProcessor lrcProcessor = new LrcProcessor();
			queues = lrcProcessor.process(inputStream);
			//����һ��UpdateTimeCallback����
			updateTimeCallback = new UpdateTimeCallback(queues);
			begin = 0 ;
			currentTimeMill = 0 ;
			nextTimeMill = 0 ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	class UpdateTimeCallback implements Runnable{
		ArrayList<Queue>queues=null;
	
		public UpdateTimeCallback(ArrayList<Queue> queues) {
			//��ArrayList����ȡ����Ӧ�Ķ������
			this.queues=queues;
	
		}
		@Override
		public void run() {
			Queue times = queues.get(0);
			Queue messages = queues.get(1);
			//����ƫ������Ҳ����˵�ӿ�ʼ����MP3������Ϊֹ���������˶���ʱ�䣬�Ժ���Ϊ��λ
			long offset = System.currentTimeMillis() - begin;
			if(currentTimeMill == 0){
				nextTimeMill = (Long)times.poll();
				message = (String)messages.poll();
			}
			if(offset >= nextTimeMill){
				Intent intent=new Intent();
				intent.setAction(AppConstant.LRC_MESSAGE_ACTION);
				intent.putExtra("lrcMessage",message);
				String offset1 = String.valueOf(offset);
				intent.putExtra("offset1", offset1);
				sendBroadcast(intent);
				nextTimeMill = (Long)times.poll();
				message = (String)messages.poll();
			}
			currentTimeMill = currentTimeMill + 10;
			handler.postDelayed(updateTimeCallback, 10);
		}
		
	}
}
