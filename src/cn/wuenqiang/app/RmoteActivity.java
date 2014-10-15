package cn.wuenqiang.app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import cn.wuenqiang.app.service.DownloadService;
import cn.wuenqiang.downoad.HttpDownloader;
import cn.wuenqiang.model.Mp3Info;
import cn.wuenqiang.xml.Mp3ListContentHandler;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
/**
 * 
 * @author Administrator
 * 列表的Activity需要继承ListActivity
 * 这个Activity是显示远程歌曲列表的
 */
public class RmoteActivity extends ListActivity {

	//定义两个常量来表示Menu中的Item的ID
	private static final int UPDATE = 1;
	private static final int ABOUT = 2;
	private List<Mp3Info> mp3Infos = null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_mp3_list);
        updateListView();
    }

    //在点击Menu按钮之后，会调用该方法，我们可以再这个方法当中加入自己的按钮控件
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, UPDATE, 1, R.string.mp3list_update);
		menu.add(0, ABOUT, 2, R.string.mp3list_about);
		return super.onCreateOptionsMenu(menu);
	}

	//当点击了菜单的选项的时候会触发此函数，具体哪个按钮被点击会被封装在MenuItem当中
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == UPDATE){
			//用户单击了更新列表按钮
			updateListView();
		}else if(item.getItemId() == ABOUT){
			//用户单击了关于按钮
			
		}
		return super.onOptionsItemSelected(item);
	}
    
	//下载XML文件
	private String downloadXML(String urlstr){
		HttpDownloader httpDownloader = new HttpDownloader();
		String result = httpDownloader.download(urlstr);
		return result;
	}
	
	//解析xml文件
	private List<Mp3Info> parse(String xmlStr) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<Mp3Info> infos = new ArrayList<Mp3Info>();
		try {
			XMLReader xmlReader = saxParserFactory.newSAXParser()
					.getXMLReader();
			Mp3ListContentHandler mp3ListContentHandler = new Mp3ListContentHandler(
					infos);
			xmlReader.setContentHandler(mp3ListContentHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			for (Iterator iterator = infos.iterator(); iterator.hasNext();) {
				Mp3Info mp3Info = (Mp3Info) iterator.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}
	
	//更新歌曲列表
	private void updateListView(){
		String xml = downloadXML("http://192.168.1.110:8080/Mp3Download/SongList/resources.xml");
		//对XML文件进行解析，并将结果放置到Mp3Info，最后将所有的Mp3Info加入到list当中
		mp3Infos = parse(xml);
		//将SimpleAdapter设置到ListActivity当中
		this.setListAdapter(buildSimpleAdapter(mp3Infos));
	}
	
	//通过List<Mp3Info>构建一个SimpleAdapter
	private SimpleAdapter buildSimpleAdapter(List<Mp3Info> mp3Infos){
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		for(Iterator iterator = mp3Infos.iterator();iterator.hasNext();){
			Mp3Info mp3Info = (Mp3Info)iterator.next();
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("mp3_name", mp3Info.getMp3Name());
			map.put("mp3_size", mp3Info.getMp3Size());
			list.add(map);
		}
		//利用list创建SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,
				R.layout.mp3info_item, new String[] { "mp3_name", "mp3_size" },
				new int[]{ R.id.mp3_name, R.id.mp3_size });
		return simpleAdapter;
	}

	//处理用户点击歌曲列表具体某一条目的下载操作
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// 根据用户点击列表当中的位置来得到响应的Mp3Info对象
		Mp3Info mp3Info = mp3Infos.get(position);
		//System.out.println("------->"+mp3Info);
		// 生成Intent对象
		Intent intent = new Intent();
		// 将Mp3Info对象存入到Intent对象当中
		intent.putExtra("mp3Info", mp3Info);
		intent.setClass(this, DownloadService.class);
		// 启动Service
		startService(intent);
		super.onListItemClick(l, v, position, id);
	}
    
    
}