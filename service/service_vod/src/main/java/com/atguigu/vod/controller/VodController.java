package com.atguigu.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.util.ConstantPropertiesUtil;
import com.atguigu.vod.util.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("eduvod/video")
@CrossOrigin
public class VodController {



    @Autowired
    private VodService vodService;

    //上传视频
    @PostMapping("uploadAliyunVideo")
    public R uploadAlyiVideo(MultipartFile file) {
        String videoId=vodService.uploadVideoAly(file);
        return R.ok().data("videoId",videoId);
    }

    //根据视频id删除阿里云视频
    @DeleteMapping("removeAliyunVideoById/{VideoId}")
    public R removeAliyunVideoById(@PathVariable String VideoId){
        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向初始化对象方法实现删除
            request.setVideoIds(VideoId);
            // 调用初始化对象删除方法
            client.getAcsResponse(request);
            return R.ok();
        }catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
    }

    //删除多个阿里云
    @DeleteMapping("delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){
        vodService.removeMoreAlyVideo(videoIdList);
        return R.ok();
    }

}



















