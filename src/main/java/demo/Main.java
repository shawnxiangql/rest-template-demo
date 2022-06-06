package demo;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Collections;

public class Main {
    private static RestTemplate template = new RestTemplate();
    private static String imageUrl = "https://cdn-images.farfetch-contents.com/15/26/65/82/15266582_36140603_480.jpg";
    private static String uploadUrl = "http://127.0.0.1:8080/upload";
    public static void main(String[] args) {
        byte[] imageBytes = download();
        upload(imageBytes);
    }

    private static void upload(byte[] imageBytes) {
        MultiValueMap<String, Resource> map = new LinkedMultiValueMap<>();
        map.put("image-name.jpg", Collections.singletonList(new InputStreamResource(new ByteArrayInputStream(imageBytes))));

        String uploadResponse = template.postForObject(uploadUrl, map, String.class);
    }

    private static byte[] download() {
        ResponseEntity<byte[]> imageResponse = template
                .getForEntity(imageUrl, byte[].class);
        byte[] body = imageResponse.getBody();
        assert body != null;
        System.out.println("image info: " + imageResponse.getHeaders() + "," + body.length);
        return body;
    }
}
