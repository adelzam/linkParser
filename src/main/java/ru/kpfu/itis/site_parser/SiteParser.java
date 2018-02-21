package ru.kpfu.itis.site_parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;


public class SiteParser {

    private int[][] mapLinked;

    public int[][] createMagic(String site, int n) {
        HashMap<String, Integer> map = createMapping(getAHref(site),n);
        map.forEach((k,v)-> {
            LinkedList<String> list= getAHref(k);
            updateLink(list,map,v);
        });
        getList(map);
        return mapLinked;
    }
    private LinkedList<String> getAHref(String site) {
        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add(0,site);
        try {
            Document doc = Jsoup.connect(site).get();
            Element mBody = doc.body();
            Elements urls = mBody.getElementsByTag("a");
            for (Element url : urls) {
                String str = url.attr("href");
                if (!str.contains("http")) str = site + str;
                linkedList.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linkedList;
    }
    private HashMap<String, Integer> createMapping(LinkedList<String> list, int n) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        mapLinked = new int[n][n];
        for (int i = 0; i <n ; i++) {
            for (int j = 0; j <n ; j++) {
                mapLinked[i][j] = 0;
            }
        }
        int k=0;
        int i=0;
        while (k<n) {
            if (!map.containsKey(list.get(i))) {
                map.put(list.get(i),k);
                k++;
            }
            i++;
        }
        return map;
    }
    private void updateLink(LinkedList<String> list, HashMap<String, Integer> map, int n) {
        HashMap<String,String> map2 = new HashMap<>();
        for (String s : list) {
            map2.put(s,s);
        }
        map.forEach((k,v)-> {
            if (map2.get(k)!=null) {
                mapLinked[n][v] = 1;
            }
        });
    }
    private HashMap<Integer, String> getList(HashMap<String, Integer> map) {
        HashMap<Integer,String> map2 = new HashMap<>();
        map.forEach((k,v)->{
            map2.put(v,k);
        });
        return map2;
    }
}
