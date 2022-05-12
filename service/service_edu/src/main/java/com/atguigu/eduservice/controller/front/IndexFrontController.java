package com.atguigu.eduservice.controller.front;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description="前台页面热门讲师及课程")
@RestController
@RequestMapping("/eduservice/indexFront")
@CrossOrigin
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;


    //查询前8条热门课程，查询前四条名师
    @Cacheable(value = "hotdata",key="'gulifront'")
    @GetMapping("index")
    public R index(){
        List<EduCourse> courselist = courseService.getHotCourseLimit();
        List<EduTeacher> teacherlist = teacherService.getHotTeacherLimit();
        return R.ok().data("courseList",courselist).data("teacherList",teacherlist);
    }



}
