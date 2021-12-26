package com.automation.framework.integration.postman;

import com.automation.framework.AutomationSuiteApplicationTests;
import com.automation.framework.core.annotation.LazyAutowired;
import com.automation.framework.data.entity.BaseEntity;
import com.automation.framework.data.entity.app.postman.Postman;
import com.automation.framework.data.entity.app.postman.Workspace;
import com.automation.framework.service.api.spotify.PostmanService;
import com.automation.framework.util.FileReader;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.automation.framework.data.Constants.POSTMAN_WORKSPACE_DATA;
import static org.apache.hc.core5.http.HttpStatus.SC_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PostmanTest extends AutomationSuiteApplicationTests {

    private Postman requestPostman;
    private Postman requestPayload;
    private Postman payload;
    private String resourceID;
    private String workspaceName;

    @LazyAutowired
    private PostmanService postmanService;

    @LazyAutowired
    private FileReader fileReader;

    @BeforeAll
    public void setupTestData(){
        requestPostman = new Postman();
        requestPayload = fileReader.readJsonFile(POSTMAN_WORKSPACE_DATA)
                .getPostmen().get(0);

        payload = requestPayload;
    }

    @BeforeEach
    public void setupWorkspace(){

        payload.getWorkspace().setName(faker.name().name());
        workspaceName = payload.getWorkspace().getName();

        postmanService.post(payload);

        resourceID = postmanService.getWorkspaces().as(BaseEntity.class)
                .getWorkspaces().stream()
                .filter(workspace -> workspace.getName().equalsIgnoreCase(workspaceName))
                .collect(Collectors.toList()).get(0).getId();
    }

    @AfterEach
    public void tearWorkspace(){

        Response response = postmanService.getWorkspace(resourceID)
                .as(Postman.class).getId() == null ? null : postmanService.delete(resourceID);
    }

    @Test
    @Order(1)
    public void verifyGetRequestForPostmanWorkspaces(){

        Response getResponse = postmanService.getWorkspaces();

        List<Workspace> workspaces = getResponse.as(BaseEntity.class).getWorkspaces();

        logger.info(workspaces);

        assertThat(getResponse.statusCode(), equalTo(SC_SUCCESS));
        assertThat(workspaces).isNotNull();

        workspaces.forEach(workspace -> {
            assertThat(workspace.getId()).isNotNull();
            assertThat(workspace.getName()).isNotNull();
            assertThat(workspace.getType()).isNotNull();
        });

        assertThat(workspaces.stream().map(Workspace::getName).collect(Collectors.toList()))
                .contains("MyWorkspace")
                .isNotNull();
    }

    @Test
    @Order(2)
    public void verifyWorkspaceCreationPostRequest(){

        Response postResponse = postmanService.post(requestPayload);

        Workspace workspace = postResponse.as(Postman.class).getWorkspace();

        assertThat(postResponse.statusCode(), equalTo(SC_SUCCESS));

        assertThat(workspace).isNotNull();
        assertThat(workspace.getId()).isNotNull();

        assertThat(workspace.getName())
                .isNotNull()
                .contains(requestPayload.getWorkspace().getName());
    }

    @Test
    @Order(3)
    public void validateUpdateWorkspacePutRequest(){

        Response updateResponse = postmanService.update(payload, resourceID);

        Workspace workspace = updateResponse.as(Postman.class).getWorkspace();

        assertThat(updateResponse.statusCode(), equalTo(SC_SUCCESS));

        Workspace updatedWorkspace = postmanService.getWorkspaces().as(BaseEntity.class)
                .getWorkspaces().stream()
                .filter(space -> space.getName().equalsIgnoreCase(workspaceName))
                .collect(Collectors.toList()).get(0);

        assertThat(updatedWorkspace)
                .isNotNull();

        assertThat(workspace.getName())
                .isNotNull()
                .contains(requestPayload.getWorkspace().getName());
    }

    @Test
    @Order(4)
    public void validateDeleteWorkspaceRequest(){

        Response updateResponse = postmanService.delete(resourceID);

        updateResponse.as(Postman.class).getWorkspace()
                .getId().equalsIgnoreCase(resourceID);

        assertThat(updateResponse.statusCode(), equalTo(SC_SUCCESS));

        List<Workspace> updatedWorkspace = postmanService.getWorkspaces().as(BaseEntity.class)
                .getWorkspaces().stream()
                .filter(space -> space.getName().equalsIgnoreCase(workspaceName))
                .collect(Collectors.toList());

        assertThat(updatedWorkspace).isNullOrEmpty();
    }





}