package com.sdc.ditiezu.util;

import android.util.Log;

import com.sdc.ditiezu.entry.SubwayListEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * CreateTime: 2018/8/11 14:43
 * Author: ShengDecheng
 */

public class JsoupUtil {

    public static List<SubwayListEntry> getSubwayList(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").timeout(3000).get();

//            doc = Jsoup.connect("http://example.com")
//                    .data("query", "Java")
//                    .userAgent("Mozilla")
//                    .cookie("auth", "token")
//                    .timeout(3000)
//                    .post();

            String title = doc.title();
            Log.e("@@@@", "initData: " + title );
            Elements trElements = doc.select("div#category_2 tr");
            Log.e("@@@", "tr.size: " + trElements.size());
            Log.e("@@@", "***************************************************");
            List<SubwayListEntry> subwayListEntrys = new ArrayList<>();
            for (Element tr : trElements) {
                SubwayListEntry subwayListEntry = new SubwayListEntry();
                //Log.e("@@@", "tr.text: " + tr.text());
                Element img = tr.selectFirst("td.fl_icn img");
                if (img != null){
                    Log.e("@@@", "img: " + img.absUrl("src"));
                    subwayListEntry.setSubway_icon(img.absUrl("src"));
                }
                if (tr.select("td") != null && tr.select("td").size() > 1) {
                    Element td = tr.select("td").get(1);
                    if (td != null) {
                        Element name = td.select("h2 > a").first();
                        if (name != null) {
                            String result = name.text();
                            Element post = td.select("h2 > em").first();
                            if (post != null){
                                result += post.text();
                                subwayListEntry.setToday_post(post.text());
                            }
                            Log.e("@@@", "name: " + name.absUrl("href") + ", " + result);
                            subwayListEntry.setSubway_name(result);
                            subwayListEntry.setSubway_url(name.absUrl("href"));
                        } else {
                            continue;
                        }

                        Element desc = td.select("p.xg2").first();
                        if (desc != null) {
                            Log.e("@@@", "desc: " + desc.text());
                            subwayListEntry.setSubway_desc(desc.text());
                        }

                        Elements moderators = td.select("span.xi2 a");
                        if (moderators != null){
                            List<SubwayListEntry.Moderator> moderatorList = new ArrayList<>();
                            for (Element element : moderators) {
                                if (element != null){
                                    Log.e("@@@", "moderator: " + element.absUrl("href") + ", " + element.text());
                                    SubwayListEntry.Moderator moderator = new SubwayListEntry.Moderator();
                                    moderator.setName(element.text());
                                    moderator.setUrl(element.absUrl("href"));
                                    moderatorList.add(moderator);
                                }
                            }
                            subwayListEntry.setModerators(moderatorList);
                        }
                    }
                }

                if (tr.select("td.fl_i") != null) {
                    Element post1 = tr.select("td.fl_i span.xi2").first();
                    Element post2 = tr.select("td.fl_i span.xg1").first();
                    if (post1 != null && post2 != null) {
                        Log.e("@@@", "post: " + post1.text() + " " + post2.text());
                        subwayListEntry.setPost_count(post1.text() + " " + post2.text());
                    }
                }

                Element news = tr.select("td.fl_by").first();
                if (news != null) {
                    Element newPost = news.select("div > a").first();
                    Element newTime = news.select("div > cite").first();
                    if (newPost != null && newTime != null) {
                        Log.e("@@@", "news: " + newPost.text() + " " + newTime.text());
                        subwayListEntry.setLast_post(newPost.text());
                        subwayListEntry.setLast_time(newTime.text());
                    }
                }
                subwayListEntrys.add(subwayListEntry);
            }

            return subwayListEntrys;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getPostList(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").timeout(3000).get();

//            doc = Jsoup.connect("http://example.com")
//                    .data("query", "Java")
//                    .userAgent("Mozilla")
//                    .cookie("auth", "token")
//                    .timeout(3000)
//                    .post();

            String title = doc.title();
            Log.e("@@@@", "initData: " + title );
            Elements tbodyElements = doc.select("tbody");
            Log.e("@@@", "tbody.size: " + tbodyElements.size());
            Log.e("@@@", "***************************************************");
            List<SubwayListEntry> subwayListEntrys = new ArrayList<>();
            for (Element tr : tbodyElements) {
                SubwayListEntry subwayListEntry = new SubwayListEntry();
                //Log.e("@@@", "tr.text: " + tr.text());
                Element postTitle = tr.selectFirst("tr > th");
                if (postTitle != null){
                    Log.e("@@@", "postTitle: " + postTitle.text());
                    //subwayListEntry.setSubway_icon(img.absUrl("src"));
                }

                Element postIcon = tr.selectFirst("td.icn");
                if (postIcon != null){
                    Log.e("@@@", "postIcon: " + postIcon.text());
                    //subwayListEntry.setSubway_icon(img.absUrl("src"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
