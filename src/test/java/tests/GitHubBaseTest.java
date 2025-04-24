package tests;

import com.google.common.io.ByteStreams;
import core.Config;
import core.GitHubCaller;
import core.HttpStatus;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;

import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

@ContextConfiguration(classes = {
        GitHubCaller.class
})
@Listeners({AllureTestNg.class})
public class GitHubBaseTest extends AbstractTestNGSpringContextTests {

    @Autowired
    GitHubCaller gitHubCaller;

    protected static final String DEFAULT_USER = Config.get("github.test.user.login");

    protected void expectedResponseCode(Response response, HttpStatus http) {
        assertThat(response.statusCode(), equalTo(http.code()));
    }

    protected void expectedResponseBody(Response response, String path, String expectedValue) {
        assertThat(response.jsonPath().getString(path), equalTo(expectedValue));
    }

    protected void expectedSecurityHeaders(Response response) {
        response.then()
                .header("Date", notNullValue())
                .header("Last-Modified", notNullValue())
                .header("Content-Type", equalTo("application/json; charset=utf-8"))
                .header("Cache-Control", allOf(
                        containsString("private"),
                        containsString("max-age=60")
                ))
                .header("Vary", containsString("Authorization"))
                .header("X-OAuth-Scopes", containsString("repo"))
                .header("X-Accepted-OAuth-Scopes", isEmptyOrNullString())
                .header("github-authentication-token-expiration", matchesRegex("^[0-9\\-: UTC]+$"))
                .header("X-GitHub-Media-Type", equalTo("github.v3; format=json"))
                .header("x-github-api-version-selected", equalTo("2022-11-28"))
                .header("X-RateLimit-Limit", equalTo("5000"))
                .header("X-RateLimit-Remaining", not(isEmptyString()))
                .header("X-RateLimit-Reset", notNullValue())
                .header("X-RateLimit-Used", notNullValue())
                .header("X-RateLimit-Resource", equalTo("core"))
                .header("Access-Control-Allow-Origin", equalTo("*"))
                .header("Access-Control-Expose-Headers", containsString("X-RateLimit-Limit"))
                .header("Strict-Transport-Security", containsString("max-age=31536000"))
                .header("X-Frame-Options", equalTo("deny"))
                .header("X-Content-Type-Options", equalTo("nosniff"))
                .header("X-XSS-Protection", equalTo("0"))
                .header("Referrer-Policy", containsString("origin-when-cross-origin"))
                .header("Content-Security-Policy", equalTo("default-src 'none'"))
                .header("Transfer-Encoding", equalTo("chunked"))
                .header("Connection", equalTo("keep-alive"))
                .header("Content-Encoding", equalTo("gzip"))
                .header("Server", equalTo("github.com"))
                .header("X-GitHub-Request-Id", notNullValue())
                .header("X-SYMC-Transaction-UUID", notNullValue());
    }

    @SneakyThrows
    protected JSONObject mergeJsonObjects(JSONObject base, JSONObject override) {
        JSONObject result = new JSONObject(base.toString());
        for (Iterator<String> it = override.keys(); it.hasNext(); ) {
            String key = it.next();
            result.put(key, override.get(key));
        }
        return result;
    }

    @SneakyThrows
    protected JSONObject reqBody(String name, Object value) {
        return new JSONObject().put(name, value);
    }

    @SneakyThrows
    protected Object getDefaultCreateRepoRequestBody(String name) {
        return new JSONObject()
                .put("name", name)
                .put("description", "Repository created via API test")
                .put("private", true)
                .put("auto_init", true);
    }

    @SneakyThrows
    protected JSONObject getDefaultCreateRepoRequestBody(String name, String desc) {
        return new JSONObject()
                .put("name", name)
                .put("description", desc);
    }

    @SneakyThrows
    protected void deleteRepoIfExists(String login, String repoName, RequestSpecification requestSpecification) {
        gitHubCaller.deleteRepository(login, repoName, requestSpecification);
    }

    @SneakyThrows
    protected void expectedResponseBodyShouldMatchJson(Response response, String jsonFileName) {
        String returnedJson = response.jsonPath().prettyPrint();
        String expectedJson = fromFile("expected/" + jsonFileName.toLowerCase());
        JSONAssert.assertEquals("Comparing expected response vs actual", expectedJson, returnedJson, JSONCompareMode.LENIENT);
    }

    protected void checkDefaultErrorMessage(Response response, String expectedMessage, String expectedCode) {
        assertThat(response.jsonPath().getString("message"), equalTo(expectedMessage));
        assertThat(response.jsonPath().getString("errors[0].resource"), equalTo("Repository"));
        assertThat(response.jsonPath().getString("errors[0].code"), equalTo("custom"));
        assertThat(response.jsonPath().getString("errors[0].field"), equalTo("name"));
        assertThat(response.jsonPath().getString("errors[0].message"), equalTo("name already exists on this account"));
        assertThat(response.jsonPath().getString("documentation_url"), equalTo("https://docs.github.com/rest/repos/repos#create-a-repository-for-the-authenticated-user"));
        assertThat(response.jsonPath().getString("status"), equalTo(expectedCode));
    }

    @SneakyThrows
    protected static String fromFile(String name) {
        try {
            return new String(ByteStreams.toByteArray(ClassLoader.getSystemResourceAsStream(name)));
        } catch (Throwable var2) {
            Throwable $ex = var2;
            throw $ex;
        }
    }
}
