package cn.wuenqiang.app;

public interface AppConstant {
	public class PlayerMsg{
		public static final int PLAY_MSG = 1;
		public static final int PAUSE_MSG = 2;
		public static final int STOP_MSG = 3;
	}
	public class URL{
		public static final String BASE_URL = "http://192.168.1.110:8080/Mp3Download/Music/";
	}
	public static final String LRC_MESSAGE_ACTION="cn.wuenqiang.mp3player.lrcmessage.action";
}
