package search;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Formatter;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Crawler
{

    public static int counting = 0;

	private static Logger logger = Logger.getLogger(Crawler.class.getName());
	private String title;
	private volatile static int threadNum = 0;
	private int urlCount = 1000;
	private volatile int visitedURL = 0;// 表示目前已经访问的网页个数
	private int threadCount = 3;// 表示最多同时允许运行多少个线程
	private double threshold = 0.91;
	private String startURL;
	private Hashtable<String, Integer> keyWords = new Hashtable<String, Integer>();
	private PriorityBlockingQueue<PriorityURL> waitforHandling = new PriorityBlockingQueue<PriorityURL>();
	private HashSet<String> isWaiting = new HashSet<String>();
	// 表示符合要求，最终要剩下显示的网页
	private Hashtable<String, String> wanted = new Hashtable<String, String>();
	// 我们保存的不相关的网页
	private HashSet<String> noneRelevant = new HashSet<String>();

	private boolean stop = false;
	ExecutorService threadPool = Executors.newCachedThreadPool();

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public int getUrlCount()
	{
		return urlCount;
	}

	public void setUrlCount(int urlCount)
	{
		this.urlCount = urlCount;
	}

	public int getThreadCount()
	{
		return threadCount;
	}

	public Enumeration<String> getKeyWords()
	{
		return keyWords.keys();
	}

	public void setThreshold(double threshold)
	{
		this.threshold = threshold;
	}

	public String getStartURL()
	{
		return startURL;
	}

	public void setStartURL(String startURL)
	{
		this.startURL = startURL;
	}

	public double getThreshold()
	{
		return threshold;
	}

	public void setThreadCount(int threadCount)
	{
		this.threadCount = threadCount;
	}

	public void addKeyWord(String word, int count)
	{
		keyWords.put(word, count);
	}

	public Crawler(String title, String start)
	{
		this.title = title;
		this.startURL = start;
	}

	public void initialize()
	{
		stop = false;
		System.out.println("访问总数：0");
		Download download = new Download();
		try
		{
			String content = download.downloadHttp(new URL(startURL));
			String title = "";
			String regex = "<title>([^<]+)</title>";
			Pattern p = Pattern.compile(regex);
			Matcher m = null;
			if(p != null && content != null){
				m = p.matcher(content);
			}
			if (m!= null && m.find())
				title = m.group(1);
			System.out.println("title is " + title);
			int count;
			// 统计关键词的词频
			count = content.split("华南理工").length - 1;

			ArrayList<String> urls = getLink(content, new URL(startURL));
			int length = urls.size();
			// 加入优先队列
			for (String s : urls)
			{
				waitforHandling.add(new PriorityURL(s, count));
				if (!isWaiting.contains(s))
					isWaiting.add(s);
				else System.out.println("重复网址：" + s);
			}
			wanted.put(startURL, title);
			visitedURL = 1;
			System.out.println("访问总数：" + visitedURL);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}


	public void search(int threadNumber)
	{
		String url, content, title = "";
		Download d = new Download();
		while (visitedURL < urlCount && !stop)
		{
			if (waitforHandling.size() > 0)
			{
				url = waitforHandling.remove().getUrl();
				isWaiting.remove(url);
			}
			else
				break;
			try
			{
				content = d.downloadHttp(new URL(url));
				if (content != null)
				{
					Hashtable<String, Integer> destination = new Hashtable<String, Integer>();
					// 统计关键词的词频

					int count = content.split("华南理工").length - 1;
					synchronized (this)
					{
						if (count < 1)
						{
							noneRelevant.add(url);
							continue;// 如果相关度小于阈值，则忽略该网页
						}
					}
					// 提取网页中的链接
					ArrayList<String> urls = getLink(content, new URL(url));
					int length = urls.size();
					String regex = "<title>([^<]+)</title>";
					Pattern p = Pattern.compile(regex);
					Matcher m = p.matcher(content);
					if (m.find())
						title = m.group(1);
					// 加入优先队列
					for (String s : urls)
					{
						// 访问不会修改，无需同步
						if (wanted.containsKey(s))
						{
							System.out.println("重复网页：" + s);
							continue;
						}
						if (noneRelevant.contains(s))
						{
							System.out.println("重复网页：" + s);
							continue;
						}
						synchronized (this)
						{
							if (!isWaiting.contains(s))
							{
								waitforHandling.add(new PriorityURL(s, count));
								isWaiting.add(s);
							}
						}
					}
					wanted.put(url, title);
					FileHandler fileHandler = new FileHandler("C:\\Users\\Lu\\Desktop\\ThemeCrawler\\log\\crawl.log");
					fileHandler.setFormatter(new Formatter() {//定义一个匿名类
						@Override
						public String format(LogRecord record) {
							return new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())+":"+
							record.getMessage() + "\n";
						}
					});
					fileHandler.setLevel(Level.INFO);
					logger.addHandler(fileHandler);
					logger.info("url:" + url +" -标题:"+ title +
					"-响应码：200" );

					try {


						String name = title;
						counting++;
						PrintWriter writer = new PrintWriter(new FileOutputStream(counting + "-" + name + ".txt"));
						writer.write(title);
						writer.write("\n");
						writer.write(url);
						writer.write("\n");
						writer.write(content);
						writer.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						counting--;
					}
					synchronized (this)
					{
						isWaiting.remove(url);
						visitedURL++;
						System.out.println("访问总数:" + visitedURL);
					}
				}
			}
			catch (MalformedURLException e)
			{
				// e.printStackTrace();
				continue;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("current thread num is " + --threadNum+",waiting size is "+waitforHandling.size());
		synchronized (this)
		{
			wanted.clear();
			waitforHandling.clear();
			noneRelevant.clear();
			isWaiting.clear();
		}
	}




	public double calRelavancy(Hashtable<String, Integer> destination)
	{
		long sum1 = 0, sum2 = 0, sum3 = 0;
		for (String key : keyWords.keySet())
		{
			sum1 += keyWords.get(key).intValue()
					* destination.get(key).intValue();
			sum2 += keyWords.get(key).intValue() * keyWords.get(key).intValue();
			sum3 += destination.get(key).intValue()
					* destination.get(key).intValue();
		}
		if (sum3 == 0)
			return 0;// 如果一个网页和我们的主题没有关系，有可能会计算出0，不能用在分母中
		return sum1 * 1.0 / (Math.sqrt(sum2) * Math.sqrt(sum3));
	}


	public ArrayList<String> getLink(String content, URL url)
	{
		ArrayList<String> urls = new ArrayList<String>();
		String regex = "<a\\s*href=\"([^>\"]*)\"[^>]*>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		String s;
		while (m.find())
		{
			s = m.group(1);
			if (s.length() == 1)
				continue;
			if (s.startsWith("/"))
				s = "http://" + url.getHost() + s;
			// System.out.println(s);
			if (s.startsWith("http"))
				urls.add(s);
		}
		return urls;
	}


	public void parallelhandle()
	{
		for (int i = 0; i < threadCount; i++)
		{
			new Task(i).start();
		}
		try
		{
			Thread.sleep(1);
		}
		catch (InterruptedException e)
		{
				e.printStackTrace();
		}
		Runnable task = new Runnable() {
			public void run() {
				while (threadNum > 0)
					;
			}
		};
		threadPool.execute(task);// 通过启动一个新的线程来执行解释程序
		
		
	}

	class Task extends Thread
	{
		int number;

		Task(int number)
		{
			this.number = number;
			threadNum++;
		}

		public void run()
		{
			search(number);
		}
	}

}
