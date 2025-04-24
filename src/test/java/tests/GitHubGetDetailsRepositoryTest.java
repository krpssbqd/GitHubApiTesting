package tests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static core.HttpStatus.OK;
import static core.HttpStatus.UNAUTHORIZED;

public class GitHubGetDetailsRepositoryTest extends GitHubBaseTest {

    @Test
    public void shouldGetRepositoryDetails() {
        // given:
        String name = "GitHubTesting4";
        Object requestBody = getDefaultCreateRepoRequestBody(name);
        gitHubCaller.createRepository(requestBody, gitHubCaller.getGitHubUser());

        // when:
        Response response = gitHubCaller.fetchRepositoryDetails(DEFAULT_USER, name, gitHubCaller.getGitHubUser());

        // then:
        expectedResponseCode(response, OK);
        expectedResponseBody(response, "full_name", "krpssbqd/" + name);
        expectedResponseBody(response, "private", "true");
        expectedResponseBodyShouldMatchJson(response, "fetchGitHubRepositories.json");
    }

    @Test
    public void shouldThrow401ForUnauthorized() {
        // given:
        Object requestBody = getDefaultCreateRepoRequestBody("GitHubTesting5");

        // when:
        Response response = gitHubCaller.createRepository(requestBody, gitHubCaller.getUnauthorizedUser());

        // then:
        expectedResponseCode(response, UNAUTHORIZED);
    }

    @Test
    public void shouldThrow422when() {
        // given:
        Object requestBody = getDefaultCreateRepoRequestBody("GitHubTesting6");
        gitHubCaller.createRepository(requestBody, gitHubCaller.getGitHubUser());

        // when:
        Response response = gitHubCaller.createRepository(requestBody, gitHubCaller.getGitHubUser());

        // then:
        checkDefaultErrorMessage(response, "Repository creation failed.", "422");
    }

    @AfterMethod
    public void cleanup() {
        cleanUpResources();
    }

    @Step("🧹Cleanup")
    public void cleanUpResources() {
        deleteRepoIfExists(DEFAULT_USER, "GitHubTesting4", gitHubCaller.getGitHubUser());
    }
}
