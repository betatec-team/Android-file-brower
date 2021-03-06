package com.leon.lfilepickerlibrary.utils;


import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.leon.lfilepickerlibrary.model.ParamEntity;
import com.leon.lfilepickerlibrary.new_lfilepicker.R;
import com.leon.lfilepickerlibrary.type.TypeEnum;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dimorinny on 24.10.15.
 */
public class FileUtils {
    public static final ArrayList<String> list = new ArrayList();

    static {
        // Image 图片类型 0-5 6个
        list.add(TypeEnum.FILE_TYPE_JPEG.name());
        list.add(TypeEnum.FILE_TYPE_GIF.name());
        list.add(TypeEnum.FILE_TYPE_PNG.name());
        list.add(TypeEnum.FILE_TYPE_BMP.name());
        list.add(TypeEnum.FILE_TYPE_WBMP.name());
        list.add(TypeEnum.FILE_TYPE_JPG.name());
        // Audio 音频类型 6-12 7个
        list.add(TypeEnum.FILE_TYPE_MP3.name());
        list.add(TypeEnum.FILE_TYPE_M4A.name());
        list.add(TypeEnum.FILE_TYPE_WAV.name());
        list.add(TypeEnum.FILE_TYPE_AMR.name());
        list.add(TypeEnum.FILE_TYPE_AWB.name());
        list.add(TypeEnum.FILE_TYPE_WMA.name());
        list.add(TypeEnum.FILE_TYPE_OGG.name());
        // Video 视频类型 13-20 8个
        list.add(TypeEnum.FILE_TYPE_MP4.name());
        list.add(TypeEnum.FILE_TYPE_M4V.name());
        list.add(TypeEnum.FILE_TYPE_3GPP.name());
        list.add(TypeEnum.FILE_TYPE_3GPP2.name());
        list.add(TypeEnum.FILE_TYPE_WMV.name());
        list.add(TypeEnum.FILE_TYPE_M3U.name());
        list.add(TypeEnum.FILE_TYPE_PLS.name());
        list.add(TypeEnum.FILE_TYPE_WPL.name());
        // 文件类型 21-29 9个
        list.add(TypeEnum.FILE_TYPE_TXT.name());
        list.add(TypeEnum.FILE_TYPE_DOC.name());
        list.add(TypeEnum.FILE_TYPE_DOCX.name());
        list.add(TypeEnum.FILE_TYPE_JSON.name());
        list.add(TypeEnum.FILE_TYPE_XML.name());
        list.add(TypeEnum.FILE_TYPE_HTML.name());
        list.add(TypeEnum.FILE_TYPE_MID.name());
        list.add(TypeEnum.FILE_TYPE_SMF.name());
        list.add(TypeEnum.FILE_TYPE_IMY.name());

    }

    public static List<File> getFileListByDirPath(String path, FileFilter filter, String endPath) {
        File directory = new File(path);
        File[] files = directory.listFiles(filter);
        List<File> result = new ArrayList<>();
        if (files == null) {
            return new ArrayList<>();
        }
        //  进行文件区分
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                result.add(file);
            } else {
                if ("ALL".equalsIgnoreCase(endPath)) {
                    result.add(file);
                } else if ("IMAGE".equalsIgnoreCase(endPath)) {
                    if (getIsImageFile(file)) {
                        result.add(file);
                    }
                } else if ("VIDEO".equalsIgnoreCase(endPath)) {
                    if (getIsVideoFile(file)) {
                        result.add(file);
                    }
                } else if ("AUDIO".equalsIgnoreCase(endPath)) {
                    if (getIsAudioFile(file)) {
                        result.add(file);
                    }
                } else if ("TEXT".equalsIgnoreCase(endPath)) {
                    if (getIsTextFile(file)) {
                        result.add(file);
                    }
                } else {
                    if (endPath.equalsIgnoreCase(getFileEnd(file))) {
                        result.add(file);
                    }

                }
            }


        }
        Collections.sort(result, new FileComparator());
        return result;
    }

    public static String cutLastSegmentOfPath(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

    /**
     * 判断是否是图片类型
     *
     * @param file
     * @return
     */
    public static boolean getIsImageFile(File file) {
        String fileEnd = getFileEnd(file);
        if (null != fileEnd) {
            for (int i = 0; i < list.size(); i++) {
                // 0-5 表示是图片类型
                if (i < 6) {   //0-5
                    // 判断图片类型
                    if (fileEnd.equalsIgnoreCase(list.get(i).split("_")[2])) {
                        // 不然是图片
                        return true;
                    }
                }

            }
        }
        return false;

    }


    /**
     * 判断是否是音频类型
     *
     * @return
     */
    public static boolean getIsAudioFile(File file) {
        String fileEnd = getFileEnd(file);
        if (null != fileEnd) {
            for (int i = 0; i < list.size(); i++) {
                // 13- 表示是音频类型
                if (i > 6 && i < 13) {
                    // 判断音频类型
                    if (fileEnd.equalsIgnoreCase(list.get(i).split("_")[2])) {
                        // 不然是音频类型
                        return true;
                    }
                }

            }
        }
        return false;

    }

    /**
     * 判断是否是视频类型
     *
     * @return
     */
    public static boolean getIsVideoFile(File file) {
        String fileEnd = getFileEnd(file);
        if (null != fileEnd) {
            for (int i = 0; i < list.size(); i++) {
                // 7-13 表示是图片类型
                if (i > 13 && i < 20) {
                    // 判断视频类型
                    if (fileEnd.equalsIgnoreCase(list.get(i).split("_")[2])) {
                        // 不熬是是图片
                        return true;
                    }
                }

            }
        }
        return false;

    }

    /**
     * 判断是否是文件类型
     *
     * @return
     */
    public static boolean getIsTextFile(File file) {
        if (getIsImageFile(file) | getIsAudioFile(file) | getIsVideoFile(file)) {
            return false;
        }
        return true;
    }

    /**
     * 获取文件的结尾
     */

    public static String getFileEnd(File file) {
        String name = file.getName();
        String[] split = name.split("\\.");
        if (split.length == 1) {
            // 表示没有后缀名
            return null;
        } else if (split.length > 1) {
            return split[split.length - 1];
        } else {
            return null;
        }

    }

    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#").format(size / Math.pow(1024,
                digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 获取文件长度
     *
     * @param file 文件
     * @return 文件长度
     */
    public static long getFileLength(final File file) {
        if (!isFile(file)) return -1;
        return file.length();
    }

    /**
     * 判断是否是文件
     *
     * @param file 文件
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isFile(final File file) {
        return file != null && file.exists() && file.isFile();
    }

    /**
     * 根据地址获取当前地址下的所有目录和文件，并且排序,同时过滤掉不符合大小要求的文件
     *
     * @param path
     * @param endPath
     * @return List<File>
     */
    public static List<File> getFileList(String path, FileFilter filter, boolean isGreater,
                                         long targetSize, String endPath) {
        List<File> list = FileUtils.getFileListByDirPath(path, filter, endPath);
        //进行过滤文件大小
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            File f = (File) iterator.next();
            if (f.isFile()) {
                //获取当前文件大小
                long size = FileUtils.getFileLength(f);
                if (isGreater) {
                    //当前想要留下大于指定大小的文件，所以过滤掉小于指定大小的文件
                    if (size < targetSize) {
                        iterator.remove();
                    }
                } else {
                    //当前想要留下小于指定大小的文件，所以过滤掉大于指定大小的文件
                    if (size > targetSize) {
                        iterator.remove();
                    }
                }
            }
        }
        return list;
    }

    /**
     * 创建文件/文件夹
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void createFile(Context context, String path, String name, int types,
                                  Locale locacalLanguage, ParamEntity mParamEntity) {
        Resources resource = getRes(context, locacalLanguage);
        File file = new File(path, name);
        if (1 == types) {
            // 判断文件是否存在
            if (file.exists()) {
                if (!mParamEntity.isHideToast()) {
                    Toast.makeText(context, resource.getString(R.string.create_exit),
                            Toast.LENGTH_LONG).show();
                }

            } else {
                try {
                    file.createNewFile();
                    if (!mParamEntity.isHideToast()) {
                        Toast.makeText(context, resource.getString(R.string.create_success), Toast.LENGTH_LONG).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    if (!mParamEntity.isHideToast()) {
                        Toast.makeText(context, resource.getString(R.string.create_fails), Toast.LENGTH_LONG).show();
                    }

                }
            }
        } else {
            file.mkdir();
            if (!mParamEntity.isHideToast()) {
                Toast.makeText(context, file.exists() ? resource.getString(R.string.create_box_exit) :
                        resource.getString(file.mkdirs() ? R.string.create_success :
                                R.string.create_fails), Toast.LENGTH_LONG).show();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Resources getRes(Context context, Locale locacalLanguage) {
        Resources rescouse = context.getResources();
//        if (locacalLanguage != null) {
//            Configuration config = rescouse.getConfiguration();
//            DisplayMetrics dm = rescouse.getDisplayMetrics();
//            config.setLocale(Locale.CHINESE);
//            rescouse.updateConfiguration(config, dm);
//        }
        return rescouse;
    }

    /**
     * 重新命名文件
     */
    public static void reName(File oldFile, File newFile) {
        oldFile.renameTo(newFile);
    }
    /**
     * 复制/移动 文件/文件夹
     */

    /**
     * 复制文件夹
     *  @param resources       源路径
     * @param target          目标路径
     * @param locacalLanguage
     * @param mParamEntity
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void copyFolder(Context context, String resources, String target, Locale locacalLanguage, ParamEntity mParamEntity)
            throws Exception {
        Resources resource = getRes(context, locacalLanguage);
        File resourceFile = new File(resources);
        if (!resourceFile.exists()) throw new Exception("源目标路径：[" + resources + "] 不存在...");
        File targetFile = new File(target);
//        targetFile.createNewFile();
        if (!targetFile.exists()) throw new Exception("存放的目标路径：[" + target + "] 不存在...");

        // 获取源文件夹下的文件夹或文件
        File[] resourceFiles = resourceFile.listFiles();

        if (resourceFiles != null && resourceFiles.length > 0) {
            for (File file : resourceFiles) {

                File file1 = new File(targetFile.getAbsolutePath() + File.separator +
                        resourceFile.getName());
                // 复制文件
                if (file.isFile()) {
                    System.out.println("文件" + file.getName());
                    // 在 目标文件夹（B） 中 新建 源文件夹（A），然后将文件复制到 A 中
                    // 这样 在 B 中 就存在 A
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    File targetFile1 = new File(file1.getAbsolutePath() +
                            File.separator + file.getName());
                    copyFile(file, targetFile1);
                } else if (file.isDirectory()) {// 复制源文件夹
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    // 复制文件夹
                    String dir1 = file.getAbsolutePath();
                    // 目的文件夹
                    String dir2 = file1.getAbsolutePath();
                    copyFolder(context, dir1, dir2,locacalLanguage,mParamEntity);
                }
            }
        } else {
            File file = new File(target, resourceFile.getName());
            if (file.exists()) {
                if (!mParamEntity.isHideToast()) {
                    Toast.makeText(context, resource.getString(R.string.copy_current),
                            Toast.LENGTH_LONG).show();
                }
            } else {
                file.mkdirs();
            }

        }


    }

    /**
     * 复制文件
     *
     * @param resource 要复制的文件的路径
     * @param target   要复制的目标
     */
    public static void copyFile(File resource, File target) throws Exception {
        // 输入流 --> 从一个目标读取数据
        // 输出流 --> 向一个目标写入数据
        long start = System.currentTimeMillis();
        // 文件输入流并进行缓冲
        FileInputStream inputStream = new FileInputStream(resource);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        // 文件输出流并进行缓冲
        FileOutputStream outputStream = new FileOutputStream(target);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        // 缓冲数组
        // 大文件 可将 1024 * 2 改大一些，但是 并不是越大就越快
        byte[] bytes = new byte[1024 * 2];
        int len;
        while ((len = inputStream.read(bytes)) != -1) bufferedOutputStream.write(bytes, 0, len);
        // 刷新输出缓冲流
        bufferedOutputStream.flush();
        //关闭流
        bufferedInputStream.close();
        bufferedOutputStream.close();
        inputStream.close();
        outputStream.close();
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start) / 1000 + " s");

    }

    /**
     * 删除文件
     */
    public static void deleteDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.isFile()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File value : files) {
                    deleteDir(value.getAbsolutePath());
                }
            }
        }
        file.delete();
    }
}
