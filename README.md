# TF-IDF-Based_Search_Engine
Search Engine based on Topic-specific Crawler and TF-IDF Algorithm
![image](https://user-images.githubusercontent.com/54580404/143533062-6aaf4b71-1998-4f41-8b4f-07396a55623d.png)
网页的爬取：crawl.java
Initialize函数处理种子url
1.访问初始网页并下载，同时回传该网页读取到的内容（读取网页的函数将在下文说明）。
2.根据正则表达式提取title，并保存为下载来的网页的保存名称，为后续的保存做准备
![image](https://user-images.githubusercontent.com/54580404/143533082-a39f61cf-9051-4996-bd4f-ed1b6a1b3b27.png)
![image](https://user-images.githubusercontent.com/54580404/143533086-31dde32f-db2d-4d55-a6e6-894ff17a00da.png)
![image](https://user-images.githubusercontent.com/54580404/143533088-057dc920-69f4-4a30-be7f-99c1876bd6d2.png)
![image](https://user-images.githubusercontent.com/54580404/143533091-444328ad-16a8-43ce-b9d1-3e709e2d9a45.png)
![image](https://user-images.githubusercontent.com/54580404/143533095-2b060c3d-f11a-4903-b62f-e5b4667ad6ba.png)
下载到的网页

3.1 倒排索引
（1）网页预处理。对实验一采集到的网页数据进行预处理，包括但不限于：正文信息提取、中文分词、停止词处理等。
（2）设计和创建倒排索引。对每个索引的词，至少应该记录其文件频率（df）。设计置入文件的数据结构，至少记录每个词在各个文档中出现的次数，即词频（tf）。同时对每个文档，记录其文档长度。
（3）对索引的过程，生成相关的统计信息，例如：创建索引所需的时间、索引的大小、词汇表长度等。
 
3.2检索系统
        （1）设计实现一个简单的检索系统，可输入检索词，并输出查询结果，按相关度排序。
        （2）对指定的查询词（IR2021查询词.txt），给出每个查询结果排序，以及相似度得分。所提交的结果将被评估。
提交的结果文件有查询结果的数据块构成。每个查询词对应一个结果数据块，每个查询词提交10条查询结果。每个结果数据块格式如下：
第一行是查询词序号，如“TD01”
每一行是一条查询结果记录，格式为： <URL Similarity>
URL：网页的规范化URL
Similarity：相似度得分
每个数据块的十条记录按相似度从高到低排序，每个数据块之间以一个空行隔开。
 
3.3检索优化及系统评测
选做以下之一：
（1）对结果进行人工判断相关或不相关，然后基于该判断用评测指标Precision@10和MAP计算系统的检索性能指标。
（2）拼写检查：针对用户拼写错误或关键字生僻而导致搜索结果不佳的问题，提供相近的词建议。该功能可通过查询案例来展示。
4. 提交内容
程序：包括源程序及注释，程序安装使用说明；
查询结果文件：查询词对应查询结果汇总
实验报告：说明程序设计的思路，并对实验过程进行分析和总结。![image](https://user-images.githubusercontent.com/54580404/143533163-e4429015-b666-499a-8d76-f93f6dca6163.png)

![image](https://user-images.githubusercontent.com/54580404/143533176-d6b0ab64-5900-4c76-9c2e-21b1f45ef0cd.png)
![image](https://user-images.githubusercontent.com/54580404/143533184-f217b894-ab30-4603-b081-3b75e8979756.png)
![image](https://user-images.githubusercontent.com/54580404/143533190-f9149107-349f-4258-82ac-bdbcaaf87019.png)
![image](https://user-images.githubusercontent.com/54580404/143533195-bed05c91-9127-49a1-aa91-926b0f9f4a85.png)![image](https://user-images.githubusercontent.com/54580404/143533210-8821dd77-be1c-4d14-8036-3ff45f0234d7.png)
![image](https://user-images.githubusercontent.com/54580404/143533228-cef4dc01-bb80-4136-96a7-cedbbcb352a1.png)

![图片](https://user-images.githubusercontent.com/54580404/143533204-04f0e65e-65bd-4155-b379-8f11aa075f6e.png)
![image](https://user-images.githubusercontent.com/54580404/143533242-ac803144-bac1-4ad7-91a6-ab159f5dfc6a.png)
![image](https://user-images.githubusercontent.com/54580404/143533248-f19cffae-bb51-4c01-8305-43a1120399e7.png)
![image](https://user-images.githubusercontent.com/54580404/143533252-a5d0c5da-48ac-48db-9b93-1d1fd7e07d45.png)
![image](https://user-images.githubusercontent.com/54580404/143533261-b00d2622-8dd4-456d-a96b-cb07b18541be.png)
![image](https://user-images.githubusercontent.com/54580404/143533265-7bf1fadd-fc51-422b-8c18-ce9277316770.png)
![image](https://user-images.githubusercontent.com/54580404/143533277-cfb7f5cc-da96-4a5e-8140-17d1afbc94d4.png)
