import com.cose.ir.exp.bean.Article;
import com.cose.ir.exp.bean.Item;
import com.cose.ir.exp.bean.Item_ori;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.*;

public class searchSystem {
    // article nums
    public static int N = 102;

    public static void main(String[] args) {
        String query = "";
        System.out.println("输入关键词");
        Scanner sc = new Scanner(System.in);
        query = sc.next();
        
        
        // build tf-idf index
        Experiment2 exp2 = new Experiment2();
        LinkedList<Item> dictionary = exp2.indexing();
        Df(dictionary);
        Tf(dictionary);
        Idf(dictionary);


        Map<Integer,BigDecimal> map = new LinkedHashMap<>();
        for (int i = 0; i < N; i++) {
            if(getTFIDF(i, query) != null){
                map.put(i,getTFIDF(i, query));
            }
        }
        
        List<Map.Entry<Integer, BigDecimal>> list = new ArrayList<>();
        list.addAll(map.entrySet());
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        
        Map<Integer, BigDecimal> result = new LinkedHashMap<>();
        for(Map.Entry<Integer, BigDecimal> entry : list){
            result.put(entry.getKey(), entry.getValue());
        }
        
        System.out.println("关键词：" + query);
        
        for(int i : result.keySet()){
            Article article = Experiment1.article_map.get(i);
            System.out.print(i + ":");
            System.out.println(article.getTitle() + " ");
            System.out.print(article.getUrl() + "      ");
            System.out.println("score:" + map.get(i));
        }

    }


    public static void Idf(LinkedList<Item> dictionary) {
        Jedis jedis = new Jedis("localhost");
        try {
            long startTime = System.currentTimeMillis();
            for (Item item : dictionary) {
                System.out.println("idf:" + item.term + " " + String.valueOf(Math.log(N / item.docs) / Math.log(10)));
                if(Math.log(N / item.docs) / Math.log(10) == 0){
                    jedis.set("idf:" + item.term, "1");
                    continue;
                }
                jedis.set("idf:" + item.term, String.valueOf(Math.log(N / item.docs) / Math.log(10)));
            }
            long endTime = System.currentTimeMillis();
            System.out.println("idf统计总时间：" + (double)(endTime - startTime)/1000 + "s"); //输出时间
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static BigDecimal getTFIDF(int doc, String term){
        Jedis jedis = new Jedis("localhost");
        String key = jedis.get("tf:"+  term + ":" + doc);
        System.out.println(key);
        if(key == null){
            return null;
        }
        BigDecimal tf = BigDecimal.valueOf(Double.parseDouble(key));
        System.out.println(tf);
        if(tf == null){
            return null;
        }
        if(jedis.get("idf:" + term) == null){
            return null;
        }
        System.out.println("idf:" + term);
        System.out.println(jedis.get("idf:" + term));
        BigDecimal idf;
        try{
            idf = new BigDecimal(jedis.get("idf:" + term));
        }catch (Exception e){
            System.out.println("出错啦");
            return null;
        }
        System.out.println(idf);
        BigDecimal tfidf = idf.multiply(tf);
        return tfidf;
    }
    
    public static void Tf(LinkedList<Item> dictionary) {
        Jedis jedis = new Jedis("localhost");
        long startTime = System.currentTimeMillis();
        for (Item item : dictionary) {
            int id = 1;
            for (Item_ori item_ori : item.ori_item_list) {
                while (id < item_ori.docId) {
                    id++;
                }
                System.out.println("tf:" + item.term + ":" + id + " " +
                        String.valueOf(item_ori.freq));
                jedis.set("tf:" + item.term + ":" + id,
                        String.valueOf(item_ori.freq));
                id = item_ori.docId + 1;
            }
            while (id <= N) {
                id++;
            }
        }
        long endTime = System.currentTimeMillis(); //获取结束时间
        System.out.println("tf统计总时间：" + (double)(endTime - startTime)/1000 + "s"); //输出时间
    }

    public static void Df(LinkedList<Item> dictionary) {
        Jedis jedis = new Jedis("localhost");
        try {
            long startTime = System.currentTimeMillis();

            for (Item item : dictionary) {
                System.out.println("df:" + item.term +" " +String.valueOf(item.docs));
                jedis.set("df:" + item.term, String.valueOf(item.docs));
            }
            long endTime = System.currentTimeMillis();
            System.out.println("df统计总时间：" + (double)(endTime - startTime)/1000 + "s");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
