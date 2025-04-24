package tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static core.HttpStatus.NO_CONTENT;

public class GitHubDeleteRepositoryTest extends GitHubBaseTest {

    @Test
    public void shouldDeleteExistingRepository() {
        // given:
        String name = "GitHubTesting2";
        Object requestBody = getDefaultCreateRepoRequestBody(name);
        gitHubCaller.createRepository(requestBody, gitHubCaller.getGitHubUser());

        // when:
        Response response = gitHubCaller.deleteRepository(DEFAULT_USER, name, gitHubCaller.getGitHubUser());

        // then:
        expectedResponseCode(response, NO_CONTENT);
    }
}
