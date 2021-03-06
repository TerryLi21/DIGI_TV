package com.linkin.mtv.util;

/**
 * @desc 文件相关工具类
 * 
 * @author cjl
 * @since 2013-9-14
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

/**
 * File Utils
 * <ul>
 * Read or write file
 * <li>{@link #readFile(String)} read file</li>
 * <li>{@link #readFileToList(String)} read file to string list</li>
 * <li>{@link #writeFile(String, String, boolean)} write file</li>
 * <li>{@link #writeFile(String, InputStream)} write file</li>
 * </ul>
 * <ul>
 * Operate file
 * <li>{@link #getFileExtension(String)}</li>
 * <li>{@link #getFileName(String)}</li>
 * <li>{@link #getFileNameWithoutExtension(String)}</li>
 * <li>{@link #getFileSize(String)}</li>
 * <li>{@link #deleteFile(String)}</li>
 * <li>{@link #isFileExist(String)}</li>
 * <li>{@link #isFolderExist(String)}</li>
 * <li>{@link #makeFolders(String)}</li>
 * <li>{@link #makeDirs(String)}</li>
 * </ul>
 * 
 * @author Trinea 2012-5-12
 */
public class FileUtil {

	public final static String FILE_EXTENSION_SEPARATOR = ".";

	/**
	 * 单位换算
	 * 
	 * @param fileSize
	 * @return
	 */
	public static String getSizeStr(long fileSize) {
		if (fileSize <= 0) {
			return "0M";
		}
		float result = fileSize;
		String suffix = "M";
		result = result / 1024 / 1024;
		return String.format("%.1f", result) + suffix;
	}

	/**
	 * read file
	 * 
	 * @param filePath
	 * @return if file not exist, return null, else return content of file
	 * @throws IOException
	 *             if an error occurs while operator BufferedReader
	 */
	public static StringBuilder readFile(String filePath) {
		File file = new File(filePath);
		StringBuilder fileContent = new StringBuilder("");
		if (file == null || !file.isFile()) {
			return null;
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (!fileContent.toString().equals("")) {
					fileContent.append("\r\n");
				}
				fileContent.append(line);
			}
			reader.close();
			return fileContent;
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}

	/**
	 * write file
	 * 
	 * @param filePath
	 * @param content
	 * @param append
	 *            is append, if true, write to the end of file, else clear
	 *            content of file and write into it
	 * @return return true
	 * @throws IOException
	 *             if an error occurs while operator FileWriter
	 */
	public static boolean writeFile(String filePath, String content,
			boolean append) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filePath, append);
			fileWriter.write(content);
			fileWriter.close();
			return true;
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}

	/**
	 * write file
	 * 
	 * @param filePath
	 * @param stream
	 * @return return true
	 * @throws IOException
	 *             if an error occurs while operator FileWriter
	 */
	public static boolean writeFile(String filePath, InputStream stream) {
		OutputStream o = null;
		try {
			o = new FileOutputStream(filePath);
			byte data[] = new byte[1024];
			int length = -1;
			while ((length = stream.read(data)) != -1) {
				o.write(data, 0, length);
			}
			o.flush();
			return true;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FileNotFoundException occurred. ", e);
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			if (o != null) {
				try {
					o.close();
					stream.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}

	/**
	 * read file to string list, a element of list is a line
	 * 
	 * @param filePath
	 * @return if file not exist, return null, else return content of file
	 * @throws IOException
	 *             if an error occurs while operator BufferedReader
	 */
	public static List<String> readFileToList(String filePath) {
		File file = new File(filePath);
		List<String> fileContent = new ArrayList<String>();
		if (file == null || !file.isFile()) {
			return null;
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				fileContent.add(line);
			}
			reader.close();
			return fileContent;
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}

	public static boolean writeListToFile(String filePath, List<String> list) {
		if (list == null || list.size() == 0) {
			return false;
		}
		FileWriter fileWriter = null;
		try {
			StringBuilder builder = new StringBuilder();
			int size = list.size();
			for (int i = 0; i < size; i++) {
				builder.append(list.get(i));
				if (i + 1 < size) {
					builder.append("\n");
				}
			}
			fileWriter = new FileWriter(filePath, false);
			fileWriter.write(builder.toString());
			fileWriter.close();
			return true;
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}

	/**
	 * get file name from path, not include suffix
	 * 
	 * <pre>
	 *      getFileNameWithoutExtension(null)               =   null
	 *      getFileNameWithoutExtension("")                 =   ""
	 *      getFileNameWithoutExtension("   ")              =   "   "
	 *      getFileNameWithoutExtension("abc")              =   "abc"
	 *      getFileNameWithoutExtension("a.mp3")            =   "a"
	 *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
	 *      getFileNameWithoutExtension("c:\\")              =   ""
	 *      getFileNameWithoutExtension("c:\\a")             =   "a"
	 *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
	 *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
	 *      getFileNameWithoutExtension("/home/admin")      =   "admin"
	 *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
	 * </pre>
	 * 
	 * @param filePath
	 * @return file name from path, not include suffix
	 * @see
	 */
	public static String getFileNameWithoutExtension(String filePath) {
		if (StringUtil.isEmpty(filePath)) {
			return filePath;
		}

		int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
		int filePosi = filePath.lastIndexOf(File.separator);
		if (filePosi == -1) {
			return (extenPosi == -1 ? filePath : filePath.substring(0,
					extenPosi));
		}
		if (extenPosi == -1) {
			return filePath.substring(filePosi + 1);
		}
		return (filePosi < extenPosi ? filePath.substring(filePosi + 1,
				extenPosi) : filePath.substring(filePosi + 1));
	}

	/**
	 * get file name from path, include suffix
	 * 
	 * <pre>
	 *      getFileName(null)               =   null
	 *      getFileName("")                 =   ""
	 *      getFileName("   ")              =   "   "
	 *      getFileName("a.mp3")            =   "a.mp3"
	 *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
	 *      getFileName("abc")              =   "abc"
	 *      getFileName("c:\\")              =   ""
	 *      getFileName("c:\\a")             =   "a"
	 *      getFileName("c:\\a.b")           =   "a.b"
	 *      getFileName("c:a.txt\\a")        =   "a"
	 *      getFileName("/home/admin")      =   "admin"
	 *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
	 * </pre>
	 * 
	 * @param filePath
	 * @return file name from path, include suffix
	 */
	public static String getFileName(String filePath) {
		if (StringUtil.isEmpty(filePath)) {
			return filePath;
		}

		int filePosi = filePath.lastIndexOf(File.separator);
		return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
	}

	/**
	 * get folder name from path
	 * 
	 * <pre>
	 *      getFolderName(null)               =   null
	 *      getFolderName("")                 =   ""
	 *      getFolderName("   ")              =   ""
	 *      getFolderName("a.mp3")            =   ""
	 *      getFolderName("a.b.rmvb")         =   ""
	 *      getFolderName("abc")              =   ""
	 *      getFolderName("c:\\")              =   "c:"
	 *      getFolderName("c:\\a")             =   "c:"
	 *      getFolderName("c:\\a.b")           =   "c:"
	 *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
	 *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
	 *      getFolderName("/home/admin")      =   "/home"
	 *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
	 * </pre>
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFolderName(String filePath) {

		if (StringUtil.isEmpty(filePath)) {
			return filePath;
		}

		int filePosi = filePath.lastIndexOf(File.separator);
		return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
	}

	/**
	 * get suffix of file from path
	 * 
	 * <pre>
	 *      getFileExtension(null)               =   ""
	 *      getFileExtension("")                 =   ""
	 *      getFileExtension("   ")              =   "   "
	 *      getFileExtension("a.mp3")            =   "mp3"
	 *      getFileExtension("a.b.rmvb")         =   "rmvb"
	 *      getFileExtension("abc")              =   ""
	 *      getFileExtension("c:\\")              =   ""
	 *      getFileExtension("c:\\a")             =   ""
	 *      getFileExtension("c:\\a.b")           =   "b"
	 *      getFileExtension("c:a.txt\\a")        =   ""
	 *      getFileExtension("/home/admin")      =   ""
	 *      getFileExtension("/home/admin/a.txt/b")  =   ""
	 *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
	 * </pre>
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileExtension(String filePath) {
		if (StringUtil.isBlank(filePath)) {
			return filePath;
		}

		int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
		int filePosi = filePath.lastIndexOf(File.separator);
		if (extenPosi == -1) {
			return "";
		}
		return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
	}

	/**
	 * Creates the directory named by the trailing filename of this file,
	 * including the complete directory path required to create this directory. <br/>
	 * <br/>
	 * <ul>
	 * <strong>Attentions：</strong>
	 * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
	 * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
	 * </ul>
	 * 
	 * @param filePath
	 * @return true if the necessary directories have been created or the target
	 *         directory already exists, false one of the directories can not be
	 *         created.
	 *         <ul>
	 *         <li>if {@link FileUtil#getFolderName(String)} return null, return
	 *         false</li>
	 *         <li>if target directory already exists, return true</li>
	 *         <li>return {@link java.io.File#makeFolder}</li>
	 *         </ul>
	 */
	public static boolean createFolder(String filePath, boolean recreate) {
		String folderName = getFolderName(filePath);
		if (StringUtil.isEmpty(folderName)) {
			return false;
		}

		File folder = new File(folderName);
		if (folder.exists()) {
			if (recreate) {
				deleteFile(folderName);
				return folder.mkdirs();
			} else {
				return true;
			}
		} else {
			return folder.mkdirs();
		}
	}

	/**
	 * @param filePath
	 * @return
	 * @see {@link #makeDirs(String)}
	 */
	public static boolean createFolder(String filePath) {
		return createFolder(filePath, false);
	}

	/**
	 * Indicates if this file represents a file on the underlying file system.
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		if (StringUtil.isBlank(filePath)) {
			return false;
		}

		File file = new File(filePath);
		return (file.exists() && file.isFile());
	}

	/**
	 * Indicates if this file represents a directory on the underlying file
	 * system.
	 * 
	 * @param directoryPath
	 * @return
	 */
	public static boolean isFolderExist(String directoryPath) {
		if (StringUtil.isBlank(directoryPath)) {
			return false;
		}

		File dire = new File(directoryPath);
		return (dire.exists() && dire.isDirectory());
	}

	/**
	 * delete file or directory
	 * <ul>
	 * <li>if path is null or empty, return true</li>
	 * <li>if path not exist, return true</li>
	 * <li>if path exist, delete recursion. return true</li>
	 * <ul>
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		if (StringUtil.isBlank(path)) {
			return true;
		}

		File file = new File(path);
		if (!file.exists()) {
			return true;
		}
		if (file.isFile()) {
			return file.delete();
		}
		if (!file.isDirectory()) {
			return false;
		}
		for (File f : file.listFiles()) {
			if (f.isFile()) {
				f.delete();
			} else if (f.isDirectory()) {
				deleteFile(f.getAbsolutePath());
			}
		}
		return file.delete();
	}

	/**
	 * get file size
	 * <ul>
	 * <li>if path is null or empty, return -1</li>
	 * <li>if path exist and it is a file, return file size, else return -1</li>
	 * <ul>
	 * 
	 * @param path
	 * @return
	 */
	public static long getFileSize(String path) {
		if (StringUtil.isBlank(path)) {
			return -1;
		}

		File file = new File(path);
		return (file.exists() && file.isFile() ? file.length() : -1);
	}

	/**
	 * get dir size
	 * 
	 * @param path
	 * @return
	 */
	@SuppressLint("NewApi")
	public static long getDirSize(String path) {

		if (StringUtil.isBlank(path)) {
			return -1;
		}

		long size = 0;

		File file = new File(path);
		if (file.exists()) {
			size = file.getUsableSpace();
			file = null;
		} else {
			size = -1;
		}

		return size;
	}

	/**
	 * 重命名文件/文件夹
	 * 
	 * @param path
	 * @param newName
	 */
	public static boolean rename(final String path, final String newName) {
		boolean result = false;
		if (TextUtils.isEmpty(path) || TextUtils.isEmpty(newName)) {
			return result;
		}
		try {
			File file = new File(path);
			if (file.exists()) {
				result = file.renameTo(new File(newName));
			}
		} catch (Exception e) {
		}

		return result;
	}

	/**
	 * 读取文件
	 * 
	 * @param filePath
	 * @return 输入流
	 */
	public static InputStream readFileInputStream(String filePath) {
		if (filePath == null || filePath.length() == 0) {
			return null;
		}

		InputStream is = null;
		try {
			if (isFileExist(filePath)) {
				File f = new File(filePath);
				is = new FileInputStream(f);
			} else {
				return null;
			}
		} catch (Exception ex) {
			return null;
		}
		return is;
	}

	/**
	 * 创建一个空的文件
	 * 
	 * @param filePath
	 * @param recreate
	 *            是否删除重建
	 * @return
	 */
	public static boolean createFile(String filePath, boolean recreate) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}
		try {
			File file = new File(filePath);
			if (file.exists()) {
				if (recreate) {
					file.delete();
					file.createNewFile();
				}
			} else {
				// 如果路径不存在，先创建路径
				File parentFile = file.getParentFile();
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
				file.createNewFile();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 将数字大小转换成“MB"、“KB”、"GB"格式
	 * 
	 * @param size
	 * @return
	 */
	public static String getSize(long size) {
		if (size < 0)
			return null;

		String result = null;
		if (size > 1024 * 1024 * 1024) {
			float f = (float) size / (1024 * 1024 * 1024);
			String s = String.valueOf(f);
			if (s.length() - s.indexOf(".") > 2)
				result = s.substring(0, s.indexOf(".") + 3);
			else
				result = s;
			return result + "GB";
		} else if (size > 1024 * 1024) {
			float f = (float) size / (1024 * 1024);
			String s = String.valueOf(f);
			if (s.length() - s.indexOf(".") > 2)
				result = s.substring(0, s.indexOf(".") + 3);
			else
				result = s;
			return result + "MB";
		} else if (size > 1024) {
			float f = (float) size / 1024;
			String s = String.valueOf(f);
			if (s.length() - s.indexOf(".") > 2)
				result = s.substring(0, s.indexOf(".") + 3);
			else
				result = s;
			return result + "KB";
		} else if (size < 1024) {
			return String.valueOf(size) + "B";
		} else
			return null;
	}

	/**
	 * 将数字大小转换成“MB"、“KB”、"GB"格式
	 * 
	 * @param size
	 * @return
	 */
	public static String getSize(int size) {
		if (size < 0)
			return null;

		String result = null;
		if (size > 1024 * 1024 * 1024) {
			float f = (float) size / (1024 * 1024 * 1024);
			String s = String.valueOf(f);
			if (s.length() - s.indexOf(".") > 2)
				result = s.substring(0, s.indexOf(".") + 3);
			return result + "GB";
		} else if (size > 1024 * 1024) {
			float f = (float) size / (1024 * 1024);
			String s = String.valueOf(f);
			if (s.length() - s.indexOf(".") > 2)
				result = s.substring(0, s.indexOf(".") + 3);
			return result + "MB";
		} else if (size > 1024) {
			float f = (float) size / 1024;
			String s = String.valueOf(f);
			if (s.length() - s.indexOf(".") > 2)
				result = s.substring(0, s.indexOf(".") + 3);
			return result + "KB";
		} else if (size < 1024) {
			return String.valueOf(size) + "B";
		} else
			return null;
	}

	/**
	 * 如果是文件夹就删除文件下所有文件，然后删除文件夹，如果是文件就直接删除文件
	 * 
	 * @param filepath
	 * @throws IOException
	 */
	public static long del(String filepath) {

		if (filepath == null)
			return -1;

		long total = 0;
		try {

			File f = new File(filepath);// 定义文件路径
			if (!f.exists())
				return -1;
			if (f.isDirectory()) {// 目录
				int i = f.listFiles().length;
				if (i > 0) {
					File delFile[] = f.listFiles();
					for (int j = 0; j < i; j++) {
						if (delFile[j].isDirectory()) {
							// 递归调用del方法并取得子目录路径
							total = total + del(delFile[j].getAbsolutePath());
						}
						total += delFile[j].length();
						delFile[j].delete();// 删除文件
					}
				}
				f.delete();
			} else
				total += f.length();
			if (f.exists())
				f.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}

	/**
	 * write the data/data/packname/file
	 * 
	 * @param fileName
	 * @param writestr
	 * @param context
	 * @param return true
	 * @throws IOException
	 *             if an error occurs while operator FileWriter
	 */
	public static boolean writeDataFile(String fileName, String writestr,
			Context context) {
		FileOutputStream fout = null;
		try {
			fout = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			byte[] bytes = writestr.getBytes();
			fout.write(bytes);
			fout.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * read the data/data/packname/file
	 * 
	 * @param fileName
	 * @param context
	 * @return
	 * @throws IOException
	 */
	public static String readDataFile(String fileName, Context context) {
		String res = null;
		FileInputStream fin = null;
		try {
			fin = context.openFileInput(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}
}
