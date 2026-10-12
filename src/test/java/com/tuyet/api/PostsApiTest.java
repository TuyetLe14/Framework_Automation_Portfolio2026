package com.tuyet.api;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PostsApiTest extends ApiBaseTest {

    @Test(groups = {"api", "smoke"}, description = "GET /posts/1 trả đúng các trường")
    public void getSinglePost() {
        given().spec(requestSpec)
        .when().get("/posts/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("userId", equalTo(1))
            .body("title", not(emptyString()))
            .body("body", not(emptyString()));
    }

    @Test(groups = {"api"}, description = "GET /posts trả 100 bài viết")
    public void getAllPosts() {
        given().spec(requestSpec)
        .when().get("/posts")
        .then()
            .statusCode(200)
            .body("size()", equalTo(100));
    }

    @DataProvider(name = "userIds")
    public Object[][] userIds() {
        return new Object[][]{{1}, {5}, {10}};
    }

    @Test(groups = {"api"}, dataProvider = "userIds",
            description = "Lọc bài viết theo userId")
    public void filterPostsByUser(int userId) {
        given().spec(requestSpec).queryParam("userId", userId)
        .when().get("/posts")
        .then()
            .statusCode(200)
            .body("size()", equalTo(10))
            .body("userId", everyItem(equalTo(userId)));
    }

    @Test(groups = {"api"}, description = "GET comment của một bài viết")
    public void getCommentsOfPost() {
        given().spec(requestSpec)
        .when().get("/posts/1/comments")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("postId", everyItem(equalTo(1)))
            .body("email", everyItem(containsString("@")));
    }

    @Test(groups = {"api", "smoke"}, description = "POST tạo bài viết trả 201")
    public void createPost() {
        String payload = """
                {
                  "title": "QA automation",
                  "body": "REST Assured portfolio test",
                  "userId": 1
                }
                """;

        given().spec(requestSpec).body(payload)
        .when().post("/posts")
        .then()
            .statusCode(201)
            .body("title", equalTo("QA automation"))
            .body("userId", equalTo(1))
            .body("id", notNullValue());
    }

    @Test(groups = {"api"}, description = "PUT cập nhật toàn bộ bài viết")
    public void updatePost() {
        String payload = """
                {
                  "id": 1,
                  "title": "updated title",
                  "body": "updated body",
                  "userId": 1
                }
                """;

        given().spec(requestSpec).body(payload)
        .when().put("/posts/1")
        .then()
            .statusCode(200)
            .body("title", equalTo("updated title"))
            .body("body", equalTo("updated body"));
    }

    @Test(groups = {"api"}, description = "PATCH chỉ cập nhật một trường")
    public void patchPost() {
        given().spec(requestSpec).body("{\"title\": \"patched title\"}")
        .when().patch("/posts/1")
        .then()
            .statusCode(200)
            .body("title", equalTo("patched title"))
            .body("userId", equalTo(1));
    }

    @Test(groups = {"api"}, description = "DELETE bài viết trả 200")
    public void deletePost() {
        given().spec(requestSpec)
        .when().delete("/posts/1")
        .then()
            .statusCode(200);
    }

    @DataProvider(name = "missingIds")
    public Object[][] missingIds() {
        return new Object[][]{{99999}, {0}};
    }

    @Test(groups = {"api"}, dataProvider = "missingIds",
            description = "Bài viết không tồn tại trả 404")
    public void getNonExistingPost(int id) {
        given().spec(requestSpec)
        .when().get("/posts/" + id)
        .then()
            .statusCode(404);
    }

    @Test(groups = {"api"}, description = "Thời gian phản hồi dưới ngưỡng cấu hình")
    public void responseTimeIsAcceptable() {
        given().spec(requestSpec)
        .when().get("/posts/1")
        .then()
            .statusCode(200)
            .time(lessThan((long) maxResponseTimeMs));
    }
}
