package cdu.diploma.mediamonitoring.external.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class RedditApi {

    public RedditApi() {

    }

    public String getAccessToken() {
        OkHttpClient client = new OkHttpClient();

        // Retrieve access token using client credentials
        String username = "Spzabt_zz";
        String password = "kLg84146ivW#?";
        String clientId = "iRYQ2DsX1m3mVNgxMzKT9Q";
        String clientSecret = "wypbDxOsceTi6tL1DyjjYGQu8kqgkA";
        String accessTokenUrl = "https://www.reddit.com/api/v1/access_token";

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(clientId, clientSecret)
        );

        HttpClient httpClient = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        HttpPost httpPost = new HttpPost(accessTokenUrl);

        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("grant_type", "password"));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));

        String responseString = "";

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpPost.setHeader("User-Agent", "/u/ user v.1.0");
            HttpResponse response;
            try {
                response = httpClient.execute(httpPost);
                try {
                    responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    System.out.println(responseString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JsonObject jsonObject = JsonParser.parseString(responseString).getAsJsonObject();
        return jsonObject.get("access_token").getAsString();
    }

//    public String getAuthToken(String[] keywords) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//
//        // Retrieve access token using client credentials
//        String clientId = "xLc3NDY1obRUKjiycPraiw";
//        String clientSecret = "o4wkbDEyMyO2z9Si-lUfRx7lzN-ytQ";
//        String username = "Spzabt_zz";
//        String password = "YOUR_PASSWORD";
//        String grantType = "kLg84146ivW#?";
//        String scope = "read";
//        String accessTokenUrl = "https://www.reddit.com/api/v1/access_token";
//        RequestBody formBody = new FormBody.Builder()
//                .add("grant_type", grantType)
//                .add("username", username)
//                .add("password", password)
//                .build();
//        Request request = new Request.Builder()
//                .url(accessTokenUrl)
//                .header("Authorization", Credentials.basic(clientId, clientSecret))
//                .post(formBody)
//                .build();
//        Response response = client.newCall(request).execute();
//        String accessToken = response.body().string();
//
//        // Search for posts based on keywords
//        String searchResponseJson = "";
//        for (String keyword : keywords) {
//            String subreddit = "all";
//            String query = keyword;
//            String sort = "relevance";
//            String searchUrl = String.format("https://www.reddit.com/r/%s/search.json?q=%s&sort=%s", subreddit, query, sort);
//            Request searchRequest = new Request.Builder()
//                    .url(searchUrl)
//                    .header("Authorization", "Bearer " + accessToken)
//                    .build();
//            Response searchResponse = client.newCall(searchRequest).execute();
//            searchResponseJson = searchResponse.body().string();
//        }
//
//
//        // Parse search response JSON to retrieve desired data
//        // ...
//        return searchResponseJson;
//    }
}
