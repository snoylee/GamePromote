package com.xygame.sg.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.txr.codec.binary.Base64;
import com.xygame.sg.imageloader.FileManager;

/**
 * 
 * 文件工具类
 * 
 */
public class FileUtil {
	private static final String TAG = "FileUtil";

	public static String RECORD_ROOT_PATH = FileManager.getSaveFilePath() + "xy_sg/chat/record/";

	public static final String CHAT_IMAGES_ROOT_PATH = FileManager.getSaveFilePath() + "xy_sg/chat/images/";

	public static final String APK_ROOT_PATH = FileManager.getSaveFilePath() + "xy_sg/chat/apk/";

	public static final String APK_ROOT_NAME = FileManager.getSaveFilePath() + "xy_sg/chat/apk/xy_le.apk";
	
	public static final String MP4_ROOT_PATH = FileManager.getSaveFilePath() + "downfile";

	/**
	 * 拷贝文件
	 * 
	 * @param fromFile
	 * @param toFile
	 * @throws IOException
	 */
	public static void copyFile(File fromFile, String toFile) throws IOException {

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = from.read(buffer)) != -1)
				to.write(buffer, 0, bytesRead); // write
		} finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
					Log.e(TAG, "", e);
				}
			if (to != null)
				try {
					to.close();
				} catch (IOException e) {
					Log.e(TAG, "", e);
				}
		}
	}

	/**
	 * 复制单个文件
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { //文件存在时
				InputStream inStream = new FileInputStream(oldPath); //读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ( (byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; //字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		}
		catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	// public static String encodeBase64File(String path) throws Exception {
	// File file = new File(path);
	// FileInputStream inputFile = new FileInputStream(file);
	// byte[] buffer = new byte[(int) file.length()];
	// inputFile.read(buffer);
	// inputFile.close();
	// return new String(Base64.encode(buffer));//new String(buffer);//(new
	// String(buffer).getBytes("utf-8")));
	// }
	//
	// public static String encodeBase64File(Bitmap bmp){
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	// return new String(Base64.encode(baos.toByteArray()));//(new
	// String(buffer).getBytes("utf-8")));
	// }

	public static String getImageByteSize(String path) {
		File file = new File(path);
		byte[] buffer = new byte[(int) file.length()];
		return String.valueOf(buffer.length);
	}

	/**
	 * 创建文件
	 * 
	 * @param file
	 * @return
	 */
	public static File createNewFile(File file) {

		try {

			if (file.exists()) {
				return file;
			}

			File dir = file.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			Log.e(TAG, "", e);
			return null;
		}
		return file;
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 */
	public static File createNewFile(String path) {
		File file = new File(path);
		return createNewFile(file);
	}// end method createText()

	/**
	 * 删除文件
	 * 
	 * @param path
	 */
	public static void deleteFile(String path) {
		File file = new File(path);
		deleteFile(file);
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}
		file.delete();
	}

	/**
	 * 向Text文件中写入内容
	 *
	 * @param content
	 * @return
	 */
	public static boolean write(String path, String content) {
		return write(path, content, false);
	}

	public static boolean write(String path, String content, boolean append) {
		return write(new File(path), content, append);
	}

	public static boolean write(File file, String content) {
		return write(file, content, false);
	}

	public static boolean write(File file, String content, boolean append) {
		if (file == null || StringUtil.empty(content)) {
			return false;
		}
		if (!file.exists()) {
			file = createNewFile(file);
		}
		FileOutputStream ops = null;
		try {
			ops = new FileOutputStream(file, append);
			ops.write(content.getBytes());
		} catch (Exception e) {
			Log.e(TAG, "", e);
			return false;
		} finally {
			try {
				ops.close();
			} catch (IOException e) {
				Log.e(TAG, "", e);
			}
			ops = null;
		}

		return true;
	}

	/**
	 * 获得文件名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		if (StringUtil.empty(path)) {
			return null;
		}
		File f = new File(path);
		String name = f.getName();
		f = null;
		return name;
	}

	/**
	 * 读取文件内容，从第startLine行开始，读取lineCount行
	 * 
	 * @param file
	 * @param startLine
	 * @param lineCount
	 * @return 读到文字的list,如果list.size<lineCount则说明读到文件末尾了
	 */
	public static List<String> readFile(File file, int startLine, int lineCount) {
		if (file == null || startLine < 1 || lineCount < 1) {
			return null;
		}
		if (!file.exists()) {
			return null;
		}
		FileReader fileReader = null;
		List<String> list = null;
		try {
			list = new ArrayList<String>();
			fileReader = new FileReader(file);
			LineNumberReader lnr = new LineNumberReader(fileReader);
			boolean end = false;
			for (int i = 1; i < startLine; i++) {
				if (lnr.readLine() == null) {
					end = true;
					break;
				}
			}
			if (end == false) {
				for (int i = startLine; i < startLine + lineCount; i++) {
					String line = lnr.readLine();
					if (line == null) {
						break;
					}
					list.add(line);

				}
			}
		} catch (Exception e) {
			Log.e(TAG, "read log error!", e);
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean createDir(File dir) {
		try {
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return true;
		} catch (Exception e) {
			Log.e(TAG, "create dir error", e);
			return false;
		}
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public static File creatSDDir(String dirName) {
		File dir = new File(dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件是否存在
	 */
	public static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public static File write2SDFromInput(String path, String fileName, InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = createNewFile(path + "/" + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[1024];
			int len = -1;
			while ((len = input.read(buffer)) != -1) {
				output.write(buffer, 0, len);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static String readFileByLines(String fileName) {
		StringBuffer fileStr = new StringBuffer();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			int line = 1;
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				fileStr.append(tempString);
				System.out.println("line " + line + ": " + tempString);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return fileStr.toString();
	}

	// 从文件中一行一行的读取文件
	public static String readFile(File file) {
		Reader read = null;
		String content = "";
		String string = "";
		BufferedReader br = null;
		try {
			read = new FileReader(file);
			br = new BufferedReader(read);
			while ((content = br.readLine().toString().trim()) != null) {
				string += content + "\r\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				read.close();
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("string=" + string);
		return string.toString();
	}

	/**
	 * 获取原图片存储路径
	 * 
	 * @return
	 */
	public static String getPhotopath(String imageName) {

		// 照片全路径
		String fileName = "";
		// 文件夹路径
		String pathUrl = CHAT_IMAGES_ROOT_PATH;
		File file = new File(pathUrl);
		if (!file.exists())
			file.mkdirs();// 创建文件夹
		fileName = CHAT_IMAGES_ROOT_PATH.concat(imageName);
		return fileName;
	}

	public static String getVidoPath(String vidoName) {

		// 照片全路径
		String fileName = "";
		// 文件夹路径
		String pathUrl = RECORD_ROOT_PATH;
		File file = new File(pathUrl);
		if (!file.exists())
			file.mkdirs();// 创建文件夹
		fileName = RECORD_ROOT_PATH.concat(vidoName);
		return fileName;
	}

	/**
	 * 根据路径获取图片资源（已缩放）
	 * 
	 * @param url
	 *            图片存储路径
	 * @param width
	 *            缩放的宽度
	 * @param height
	 *            缩放的高度
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String url, double width, double height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false
		Bitmap bitmap = BitmapFactory.decodeFile(url);
		// 防止OOM发生
		options.inJustDecodeBounds = false;
		int mWidth = bitmap.getWidth();
		int mHeight = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth;
		float scaleHeight;
		// try {
		// ExifInterface exif = new ExifInterface(url);
		// String model = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// 按照固定宽高进行缩放
		// 这里希望知道照片是横屏拍摄还是竖屏拍摄
		// 因为两种方式宽高不同，缩放效果就会不同
		// 这里用了比较笨的方式
		if (mWidth <= mHeight) {
			scaleWidth = (float) (width / mWidth);
			scaleHeight = (float) (height / mHeight);
		} else {
			scaleWidth = (float) (height / mWidth);
			scaleHeight = (float) (width / mHeight);
		}
		// matrix.postRotate(90); /* 翻转90度 */
		// 按照固定大小对图片进行缩放
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, mWidth, mHeight, matrix, true);
		// 用完了记得回收
		bitmap.recycle();
		return newBitmap;
	}

	public static void saveHighlyScalePhoto(String url, Bitmap bitmap) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(url);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getPhotoName(Context context) {
		return getDeviceUUID(context) + System.currentTimeMillis() + ".jpg";
	}

	public static String getDeviceUUID(Context context) {
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, tmPhone, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		return uniqueId;
	}

	/**
	 * 存储缩放的图片
	 * 
	 * @param data
	 *            图片数据
	 */
	public static void saveScalePhoto(String url, Bitmap bitmap) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(url);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 对图片进行等比例压缩
	 * 
	 * @param url
	 */
	public static Bitmap compressImages(String url) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(url, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateInSampleSize(options, 320, 480);

		bitmap = BitmapFactory.decodeFile(url, options);

		int angle = readPictureDegree(url);

		if (bitmap != null) {
			Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / Constants.FIX_SCALE,
					bitmap.getHeight() / Constants.FIX_SCALE);
			// 释放原始图片占用的内存，防止out of memory异常发生
			// bitmap.recycle();
			if (angle != 0) {
				smallBitmap = rotaingImageView(angle, smallBitmap);
			}
			bitmap = null;
			return smallBitmap;// ImageTools.compressImage();
		} else {
			return null;
		}

	}

	public static Bitmap getPhotoByPath(String url) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(url, options);

		options.inSampleSize = calculateInSampleSize(options, 960, 1080);
		options.inJustDecodeBounds = false;

		bitmap = BitmapFactory.decodeFile(url, options);

		int angle = readPictureDegree(url);

		if (bitmap != null) {
			if (angle != 0) {
				bitmap = rotaingImageView(angle, bitmap);
			}
			return bitmap;// ImageTools.compressImage();
		} else {
			return null;
		}

	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @date 2015年8月20日下午9:10:00
	 * @param path
	 *            图片的绝对路径
	 * @return 旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			default:
				break;
			}
		} catch (IOException e) {
		}
		return degree;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @date 2015年8月20日下午10:01:11
	 * @param context
	 * @param photoUri
	 * @return
	 */
	public static int getOrientation(Context context, Uri photoUri) {
		int orientation = 0;
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);
		if (cursor != null) {
			if (cursor.getCount() != 1) {
				return -1;
			}
			cursor.moveToFirst();
			orientation = cursor.getInt(0);
			cursor.close();
		}
		return orientation;
	}

	/**
	 * 旋转
	 * 
	 * @date 2015年8月20日下午9:10:34
	 * @param agnle
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotaingImageView(int agnle, Bitmap bitmap) {
		Matrix matrix = new Matrix();

		matrix.postRotate(agnle);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap.recycle();
		bitmap = null;
		return resizedBitmap;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	/**
	 * 对图片进行等比例压缩
	 *
	 */
	public static Bitmap compressImages(Bitmap bitmap) {

		Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / Constants.FIX_SCALE,
				bitmap.getHeight() / Constants.FIX_SCALE);
		// 释放原始图片占用的内存，防止out of memory异常发生
		// bitmap.recycle();
		return smallBitmap;
	}

	public static int copy(String fromFile, String toFile)
	{
		//要复制的文件目录
		File[] currentFiles;
		File root = new File(fromFile);
		//如同判断SD卡是否存在或者文件是否存在
		//如果不存在则 return出去
		if(!root.exists())
		{
			return -1;
		}
		//如果存在则获取当前目录下的全部文件 填充数组
		currentFiles = root.listFiles();

		//目标目录
		File targetDir = new File(toFile);
		//创建目录
		if(!targetDir.exists())
		{
			targetDir.mkdirs();
		}
		//遍历要复制该目录下的全部文件
		for(int i= 0;i<currentFiles.length;i++)
		{
			if(currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
			{
				copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

			}else//如果当前项为文件则进行文件拷贝
			{
				CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
			}
		}
		return 0;
	}


	//文件拷贝
	//要复制的目录下的所有非子目录(文件夹)文件拷贝
	public static int CopySdcardFile(String fromFile, String toFile)
	{

		try
		{
			//目标目录
			File targetDir = new File(FileUtil.RECORD_ROOT_PATH);
			//创建目录
			if(!targetDir.exists())
			{
				targetDir.mkdirs();
			}
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0)
			{
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return 0;

		} catch (Exception ex)
		{
			return -1;
		}
	}

	public static boolean ExistSDCard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static byte[] doDecode(byte[] image, String skey) {
		int len = image.length < 1000 ? image.length : 1000;
		for (int i = 0, j = 0; i < len; i++, j++) {
			if (j == skey.length())
				j = 0;
			image[i] = (byte) (image[i] ^ skey.charAt(j));
		}
		return Base64.encodeBase64(image);
	}

	public static byte[] decode(byte[] image, String skey) {
		byte[] db = Base64.decodeBase64(image);
		int len = db.length;
		len = len < 1000 ? len : 1000;
		int sl = skey.length();
		for (int i = 0, j = 0; i < len; i++, j++) {
			if (j == sl)
				j = 0;
			db[i] = (byte) (db[i] ^ skey.charAt(j));
		}
		return db;
	}

	public static String getNewPhotoPath(String photoPath){
		String[] array=photoPath.split("/");
		String newPath=CHAT_IMAGES_ROOT_PATH.concat(array[array.length-1]);
		FileUtil.CopySdcardImageFile(photoPath,newPath);
		return newPath;
	}

	//文件拷贝
	//要复制的目录下的所有非子目录(文件夹)文件拷贝
	public static int CopySdcardImageFile(String fromFile, String toFile)
	{

		try
		{
			//目标目录
			File targetDir = new File(FileUtil.CHAT_IMAGES_ROOT_PATH);
			//创建目录
			if(!targetDir.exists())
			{
				targetDir.mkdirs();
			}
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0)
			{
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return 0;

		} catch (Exception ex)
		{
			return -1;
		}
	}
}