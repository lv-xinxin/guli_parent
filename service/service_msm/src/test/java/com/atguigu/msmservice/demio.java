package com.atguigu.msmservice;

import redis.clients.jedis.Jedis;

public class demio {

    public static void main(String[] args) {
         Jedis jedis=new  Jedis("127.0.0.1", 6379);

        jedis.set("wxf", "我很强");
        String value=jedis.get("wxf");
        System.out.println(value);
        //释放资源
        jedis.close();
    }



}
