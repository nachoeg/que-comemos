package ttps.spring.entrega5.util;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class ImgurUploader {

  @Value("${imgur.client.id}")
  private String imgurClientId;

  public String upload(MultipartFile foto) throws IOException {
    OkHttpClient client = new OkHttpClient();

    RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("image", foto.getOriginalFilename(),
            RequestBody.create(foto.getBytes(), MediaType.parse(foto.getContentType())))
        .build();

    Request request = new Request.Builder()
        .url("https://api.imgur.com/3/image")
        .post(requestBody)
        .addHeader("Authorization", "Client-ID " + imgurClientId)
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }
      String responseBody = response.body().string();
      String fotoUrl = new ObjectMapper().readTree(responseBody).path("data").path("link").asText();
      return fotoUrl;
    }
  }
}