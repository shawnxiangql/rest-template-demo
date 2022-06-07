package demo;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class UploadTest {

    private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    private static void upload2() throws Exception {
        String uploadUrl = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=57_Erm-7ST-603j8q_z3i0ui5Bf43VgBDSU5YZcih4u1Lxah0YIvlCrprN35nezv7oWoPthXVT9ujELKtMLYrvQH6LWZ-jSxk8DX70qReMrXC-Vfdg_3JkRnNrn_AajgcgembAjMK_lcqNCQ2O6EZQcAFAUPG&type=images";

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("https");
        uriBuilder.setHost("api.weixin.qq.com");
        uriBuilder.setPath("cgi-bin/media/upload");
        uriBuilder.setCustomQuery("access_token=57_YQjbpu4S-nbDbQOC0IRZ_8V3gfZLovx1N0zjG98HoeZOjhekwOEhAmKUeyTkL8Qe3jfr9JGAxdU1fRvLsbo_wjpvh4Svf7rdO6fYQsTy0tRAspRhmyD1J8wgWLgqYUJjRjicnFCAz0lLA2jLXMDeAFAGVM&type=images");
        URI build = uriBuilder.build();


        HttpPost httpPost = new HttpPost(build);


        InputStream inputStream = cdnUrl2InputStream("https://cdn-images.farfetch-contents.com/15/26/65/82/15266582_36140603_480.jpg");
        File file = new File("/Users/xuezhe.jin/workspace/wechat-oa-outbound-gateway/2.jpg");
        copyInputStreamToFile(inputStream, file);


        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addPart("media", new FileBody(file));
        HttpEntity entity = multipartEntityBuilder.build();
        httpPost.setEntity(entity);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(httpPost);
        InputStream is = response.getEntity().getContent();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder buffer = new StringBuilder();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        System.out.println("发送消息收到的返回："+buffer.toString());
        in.close();
        is.close();
    }


    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        // append = false
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

    }

    public static String upload(String filePath, String accessToken, String type) throws Exception {
//        File file = new File(filePath);


        InputStream inputStream = cdnUrl2InputStream("https://cdn-images.farfetch-contents.com/15/26/65/82/15266582_36140603_480.jpg");

//        File file = new File("/Users/xuezhe.jin/workspace/wechat-oa-outbound-gateway/2.jpg");
//
//        copyInputStreamToFile(inputStream, file);
//
//        if (!file.exists() || !file.isFile()) {
//            throw new IOException("文件不存在");
//        }

        String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);

        URL urlObj = new URL(url);
        //连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "multipart/form-data;");


        //设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + "a.png" + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        byte[] head = sb.toString().getBytes(StandardCharsets.UTF_8);

        //获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        //输出表头
        out.write(head);

        //文件正文部分
        //把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(inputStream);
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        //结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

        out.write(foot);

        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
            //定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        System.out.println(result);
        return "";
    }


    public static void main(String[] args) throws Exception {
        upload("/Users/xuezhe.jin/workspace/wechat-oa-outbound-gateway/1.jpg", "57_YQjbpu4S-nbDbQOC0IRZ_8V3gfZLovx1N0zjG98HoeZOjhekwOEhAmKUeyTkL8Qe3jfr9JGAxdU1fRvLsbo_wjpvh4Svf7rdO6fYQsTy0tRAspRhmyD1J8wgWLgqYUJjRjicnFCAz0lLA2jLXMDeAFAGVM", "image");
    }

    private static InputStream cdnUrl2InputStream(String url) throws Exception {

        return new ByteArrayInputStream(Files.readAllBytes(Paths.get("./img.png")));
    }
}
