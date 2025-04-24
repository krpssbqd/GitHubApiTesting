package core;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

@Component
public class GitHubCaller extends ServiceCaller {

    private final String BASE_URL = Config.get("github.test.base.url");
    private final String GET_USERS = "/user";
    private final String POST_REPOSITORY = "/user/repos";
    private final String GET_DELETE_REPOSITORY = "/repos/{login}/{repoName}";

    public Response getUsers(RequestSpecification requestSpecification) {
        return get(GET_USERS, requestSpecification);
    }

    public Response createRepository(Object requestBody, RequestSpecification requestSpecification) {
        return post(POST_REPOSITORY, requestBody, requestSpecification);
    }

    public Response fetchRepositoryDetails(String login, String repoName, RequestSpecification requestSpecification) {
        return get(GET_DELETE_REPOSITORY
                        .replace("{login}", login)
                        .replace("{repoName}", repoName),
                requestSpecification);
    }

    public Response deleteRepository(String login, String repoName, RequestSpecification requestSpecification) {
        return delete(GET_DELETE_REPOSITORY
                        .replace("{login}", login)
                        .replace("{repoName}", repoName),
                requestSpecification);
    }

    public RequestSpecification getGitHubUser() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .addHeader("Authorization", "Bearer " + Config.get("github.test.user.token"))
                .build();
    }

    public RequestSpecification getUnauthorizedUser() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .build();
    }
}
