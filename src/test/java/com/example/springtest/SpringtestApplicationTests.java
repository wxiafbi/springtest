package com.example.springtest;

import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.junit4.SpringRunner;


// import com.example.springtest.Mapper.UserMapper;
import com.example.springtest.pojo.User;

@SpringBootTest
class SpringtestApplicationTests {

	@Test
	void contextLoads() {
		// System.out.println("测试");
		// @Autowired
		User user = new User(1, "张三", (short) 18, (short) 1, "18800000000");
		System.out.println(user.getId());

	}

}
