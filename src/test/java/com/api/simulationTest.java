package com.api;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.*;
import org.junit.Test;
import io.restassured.http.ContentType;

public class simulationTest {

    public static int randomNumber() {
        return (int) Math.floor(Math.random() * (9 - 0 + 1) + 0);
    }

    String[] cpfsWithRestriction = { "97093236014", "60094146012", "84809766080", "62648716050",
            "26276298085", "01317496094", "55856777050", "19626829001", "24094592008", "58063164083" };

    String[] cpfsWithoutRestriction = { "172.153.098-35", "088.458.748-77", "420.591.348-62 ", "404.220.138-54",
            "025.220.606-19", "211.246.041-20", "299.827.458-75", "288.892.178-20", "400.640.618-50" };

    int random = randomNumber();

    @Test
    public void consultCPFWithRestriction() {
        baseURI = "http://localhost:8080/api/v1";

        when().get("/restricoes/{cpf}", cpfsWithRestriction[random])
                .then().assertThat()
                .statusCode(200).body("mensagem", equalTo("O CPF " + cpfsWithRestriction[random] + " tem problema"));
    }

    @Test
    public void consultCPFWithoutRestriction() {
        baseURI = "http://localhost:8080/api/v1";
        when().get("/restricoes/{cpf}", cpfsWithoutRestriction[random])
                .then().assertThat().statusCode(204);
    }

    @Test
    public void newSimulation() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cpf", cpfsWithRestriction[random]);
        map.put("nome", "Alif Corrêa Flores");
        map.put("email", "alifcorrea.qa@gmail.com");
        map.put("valor", 1200);
        map.put("parcelas", 12);
        map.put("seguro", true);
        JSONObject request = new JSONObject(map);

        given().contentType(ContentType.JSON).body(request.toJSONString())
                .when().post("/simulacoes")
                .then().statusCode(201);
    }

    @Test
    public void getAllSimulations() {
        baseURI = "http://localhost:8080/api/v1";
        when().get("/simulacoes").then().statusCode(200).log().body();
    }

    @Test
    public void getSimulationByCPF() {
        baseURI = "http://localhost:8080/api/v1";
        when().get("/simulacoes/{cpf}", "97093236014")
                .then().statusCode(200);
    }

    @Test
    public void editSimulation() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cpf", cpfsWithRestriction[0]);
        map.put("nome", "Alif Corrêa Flores Final");
        map.put("email", "alifcorreas.qa@gmail.com");
        map.put("valor", 1500);
        map.put("parcelas", 24);
        map.put("seguro", false);
        JSONObject request = new JSONObject(map);

        given().contentType(ContentType.JSON).body(request.toJSONString())
                .when().put("/simulacoes/{cpf}", cpfsWithRestriction[0])
                .then().statusCode(200);
    }

    @Test
    public void deleteSimulation() {
        when().delete("/simulacoes/{id}", 1)
                .then().statusCode(200)
                .body(equalTo("OK"));
    }
}
