package com.rcoem.filmrentalui.service;

import com.rcoem.filmrentalui.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class StaffApiService {

    private final String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public StaffApiService(@Value("${api.base-url}") String baseUrl) {
        this.baseUrl = baseUrl + "/staffs";
    }

    /**
     * Get all staff with pagination using manual URL string
     */
    public StaffPageResponse getAllStaff(int page, int size) {
        String url = baseUrl + "?page=" + page + "&size=" + size;
        return restTemplate.getForObject(url, StaffPageResponse.class);
    }

    /**
     * Get staff by active status using manual URL string
     */
    public StaffPageResponse getStaffByActiveStatus(boolean active, int page, int size) {
        String url = baseUrl + "/search/findByActive?active=" + active + "&page=" + page + "&size=" + size;
        return restTemplate.getForObject(url, StaffPageResponse.class);
    }

    /**
     * Search by FirstName, LastName, Email, or Username using manual URL string
     */
    public StaffPageResponse searchStaff(String searchTerm, int page, int size) {
        String endpoint = "/search/findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrUsernameContainingIgnoreCase";

        String url = baseUrl + endpoint +
                "?firstName=" + searchTerm +
                "&lastName=" + searchTerm +
                "&email=" + searchTerm +
                "&username=" + searchTerm +
                "&page=" + page +
                "&size=" + size;

        return restTemplate.getForObject(url, StaffPageResponse.class);
    }

    // CREATE
    public void saveStaff(StaffDTO staff) {
        staff.setStore(baseUrl.replace("/staff","/stores")+"/"+staff.getStore());
        staff.setAddress(baseUrl.replace("/staff","addresses/")+"/"+staff.getAddress());
        restTemplate.postForObject(baseUrl, staff, StaffDTO.class);
    }

    // UPDATE
    public void updateStaff(Byte id, StaffDTO staff) {
        if (staff.getStore() != null) {
            staff.setStore(baseUrl.replace("/staff","/stores")+"/"+staff.getStore());
        }
        if (staff.getAddress() != null) {
            staff.setAddress(baseUrl.replace("/staff","addresses/")+"/"+staff.getAddress());
        }
        restTemplate.put(baseUrl + "/" + id, staff);
    }

    // DELETE
    public void deleteStaff(Byte id) {
        restTemplate.delete(baseUrl + "/" + id);
    }

    // GET SINGLE (for editing)
    public StaffDTO getStaffById(Byte id) {
        // 1. Get the raw response from the backend
        String url = baseUrl +"/"+ id;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        StaffDTO dto = new StaffDTO();
        dto.setStaffId(id);
        dto.setFirstName((String) response.get("firstName"));
        dto.setLastName((String) response.get("lastName"));
        dto.setEmail((String) response.get("email"));
        dto.setUsername((String) response.get("username"));
        dto.setActive((Boolean) response.get("active"));

        return dto;
    }

    // Helper method to parse the ID from "http://.../1"
    private Short extractIdFromLink(Map<String, Object> links, String relation) {
        try {
            Map<String, String> rel = (Map<String, String>) links.get(relation);
            String href = rel.get("href").replaceAll("\\{.*\\}", ""); // Remove {?projection}
            if (href.endsWith("/")) href = href.substring(0, href.length() - 1);
            return Short.parseShort(href.substring(href.lastIndexOf('/') + 1));
        } catch (Exception e) {
            return null;
        }
    }

}