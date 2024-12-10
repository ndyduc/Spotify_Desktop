package org.music.getAPI;

import com.google.gson.Gson;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class Retrofit_client {
    private static final String BASE_URL = "https://api.spotify.com/";
    private static Retrofit retrofit = null;

    public static String getAccessToken() {
        String artist = "abc";
        Retrofit_client spotifyx = new Retrofit_client();
        spotifyx.GetArtistbyID(artist);
        spotifyx.Searching("Cigaret after sex");
        return getaccess();
    }

    public static Retrofit getClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Authorization", "Bearer " + getAccessToken()) // Thêm Access Token vào header
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static final String clientId = "a3eec1de559f459fbe57cd844753ecac";
    private static final String clientSecret = "c59464b43d914398acf70df7ee9921ef";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:8080/callback")) // URI chuyển hướng
            .build();

    public static String getaccess(){
        try {
            final ClientCredentials clientCredentials = spotifyApi.clientCredentials()
                    .build()
                    .execute();

            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            return clientCredentials.getAccessToken();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(Retrofit_client.class);

    //dung retrofit goi api chinh
    public void GetArtistbyID(String id) {
        SpotifyApiService apiService = Retrofit_client.getClient().create(SpotifyApiService.class);
        Call<Artist> call = apiService.getArtist(id);

        call.enqueue(new Callback<Artist>() {
            @Override
            public void onResponse(Call<Artist> call, retrofit2.Response<Artist> response) {
                if (response.isSuccessful()) {
                    Artist artist = response.body();
                    Gson gson = new Gson();
                    String json = gson.toJson(artist);
                    logger.info(json);
                } else {
                    logger.error("Response Error: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<Artist> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //dung framework cua spotify
    public void Searching(String name) {
        String accessToken = Retrofit_client.getAccessToken();
        SearchArtistsRequest request = new SearchArtistsRequest.Builder(accessToken)
                .q(name)
                .limit(20)
                .build();

        try {
            Paging<Artist> artists = request.execute();
            System.out.println("Total artists found: " + artists.getTotal()); // Using getTotal() method
            for (Artist artist : artists.getItems()) {
                System.out.println("\n#############"+artist.getId());
                System.out.println("Artist Name: " + artist.getName());
                System.out.println("Spotify URL: " + artist.getUri());
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            logger.error(e.getMessage());
        }
    }
}