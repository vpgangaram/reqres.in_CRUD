package com.example.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String BASE_URL = "https://reqres.in/api";
    private WebClient webClient;

    public UserServiceImpl() {
        this.webClient = WebClient.builder().baseUrl(BASE_URL).build();
    }

    @Override
    public List<User> getAllUsers() {
        return webClient.get().uri("/users").retrieve().bodyToMono(UsersResponse.class).block().getData();
    }

    @Override
    public String createUser(createUser user) {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        // The static field UserServiceImpl.BASE_URL should be accessed in a static way
        HttpPost httpPost = new HttpPost(UserServiceImpl.BASE_URL + "/users");
        httpPost.setHeader("Content-type", "application/json");

        String json = String.format("{\"name\": \"%s\", \"job\": \"%s\"}", user.getName(), user.getJob());

        StringEntity stringEntity;
        try {
            stringEntity = new StringEntity(json);
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            httpClient.close();
            return responseBody;
        } catch (UnsupportedEncodingException e) {
            logger.error("Unsupported encoding", e);
        } catch (ParseException | IOException e) {
            logger.error("HTTP request failed", e);
        }
        return null;
    }

    @Override
    public String updateUser(Long id, createUser user) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPut httpPut = new HttpPut(UserServiceImpl.BASE_URL+ "/users/" + id);
        httpPut.setHeader("Content-type", "application/json");

        String json = String.format("{\"name\": \"%s\", \"job\": \"%s\"}", user.getName(), user.getJob());

        StringEntity stringEntity;
        try {
            stringEntity = new StringEntity(json);
            httpPut.setEntity(stringEntity);
            CloseableHttpResponse response = httpClient.execute(httpPut);
            try {
                String responseBody = EntityUtils.toString(response.getEntity());
                return responseBody;
            } finally {
                response.close();
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Unsupported encoding", e);
        } catch (ParseException | IOException e) {
            logger.error("HTTP request failed", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error("Error closing HTTP client", e);
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteUser(Long id) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpDelete httpDelete = new HttpDelete(UserServiceImpl.BASE_URL+ "/users/" + id);
        httpDelete.setHeader("Content-type", "application/json");

        try{
            CloseableHttpResponse response = httpClient.execute(httpDelete);
            try {
                ResponseEntity<String> responseBody =(ResponseEntity<String>) response.getEntity();
                logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n"+ response + "\n ======================================================================");
                return responseBody;
            } finally {
                response.close();
            }          
        } catch (UnsupportedEncodingException e) {
            logger.error("Unsupported encoding", e);
        } catch (ParseException | IOException e) {
            logger.error("HTTP request failed", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error("Error closing HTTP client", e);
            }
        }
        return null;

    }

    
}
