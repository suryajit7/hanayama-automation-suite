package com.automation.framework.gui.module.visa;

import com.automation.framework.data.entity.app.ems.User;
import com.automation.framework.data.repo.UserRepository;
import com.automation.framework.gui.BaseTestNGTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.List;

public class VisaRegistrationTest extends BaseTestNGTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void visaTest() {

        List<User> users = this.userRepository.findByFirstNameStartingWith("Bran");
        System.out.println(users);
        logger.info("some info");
    }
}
