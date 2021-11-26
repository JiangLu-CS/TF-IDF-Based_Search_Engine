package search;
public class bin {
    public static void main(String[] args) throws InterruptedException {
        Crawler crawler = new Crawler("华南理工","https://www.scut.edu.cn/new/");
        crawler.addKeyWord("华南理工", 0);
        crawler.initialize();
        crawler.parallelhandle();
        crawler.setStartURL("https://www.scut.edu.cn/new/");
        crawler.setTitle("华南理工");
        crawler.setThreadCount(3);
        crawler.setUrlCount(200);
    }
}
