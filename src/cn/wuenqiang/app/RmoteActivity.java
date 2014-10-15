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
 * �б��Activity��Ҫ�̳�ListActivity
 * ���Activity����ʾԶ�̸����б��
 */
public class RmoteActivity extends ListActivity {

	//����������������ʾMenu�е�Item��ID
	private static final int UPDATE = 1;
	private static final int ABOUT = 2;
	private List<Mp3Info> mp3Infos = null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_mp3_list);
        updateListView();
    }

    //�ڵ��Menu��ť֮�󣬻���ø÷��������ǿ���������������м����Լ��İ�ť�ؼ�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, UPDATE, 1, R.string.mp3list_update);
		menu.add(0, ABOUT, 2, R.string.mp3list_about);
		return super.onCreateOptionsMenu(menu);
	}

	//������˲˵���ѡ���ʱ��ᴥ���˺����������ĸ���ť������ᱻ��װ��MenuItem����
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == UPDATE){
			//�û������˸����б�ť
			updateListView();
		}else if(item.getItemId() == ABOUT){
			//�û������˹��ڰ�ť
			
		}
		return super.onOptionsItemSelected(item);
	}
    
	//����XML�ļ�
	private String downloadXML(String urlstr){
		HttpDownloader httpDownloader = new HttpDownloader();
		String result = httpDownloader.download(urlstr);
		return result;
	}
	
	//����xml�ļ�
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
	
	//���¸����б�
	private void updateListView(){
		String xml = downloadXML("http://192.168.1.110:8080/Mp3Download/SongList/resources.xml");
		//��XML�ļ����н���������������õ�Mp3Info��������е�Mp3Info���뵽list����
		mp3Infos = parse(xml);
		//��SimpleAdapter���õ�ListActivity����
		this.setListAdapter(buildSimpleAdapter(mp3Infos));
	}
	
	//ͨ��List<Mp3Info>����һ��SimpleAdapter
	private SimpleAdapter buildSimpleAdapter(List<Mp3Info> mp3Infos){
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		for(Iterator iterator = mp3Infos.iterator();iterator.hasNext();){
			Mp3Info mp3Info = (Mp3Info)iterator.next();
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("mp3_name", mp3Info.getMp3Name());
			map.put("mp3_size", mp3Info.getMp3Size());
			list.add(map);
		}
		//����list����SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,
				R.layout.mp3info_item, new String[] { "mp3_name", "mp3_size" },
				new int[]{ R.id.mp3_name, R.id.mp3_size });
		return simpleAdapter;
	}

	//�����û���������б����ĳһ��Ŀ�����ز���
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// �����û�����б��е�λ�����õ���Ӧ��Mp3Info����
		Mp3Info mp3Info = mp3Infos.get(position);
		//System.out.println("------->"+mp3Info);
		// ����Intent����
		Intent intent = new Intent();
		// ��Mp3Info������뵽Intent������
		intent.putExtra("mp3Info", mp3Info);
		intent.setClass(this, DownloadService.class);
		// ����Service
		startService(intent);
		super.onListItemClick(l, v, position, id);
	}
    
    
}