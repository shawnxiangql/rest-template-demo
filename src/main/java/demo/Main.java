package demo;

import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;

public class Main {
    private static RestTemplate template = new RestTemplate();
    private static String imageUrl = "https://cdn-images.farfetch-contents.com/15/26/65/82/15266582_36140603_480.jpg";
    private static String uploadUrl = "http://127.0.0.1:8080/upload";
    private static String tencentUrl = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=57_YQjbpu4S-nbDbQOC0IRZ_8V3gfZLovx1N0zjG98HoeZOjhekwOEhAmKUeyTkL8Qe3jfr9JGAxdU1fRvLsbo_wjpvh4Svf7rdO6fYQsTy0tRAspRhmyD1J8wgWLgqYUJjRjicnFCAz0lLA2jLXMDeAFAGVM" +
            "&type=image";

    public static void main(String[] args) {
        template.setInterceptors(Collections.singletonList(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                System.out.println(">>" + request.getHeaders());
                System.out.println(">>" + Streams.asString(new ByteArrayInputStream(body)));
                return execution.execute(request, body);
            }
        }));
        byte[] imageBytes = download();
        upload(imageBytes);
    }

    private static void upload(byte[] imageBytes) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.put("media", Collections.singletonList(new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return "a.png";
            }
        }));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        String uploadResponse = template.postForObject(tencentUrl, requestEntity, String.class);
        System.out.println("============" + uploadResponse);
        System.out.println();
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
