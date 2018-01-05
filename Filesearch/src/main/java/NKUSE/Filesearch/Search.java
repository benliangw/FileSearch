package NKUSE.Filesearch;

import java.awt.AWTException;
import java.io.File;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Search
{	
	public static void search_main(int index) throws AWTException
	{
		String index_path = BuildIndex.index_list[index];
		String search_path = BuildIndex.search_list[index];
		Scanner scanner = new Scanner(System.in);
		ConsoleControl.clear();
		System.out.println("您当前搜索的目录："+search_path);
		while(true)
		{
			System.out.println("请输入您想搜索的内容, 输入\":q!\"以退出");
			String keyWord = scanner.nextLine();
			if(keyWord.isEmpty())
				continue;
			if(keyWord.equals(":q!"))
				break;
			search(keyWord, index_path);
		}
	}
	
	public static void search(String keyWord, String index_path)
	{
		try
		{
			// 1、创建Directory
			Directory directory = FSDirectory.open(new File(index_path));
			System.out.println(directory.toString());
			// 2、创建IndexReader
			DirectoryReader directoryReader = DirectoryReader.open(directory);
			// 3、根据IndexReader创建IndexSearch
			IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
			// 4、创建搜索的Query
			Analyzer analyzer = new IKAnalyzer(true); // 使用IK分词
			
			String[] fields = {"fileName", "fileContent"}; // 要搜索的字段，一般搜索时都不会只搜索一个字段
			// 字段之间的与或非关系，MUST表示and，MUST_NOT表示not，SHOULD表示or，有几个fields就必须有几个clauses
			BooleanClause.Occur[] clauses = {BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};
			// MultiFieldQueryParser表示多个域解析， 同时可以解析含空格的字符串，如果我们搜索"上海 中国"
			System.out.println("You searched "+keyWord);
			Query multiFieldQuery = MultiFieldQueryParser.parse(Version.LUCENE_45, keyWord, fields, clauses, analyzer);
			
			// 5、根据searcher搜索并且返回TopDocs
			TopDocs topDocs = indexSearcher.search(multiFieldQuery, 100); // 搜索前100条结果
			System.out.println("共找到匹配处：" + topDocs.totalHits); 
			// 6、根据TopDocs获取ScoreDoc对象
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			System.out.println("共找到匹配文档数：" + scoreDocs.length);
			for (ScoreDoc scoreDoc : scoreDocs)
			{
				// 7、根据searcher和ScoreDoc对象获取具体的Document对象
				Document document = indexSearcher.doc(scoreDoc.doc);
				String file_type = document.get("fileType"); 
				String file_path = document.get("filePath");
				String file_name = document.get("fileName");
				System.out.println("Name:"+file_name);
				System.out.println("\tPath:"+file_path);
				System.out.println("\tType:"+file_type);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
