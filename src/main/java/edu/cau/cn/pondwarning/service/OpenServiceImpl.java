package edu.cau.cn.pondwarning.service;

import edu.cau.cn.pondwarning.dao.localdb.DevHisDataMapper;
import edu.cau.cn.pondwarning.dao.localdb.DeviceMapper;
import edu.cau.cn.pondwarning.dao.localdb.PondMapper;
import edu.cau.cn.pondwarning.dao.localdb.VdeviceMapper;
import edu.cau.cn.pondwarning.entity.localdb.DevHisData;
import edu.cau.cn.pondwarning.entity.localdb.Device;
import edu.cau.cn.pondwarning.entity.localdb.Pond;
import edu.cau.cn.pondwarning.entity.localdb.Vdevice;
import edu.cau.cn.pondwarning.util.CommonConstant;
import edu.cau.cn.pondwarning.util.CommonUtil;
import edu.cau.cn.pondwarning.util.HlsDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenServiceImpl implements OpenService{

    @Autowired
    private PondMapper pondMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private DevHisDataMapper devHisDataMapper;
    @Autowired
    private VdeviceMapper vdeviceMapper;

    @Value("${user.root}")
    private String root;

    @Override
    public Map<String, Object> getPondList() {
        Map<String,Object> map = new HashMap<>();
        String useruuid="993a2291-9792-4b17-ae1c-b5212ac8a8c7";
        List<Pond> pondsList = pondMapper.selectPondsFew(useruuid);
        map.put("datalist",pondsList);
        return map;
    }

    @Override
    public Map<String, Object> getDevList(String ponduuid) {
        Map<String,Object> map = new HashMap<>();
        String useruuid = "993a2291-9792-4b17-ae1c-b5212ac8a8c7";
        List<Device> devicesList = deviceMapper.selectDevicesFewByPond(useruuid,ponduuid);
        map.put("datalist",devicesList);
        return map;
    }

    @Override
    public Map<String, Object> getDataList(String wdevid) {
        Map<String, Object> map = new HashMap<>();
        List<DevHisData> dataList = devHisDataMapper.selectDevHisDatasDesc(wdevid);
        map.put("datalist",dataList);
        return map;
    }

    @Override
    public Map<String, Object> getVideoImg(String vdevid) {
        Map<String,Object> map = new HashMap<>();
        String useruuid = "993a2291-9792-4b17-ae1c-b5212ac8a8c7";
        String rootPath="C:/Users/Administrator/Desktop/User_file/fanyuxing/Deployment_file/pwarning-web/nginx-1.22.0/dist/img/video/"+useruuid.replace("-","")+"/mp4";//存放视频的地址，先放在本地
//        String rootPath=root+useruuid.replace("-","")+"/mp4";//存放视频和截图的地址，先放在本地
        String targetPath="C:/Users/Administrator/Desktop/User_file/fanyuxing/Deployment_file/pwarning-web/nginx-1.22.0/dist/img/video/";//存放截图的地址，先放在本地
        File dir = new File(rootPath);
        if (!dir.exists()) {// 判断目录是否存在，不存在则创建目录
            dir.mkdir();
        }
        Vdevice vdevice = vdeviceMapper.selectOneVdevice(vdevid);
        String originUrlpath = vdevice.getUrl();//这个是m3u8直播地址
        String preUrlPath ="";
        String fileName = "";
        try{
            HlsDownloader downLoader = new HlsDownloader(originUrlpath, preUrlPath, rootPath);
            //downLoader.setThreadQuantity(10);
            List vlist = downLoader.download1();
            map.put(CommonConstant.STATUS_KEY, CommonConstant.GET_VDEVICE_SUCCESS);
            map.put("videoUrl", vlist.get(vlist.size()-1)+"?proto=https");
            System.out.println(vlist.toString());
//            fileName = downLoader.download(false);//true用多线程下载，false不用多线程
//            if(fileName.isEmpty()){
//                System.out.println("下载失败");
//                map.put(CommonConstant.STATUS_KEY, CommonConstant.GET_VDEVICE_FAIL);
//                return map;
//            }else {
//                System.out.println("下载成功");
//                String name = HlsDownloader.getTempPath2(rootPath, fileName,targetPath);//这个是视频截图方法
//                System.out.println("name:" + name);
//                String path =targetPath+"/"+ name+".jpg";
//                String url="http://43.138.85.171:8001/img/video/"+name+".jpg";
//                Map<String,Integer> map1=CommonUtil.ImageWidthAndHeight(path);
//                map.put(CommonConstant.STATUS_KEY, CommonConstant.GET_VDEVICE_SUCCESS);
//                map.put("imgUrl", url);
//                map.put("width", map1.get("width"));
//                map.put("height", map1.get("height"));
//                // 删除无用文件夹
//                boolean flag1 = HlsDownloader.DeleteFolder(rootPath + "/" + name);
//                // 删除无用文件
//                boolean flag2 = HlsDownloader.DeleteFolder(rootPath + "/" + fileName);
//                if (flag1 && flag2) {
//                    System.out.println("删除成功");
//                } else {
//                    System.out.println("删除失败");
//                    CommonUtil.delAllFile(rootPath);
//                }
//            }
        }catch (Exception e){
            map.put(CommonConstant.STATUS_KEY, CommonConstant.GET_VDEVICE_FAIL);
            return map;
        }
        return map;
    }
}
