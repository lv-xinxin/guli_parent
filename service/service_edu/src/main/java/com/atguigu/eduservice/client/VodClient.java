package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)//调用服务名
@Component
public interface VodClient {

    //根据视频id删除阿里云视频
    @DeleteMapping("/eduvod/video/removeAliyunVideoById/{VideoId}")
    public R removeAliyunVideoById(@PathVariable("VideoId") String VideoId);


    //定义调用删除多个视频的方法
    //删除多个阿里云
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);

}
