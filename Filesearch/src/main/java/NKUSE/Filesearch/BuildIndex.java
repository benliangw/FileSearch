package NKUSE.Filesearch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
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
import org.apache.tika.Tika;
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
				System.out.println("path="+path);
				System.out.println("content="+content);
				System.out.println("type="+type);
				
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
}
