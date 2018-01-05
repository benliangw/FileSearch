package NKUSE.Filesearch;

import java.awt.AWTException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class BuildIndex
{
	private static ArrayList<String> listname = new ArrayList<String>();
	private static int MaxIndexNum = 10;
	public static String[] index_list;
	public static String[] search_list;
	
	public static void Initial()
	{
		index_list = new String[MaxIndexNum];
		for(int i = 0; i < MaxIndexNum; i++)
			index_list[i] = "./index/"+i;
		search_list = new String[MaxIndexNum];
	}

	public static void buildIndex_main() throws FileNotFoundException, AWTException
	{
		System.out.println("请输入索引建立的位置(1-10的整数)，输入其他则返回上级菜单");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		int now = 0;
		if(input.equals("1"))
			now = 0;
		else if(input.equals("2"))
			now = 1;
		else if(input.equals("3"))
			now = 2;
		else if(input.equals("4"))
			now = 3;
		else if(input.equals("5"))
			now = 4;
		else if(input.equals("6"))
			now = 5;
		else if(input.equals("7"))
			now = 6;
		else if(input.equals("8"))
			now = 7;
		else if(input.equals("9"))
			now = 8;
		else if(input.equals("10"))
			now = 9;
		else
			now = 100;
		if(now < MaxIndexNum && now >= 0)
		{
			System.out.println("请输入目录(绝对路径)");
			String rootdir = scanner.nextLine();
			createIndex(rootdir, now);
			System.out.println("建立索引成功！输入1以返回上层菜单,输入2使用当前索引进行查询");
			String choose_num = scanner.nextLine() ;
			while(true)
			{
				if(choose_num.equals("1"))
					break;
				if(choose_num.equals("2"))
				{
					Search.search_main(now);
					break;
				}
			}				
		}
	}
	
	public static void ShowAllIndex() throws AWTException
	{
		Scanner scanner = new Scanner(System.in);
		int num = 0;
		for(int i = 0; i < MaxIndexNum; i++)
		{
			if(search_list[i] != null)
				num++;
		}
		if(num == 0)
		{
			System.out.println("当前无索引，请先建立索引！(输入0以返回上层菜单)");
			while(true)
			{
				if(scanner.nextLine().equals("0"))
					return;
			}
		}
		else
		{
			System.out.println("所有索引：(输入0返回上层菜单,输入对应数字使用该索引进行搜索)");
			for(int i = 0; i < MaxIndexNum; i++)
			{
				if(search_list[i] != null)
					System.out.println((i+1)+": "+search_list[i]);
			}
		}
		while(true)
		{
			String inputnum = scanner.nextLine();
			if(inputnum.equals("0"))
				break;
			else if(inputnum.equals("1") && search_list[0] != null)
			{
				Search.search_main(0);
				break;
			}
			else if(inputnum.equals("2") && search_list[1] != null)
			{
				Search.search_main(1);
				break;
			}
			else if(inputnum.equals("3") && search_list[2] != null)
			{
				Search.search_main(2);
				break;
			}
			else if(inputnum.equals("4") && search_list[3] != null)
			{
				Search.search_main(3);
				break;
			}
			else if(inputnum.equals("5") && search_list[4] != null)
			{
				Search.search_main(4);
				break;
			}
			else if(inputnum.equals("6") && search_list[5] != null)
			{
				Search.search_main(5);
				break;
			}
			else if(inputnum.equals("7") && search_list[6] != null)
			{
				Search.search_main(6);
				break;
			}
			else if(inputnum.equals("8") && search_list[7] != null)
			{
				Search.search_main(7);
				break;
			}
			else if(inputnum.equals("9") && search_list[8] != null)
			{
				Search.search_main(8);
				break;
			}
			else if(inputnum.equals("10") && search_list[9] != null)
			{
				Search.search_main(9);
				break;
			}
		}
	}
	
	public static void dealwithallfile(String filepath)
	{
		File file = new File(filepath);
		if (!file.isDirectory())
			listname.add(filepath);
		else
		{
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++)
			{
				File readfile = new File(filepath);
				if (!readfile.isDirectory())
					listname.add(filepath);
				else if (readfile.isDirectory())
					dealwithallfile(filepath + "/" + filelist[i]);
			}
		}
	}

	public static void createIndex(String rootdir, int index) throws FileNotFoundException
	{
		String index_path = index_list[index];
		search_list[index] = rootdir;
		IndexWriter indexWriter = null;
		try
		{
			File index_file = new File(index_path);
			Directory directory = FSDirectory.open(index_file);
			Analyzer analyzer = new IKAnalyzer();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_45, analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			indexWriter.deleteAll();// 清除以前的index
			dealwithallfile(rootdir);
			for (int i = 0; i < listname.size(); i++)
			{
				File file = new File(listname.get(i));
				Tika tika = new Tika();
				Document document = new Document();
				
				String name = file.getName();
				String path = file.getAbsolutePath();
				String content = tika.parseToString(file);
				String type = tika.detect(file);

				Field fileName = new Field("fileName", name, TextField.TYPE_STORED);
				Field filePath = new Field("filePath", path, TextField.TYPE_STORED);
				Field fileContent = new Field("fileContent", content, TextField.TYPE_STORED);
				Field fileType = new Field("fileType", type, TextField.TYPE_STORED);

				System.out.println("name="+name);
				System.out.println("\tpath="+path);
				//System.out.println("content="+content);
				System.out.println("\ttype="+type);
				
				document.add(fileName);
				document.add(filePath);
				document.add(fileContent);
				document.add(fileType);

				indexWriter.addDocument(document);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (indexWriter != null)
					indexWriter.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void ClearAllIndex()
	{
		for(int i = 0; i < MaxIndexNum; i++)
		{
			search_list[i] = null;
		}
		System.out.println("清空索引成功!输入0返回主菜单");
		Scanner scanner = new Scanner(System.in);
		while(true)
		{
			if(scanner.nextLine().equals("0"))
				break;
		}
	}
	
	public static void ClearTheIndex() throws AWTException
	{
		System.out.println("所有索引：(输入0以返回上层菜单,输入对应数字删除该索引)");
		for(int i = 0; i < MaxIndexNum; i++)
		{
			if(search_list[i] != null)
			{
				System.out.println((i+1)+": "+search_list[i]);
			}
		}
		Scanner scanner = new Scanner(System.in);
		while(true)
		{
			String inputnum = scanner.nextLine();
			if(inputnum.equals("0"))
				break;
			else if(inputnum.equals("1") && search_list[0] != null)
			{
				search_list[0] = null;
				break;
			}
			else if(inputnum.equals("2") && search_list[1] != null)
			{
				search_list[1] = null;
				break;
			}
			else if(inputnum.equals("3") && search_list[2] != null)
			{
				search_list[2] = null;
				break;
			}
			else if(inputnum.equals("4") && search_list[3] != null)
			{
				search_list[3] = null;
				break;
			}
			else if(inputnum.equals("5") && search_list[4] != null)
			{
				search_list[4] = null;
				break;
			}
			else if(inputnum.equals("6") && search_list[5] != null)
			{
				search_list[5] = null;
				break;
			}
			else if(inputnum.equals("7") && search_list[6] != null)
			{
				search_list[6] = null;
				break;
			}
			else if(inputnum.equals("8") && search_list[7] != null)
			{
				search_list[7] = null;
				break;
			}
			else if(inputnum.equals("9") && search_list[8] != null)
			{
				search_list[8] = null;
				break;
			}
			else if(inputnum.equals("10") && search_list[9] != null)
			{
				search_list[9] = null;
				break;
			}
		}
	}
	
}
