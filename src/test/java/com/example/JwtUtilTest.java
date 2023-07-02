package com.example;

import com.example.sys.pojo.User;
import com.example.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testCreatJwt(){
        User user = new User();
        user.setUsername("Jarry");
        user.setEmail("2864937849@qq.com");
        String token = jwtUtil.createToken(user);
        System.out.println(token);
    }

    @Test
    public void testParseJwt(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjYWM2YjBlMy0wOTcxLTQxOTYtODUzZC1kMjNlZjFmMDg3Y2QiLCJzdWIiOiJ7XCJlbWFpbFwiOlwiMjg2NDkzNzg0OUBxcS5jb21cIixcInVzZXJuYW1lXCI6XCJKYXJyeVwifSIsImlzcyI6InN5c3RlbSIsImlhdCI6MTY4NzU5MTYxMywiZXhwIjoxNjg3NTkzNDEzfQ.UWccC8vx5YdBUdYIN0q0E2suCtoYuKhr0jDeyagKZ0c";
        Claims claims = jwtUtil.parseToken(token);
        System.out.println(claims);
    }

    @Test
    public void testParseJwtT(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjYWM2YjBlMy0wOTcxLTQxOTYtODUzZC1kMjNlZjFmMDg3Y2QiLCJzdWIiOiJ7XCJlbWFpbFwiOlwiMjg2NDkzNzg0OUBxcS5jb21cIixcInVzZXJuYW1lXCI6XCJKYXJyeVwifSIsImlzcyI6InN5c3RlbSIsImlhdCI6MTY4NzU5MTYxMywiZXhwIjoxNjg3NTkzNDEzfQ.UWccC8vx5YdBUdYIN0q0E2suCtoYuKhr0jDeyagKZ0c";
        User user= jwtUtil.parseToken(token,User.class);
        System.out.println(user);
    }
}
