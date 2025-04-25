package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static core.HttpStatus.CREATED;

public class GitHubCreateRepositoryTest extends GitHubBaseTest {

    @DataProvider(name = "createRepoRequestBody")
    public Object[] createRepoProvider() {
        return new Object[]{
                reqBody("delete_branch_on_merge", true),
                reqBody("delete_branch_on_merge", true),
                reqBody("delete_branch_on_merge", false),
                reqBody("allow_rebase_merge", true),
                reqBody("allow_rebase_merge", false),
                reqBody("allow_squash_merge", true),
                reqBody("allow_squash_merge", false),
                reqBody("allow_merge_commit", true),
                reqBody("allow_merge_commit", false),
                reqBody("is_template", true),
                reqBody("is_template", false),
                reqBody("has_wiki", true),
                reqBody("has_wiki", false),
                reqBody("has_projects", true),
                reqBody("has_projects", false),
                reqBody("has_issues", true),
                reqBody("has_issues", false),
                reqBody("homepage", "www.test.com"),
                reqBody("private", false),
                reqBody("private", true)
        };
    }

    @SneakyThrows
    @Test(dataProvider = "createRepoRequestBody")
    @Description("Should create repositories")
    public void shouldCreateNewRepository(JSONObject requestBodyExtension) {
        // given:
        JSONObject requestBodyMandatory = getDefaultCreateRepoRequestBody(
                "GitHubTesting1",
                "Repository created via API test");
        JSONObject requestBody = mergeJsonObjects(requestBodyMandatory, requestBodyExtension);

        // when:
        Response response = gitHubCaller.createRepository(requestBody, gitHubCaller.getGitHubUser());

        // then:
        expectedResponseCode(response, CREATED);
        String key = requestBodyExtension.keys().next().toString();
        expectedResponseBody(response, key, requestBodyExtension.get(key).toString());
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
        deleteRepoIfExists(DEFAULT_USER, "GitHubTesting1", gitHubCaller.getGitHubUser());
        deleteRepoIfExists(DEFAULT_USER, "GitHubTesting6", gitHubCaller.getGitHubUser());
    }
}
