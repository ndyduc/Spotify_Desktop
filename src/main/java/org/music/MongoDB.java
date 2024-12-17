package org.music;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.music.models.DB.Love_Artists;
import org.music.models.DB.Playlists;
import org.music.models.DB.Queue_Tracks;
import org.music.models.DB.Users;
import org.music.models.Queue_Item;
import org.music.models.Search_Tracks.Collection;

import java.io.File;
import java.util.*;

public class MongoDB {
    MongoDatabase database;
    public MongoDB() {
        String uri = "mongodb://localhost:30202";

        try {
            MongoClient mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase("ndyduc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String Get_Client_ID() {
        try {
            MongoCollection<Document> collection = database.getCollection("SoundCloud_API_client_id");

            Document doc = collection.find().first();
            if (doc != null) {
                return doc.getString("client_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Nếu không có tài liệu phù hợp, trả về null
    }

    public void Insert_Love_Artist(Love_Artists loveArtist) {
        try {
            MongoCollection<Document> collection = database.getCollection("Love_Artists");
            Bson filter = Filters.and(
                    Filters.eq("Owner", "_ndyduc_"),
                    Filters.eq("Id_artist", loveArtist.getArtist_id())
            );

            // Kiểm tra xem có tài liệu nào thỏa mãn bộ lọc không
            Document existingDocument = collection.find(filter).first();

            if (existingDocument == null) {
                Document doc = new Document("_id", new ObjectId()) // ID tự động sinh
                        .append("Id_artist", loveArtist.getArtist_id())
                        .append("Owner", "_ndyduc_")
                        .append("Name_Artist", loveArtist.getArtist_name())
                        .append("Image", loveArtist.getArtist_img());

                collection.insertOne(doc);
            } else {
                // Nếu có tài liệu trùng, không thực hiện insert
                System.out.println("Love Artist with the same Owner and Id_artist already exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Delete_Love_Artist(Love_Artists loveArtist) {
        try {
            MongoCollection<Document> collection = database.getCollection("Love_Artists");
            String idAsString = loveArtist.getId();
            ObjectId id = new ObjectId(idAsString);

            Bson filter = Filters.eq("_id", id);
            DeleteResult result = collection.deleteOne(filter);
            if (result.getDeletedCount() > 0) System.out.println("Love Artist deleted successfully.");
            else System.out.println("No document found with the given _id.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Love_Artists> Get_Love_Artists_By_Owner(String owner) {
        List<Love_Artists> loveArtists = new ArrayList<>();
        try {
            MongoCollection<Document> collection = database.getCollection("Love_Artists");
            Bson filter = Filters.eq("Owner", owner);

            FindIterable<Document> results = collection.find(filter);
            for (Document doc : results) {
                Love_Artists artist = new Love_Artists(
                        doc.getObjectId("_id").toHexString(),
                        owner,
                        doc.getInteger("Id_artist"),
                        doc.getString("Name_Artist"),
                        doc.getString("Image")
                );
                loveArtists.add(artist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loveArtists;
    }

    public void Insert_User(Users user) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users");
            Document doc = new Document("_id", new ObjectId()) // ID tự động sinh
                    .append("username", user.getUsername())
                    .append("password", user.getPassword())
                    .append("email", user.getEmail());
            collection.insertOne(doc);
            System.out.println("User inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Insert a playlist into the Playlists collection
    public void Insert_into_playlist(Playlists playlist) {
        try {
            MongoCollection<Document> collection = database.getCollection("Playlists");
            Document doc = new Document("_id", new ObjectId()) // ID tự động sinh
                    .append("name", playlist.getName())
                    .append("owner", playlist.getOwner())
                    .append("description", playlist.getDescription())
                    .append("is_shuffle", playlist.getIs_shuffle())
                    .append("Image", playlist.getImage())
                    .append("status", playlist.getStatus())
                    .append("created_at", playlist.getCreated_at() != null ? playlist.getCreated_at() : new Date())
                    .append("is_dl", false)
                    .append("is_pin", false);
            collection.insertOne(doc);
            System.out.println("Playlist inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int Count_Playlists() {
        try {
            MongoCollection<Document> collection = database.getCollection("Playlists");
            long count = collection.countDocuments();
            return (int) count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Playlists> get_Playlists(String owner) {
        List<Playlists> playlistsList = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("Playlists");

        collection.find(Filters.eq("owner", owner))
                .sort(Sorts.descending("is_pin")) // Sắp xếp theo trường is_pin đảo ngược
                .forEach(doc -> {
                    String id = doc.getObjectId("_id") != null ? doc.getObjectId("_id").toString() : null;
                    String name = doc.getString("name");
                    String ownerValue = doc.getString("owner");
                    String description = doc.getString("description");
                    Boolean isShuffle = doc.getBoolean("is_shuffle", false); // Mặc định là false nếu không có
                    String image = doc.getString("Image");
                    String status = doc.getString("status");
                    String createAt = doc.getString("created_at");
                    Boolean isPin = doc.getBoolean("is_pin", false);
                    Boolean isDl = doc.getBoolean("is_dl");

                    Playlists playlist = new Playlists(id, name, ownerValue, description, isShuffle, image, status, createAt, isPin, isDl);
                    playlistsList.add(playlist);
                });
        return playlistsList;
    }

    public void Update_Playlist(Playlists playlist, Boolean img) {
        try {
            var collection = database.getCollection("Playlists");
            var filter = new org.bson.Document("_id", new ObjectId(playlist.getId()));

            var updateDocument = new org.bson.Document("$set", new org.bson.Document()
                    .append("name", playlist.getName())
                    .append("description", playlist.getDescription())
            );
            List<Playlists> i = get_Playlists(playlist.getOwner());
            if(img) updateDocument = new org.bson.Document("$set", new org.bson.Document()
                        .append("name", playlist.getName())
                        .append("description", playlist.getDescription())
                        .append("Image", playlist.getImage())
                        );


            collection.updateOne(filter, updateDocument);

            System.out.println("Playlist updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating playlist: " + e.getMessage());
        }
    }

    public void DL_Playlist(Playlists playlist) {
        try {
            var collection = database.getCollection("Playlists");
            var filter = new org.bson.Document("_id", new ObjectId(playlist.getId()));
            if(!playlist.getIs_dl()) System.out.println("?????????????????");
            var updateDocument = new org.bson.Document("$set", new org.bson.Document()
                    .append("is_dl", playlist.getIs_dl())
            );
            collection.updateOne(filter, updateDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove_Playlist(String playlistId) {
        MongoCollection<Document> collection = database.getCollection("Playlists");
        Bson filter = Filters.eq("_id", new ObjectId(playlistId));
        DeleteResult result = collection.deleteOne(filter);
    }

    public void Insert_Song(Queue_Item song) {
        try {
            MongoCollection<Document> collection = database.getCollection("Songs");

            Document doc = new Document("_id", new ObjectId()) // ID tự động sinh
                    .append("ImgCover", song.getImgCover())
                    .append("title", song.getTitle())
                    .append("artist", song.getArtist())
                    .append("album", song.getAlbum())
                    .append("fileName", song.getTitle()+" - "+song.getArtist()+".mp3")
                    .append("link", song.getLink())
                    .append("where", song.getWhere())
                    .append("artist_id", song.getArtist_id())
                    .append("duration", song.getDuration())
                    .append("genre", song.getGenre());

            collection.insertOne(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Queue_Item> getSongsByWhere(String where) {
        List<Queue_Item> songs = new ArrayList<>();
        try {
            MongoCollection<Document> collection = database.getCollection("Songs");

            Document query = new Document("where", where);

            for (Document doc : collection.find(query)) {
                Queue_Item item = new Queue_Item();
                item.setMongoID(doc.getObjectId("_id").toString());
                item.setImgCover(doc.getString("ImgCover"));
                item.setTitle(doc.getString("title"));
                item.setArtist(doc.getString("artist"));
                item.setAlbum(doc.getString("album"));
                item.setFileName(doc.getString("fileName"));
                item.setLink(doc.getString("link"));
                item.setWhere(doc.getString("where"));
                item.setArtist_id(doc.getInteger("artist_id"));
                item.setDuration(doc.getInteger("duration"));
                item.setGenre(doc.getString("genre"));

                songs.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    // Hàm kiểm tra xem bài hát đã tồn tại trong cơ sở dữ liệu chưa dựa trên "link" và "where"
    public boolean isSongExists(String link, String where) {
        MongoCollection<Document> collection = database.getCollection("Songs");
        Document query = new Document("link", link).append("where", where);
        return collection.countDocuments(query) > 0;
    }

    public void Remove_Song(String id) {
        try {
            MongoCollection<Document> collection = database.getCollection("Songs");
            ObjectId objectId = new ObjectId(id);
            Bson filter = Filters.eq("_id", objectId);

            collection.deleteOne(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add playlist to Love_Artists collection
    public void Add_to_love(Playlists playlist) {
        try {
            MongoCollection<Document> collection = database.getCollection("Love_Artists");
            Document doc = new Document("_id", new ObjectId()) // ID tự động sinh
                    .append("playlist_id", playlist.getId())
                    .append("name", playlist.getName())
                    .append("owner", playlist.getOwner());
            collection.insertOne(doc);
            System.out.println("Playlist added to Love_Artists successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Remove_from_love(int userId, int artistId) {
        try {
            var collection = database.getCollection("love_artists");
            var filter = new org.bson.Document("user_id", userId)
                    .append("artist_id", artistId);

            var result = collection.deleteOne(filter);

            if (result.getDeletedCount() > 0) {
                System.out.println("Removed artist from user's love list successfully!");
            } else {
                System.out.println("No matching artist found for this user to remove.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error removing artist from love list: " + e.getMessage());
        }
    }

    public String File_name(Collection i){
        String nam = (i.getPublisher_metadata() != null && i.getPublisher_metadata().getArtist() != null)
                ? i.getPublisher_metadata().getArtist()
                : i.getUser().getUsername();

        String file = i.getTitle()+" - "+nam+".mp3";
        return file;
    }

    public String get_Artist(Collection i){
        String nam = (i.getPublisher_metadata() != null && i.getPublisher_metadata().getArtist() != null)
                ? i.getPublisher_metadata().getArtist()
                : i.getUser().getUsername();
        return nam;
    }

    public boolean checkFile(String fileName) {
        File folder1 = new File("./mp3_queue/"+fileName);
        File folder2 = new File("./mp3_playlist/"+fileName);
        return folder1.exists() || folder2.exists();
    }

    public String Spaces(int number) {
        return String.format("%,d", number).replace(",", " ");
    }

    public int get_Duration(int durationmillis){
        return durationmillis/1000;
    }
    public String get_duration(int durationMillis) {
        int totalSeconds = durationMillis / 1000; // Chuyển sang giây
        int minutes = totalSeconds / 60;         // Tính số phút
        int seconds = totalSeconds % 60;         // Tính số giây còn lại

        // Trả về chuỗi định dạng "m:ss"
        return String.format("%d:%02d", minutes, seconds);
    }
}