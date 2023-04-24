package com.api;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.simple.*;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import com.github.javafaker.*;

public class simulationTest {

    @Before
    public void initialConfigs() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/api/v1";
    }

    Faker faker = new Faker(new Locale("pt-BR"));
    String name = faker.name().name();
    int monetaryValue = faker.number().numberBetween(1000, 20000);
    int installments = faker.number().numberBetween(1, 60);
    String email = "faker@gmail.com";

    String[] cpfsWithRestriction = { "97093236014", "60094146012", "84809766080", "62648716050",
            "26276298085", "01317496094", "55856777050", "19626829001", "24094592008", "58063164083" };

    String[] cpfsWithoutRestriction = { "17215309835", "08845874877", "42059134862 ", "40422013854",
            "02522060619", "21124604120", "29982745875", "28889217820", "40064061850" };

    int random = randomNumber();

    @Test
    public void consultCPFWithRestriction() {
        when().get("/restricoes/{cpf}", cpfsWithRestriction[random])
                .then().assertThat()
                .statusCode(200).body("mensagem", equalTo("O CPF " + cpfsWithRestriction[random] + " tem problema"));
    }

    @Test
    public void consultCPFWithoutRestriction() {
        when().get("/restricoes/{cpf}", cpfsWithoutRestriction[random])
                .then().assertThat().statusCode(204);
    }

    @Test
    public void newSimulation() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cpf", cpfsWithRestriction[random]);
        map.put("nome", name);
        map.put("email", email);
        map.put("valor", monetaryValue);
        map.put("parcelas", installments);
        map.put("seguro", true);
        JSONObject request = new JSONObject(map);

        given().contentType(ContentType.JSON).body(request.toJSONString())
                .when().post("/simulacoes")
                .then().statusCode(201);
    }

    @Test
    public void newSimulationWithError() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cpf", cpfsWithRestriction[random]);
        map.put("nome", name);
        map.put("email", name + "@gmail.com");
        // map.put("valor", 1200);
        map.put("parcelas", installments);
        map.put("seguro", true);
        JSONObject request = new JSONObject(map);

        given().contentType(ContentType.JSON).body(request.toJSONString())
                .when().post("/simulacoes")
                .then().statusCode(400);
    }

    @Test
    public void getAllSimulations() {
        when().get("/simulacoes").then().statusCode(200).log().body();
    }

    @Test
    public void getSimulationByCPF() {
        when().get("/simulacoes/{cpf}", "66414919004")
                .then().statusCode(200);
    }

    @Test
    public void getUnknomnSimulationByCPF() {
        when().get("/simulacoes/{cpf}", cpfsWithoutRestriction[random])
                .then().statusCode(404);
    }

    @Test
    public void editSimulation() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cpf", "66414919004");
        map.put("nome", "Fulano de QA");
        map.put("email", "fulano.qa@gmail.com");
        map.put("valor", monetaryValue);
        map.put("parcelas", installments);
        map.put("seguro", false);
        JSONObject request = new JSONObject(map);

        given().contentType(ContentType.JSON).body(request.toJSONString())
                .when().put("/simulacoes/{cpf}", "66414919004")
                .then().statusCode(200);
    }

    @Test
    public void editSimulationWithError() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cpf", cpfsWithoutRestriction[random]);
        map.put("nome", "Alif Corrêa Flores Final");
        map.put("email", "alifcorreas.qa@gmail.com");
        map.put("valor", monetaryValue);
        map.put("parcelas", installments);
        map.put("seguro", false);
        JSONObject request = new JSONObject(map);

        given().contentType(ContentType.JSON).body(request.toJSONString())
                .when().put("/simulacoes/{cpf}", cpfsWithRestriction[0])
                .then().statusCode(404);
    }

    @Test
    public void deleteSimulation() {
        when().delete("/simulacoes/{id}", 13)
                .then().statusCode(200)
                .body(equalTo("OK"));
    }

    public static int randomNumber() {
        return (int) Math.floor(Math.random() * (9 - 0 + 1) + 0);
    }
}
