package com.ivpomazkov.simplerss;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private List<String> mURLs;
    private static final String TAG = "GETTER:";

    public void getNews(){
        List<NewsItem> newsItems = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                NewsList newsList = NewsList.get(getApplicationContext());
                List<String> activeURLs = RSSChannelList.get(getApplicationContext()).getActiveURLs();
                Log.d("info", TAG + " active urls received, " + activeURLs.size());
                String rssString = "";
                for (String rssUrl : activeURLs) {
                    rssString = "";
                    Log.d("info", TAG + " creating request to " + rssUrl);
                    OkHttpClient client = new OkHttpClient();
                    try {
                    Request request = new Request.Builder()
                            .url(rssUrl)
                            .build();
                        Log.d("info", TAG + " executing request to " + rssUrl);
                        Response response = client.newCall(request).execute();
                        rssString = response.body().string();
                        Log.d("info", TAG + " received rss string: " + rssString);
                    } catch (IOException e) {
                        Log.d("info", TAG +  e.toString());
                    } catch (IllegalArgumentException e){
                        Log.d("info", TAG +  e.toString());
                    }
                    List<NewsItem> itemList = new ArrayList<>();
                    try {
                        //rssString = "<rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:media=\"http://search.yahoo.com/mrss/\"><channel><title>TUT.BY: Новости ТУТ - Главные новости</title><link>http://news.tut.by/</link><description>TUT.BY: Новости ТУТ - Главные новости</description><language>ru</language><image><url>http://img.tyt.by/i/rss/news/logo.gif</url><title>TUT.BY: Новости ТУТ - Главные новости</title><link>http://news.tut.by/</link></image><pubDate>Tue, 13 Jun 2016 09:44:13 +0300</pubDate><lastBuildDate>Tue, 14 Jun 2016 09:05:29 +0300</lastBuildDate><ttl>5</ttl><atom:link href=\"http://news.tut.by/rss/index.rss\" rel=\"self\" type=\"application/rss+xml\" /><item><title>новая Рекордное по количеству записавшихся: абитуриенты сдают ЦТ по русскому языку</title><link>http://news.tut.by/society/500368.html?utm_campaign=news-feed&#x26;utm_medium=rss&#x26;utm_source=rss-news</link><description>&#x3C;img src=\"http://img.tyt.by/thumbnails/n/brushko/0b/8/centralzovannoe_testirovanie_14062015_tutby_brush_phsl_img_01.jpg\" width=\"72\" height=\"48\" alt=\"Фото: Дмитрий Брушко, TUT.BY\" border=\"0\" align=\"left\" hspace=\"5\" /&#x3E;Директор Республиканского института контроля знаний Министерства образования Николай Феськов напомнил о том, что использовать в ходе испытаний устройства связи категорически запрещено. \"На первом испытании, которое прошло 13 июня (по белорусскому языку), не было нарушений, надеюсь, и сегодня абитуриенты будут сознательны и внимательны\".&#x3C;br clear=\"all\" /&#x3E;</description><atom:author><atom:name>TUT.BY</atom:name><atom:uri>http://news.tut.by/author/490~613.html</atom:uri></atom:author><category domain=\"http://news.tut.by/society/\">Общество</category><enclosure url=\"http://img.tyt.by/n/brushko/0b/8/centralzovannoe_testirovanie_14062015_tutby_brush_phsl_img_01.jpg\" type=\"image/jpeg\" length=\"344798\" /><guid isPermaLink=\"true\">http://news.tut.by/society/500368.html?utm_campaign=news-feed&#x26;utm_medium=rss&#x26;utm_source=rss-news</guid><pubDate>Tue, 14 Jun 2016 08:26:00 +0300</pubDate><media:content url=\"http://img.tyt.by/n/brushko/0b/8/centralzovannoe_testirovanie_14062015_tutby_brush_phsl_img_01.jpg\" type=\"image/jpeg\" medium=\"image\" height=\"800\" width=\"1200\" fileSize=\"344798\" /></item><item><title>Белорусские баскетболистки с победы начали отборочный турнир к Олимпиаде-2016</title><link>http://sport.tut.by/news/basketball/500340.html?utm_campaign=news-feed&#x26;utm_medium=rss&#x26;utm_source=rss-news</link><description>&#x3C;img src=\"http://img.tyt.by/thumbnails/n/sport/0d/a/02_basketbol_belarus_zam_tutby_phsl_24022016.jpg\" width=\"72\" height=\"48\" alt=\"Фото: Вадим Замировский, TUT.BY\" border=\"0\" align=\"left\" hspace=\"5\" /&#x3E;Белорусские баскетболистки с непростой победы над бронзовым призером чемпионата Африки начали отборочный турнир к Олимпиаде-2016.&#x3C;br clear=\"all\" /&#x3E;</description><atom:author><atom:name>SPORT.TUT.BY</atom:name><atom:uri>http://sport.tut.by</atom:uri></atom:author><category domain=\"http://sport.tut.by/news/basketball/\">Баскетбол</category><enclosure url=\"http://img.tyt.by/n/sport/0d/a/02_basketbol_belarus_zam_tutby_phsl_24022016.jpg\" type=\"image/jpeg\" length=\"968599\" /><guid isPermaLink=\"true\">http://sport.tut.by/news/basketball/500340.html?utm_campaign=news-feed&#x26;utm_medium=rss&#x26;utm_source=rss-news</guid><pubDate>Mon, 13 Jun 2016 20:47:00 +0300</pubDate><media:content url=\"http://img.tyt.by/n/sport/0d/a/02_basketbol_belarus_zam_tutby_phsl_24022016.jpg\" type=\"image/jpeg\" medium=\"image\" height=\"800\" width=\"1200\" fileSize=\"968599\" /></item></channel></rss>";
                        itemList = parseRSSString(rssString);
                    } catch (ParserConfigurationException e){
                        Log.d("info", TAG + " parser exception" + e);
                    }  catch (IOException e){
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                    Log.d("info", TAG + " found some news = " + itemList.size());
                    newsList.addNews(itemList, false);
                }
                Intent intent = new Intent(RSSActivity.UPDATE_ACTION);
                intent.putExtra(RSSGetter.READY_TO_UPDATE,true);
                Log.d("info", TAG + " intent UPDATE_ACTION sent");
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
                String pubDate = curElement.getElementsByTagName("pubDate").item(0).getTextContent();
                String imageUrl = extractImageUrl(description);
                String itemUrl = curElement.getElementsByTagName("link").item(0).getTextContent();
                NewsItem item = new NewsItem();
                item.setTitle(title);
                item.setDescription(description);
                item.setImageLink(imageUrl);
                item.setLink(itemUrl);
                DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                try {
                    item.setPubDate(formatter.parse(pubDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                nList.add(item);
            }
       }

       return nList;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("info", TAG + " ->onBind()");
        return null;
    }

    @Override
    public void onCreate(){
        Log.d("info", TAG + " ->onCreate()");
        mURLs = RSSChannelList.get(getApplicationContext()).getActiveURLs();
    }

    @Override
    public void onDestroy(){
        Log.d("info", TAG + " ->onDestroy()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("info", TAG + " ->onStartCommand()");
        getNews();
        return super.onStartCommand(intent, flags, startId);
    }

    private String extractImageUrl(String description){
        org.jsoup.nodes.Document document = Jsoup.parse(description);
        Elements imgs = document.select("img");

        for (org.jsoup.nodes.Element img : imgs) {
            if (img.hasAttr("src")) return img.attr("src");
        }

        return "";
    }

}
