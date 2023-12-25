package com.hui.rest.webservices.restfulwebservices.expense;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.rest.webservices.restfulwebservices.expense.repository.ExpenseRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ExpenseJpaResource {

    private ExpenditureListService expenseService;

    private ExpenseRepository expenseRepository;

    public ExpenseJpaResource(ExpenditureListService expenseService, ExpenseRepository expenseRepository){
        this.expenseService=expenseService;
        this.expenseRepository=expenseRepository;
    }



    @GetMapping("/{type}/expenses")
    public List<ExpenditureList> retrieveExpense(@PathVariable String type){
        return expenseService.findByType(type);
    }

    @GetMapping("/{username}/expenses/{currency}")
    public List<ExpenditureList> retrieveExpenses(@PathVariable String username,
                                                  @PathVariable String currency,
                                                  @RequestParam(required = false) String type){
        System.out.println("Received Search Term: " + type);
        List<ExpenditureList> expenditures = null;
        if(type != null && !type.isEmpty()){
            expenditures = expenseRepository.findByUsernameAndTypeContains(username, type);
        } else {
            expenditures = expenseRepository.findByUsername(username);
        }

        for ( ExpenditureList exp : expenditures) {

            RestTemplate restTemplate = new RestTemplate();
            String fooResourceUrl = "https://currency-converter-by-api-ninjas.p.rapidapi.com/v1/convertcurrency";
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-RapidAPI-Key", "03939cc397msh8dcd64407a76398p17c483jsncfe1fcb69495");
            headers.add("X-X-RapidAPI-Host-Key", "currency-converter-by-api-ninjas.p.rapidapi.com");



            HttpEntity entity = new HttpEntity<>(headers);

            try {
                // Update the URI to include the query parameters
                URI uri = UriComponentsBuilder.fromUriString(fooResourceUrl)
                        .queryParam("have", "EUR")
                        .queryParam("amount", String.valueOf(exp.getFee()))
                        .queryParam("want", currency)
                        .queryParam("type", type)
                        .build()
                        .toUri();

                ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);



                // Check if the response status is OK (200)
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    // Parse the JSON response body
                    String responseBody = responseEntity.getBody();
                    // Use a JSON parsing library (e.g., Jackson) to extract the "result" value
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(responseBody);
                    double result = jsonNode.get("new_amount").asDouble();

                    // Now 'result' contains the value you are looking for
                    System.out.println("Result: " + result);
                    exp.setFee(result);
                } else {
                    // Handle non-successful response
                    System.out.println("Error: " + responseEntity.getStatusCodeValue());
                }
            } catch (RestClientException | IOException e) {
                // Handle RestClientException
                e.printStackTrace();
            }

            //exp.setFee(Double.valueOf(response.getBody()));

        }
        return expenditures;
    }

    @GetMapping("/users/{username}/expenses/{id}")
    public ExpenditureList retrieveExpenses(@PathVariable String username, @PathVariable int id){

        return expenseRepository.findById(id).orElse(new ExpenditureList());
    }

    @DeleteMapping("/users/{username}/expenses/{id}")
    public ResponseEntity<Void> deleteExpenses(@PathVariable String username, @PathVariable int id){
        //expenseService.deleteById(id);
        expenseRepository.deleteById(id);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("/users/{username}/expenses/{id}")
    public ExpenditureList updateExpenses(@PathVariable String username,
                                               @PathVariable int id,
                                               @RequestBody ExpenditureList expense) {

        expenseRepository.save(expense);
        return expense;
    }

    @PostMapping("/users/{id}/expense")
    public ExpenditureList createExpenses(@PathVariable String username,
                                          @RequestBody ExpenditureList expense) {

        //ExpenditureList createdExpense = expenseService.addExpend(username,
        //        expense.getType(), expense.getFee(), expense.getDate());

        //ExpenditureList createdExpense = expenseRepository.addExpend(username,
        //        expense.getType(), expense.getFee(), expense.getDate());


        expense.setUsername(username);
        expense.setId(null);

        //expense.setUser();
       return expenseRepository.save(expense);

        //ExpenditureList createdExpense = expenseRepository.save(expense);

        //return createdExpense;
    }




}
