package NKUSE.Filesearch;

import java.awt.AWTException;
import java.io.IOException;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args) throws AWTException, IOException
	{
		Scanner scanner = new Scanner(System.in);
		BuildIndex.Initial();
		while(true)
		{
			scanner = new Scanner(System.in);
			ConsoleControl.clear();
			ConsoleControl.welcome();
			String choose_num = scanner.nextLine();
			if(choose_num.equals("1"))
			{
				ConsoleControl.clear();
				BuildIndex.ShowAllIndex();
			}
			else if(choose_num.equals("2"))
			{
				ConsoleControl.clear();
				BuildIndex.buildIndex_main();
			}
			else if(choose_num.equals("3"))
			{
				ConsoleControl.clear();
				BuildIndex.ShowAllIndex();
			}
			else if(choose_num.equals("4"))
			{
				ConsoleControl.clear();
				BuildIndex.ClearAllIndex();
			}
			else if(choose_num.equals("5"))
			{
				ConsoleControl.clear();
				BuildIndex.ClearTheIndex();
			}
			else if(choose_num.equals("6"))
			{
				System.out.println("谢谢使用,再见！");
				break;
			}
			else 
			{
				System.out.println("输入错误，请重新输入!");
				continue;
			}
		}
		scanner.close();
	}
}
