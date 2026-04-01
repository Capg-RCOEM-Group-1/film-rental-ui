import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

public class TestClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> payload = new HashMap<>();
        payload.put("firstName", "Test");
        payload.put("lastName", "User");
        payload.put("email", "test@test.com");
        payload.put("active", true);
        payload.put("store", "http://localhost:8080/stores/1");
        payload.put("address", "http://localhost:8080/addresses/1");
        
        try {
            String res = restTemplate.postForObject("http://localhost:8080/customers", payload, String.class);
            System.out.println("Success: " + res);
        } catch (Exception e) {
            System.out.println("Error 1: " + e.getMessage());
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
            String res2 = restTemplate.postForObject("http://localhost:8080/customers", request, String.class);
            System.out.println("Success 2: " + res2);
        } catch (Exception e) {
            System.out.println("Error 2: " + e.getMessage());
        }
    }
}
