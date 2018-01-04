package NKUSE.Filesearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

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
import org.wltea.analyzer.lucene.IKAnalyzer;

public class BuildIndex
{
	private static ArrayList<String> listname = new ArrayList<String>();

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

	public static void createIndex(String rootdir, String index_path) throws FileNotFoundException
	{
		IndexWriter indexWriter = null;
		try
		{
			Directory directory = FSDirectory.open(new File(index_path));
			Analyzer analyzer = new IKAnalyzer();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_45, analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			indexWriter.deleteAll();// 清除以前的index
			dealwithallfile(rootdir);
			for (int i = 0; i < listname.size(); i++)
			{
				File file = new File(listname.get(i));
				byte[] file_byte = PreFile.getBytesFromFile(file);
				Document document = new Document();
				if (file.getName().toLowerCase().endsWith(".txt") || file.getName().toLowerCase().endsWith(".c")
						|| file.getName().toLowerCase().endsWith(".h") || file.getName().toLowerCase().endsWith(".S")
						|| file.getName().toLowerCase().endsWith(".cpp")
						|| file.getName().toLowerCase().endsWith(".hpp"))
				{
					System.out.println("filepath=" + file.getAbsolutePath());
					String path = file.getAbsolutePath();
					String name = file.getName();
					String content = PreFile.getTextFormTxt(file_byte);//支持各种编码
					System.out.println("content="+content);
					Field fileName = new Field("fileName", name, TextField.TYPE_STORED);
					Field filePath = new Field("filePath", path, TextField.TYPE_STORED);
					Field fileContent = new Field("fileContent", content, TextField.TYPE_STORED);
					Field fileType = new Field("fileType", "txt", TextField.TYPE_STORED);
					document.add(fileName);
					document.add(fileContent);
					document.add(filePath);
					document.add(fileType);
				}
				else if(file.getName().toLowerCase().endsWith(".ppt") || file.getName().toLowerCase().endsWith(".pptx"))
				{
					
				}
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
}
