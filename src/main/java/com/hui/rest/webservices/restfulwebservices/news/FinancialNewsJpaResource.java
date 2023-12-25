package com.hui.rest.webservices.restfulwebservices.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FinancialNewsJpaResource {


    @GetMapping("/financial-news")
    public List<FinancialNews> retrieveAllNews() {
        List<FinancialNews> news = new ArrayList<>();


        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://mboum-finance.p.rapidapi.com/ne/news";
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RapidAPI-Key", "03939cc397msh8dcd64407a76398p17c483jsncfe1fcb69495");
        headers.add("X-X-RapidAPI-Host-Key", "mboum-finance.p.rapidapi.com");

        HttpEntity entity = new HttpEntity<>(headers);

        try {
            URI uri = UriComponentsBuilder.fromUriString(fooResourceUrl)
                    //.queryParam("keyword", "finance")
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

                // 在 if (!jsonNode.isEmpty()) 块内
                JsonNode dataArray = jsonNode.get("body");
                if (dataArray.isArray()) {
                    int count = 0;
                    for (JsonNode articleNode : dataArray) {
                        if (count < 1) {
                            // 提取每篇文章的标题、摘要和 URL
                            String headline = articleNode.path("title").asText();
                            //String summary = articleNode.path("summary").asText();
                            String url = articleNode.path("link").asText();

                            // 创建 FinancialNews 对象并设置其属性
                            FinancialNews financialNews = new FinancialNews();
                            financialNews.setHeadline(headline);
                            //financialNews.setSummary(summary);
                            financialNews.setUrl(url);

                            // 将 FinancialNews 对象添加到列表中
                            news.add(financialNews);

                            // 打印标题，摘要和 URL
                            System.out.println("title：" + headline);
                            //System.out.println("摘要：" + summary);
                            System.out.println("URL：" + url);

                            count++;
                        }
                    }
                }


            } else {
                // Handle non-successful response
                System.out.println("Error: " + responseEntity.getStatusCodeValue());
            }

        } catch (RestClientException | IOException e) {
            // Handle RestClientException
            e.printStackTrace();
        }
        return news;
    }
}
