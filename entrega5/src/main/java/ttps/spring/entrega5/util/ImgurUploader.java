package ttps.spring.entrega5.util;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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

      // Parse the response to get the URL
      String responseBody = response.body().string();
      // Assuming the response is in JSON format and contains a field "link" with the
      // URL
      // You can use a JSON library like Jackson or Gson to parse the response
      // Here, we'll just extract the URL using a simple string manipulation
      String fotoUrl = responseBody.substring(responseBody.indexOf("\"link\":\"") + 8);
      fotoUrl = fotoUrl.substring(0, fotoUrl.indexOf("\""));
      return fotoUrl;
    }
  }
}