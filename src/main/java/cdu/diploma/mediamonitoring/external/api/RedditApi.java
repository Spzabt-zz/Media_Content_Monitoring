package cdu.diploma.mediamonitoring.external.api;

import cdu.diploma.mediamonitoring.domain.model.ApiCredentials;
import cdu.diploma.mediamonitoring.domain.model.User;
import cdu.diploma.mediamonitoring.domain.repo.ApiCredentialsRepo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class RedditApi {
    private final ApiCredentialsRepo apiCredentialsRepo;

    public RedditApi(ApiCredentialsRepo apiCredentialsRepo) {
        this.apiCredentialsRepo = apiCredentialsRepo;
    }

    public String getAccessToken(User user) {
        OkHttpClient client = new OkHttpClient();

        ApiCredentials apiCredentials = apiCredentialsRepo.findApiCredentialsByUserId(user.getId());

        String username = apiCredentials.getRedditUsername();
        String password = apiCredentials.getRedditPassword();
        String clientId = apiCredentials.getRedditClientId();
        String clientSecret = apiCredentials.getRedditClientSecret();
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
}
