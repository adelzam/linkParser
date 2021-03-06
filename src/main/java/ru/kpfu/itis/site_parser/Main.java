package ru.kpfu.itis.site_parser;

import java.util.HashMap;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Do you want see standard example? yes/no");
        String k = in.nextLine();
        String site = null;
        int n = 0;
        if (k.equals("yes")||(k.equals(""))) {
             site= "https://www.artlebedev.ru";
            n =10;
        }
        if (k.equals("no")) {
            System.out.println("Enter web-site like https://site.net");
            site = in.nextLine();
            System.out.println("Enter N");
            n = in.nextInt();
        }
        System.out.println("Please wait");
        SiteParser siteParser = new SiteParser(n);
        int[][] mapLinked = siteParser.createMagic(site,n);
        String decorLine="  | ";
        String decorLine2="---";
        for (int i = 0; i <n ; i++) {
            decorLine=decorLine+i+" | ";
            decorLine2+="----";
        }
        for (int i = 0; i <mapLinked.length ; i++) {
            if (i==0) System.out.println(decorLine);
            System.out.println(decorLine2);
            for (int j = 0; j <mapLinked.length ; j++) {
                if(j==0) System.out.print(i+" | ");
                System.out.print(mapLinked[i][j]+" | ");
            }
            System.out.println();
        }
siteParser.printMap();
        HashMap<String, Double> result = siteParser.getPageRank(siteParser.getLinkList(),n,mapLinked,null);
        result.forEach((key,v)-> System.out.println(key+"  "+v+";  "));
    }
}
