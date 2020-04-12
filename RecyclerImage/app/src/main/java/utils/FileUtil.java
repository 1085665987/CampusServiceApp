package utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class FileUtil {
    public static String IMAGE_SAVE_PATH="/storage/emulated/0/xiaoyunxing";

    private static String TAG="FileUtil";

    public static File createFile () throws Exception{
        File dirs = new File(Environment.getExternalStorageDirectory()+"/xiaoyunxing");
        if (!dirs.exists()){
            dirs.mkdirs();
        }
        return dirs;
    }
    public static String getImageSavePath() throws Exception{
        String path=createFile().getAbsolutePath() + "/" + Calendar.getInstance().getTimeInMillis() + ".jpg";
        if (path==null){
            Log.e(TAG,"空路径");
        }
        Log.e(TAG,path);
        return path;
    }


    public static String getFilePath(File file) throws Exception{
        String path=file.getAbsolutePath();
        return path;
    }

    public static int doDeleteFile(File file){
        if (file.exists()){
            file.delete();
            if (!file.exists())
                Log.e(TAG,"文件已删除");
                return 1;
        }
        return 0;
    }

    /**
     * 获得指定文件的byte数组
     */
    private byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static File getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!(dir.exists()&&dir.isDirectory())){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+"/"+fileName);
            if (file.exists()){
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }
}
