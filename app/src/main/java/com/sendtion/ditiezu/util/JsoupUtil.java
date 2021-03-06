package com.sendtion.ditiezu.util;


import com.sendtion.ditiezu.entry.PostListEntry;
import com.sendtion.ditiezu.entry.SubwayListEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 解析都市地铁
 * CreateTime: 2018/8/11 14:43
 * Author: ShengDecheng
 */

public class JsoupUtil {

    /**
     * 解析地铁列表
     */
    public static List<SubwayListEntry> getSubwayList(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").timeout(6000).get();

//            doc = Jsoup.connect("http://example.com")
//                    .data("query", "Java")
//                    .userAgent("Mozilla")
//                    .cookie("auth", "token")
//                    .timeout(3000)
//                    .post();

            String title = doc.title();
            //Log.e("@@@@", "initData: " + title );
            Elements trElements = doc.select("div#category_2 tr");
            //Log.e("@@@", "tr.size: " + trElements.size());
            //Log.e("@@@", "***************************************************");
            List<SubwayListEntry> subwayListEntrys = new ArrayList<>();
            for (Element tr : trElements) {
                SubwayListEntry subwayListEntry = new SubwayListEntry();
                //Log.e("@@@", "tr.text: " + tr.text());
                Element img = tr.selectFirst("td.fl_icn img");
                if (img != null){
                    //Log.e("@@@", "img: " + img.absUrl("src"));
                    subwayListEntry.setSubway_icon(img.absUrl("src"));
                } else {
                    continue;
                }
                if (tr.select("td") != null && tr.select("td").size() > 1) {
                    Element td = tr.select("td").get(1);
                    if (td != null) {
                        Element name = td.select("h2 > a").first();
                        if (name != null) {
                            String result = name.text();
                            Element post = td.select("h2 > em").first();
                            if (post != null){
                                //result += post.text();
                                subwayListEntry.setToday_post(post.text());
                            }
                            //Log.e("@@@", "name: " + name.absUrl("href") + ", " + result);
                            subwayListEntry.setSubway_name(result);
                            subwayListEntry.setSubway_url(name.absUrl("href"));
                        } else {
                            continue;
                        }

                        Element desc = td.select("p.xg2").first();
                        if (desc != null) {
                            //Log.e("@@@", "desc: " + desc.text());
                            subwayListEntry.setSubway_desc(desc.text());
                        }

                        Elements moderators = td.select("span.xi2 a");
                        if (moderators != null){
                            List<SubwayListEntry.Moderator> moderatorList = new ArrayList<>();
                            for (Element element : moderators) {
                                if (element != null){
                                    //Log.e("@@@", "moderator: " + element.absUrl("href") + ", " + element.text());
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
                        //Log.e("@@@", "post: " + post1.text() + " " + post2.text());
                        subwayListEntry.setPost_count(post1.text() + " " + post2.text());
                    }
                }

                Element news = tr.select("td.fl_by").first();
                if (news != null) {
                    Element newPost = news.select("div > a").first();
                    if (newPost != null){
                        subwayListEntry.setLast_post(newPost.text()); //最后发布的帖子
                    }
                    Element newOper = news.select("div > cite").first();
                    if (newOper != null) {
                        Element lastTime = newOper.selectFirst("span");
                        if (lastTime != null){
                            subwayListEntry.setLast_time(lastTime.text()); //最后发布时间
                            subwayListEntry.setLast_time2(lastTime.attr("title"));
                            //Log.e("@@@", "lastTime: " + lastTime.text());
                        }
                        Element lastUser = newOper.selectFirst("a");
                        if (lastUser != null){
                            subwayListEntry.setLast_user(lastUser.text()); //最后发布人
                            //Log.e("@@@", "lastUser: " + lastUser.text());
                        }
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

    /**
     * 解析帖子列表
     */
    public static List<PostListEntry> getPostList(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").timeout(6000).get();

//            doc = Jsoup.connect("http://example.com")
//                    .data("query", "Java")
//                    .userAgent("Mozilla")
//                    .cookie("auth", "token")
//                    .timeout(3000)
//                    .post();

            String title = doc.title();
            //Log.e("@@@@", "initData: " + title );
            Elements tbodyElements = doc.select("tbody");
            //Log.e("@@@", "tbody.size: " + tbodyElements.size());
            //Log.e("@@@", "***************************************************");
            List<PostListEntry> postListEntries = new ArrayList<>();
            for (Element tr : tbodyElements) {
                PostListEntry postListEntry = new PostListEntry();
                //Log.e("@@@", "tr.text: " + tr.text());
                Element postTh = tr.selectFirst("tr > th");
                if (postTh != null){
                    Element postLine = postTh.selectFirst("em a"); //线路
                    Element postImg = postTh.selectFirst("img"); //图片附件
                    Element postNew = postTh.selectFirst("a.xi1"); //是否新帖
                    Element postTitle = postTh.selectFirst("a.xst"); //标题
                    if (postTitle != null){
                        //Log.e("@@@", "postTitle: " + postTitle.text() + ", " + postTitle.absUrl("href"));
                        postListEntry.setPost_title(postTitle.text());
                        postListEntry.setPost_url(postTitle.absUrl("href"));
                        if (postLine != null) {
                            postListEntry.setPost_line(postLine.text());
                        }
                        if (postImg != null){
                            postListEntry.setIs_has_img(postImg.absUrl("src"));
                        }
                        if (postNew != null){
                            postListEntry.setIs_new_post(postNew.text());
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }

                Element postIcon = tr.selectFirst("td.icn img");
                if (postIcon != null){
                    //Log.e("@@@", "postIcon: " + postIcon.absUrl("src"));
                    postListEntry.setPost_icon(postIcon.absUrl("src"));
                }

                Element postCreator = tr.selectFirst("td.by");
                if (postCreator != null){
                    //Log.e("@@@", "postCreator: " + postCreator.text());
                    postListEntry.setPost_creator(postCreator.text());
                }

                Element postNum = tr.selectFirst("td.num");
                if (postNum != null){
                    Element postReply = postNum.selectFirst("a");
                    Element postRead = postNum.selectFirst("em");
                    if (postReply != null && postRead != null){
                        //Log.e("@@@", "postReply: " + postReply.text() + " / " + postRead.text());
                        postListEntry.setReply_count(postReply.text() + " / " + postRead.text());
                    }
                }

                Element postLast = tr.selectFirst("td.kmhf");
                if (postLast != null){
                    //Log.e("@@@", "postLast: " + postLast.text());
                    //postListEntry.setLast_time(postLast.text());
                    Element lastUser = postLast.selectFirst("cite > a");
                    if (lastUser != null){
                        postListEntry.setLast_user(lastUser.text());
                        //Log.e("@@@", "lastUser: " + lastUser.text());
                    }
                    Element lastTime = postLast.selectFirst("em span");
                    if (lastTime != null){
                        postListEntry.setLast_time(lastTime.text());
                        postListEntry.setLast_time2(lastTime.attr("title"));
                        //Log.e("@@@", "lastTime: " + lastTime.text());
                        //Log.e("@@@", "lastTime2: " + lastTime.attr("title"));
                    } else {
                        lastTime = postLast.selectFirst("em > a");
                        if (lastTime != null){
                            postListEntry.setLast_time(lastTime.text());
                            postListEntry.setLast_time2(lastTime.text());
                        }
                    }
                }
                postListEntries.add(postListEntry);
            }

            return postListEntries;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
