package NKUSE.Filesearch;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args) throws FileNotFoundException
	{
		System.out.println("请输入要建立索引的文件夹的绝对路径");//TODO:实现文件框选择文件夹
		String indexdir = "F://IR_test//index"; //TODO:可以建立多个索引
		Scanner scanner = new Scanner(System.in);
		String rootPath = scanner.nextLine();
		BuildIndex.createIndex(rootPath, indexdir);
		System.out.println("建立索引成功，请输入要查询的词语");//TODO:多次查询
		while(true)
		{
			String keyword = scanner.nextLine();
			if(keyword.equals(":wq"))
				break;
			Search.search(keyword, indexdir);
			
		}
	}
}
