package cn.wuenqiang.app;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
/**
 * 
 * @author Administrator
 * ��Զ�̸����б�ͱ��ظ����б��Activity��֯��һ��
 */
public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		
		//�õ�TabHost�������TabActivity�Ĳ���ͨ����������������
		TabHost tabHost = this.getTabHost();
		//����һ��Intent���󣬸ö���ָ��Mp3ListActivity������һ��ҳ�����ɶ��Activity��ɵģ�������Ҫ��Intent��������һЩActivity��
		Intent remoteIntent = new Intent();
		remoteIntent.setClass(this, RmoteActivity.class);
		//����һ��TabSpec����������������һ��ѡ��ҳ��
		TabHost.TabSpec remoteSpec = tabHost.newTabSpec("Remote");
		//���ø�ѡ��ҳ���ͼ��ͱ��⣬ͨ��ϵͳ�õ��Դ�ͼƬ
		Resources res = this.getResources();
		remoteSpec.setIndicator("Remote",res.getDrawable(android.R.drawable.stat_sys_download));
		//���ø�ѡ��ҳ�������
		remoteSpec.setContent(remoteIntent);
		//�����úõ�TabSpec��ӵ�TabHost
		tabHost.addTab(remoteSpec);
		
		Intent localIntent = new Intent();
		localIntent.setClass(this, LocalActivity.class);
		//����һ��TabSpec����������������һ��ѡ��ҳ��
		TabHost.TabSpec localSpec = tabHost.newTabSpec("Local");
		localSpec.setIndicator("Local",res.getDrawable(android.R.drawable.stat_sys_upload));
		//���ø�ѡ��ҳ�������
		localSpec.setContent(localIntent);
		//�����úõ�TabSpec��ӵ�TabHost
		tabHost.addTab(localSpec);
		
	}

}
