package cn.wuenqiang.xml;

import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import cn.wuenqiang.model.Mp3Info;

/**
 * 
 * @author Administrator
 * 用于解析xml文件（歌曲列表）
 */
public class Mp3ListContentHandler extends DefaultHandler {
	private List<Mp3Info> infos = null;
	private Mp3Info mp3Info = null;
	private String tagName = null;

	public Mp3ListContentHandler(List<Mp3Info> infos) {
		super();
		this.infos = infos;
	}

	public List<Mp3Info> getInfos() {
		return infos;
	}

	public void setInfos(List<Mp3Info> infos) {
		this.infos = infos;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String temp = new String(ch, start, length);
		if (tagName.equals("id")) {
			mp3Info.setId(temp);
		} else if (tagName.equals("mp3.name")) {
			mp3Info.setMp3Name(temp);
		} else if (tagName.equals("mp3.size")) {
			mp3Info.setMp3Size(temp);
		} else if (tagName.equals("lrc.name")) {
			mp3Info.setLrcName(temp);
		} else if (tagName.equals("lrc.size")) {
			mp3Info.setLrcSize(temp);
		}
	}

	@Override
	public void endDocument() throws SAXException {
	}

	//读取完一个resource表示已经读取完一首歌曲的信息，就将这首歌加入到歌曲列表infos中
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("resource")) {
			infos.add(mp3Info);
		}
		tagName = "";

	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.tagName = localName;
		if (tagName.equals("resource")) {
			mp3Info = new Mp3Info();
		}
	}

}
