package com.ivpomazkov.simplerss;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ivpomazkov on 03.06.2016.
 */
public class RSSGetter extends Service {
    public static final String READY_TO_UPDATE = "update";
    List<String> mURLs;


    public void getNews(){
        List<NewsItem> newsItems = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                NewsList newsList = NewsList.get(getApplicationContext());
                List<String> activeURLs = RSSChannelList.get(getApplicationContext()).getActiveURLs();
                Log.d("info", "active urls received, " + activeURLs.size());
                String rssString = "";
                for (String rssUrl : activeURLs) {
                    //!!get rssString from web
                    rssString = "";
                    Log.d("info", "creating request to " + rssUrl);
                    OkHttpClient client = new OkHttpClient();
                    try {
                    Request request = new Request.Builder()
                            .url(rssUrl)
                            .build();
                        Log.d("info", "executing request to " + rssUrl);
                        Response response = client.newCall(request).execute();
                        rssString = response.body().string();
                        Log.d("info","received rss string: " + rssString);
                    } catch (IOException e) {
                        Log.d("info", e.toString());
                    } catch (IllegalArgumentException e){
                        Log.d("info", e.toString());
                    }
                    List<NewsItem> itemList = new ArrayList<>();
                    try {
                        rssString = "<rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:media=\"http://search.yahoo.com/mrss/\"><channel><title>TUT.BY: Новости ТУТ - Главные новости</title><link>http://news.tut.by/</link><description>TUT.BY: Новости ТУТ - Главные новости</description><language>ru</language><image><url>http://img.tyt.by/i/rss/news/logo.gif</url><title>TUT.BY: Новости ТУТ - Главные новости</title><link>http://news.tut.by/</link></image><pubDate>Mon, 06 Jun 2016 17:28:50 +0300</pubDate><lastBuildDate>Mon, 06 Jun 2016 17:23:00 +0300</lastBuildDate><ttl>5</ttl><atom:link href=\"http://news.tut.by/rss/index.rss\" rel=\"self\" type=\"application/rss+xml\" /><item><title>Серьезное ЧП на \"ГродноАзоте\": два человека погибли, еще двое - в реанимации</title><link>http://news.tut.by/accidents/499345.html?utm_campaign=news-feed&#x26;utm_medium=rss&#x26;utm_source=rss-news</link><description>&#x3C;img src=\"http://img.tyt.by/thumbnails/n/regiony/0c/d/pozhar_azot_2016.jpg\" width=\"72\" height=\"43\" alt=\"Фото: vk.com/autogrodnoby\" border=\"0\" align=\"left\" hspace=\"5\" /&#x3E;Возбуждено уголовное дело по статье 428 УК Беларуси (служебная халатность).&#x3C;br clear=\"all\" /&#x3E;</description></item><item><title>Выборы в Палату представителей назначены на 11 сентября</title><link>http://news.tut.by/politics/499334.html?utm_campaign=news-feed&#x26;utm_medium=rss&#x26;utm_source=rss-news</link><description>&#x3C;img src=\"http://img.tyt.by/thumbnails/n/00/d/nac_sobranye_parlament.jpg\" width=\"72\" height=\"48\" alt=\"Фото: house.gov.by\" border=\"0\" align=\"left\" hspace=\"5\" /&#x3E;Александр Лукашенко 6 июня подписал указы о назначении выборов в обе палаты белорусского парламента - Палату представителей и Совет Республики.&#x3C;br clear=\"all\" /&#x3E;</description></item></channel></rss>";
                        itemList = parseRSSString(rssString);
                    } catch (ParserConfigurationException e){
                        Log.d("info", "parser exception" + e);
                    }  catch (IOException e){
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                    Log.d("info", "found some news = " + itemList.size());
                    newsList.addNews(itemList, false);
                }
                Intent intent = new Intent(RSSActivity.UPDATE_ACTION);
                intent.putExtra(RSSGetter.READY_TO_UPDATE,true);
                Log.d("info", "intent UPDATE_ACTION sent");
                sendBroadcast(intent);
            }
        }).start();

    }

   private List<NewsItem> parseRSSString(String rssString) throws ParserConfigurationException, IOException, SAXException {
       List<NewsItem> nList = new ArrayList<>();
       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
       DocumentBuilder db = dbf.newDocumentBuilder();
       Document document = db.parse(new InputSource(new StringReader(rssString)));
       Element element = document.getDocumentElement();
       NodeList nodeList = element.getElementsByTagName("item");

       for (int i = 0; i < nodeList.getLength(); i++){

           Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element curElement = (Element) node;
                String title = curElement.getElementsByTagName("title").item(0).getTextContent();
                String description = curElement.getElementsByTagName("description").item(0).getTextContent();
                NewsItem item = new NewsItem();
                item.setTitle(title);
                item.setDescription(description);
                Log.d("info", title + " " + description);
                nList.add(item);
            }
       }

       return nList;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("info", "Service->onBind()");
        return null;
    }

    @Override
    public void onCreate(){
        Log.d("info", "Service->onCreate()");
        mURLs = RSSChannelList.get(getApplicationContext()).getActiveURLs();
    }

    @Override
    public void onDestroy(){
        Log.d("infoService", "Service->onDestroy()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("info", "Service->onStartCommand()");
        getNews();
        return super.onStartCommand(intent, flags, startId);
    }

}
