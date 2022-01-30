package com.automation.framework.env.driver;

import com.automation.framework.core.annotation.LazyConfiguration;
import com.automation.framework.core.annotation.ThreadScopeBean;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.net.URL;

import static com.automation.framework.data.Constants.BROWSER;
import static io.appium.java_client.remote.MobileCapabilityType.*;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

@Slf4j
@LazyConfiguration
public class MobileAppiumDriver {

    @Value("${appium.server.url}")
    protected URL appiumServerUrl;

    @Value("${android.app.dir.path}")
    protected URL appiumApp;

    @ThreadScopeBean
    @ConditionalOnProperty(name = BROWSER, havingValue = "android")
    public AppiumDriver getAndroidDriver(){
        log.info("activated");
        return new AppiumDriver(appiumServerUrl, configureDroidCapabilitiesOptions());
    }

    public DesiredCapabilities configureDroidCapabilitiesOptions(){
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(PLATFORM_NAME,"Android");
        capabilities.setCapability(DEVICE_NAME,"Pixel3");
        capabilities.setCapability(AUTOMATION_NAME,"UiAutomator2");
        capabilities.setCapability(UDID,"emulator-5554");
        capabilities.setCapability(APP,"C:\\apk\\ApiDemos-debug.apk");
        return capabilities;
    }


}