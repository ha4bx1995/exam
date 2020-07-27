package cn.edu.hgu.exam.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestJwt {
    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId","1");
        map.put("nickname","tom");
        String ip = "127.0.0.1";
        String time = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());
        String encode = JwtUtil.encode("20200418exam", map, ip + time);
        System.out.print(encode);
    }
}
