package org.music.getAPI;

import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;
import retrofit2.Call;
import retrofit2.http.*;

public interface SpotifyApiService {
    @GET("v1/artists/{id}")
    Call<Artist> getArtist(@Path("id") String artistId);

    @GET("v1/search")
    Call<SearchArtistsRequest> getArtistByName(@Query("q") String artistName, @Query("type") String type);
}