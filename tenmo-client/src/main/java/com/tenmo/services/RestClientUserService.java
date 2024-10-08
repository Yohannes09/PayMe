//package com.tenmo.services;
//
//import com.tenmo.security.model.User;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Optional;
//
//public class RestClientUserService implements ClientUserService{
//    private static final String ENDPOINT = "http://localhost:8080:api/tenmo/user";
//
//    private String token;
//
//    private final RestTemplate restTemplate;
//
//    public RestClientUserService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public RestClientUserService() {
//        this.restTemplate = new RestTemplate();
//    }
//
//    @Override
//    public Optional<User> getUserById(int userId) {
//        String url = String.format("%s/%d", ENDPOINT, userId);
//
//        try{
//            return Optional.ofNullable(restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    getEntityWithBearer(),
//                    User.class
//            ).getBody());
//
//        }catch (RestClientException clientException){
//            System.out.println("Error: " + clientException.getMessage());
//            return Optional.empty();
//        }
//    }
//
//    public void setToken(String token){
//        this.token = token;
//    }
//
//    public HttpEntity getEntityWithBearer(){
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setBearerAuth(token);
//        return new HttpEntity(httpHeaders);
//    }
//}
