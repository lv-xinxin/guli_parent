package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class VodFileDegradeFeignClient implements VodClient{

//出错之后，执行
    @Override
    public R removeAliyunVideoById(String VideoId) {
        return R.error().message("删除视频出错了");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错了");
    }
}
