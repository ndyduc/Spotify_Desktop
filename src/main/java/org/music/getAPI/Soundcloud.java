package org.music.getAPI;

import org.music.MongoDB;
import org.music.models.*;
import org.music.models.Search_User.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class Soundcloud {
    static MongoDB mongo = new MongoDB();
    private static final Logger logger = LoggerFactory.getLogger(Soundcloud.class);
    private static final String BASE_URL = "https://api-v2.soundcloud.com/";
    private static Retrofit retrofit = null;
    private static final String clientid = mongo.Get_Client_ID();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public Queue_Item getQueue_Item(String query) {
        SoundcloudApi apiService = getClient().create(SoundcloudApi.class);
        Call<Songs> call = apiService.searchTracks(query, clientid, 1, 0, 1);
        try {
            Response<Songs> response = call.execute();
            if (response.isSuccessful()) {
                Songs soundcloudResponse = response.body();
                assert soundcloudResponse != null;
                List<org.music.models.Search_Tracks.Collection> tracks = soundcloudResponse.getCollection();

                if (tracks != null && !tracks.isEmpty()) {
                    Queue_Item item = new Queue_Item();
                    item.setTitle(tracks.getFirst().getTitle());
                    item.setArtist(tracks.getFirst().getUser().getUsername());
                    item.setLink(tracks.getFirst().getPermalink_url());
                    item.setFileName(tracks.getFirst().getTitle()+" - "+tracks.getFirst().getUser().getUsername()+".mp3");
                    item.setImgCover(tracks.getFirst().getArtwork_url());
                    item.setArtist_id(tracks.getFirst().getUser().getId());
                    item.setDuration(tracks.getFirst().getDuration());
                    item.setGenre(tracks.getFirst().getGenre());
                    return item;
                } else {
                    System.out.println("No tracks found");
                    return new Queue_Item();
                }
            } else {
                System.out.println("Error: " + response.code());
                return new Queue_Item();
            }
        } catch (IOException e) {
            System.out.println("Failure: " + e.getMessage());
            return new Queue_Item();
        }
    }

    public org.music.models.Search_Tracks.Collection get_track_by_id(int id) {
        SoundcloudApi apiService = getClient().create(SoundcloudApi.class);
        Call<org.music.models.Search_Tracks.Collection> call = apiService.get_track_by_id(id, clientid);
        try{
            Response<org.music.models.Search_Tracks.Collection> response = call.execute();
            if (response.isSuccessful()) {
                org.music.models.Search_Tracks.Collection collection = response.body();
                return collection;
            } else return null;
        } catch (IOException e) {
            System.out.println("Failure: " + e.getMessage());
            return  null;
        }

    }

    public Songs get_n_Tracks(String query, int limit) {
        SoundcloudApi apiService = getClient().create(SoundcloudApi.class);
        Call<Songs> call = apiService.searchTracks(query, clientid, limit, 0, 1);
        try {
            Response<Songs> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else return null;
        } catch (IOException e) {
            System.out.println("Failure: " + e.getMessage());
            return null;
        }
    }

    public Users getUsers(String query, int limit) {
        SoundcloudApi apiService = getClient().create(SoundcloudApi.class);
        Call<Users> call = apiService.searchUsers(query, clientid, limit, 0, 1);
        try {
            Response<Users> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                System.out.println("Error: " + response.code());
                return null;
            }
        } catch (IOException e) {
            System.out.println("Failure: " + e.getMessage());
            return null;
        }
    }

    public Collection get_user_by_id(int id) {
        SoundcloudApi apiService = getClient().create(SoundcloudApi.class);
        Call<Collection> call = apiService.getUserbyid(id, clientid);
        try {
            Response<Collection> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else return null;
        } catch (IOException e){
            System.out.println("Failure: " + e.getMessage());
            return null;
        }
    }

    public Collection getBestUser(String query) {
        SoundcloudApi apiService = getClient().create(SoundcloudApi.class);
        Call<Users> call = apiService.searchUsers(query, clientid, 1, 0, 1);
        try {
            Response<Users> response = call.execute();
            if (response.isSuccessful()) {
                Users soundcloudResponse = response.body();
                List<Collection> users = soundcloudResponse.getCollection();
                if (users == null || users.isEmpty()) { return null; }

                Collection bestUser = users.getFirst();
                for (Collection collection : users) {
                    if (collection.getFollowers_count() > bestUser.getFollowers_count()) {
                        bestUser = collection;
                    }
                }
                return bestUser;
            } else {
                System.out.println("API error: " + response.errorBody().string());
                return null;
            }
        } catch (IOException e) {
            System.out.println("Failure: " + e.getMessage());
            return null;
        }
    }

    public Albums get_Albums(String query, int limit) {
        SoundcloudApi apiService = getClient().create(SoundcloudApi.class);
        Call<Albums> call = apiService.searchAlbum(query, clientid, limit, 0, 1);
        try{
            Response<Albums> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else return null;
        } catch (IOException e) {
            System.out.println("Failure: " + e.getMessage());
            return null;
        }
    }

    public String IMG500x500(String url) {
        if (url != null && url.endsWith("large.jpg")) {
            return url.replace("large.jpg", "t500x500.jpg");
        }
        return url;
    }
}