package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @RequestMapping("/upload")
    public String upload(HttpServletRequest request, @RequestHeader Map headers) {
        System.out.printf("receive request: %s %s %s \n", request.getMethod(), request.getRequestURL(), headers.toString());
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
            System.out.println("ContentLength: " + req.getContentLength());
            return "ok:" + req.getContentLength() + "," + req.getMultiFileMap().values();
        }
        return "error";
    }
}
