package com.automation.framework.page.site.saucedemo;


import com.automation.framework.core.annotation.Page;
import com.automation.framework.page.BasePage;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

import static com.automation.framework.page.site.saucedemo.MenuOption.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

@Page
@Getter
@Setter
public class MenuNavigationPage extends BasePage {


    private static final String MENU = "//li[@class='main-menu-first-level-list-item']/child::a/child::b";
    private static final String ORANGE_HRM_LOGO = "//img[@alt='OrangeHRM']";

    @FindBy(how = How.XPATH, using = MENU)
    private WebElement menu;

    @Step
    public MenuNavigationPage moveToMenuOption(List<MenuOption> menuOptions) {
        for (MenuOption option : menuOptions) {
            wait.until(visibilityOf(this.driver.findElement(By.id(option.getMenuId())))).click();
        } return this;
    }

    @Step
    public MenuNavigationPage navigateToMenu(MenuOption menuOption) {

        wait.until(visibilityOfElementLocated(By.xpath(ORANGE_HRM_LOGO)));

        switch (menuOption) {

            case USERS:
                moveToMenuOption(List.of(ADMIN, USER_MANAGEMENT, USERS));
                break;
        }
        return this;
    }

}
