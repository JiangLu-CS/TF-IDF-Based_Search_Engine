import com.cose.ir.exp.bean.Article;
import com.cose.ir.exp.bean.Item;
import com.cose.ir.exp.bean.Item_ori;
import com.cose.ir.exp.demo.NlpirMethod;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BackIndex {

    public static Map<Integer, Article> article_map = new HashMap<>(); //键为文章编号 docId

    public static void main(String[] args) {
        BackIndex BackIndex = new BackIndex();
        BackIndex.indexing();
    }

    public LinkedList<Item> indexing() {
        LinkedList<Item_ori> list = new LinkedList<>();
        String in_path = "articles";
        partWord(in_path, list);
        list = sortItems(list);
        //词频算法：统计在每个文档中出现的每个 item 的词频 tf
        wordFrequency(list);

        //去重算法：计算出现每个 item 的文档个数 df，将重复出现的 item 进行去重处理
        //同时创建索引结构：建立字典结构和 PostingList 结构，存储 items 和 df、DocIDs 和 tf
        LinkedList<Item> dictionary = deduplAndCreateIndex(list);

        // 输出字典结构和 PostingList 结构到文件
        outDictionary2File(dictionary, "result/fenci_result");

        System.out.println("倒排索引建立完成");

        return dictionary;
    }


    public void partWord(String in_path, LinkedList<Item_ori> list) {
        File in_directory = new File(in_path);
        File[] files = in_directory.listFiles();
        long startTime = System.currentTimeMillis();
        int i = 0;
        for (File file : files) {
            i++;
            StringBuilder stringBuilder = new StringBuilder();
            int index = Integer.valueOf(file.getName().split("-")[0]);
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                String title = br.readLine(); //第一行是url
                String url = br.readLine(); //第二行是文章标题
                if(br == null){
                    continue;
                }
                String content;
                while ((content = br.readLine()) != null) {
                    stringBuilder.append(content);
                }
                article_map.put(index, new Article(index, title, url, stringBuilder.toString().substring(0,20), url));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            String content = stringBuilder.toString();
            String result = NlpirMethod.NLPIR_ParagraphProcess(content, 0);

            String[] strings = result.split(" ");
            for (String str : strings) {
                if (str.length() > 0) {
                    list.add(new Item_ori(str, index));
                }
            }
        }
        long endTime = System.currentTimeMillis(); //获取结束时间
        System.out.println("分词运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
    }


    public LinkedList<Item_ori> sortItems(LinkedList<Item_ori> list) {
        long startTime = System.currentTimeMillis();
        list = list
                .stream()
                .sorted()
                .distinct()
                .collect(Collectors.toCollection(LinkedList::new));
        long endTime = System.currentTimeMillis();
        System.out.println("排序运行时间：" + (endTime - startTime) + "ms");
        return list;
    }

    public void wordFrequency(LinkedList<Item_ori> list) {
        long startTime = System.currentTimeMillis();
        Iterator<Item_ori> iterator = list.iterator();
        Item_ori last = iterator.next();
        Item_ori current;
        while (iterator.hasNext()) {
            current = iterator.next();
            if (last.docId.equals(current.docId) && last.term.equals(current.term)) {
                last.freq++;
                iterator.remove();
                continue;
            }
            last = current;
        }

        long endTime = System.currentTimeMillis(); //获取结束时间
        System.out.println("词频算法运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
    }

    private LinkedList<Item> deduplAndCreateIndex(LinkedList<Item_ori> list) {
        long startTime = System.currentTimeMillis();
        LinkedList<Item> item_list = new LinkedList<>();
        Item cur_item;
        Iterator<Item_ori> iterator = list.iterator();
        Item_ori current = iterator.next();
        cur_item = new Item(current.term, current.freq, current);
        item_list.add(cur_item);
        while (iterator.hasNext()) {
            current = iterator.next();
            if (current.term.equals(cur_item.term)) {
                cur_item.docs++;
                cur_item.freq_total += current.freq;
                cur_item.ori_item_list.add(current);
            } else {
                cur_item = new Item(current.term, current.freq, current);
                item_list.add(cur_item);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("创建索引的时间：" + (endTime - startTime) + "ms");

        return item_list;
    }
    //输出字典结构和 PostingList 结构到文件
    public static void outDictionary2File(LinkedList<Item> dictionary, String fileName) {
        BufferedWriter out = null;
        int count = 0;
        try {
            File out_file = new File(fileName + ".txt");
            out_file.createNewFile();
            out = new BufferedWriter(new FileWriter(out_file));
            for (Item item : dictionary) {
                Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher matcher;
                String prop = NlpirMethod.NLPIR_GetWordPOS(item.term);
                int pos = prop.indexOf("/") + 1;
                matcher = p.matcher(item.term);
                if(!matcher.find()){
                    System.out.println("停用词：" + item.term + " 删除了");
                    System.out.println(item.term + "在创建倒排索引的时候删除了");
                    continue;
                }
                String content = item.term + " : " + item.docs + " : " + item.freq_total + " , ";
                for (Item_ori item_ori : item.ori_item_list) {
                    content += "[ " + item_ori.docId + " : " + item_ori.freq + " ] -> ";
                }
                out.write(content + "\r\n");
                count++;
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                    System.out.println("共创建索引：" + count);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
