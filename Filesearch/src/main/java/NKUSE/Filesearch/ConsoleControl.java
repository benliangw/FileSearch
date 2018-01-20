package NKUSE.Filesearch;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ConsoleControl
{
	public static void clear() throws AWTException
	{
		for(int i = 0; i < 30; i++)
			System.out.print("\n");  
	    System.out.flush(); 
		/*
		try
	    {
	        String os = System.getProperty("os.name");

	        if (os.contains("Windows"))
	        {
	            Runtime.getRuntime().exec("cls");
	        }
	        else
	        {
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (Exception exception)
	    {
	        //  Handle exception.
	    }
	    */
		/*
		Robot r = new Robot();
		r.mousePress(InputEvent.BUTTON3_MASK); // 按下鼠标右键
		r.mouseRelease(InputEvent.BUTTON3_MASK); // 释放鼠标右键
		r.keyPress(KeyEvent.VK_CONTROL); // 按下Ctrl键
		r.keyPress(KeyEvent.VK_R); // 按下R键
		r.keyRelease(KeyEvent.VK_R); // 释放R键
		r.keyRelease(KeyEvent.VK_CONTROL); // 释放Ctrl键
		r.delay(100);
		*/
	}
	
	public static void welcome()
	{
		System.out.println("欢迎使用查询系统，请选择对应序号");
		System.out.println("1:显示所有索引");
		System.out.println("2:建立新的索引");
		System.out.println("3:使用已有索引进行查询");
		System.out.println("4:清空所有索引");
		System.out.println("5:清除某个索引");
		System.out.println("6:退出系统");
	}
}
