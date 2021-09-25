package com.automation.framework.page.app.hrm.admin.usermanagement;

import com.automation.framework.core.annotation.Page;
import com.automation.framework.page.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

@Page
public class Users extends BasePage {

    private final String USERNAME_FIELD = "searchSystemUser_userName";
    private final String USER_TYPE = "searchSystemUser_userType";
    private final String EMPLOYEE_NAME = "searchSystemUser_employeeName_empName";
    private final String USER_STATUS = "searchSystemUser_status";
    private final String SEARCH_BUTTON = "searchBtn";
    private final String RESET_BUTTON = "resetBtn";

    @FindBy(id = USERNAME_FIELD)
    private WebElement username;

    @FindBy(id = USER_TYPE)
    private WebElement userType;

    @FindBy(id = EMPLOYEE_NAME)
    private WebElement employeeName;

    @FindBy(id = USER_STATUS)
    private WebElement userStatus;

    @FindBy(id = SEARCH_BUTTON)
    private WebElement searchButton;

    @FindBy(id = RESET_BUTTON)
    private WebElement resetButton;


    public void enterUsername(String username) {
        wait.until(visibilityOfElementLocated(By.id(USERNAME_FIELD)));
        enterText(this.username, username);
    }

    public void selectUserType(UserType userType) {
        wait.until(visibilityOfElementLocated(By.id(USER_TYPE)));
        this.userType.click();
        selectByVisibleTextInDropdown(this.userType, userType.name());
    }

    public void enterEmployeeName(String employeeName) {
        wait.until(visibilityOfElementLocated(By.id(EMPLOYEE_NAME)));
        enterText(this.employeeName, employeeName);
    }

    public void selectUserStatus(UserStatus userStatus) {
        wait.until(visibilityOfElementLocated(By.id(USER_STATUS)));
        this.userStatus.click();
        selectByVisibleTextInDropdown(this.userStatus, userStatus.name());
    }

    public void clickSearchButton() {
        wait.until(visibilityOfElementLocated(By.id(SEARCH_BUTTON)));
        this.searchButton.click();
    }

    public void clickResetButton() {
        wait.until(visibilityOfElementLocated(By.id(RESET_BUTTON)));
        this.resetButton.click();
    }


}
