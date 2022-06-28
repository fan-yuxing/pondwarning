package edu.cau.cn.pondwarning.util;

//HlsDownloader类
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析m3u8视频连接，保存视频
 */
// https://my.oschina.net/haitaohu/blog/1941179
public class HlsDownloader {

    private String uuid;
    private String originUrlpath;
    private String preUrlPath;
    private String rootPath;
    private String fileName;
    private String folderPath;
    private int threadQuantity = 10;

    public int getThreadQuantity() {
        return threadQuantity;
    }

    public void setThreadQuantity(int threadQuantity) {
        this.threadQuantity = threadQuantity;
    }


    public HlsDownloader(String originUrlpath, String preUrlPath, String rootPath) {
        this.uuid = UUID.randomUUID().toString().replaceAll("-", "");
        this.originUrlpath = originUrlpath;
        this.preUrlPath = preUrlPath;
        this.rootPath = rootPath;

        this.fileName = uuid + ".mp4";

        this.folderPath = rootPath + File.separator + uuid;
        File file = new File(folderPath);
        if (!file.exists()) file.mkdirs();
    }

    public List download1() throws Exception {

        String indexStr = getIndexFile();

        List urlList = analysisIndex(indexStr);

        return urlList;
    }

    public String download(boolean isAsync) throws Exception {

        String indexStr = getIndexFile();

        List urlList = analysisIndex(indexStr);

        HashMap<Integer, String> keyFileMap = new HashMap<>();

        if (isAsync) {
            downLoadIndexFileAsync(urlList, keyFileMap);

            while (keyFileMap.size() < urlList.size()) {
                //System.out.println("当前下载数量"+keyFileMap.size());
                Thread.sleep(3000);
            }
        } else {
            keyFileMap = downLoadIndexFile(urlList);
        }

        return composeFile(keyFileMap);
    }

    /* 下载索引文件 */
    public String getIndexFile() throws Exception {
        URL url = new URL(originUrlpath);
        String content = "";

        for (int i=0;i<1;i++){   //这里的循环是当下载直播流的时候，一次下载只能有一个ts文件，这里我循环了四次，下载四个ts文件，在合成视频。
            //下载资源
            try
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                System.err.println(in.readLine());
                String line;
                while ((line = in.readLine()) != null) {
                    content += line + "\n";
                }
                in.close();
            }catch (Exception e){
                System.err.println("错误为：");
                System.err.println(e);
            }

        }
        return content;
    }

    /* 解析索引文件 */
    public List analysisIndex(String content) throws Exception {
        Pattern pattern = Pattern.compile(".*ts");
        Matcher ma = pattern.matcher(content);

        List<String> list = new ArrayList<String>();

        while (ma.find()) {
            list.add(ma.group());
        }

        return list;
    }

    /* 下载视频片段 */
    public HashMap downLoadIndexFile(List<String> urlList) {
        HashMap<Integer, String> keyFileMap = new HashMap<>();

        for (int i = 0; i < urlList.size(); i++) {
            String subUrlPath = urlList.get(i);
            String fileOutPath = folderPath + File.separator + i + ".ts";
            keyFileMap.put(i, fileOutPath);
            try {
                downloadNet(preUrlPath + subUrlPath, fileOutPath);

                System.out.println("成功：" + (i + 1) + "/" + urlList.size());
            } catch (Exception e) {
                System.err.println("失败：" + (i + 1) + "/" + urlList.size());
            }
        }

        return keyFileMap;
    }

    public void downLoadIndexFileAsync(List<String> urlList, HashMap<Integer, String> keyFileMap) throws Exception {
        int downloadForEveryThread = (urlList.size() + threadQuantity - 1) / threadQuantity;
        if (downloadForEveryThread == 0) downloadForEveryThread = urlList.size();

        for (int i = 0; i < urlList.size(); i += downloadForEveryThread) {
            int startIndex = i;
            int endIndex = i + downloadForEveryThread - 1;
            if (endIndex >= urlList.size())
                endIndex = urlList.size() - 1;

            new DownloadThread(urlList, startIndex, endIndex, keyFileMap).start();
        }
    }

    /**
     * 视频片段合成
     */
    public String composeFile(HashMap<Integer, String> keyFileMap) throws Exception {

        if (keyFileMap.isEmpty()) return null;

        String fileOutPath = rootPath + File.separator + fileName;
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileOutPath));
        byte[] bytes = new byte[1024];
        int length = 0;
        for (int i = 0; i < keyFileMap.size(); i++) {
            String nodePath = keyFileMap.get(i);
            File file = new File(nodePath);
            if (!file.exists()) continue;

            FileInputStream fis = new FileInputStream(file);
            while ((length = fis.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }
        }

        return fileName;
    }


    class DownloadThread extends Thread {
        private List<String> urlList;
        private int startIndex;
        private int endIndex;
        private HashMap<Integer, String> keyFileMap;

        public DownloadThread(List<String> urlList, int startIndex, int endIndex, HashMap<Integer, String> keyFileMap) {
            this.urlList = urlList;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.keyFileMap = keyFileMap;
        }

        @Override
        public void run() {
            for (int i = startIndex; i <= endIndex; i++) {
                String subUrlPath = urlList.get(i);
                String fileOutPath = folderPath + File.separator + i + ".ts";
                keyFileMap.put(i, fileOutPath);
//                String message = "%s: 线程 " + (startIndex / (endIndex - startIndex) + 1)
//                        + ", " + (i + 1) + "/" + urlList.size() + ", 合计: %d";
                try {
                    downloadNet(preUrlPath + subUrlPath, fileOutPath);

                    System.out.println(String.format("message", "成功", keyFileMap.size()));
                } catch (Exception e) {
                    System.err.println(String.format("message", "失败", keyFileMap.size()));
                }
            }
        }
    }

    private void downloadNet(String fullUrlPath, String fileOutPath) throws Exception {
        System.err.println(fullUrlPath);
        // 下载网络文件
        int byteread = 0;

        URL url = new URL(fullUrlPath);
        URLConnection conn = url.openConnection();
        InputStream inStream = conn.getInputStream();
        FileOutputStream fs = new FileOutputStream(fileOutPath);

        byte[] buffer = new byte[1204];
        while ((byteread = inStream.read(buffer)) != -1) {
            //bytesum += byteread;
            fs.write(buffer, 0, byteread);
        }
    }

    /**
     * 以下所有方法是视频截图工具
     */
    //getTempPath方法，这个是视频下载成功后，进行帧的图片截取
    public static String getTempPath(String filePath, String fileName) throws Exception {
        System.out.println(fileName);
        String[] split = fileName.split("\\.");
        System.out.println(split.length);
        String oName=split[0];
        System.out.println(oName);
        String sName=split[1];
//        String tempPath="    ";//保存的目标路径
        File targetFile = new File(filePath+"/"+oName+".jpg");
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        File file2 = new File(filePath+"/"+fileName);
        if (file2.exists()) {
//            log.info("文件存在，路径正确！");

            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file2);
            ff.start();
            String rotate_old=ff.getVideoMetadata("rotate");//视频旋转角度，可能是null
            int ftp = ff.getLengthInFrames();
            int flag=0;
            Frame frame = null;
            while (flag <= ftp) {
                //获取帧
                frame = ff.grabImage();
                //过滤前3帧，避免出现全黑图片
                if ((flag>2)&&(frame != null)) {
                    break;
                }
                flag++;
            }
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage srcImage = converter.getBufferedImage(frame);
            int srcImageWidth = srcImage.getWidth();
            int srcImageHeight = srcImage.getHeight();
//            int width = 480;
//            int height = (int) (((double) width / srcImageWidth) * srcImageHeight);
//            BufferedImage thumbnailImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//            thumbnailImage.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            BufferedImage thumbnailImage = new BufferedImage(srcImageWidth, srcImageHeight, BufferedImage.TYPE_3BYTE_BGR);
            thumbnailImage.getGraphics().drawImage(srcImage.getScaledInstance(srcImageWidth, srcImageHeight, Image.SCALE_SMOOTH), 0, 0, null);
            ImageIO.write(thumbnailImage, "jpg", targetFile);
//            ImageIO.write(FrameToBufferedImage(frame), "jpg", targetFile);
            ff.close();
            ff.stop();
            if(rotate_old!=null && !rotate_old.isEmpty()){
                int rotate=Integer.parseInt(rotate_old);
                rotatePhonePhoto(filePath+"/"+oName+".jpg",rotate);
            }
            return oName;
        }
        return oName;
    }
    /**
     * 以下所有方法是视频截图工具
     */
    //getTempPath方法，这个是视频下载成功后，进行帧的图片截取
    public static String getTempPath2(String filePath, String fileName, String targetPath) throws Exception {
        System.out.println(fileName);
        String[] split = fileName.split("\\.");
        System.out.println(split.length);
        String oName=split[0];
        System.out.println(oName);
        String sName=split[1];
//        String tempPath="    ";//保存的目标路径
        File targetFile = new File(targetPath+"/"+oName+".jpg");
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        File file2 = new File(filePath+"/"+fileName);
        if (file2.exists()) {
//            log.info("文件存在，路径正确！");

            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file2);
            ff.start();
            String rotate_old=ff.getVideoMetadata("rotate");//视频旋转角度，可能是null
            int ftp = ff.getLengthInFrames();
            int flag=0;
            Frame frame = null;
            while (flag <= ftp) {
                //获取帧
                frame = ff.grabImage();
                //过滤前3帧，避免出现全黑图片
                if ((flag>2)&&(frame != null)) {
                    break;
                }
                flag++;
            }
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage srcImage = converter.getBufferedImage(frame);
            int srcImageWidth = srcImage.getWidth();
            int srcImageHeight = srcImage.getHeight();
//            int width = 480;
//            int height = (int) (((double) width / srcImageWidth) * srcImageHeight);
//            BufferedImage thumbnailImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//            thumbnailImage.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            BufferedImage thumbnailImage = new BufferedImage(srcImageWidth, srcImageHeight, BufferedImage.TYPE_3BYTE_BGR);
            thumbnailImage.getGraphics().drawImage(srcImage.getScaledInstance(srcImageWidth, srcImageHeight, Image.SCALE_SMOOTH), 0, 0, null);
            ImageIO.write(thumbnailImage, "jpg", targetFile);
//            ImageIO.write(FrameToBufferedImage(frame), "jpg", targetFile);
            ff.close();
            ff.stop();
            if(rotate_old!=null && !rotate_old.isEmpty()){
                int rotate=Integer.parseInt(rotate_old);
                rotatePhonePhoto(filePath+"/"+oName+".jpg",rotate);
            }
            return oName;
        }
        return oName;
    }

    private static RenderedImage FrameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }
    public static String rotatePhonePhoto(String fullPath, int angel) {
        BufferedImage src;
        try {
            src = ImageIO.read(new File(fullPath));
            int src_width = src.getWidth(null);
            int src_height = src.getHeight(null);

            int swidth = src_width;
            int sheight = src_height;

            if (angel == 90 || angel == 270) {
                swidth = src_height;
                sheight = src_width;
            }
            Rectangle rect_des = new Rectangle(new Dimension(swidth, sheight));
            BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = res.createGraphics();
            g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
            g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
            g2.drawImage(src, null, null);
            ImageIO.write(res,"jpg", new File(fullPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullPath;

    }

    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }


}
