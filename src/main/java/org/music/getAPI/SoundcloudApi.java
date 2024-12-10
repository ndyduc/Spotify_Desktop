package org.music.getAPI;

import org.music.models.Albums;
import org.music.models.Songs;
import org.music.models.Users;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import org.music.models.Search_Tracks.Collection;

public interface SoundcloudApi {
    // API v2 cho phép tìm kiếm bài hát với nhiều tham số hơn
    @GET("search/tracks")
    Call<Songs> searchTracks(
            @Query("q") String query,                // Từ khóa tìm kiếm
            @Query("client_id") String clientId,     // Client ID của ứng dụng
            @Query("limit") int limit,               // Giới hạn số bài hát trả về
            @Query("offset") int offset,             // Phân trang
            @Query("linked_partitioning") int linkedPartitioning  // Để phân trang liên kết (thường là 1)
    );

    @GET("tracks/{trackid}")
    Call<Collection> getTracks(
            @Path("trackId") int trackId,
            @Query("client_id") String clientid
    );

    @GET("search/users")
    Call<Users> searchUsers(
            @Query("q") String query,
            @Query("client_id") String client_id,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("linked_partitioning") int linkedPartitioning
    );

    @GET("users/{user_id}")
    Call<org.music.models.Search_User.Collection> getUserbyid(
            @Path("user_id") int userid,
            @Query("client_id") String client_id
    );

    @GET("search/playlists")
    Call<Albums> searchAlbum(
            @Query("q") String query,
            @Query("client_id") String client_id,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("linked_partitioning") int linkedPartitioning
    );

    @GET("tracks/{track_id}")
    Call<Collection> get_track_by_id(
            @Path("track_id") int trackId,
            @Query("client_id") String clientId
    );
 }