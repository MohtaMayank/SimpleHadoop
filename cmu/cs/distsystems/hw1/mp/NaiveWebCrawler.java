package cmu.cs.distsystems.hw1.mp;

/**
 * Created with IntelliJ IDEA.
 * User: mimighostipad
 * Date: 1/30/13
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */

import cmu.cs.distsystems.hw1.MigratableProcess;
import cmu.cs.distsystems.hw1.TransactionalFileInputStream;
import cmu.cs.distsystems.hw1.TransactionalFileOutputStream;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NaiveWebCrawler extends MigratableProcess{

    private Queue<String> URLQueue;
    private HashSet<String> visted;
    private TransactionalFileOutputStream parsedResults;
    public final static String titlePattern = "<title>(.*)</title>";
    public final static String htmlPattern = "http://([^\"']*)";


    public NaiveWebCrawler(String[] args){
        super(args);
        this.URLQueue = new LinkedList<String>();
        this.URLQueue.add(args[0]);
        this.parsedResults = new TransactionalFileOutputStream
                (args[1],Boolean.parseBoolean(args[2]));
        this.visted = new HashSet<String>();

    }

    public String fetchPage(String webURL) {
        try {
            URL url = new URL(webURL);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while(line != null){
                sb.append(line);
                line = br.readLine();
            }

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<String> extractURLs(String doc){
        Pattern p = Pattern.compile(htmlPattern);
        Matcher m = p.matcher(doc);
        List<String> urlList = new ArrayList<String>();


        while(m.find()){
            MatchResult mr = m.toMatchResult();
            String url = doc.substring(mr.start(),mr.end());
            if(!this.visted.contains(url)){
                urlList.add(url);
            }
        }

        return urlList;
    }

    public String parseDocument(String doc){

        Pattern p = Pattern.compile(titlePattern);
        Matcher m = p.matcher(doc);

        if(m.find()){
            MatchResult mr = m.toMatchResult();
            return doc.substring(mr.start(),mr.end());
        }

        return "";

    }

    public void saveParsedDocuments(String parsedDoc){
        PrintWriter pw = new PrintWriter(this.parsedResults);
        pw.write(parsedDoc + "\n");
        pw.flush();
    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean doNextStep() {
        if(URLQueue.size() == 0) return false;
        String url = URLQueue.poll();
        String page = fetchPage(url);
        if(page.equals("")) return true;

        this.visted.add(url);
        String content = parseDocument(page);
        List<String> newUrls = extractURLs(page);

        this.URLQueue.addAll(newUrls);
        this.saveParsedDocuments(content);

        return true;
    }

    @Override
    public AFFINITY getAffinity() {
        return AFFINITY.WEAK;
    }

    public static void main(String[] args){
        String[] nargs = {"1","/Users/mimighostipad/Documents/testcrawler.txt","false"};
        NaiveWebCrawler nwc = new NaiveWebCrawler(nargs);

        String content = nwc.parseDocument(nwc.fetchPage("http://www.cmu.edu"));
        System.out.println(content);
        nwc.extractURLs(nwc.fetchPage("http://www.cmu.edu"));
        nwc.saveParsedDocuments(content);


    }
}
