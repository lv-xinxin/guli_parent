package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {

    String uploadVideoAly(MultipartFile file);

    //删除多个阿里云
    void removeMoreAlyVideo(List videoIdList);
}
