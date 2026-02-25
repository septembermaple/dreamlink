package com.dreamlink.config;

import com.dreamlink.entity.User;
import com.dreamlink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 初始化测试用户
        if (userRepository.count() == 0) {
            // 创建管理员用户
            User admin1 = new User();
            admin1.setUsername("admin1");
            admin1.setPassword(passwordEncoder.encode("admin1"));
            admin1.setNickname("管理员1");
            admin1.setAvatar("https://api.dicebear.com/9.x/avataaars/svg?seed=Aries");
            admin1.setConstellation("白羊座");
            admin1.setPhone("13800138001");
            admin1.setIsAdmin(true);
            userRepository.save(admin1);

            User admin2 = new User();
            admin2.setUsername("admin2");
            admin2.setPassword(passwordEncoder.encode("admin2"));
            admin2.setNickname("管理员2");
            admin2.setAvatar("https://api.dicebear.com/9.x/avataaars/svg?seed=Taurus");
            admin2.setConstellation("金牛座");
            admin2.setPhone("13800138002");
            admin2.setIsAdmin(true);
            userRepository.save(admin2);

            // 创建普通用户
            User user3 = new User();
            user3.setUsername("admin3");
            user3.setPassword(passwordEncoder.encode("admin3"));
            user3.setNickname("管理员3");
            user3.setAvatar("https://api.dicebear.com/9.x/avataaars/svg?seed=Gemini");
            user3.setConstellation("双子座");
            user3.setPhone("13800138003");
            user3.setIsAdmin(false);
            userRepository.save(user3);

            System.out.println("测试用户初始化完成");
        }
    }

}
