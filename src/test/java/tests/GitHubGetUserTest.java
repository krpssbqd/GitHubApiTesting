package tests;

import io.restassured.response.Response;

import static core.HttpStatus.OK;

import org.testng.annotations.Test;

public class GitHubGetUserTest extends GitHubBaseTest {

    @Test
    public void shouldGetCorrectGitHubLogin() {
        // when:
        Response response = gitHubCaller.getUsers(gitHubCaller.getGitHubUser());

        // then:
        expectedResponseCode(response, OK);
        expectedSecurityHeaders(response);
        expectedResponseBody(response, "login", DEFAULT_USER);
    }
}
