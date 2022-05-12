package com.atguigu.eduservice.controller;


import com.alibaba.excel.util.StringUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@RestController
@RequestMapping("/eduservice/edu-video")
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;


    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo video){
         eduVideoService.save(video);
         return R.ok();
    }

    //根据小节id查询
    @GetMapping("getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable String videoId) {
        EduVideo eduVideo = eduVideoService.getById(videoId);
        return R.ok().data("video", eduVideo);
    }
    //修改小节
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo video) {
        eduVideoService.updateById(video);
        return R.ok();
    }

    //删除小节
    @DeleteMapping("deleteVideoByid/{videoId}")
    public R deleteVideo(@PathVariable("videoId") String videoId){
        //根据小节id获取 视频id,调用方法现实视频删除
        EduVideo eduVideo = eduVideoService.getById(videoId);

        String videoSourceId = eduVideo.getVideoSourceId();

        if (!StringUtils.isEmpty(videoSourceId)){
            R r = vodClient.removeAliyunVideoById(videoSourceId);
            if (r.getCode()==20001) {
                throw new GuliException(20001,"删除视频失败 熔断器....");
            }
        }

        eduVideoService.removeById(videoId);
        return R.ok();
    }

}

