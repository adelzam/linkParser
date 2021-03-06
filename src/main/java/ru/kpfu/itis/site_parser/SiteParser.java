package ru.kpfu.itis.site_parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;


public class SiteParser {

    //adjacency matrix of links
    private int[][] mapLinked;
    //map of links and their positions
    private HashMap<String, Integer> map;

    private LinkedList<String> linkList;

    public SiteParser(int n) {
        mapLinked = new int[n][n];
        map = new HashMap<>();
        linkList = new LinkedList<>();
    }

    //main method in class
    //crate map of links and search connections between different links in map
    public int[][] createMagic(String site, int n) {
        //create map
        map = createMapping(getAHref(site), n);
        //create connections
        map.forEach((k, v) -> {
            LinkedList<String> list = getAHref(k);
            updateLink(list, map, v);
        });
        //get legend for the adjacency matrix
        getList(map);
        return mapLinked;
    }

    //get all links in site page
    private LinkedList<String> getAHref(String site) {
        //list of links
        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add(0, site);
        try {
            //parse html page
            Document doc = Jsoup.connect(site).get();
            Element mBody = doc.body();
            //get all elements with tag 'a'
            Elements urls = mBody.getElementsByTag("a");
            //get all elements with attr 'href'
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

    //create matrix and map with size n with connections between different links in map
    private HashMap<String, Integer> createMapping(LinkedList<String> list, int n) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        //initialize adjacency matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mapLinked[i][j] = 0;
            }
        }
        int k = 0;
        int i = 0;
        //put to map different links from list
        while (k < n) {
            if ((!map.containsKey(list.get(i)))) {
                map.put(list.get(i), k);
                linkList.add(list.get(i));
                k++;
            }
            i++;
        }
        return map;
    }

    //update matrix. If between pages i and j of site have link system put 1 in mapLinked[i][j]
    private void updateLink(LinkedList<String> list, HashMap<String, Integer> map, int n) {
        HashMap<String, String> map2 = new HashMap<>();
        for (String s : list) {
            map2.put(s, s);
        }
        map.forEach((k, v) -> {
            if (map2.get(k) != null) {
                mapLinked[n][v] = 1;
            }
        });
    }

    //revers map to print legend for the adjacency matrix
    private HashMap<Integer, String> getList(HashMap<String, Integer> map) {
        HashMap<Integer, String> map2 = new HashMap<>();
        map.forEach((k, v) -> {
            map2.put(v, k);
        });
        return map2;
    }

    private double[] makePageRank(int[][] linkMatrix, double[] linkVector, int n) {
        double[] linkVectorBuff = new double[n];
        for (int i = 0; i < n; i++) {
            linkVectorBuff[i] += linkVector[i];
            for (int j = 0; j < n; j++) {
                int k = 0;
                for (int i1 = 0; i1 < linkMatrix[j].length; i1++) {
                    if ((linkMatrix[j][i1] == 1) && (j != i1)) {
                        k++;
                    }
                }
                if (i != j) {
                    if (k != 0)
                        linkVectorBuff[i] += (linkVector[j] * linkMatrix[i][j] * 0.85 / k);
                }
            }

        }
        return linkVectorBuff;

    }

    public HashMap<String, Double> getPageRank(LinkedList<String> list, int n, int[][] linkMatrix, HashMap<String, Integer> prevPageRank) {
        HashMap<String, Double> pageRank = new HashMap<>();
        if (prevPageRank == null) {
            for (String s : list) {
                pageRank.put(s, (double) 1);
            }
        }
        double[] vectorRank = new double[n];
        for (int i = 0; i < vectorRank.length; i++) {
            vectorRank[i] = 1;
        }
        int i = 0;
        for (String s : list) {
            vectorRank[0] = pageRank.get(s);
            i++;
        }
        for (int j = 0; j <10; j++) {
            vectorRank = makePageRank(linkMatrix, vectorRank, n);
        }
        i = 0;
        for (String s : list) {
            pageRank.replace(s, vectorRank[i]);
            i++;
        }
        return pageRank;
    }

    //print legend for the adjacency matrix
    public void printMap() {
        HashMap<Integer, String> mapBuff = getList(map);
        mapBuff.forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });
    }

    public LinkedList<String> getLinkList() {
        return linkList;
    }
}
