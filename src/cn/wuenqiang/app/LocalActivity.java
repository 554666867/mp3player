package cn.wuenqiang.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.wuenqiang.model.Mp3Info;
import cn.wuenqiang.utils.FileUtils;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
/**
 * 
 * @author Administrator
 * 这个Activity是用于显示本地的歌曲列表的
 */
public class LocalActivity extends ListActivity {
	
	private List<Mp3Info> mp3Infos = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.local_mp3_list);

	}

	@Override
	protected void onResume() {		
		FileUtils fileUtils = new FileUtils();
		mp3Infos = fileUtils.getMp3Files("mp3/");
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		for(Iterator iterator = mp3Infos.iterator();iterator.hasNext();){
			Mp3Info mp3Info = (Mp3Info)iterator.next();
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("mp3_name", mp3Info.getMp3Name());
			map.put("mp3_size", mp3Info.getMp3Size());
			list.add(map);
		}
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,
				R.layout.mp3info_item, new String[] { "mp3_name", "mp3_size" },
				new int[]{ R.id.mp3_name, R.id.mp3_size });
		this.setListAdapter(simpleAdapter);
		
		super.onResume();
	}
	


	//用户点击本地歌曲列表的某个选项的时候执行
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if(mp3Infos != null){
			Mp3Info mp3Info = mp3Infos.get(position);
			Intent intent = new Intent();
			intent.putExtra("mp3Info", mp3Info);
			intent.setClass(this, PlayerActivity.class);
			startActivity(intent);
		}
	}
	
}
