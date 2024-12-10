package org.music;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.music.models.DB.Playlists;
import org.music.models.DB.Queue_Tracks;
import org.music.models.DB.Users;
import org.music.models.Queue_Item;
import org.music.models.Search_Tracks.Collection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
                    .append("image", playlist.getImage())
                    .append("status", playlist.getStatus())
                    .append("created_at", playlist.getCreated_at() != null ? playlist.getCreated_at() : new Date());
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
                    String image = doc.getString("image");
                    String status = doc.getString("status");
                    String createAt = doc.getString("created_at");
                    Boolean isPin = doc.getBoolean("is_pin", false);
                    Boolean isDl = doc.getBoolean("is_dl", false);

                    Playlists playlist = new Playlists(id, name, ownerValue, description, isShuffle, image, status, createAt, isPin, isDl);
                    playlistsList.add(playlist);
                });
        return playlistsList;
    }

    public void Update_Playlist(Playlists playlist) {
        try {
            var collection = database.getCollection("Playlists");
            var filter = new org.bson.Document("_id", new ObjectId(playlist.getId()));

            var updateDocument = new org.bson.Document("$set", new org.bson.Document()
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
                    .append("where", song.getWhere());

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
                // Tạo đối tượng Queue_Item từ kết quả truy vấn
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

        // Kiểm tra nếu có ít nhất một bài hát thỏa mãn query
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

    public void Insert_into_queue(Queue_Tracks queue) {
        try {
            MongoCollection<Document> collection = database.getCollection("Queue");
            Document doc = new Document("_id", new ObjectId()) // ID tự động sinh
                    .append("owner", queue.getOwner())
                    .append("is_shuffle", queue.getIs_shuffle())
                    .append("is_loop", queue.getIs_loop());
            collection.insertOne(doc);
            System.out.println("Queue track inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Remove_from_queue(Queue_Tracks queue) {
        try {
            MongoCollection<Document> collection = database.getCollection("Queue");
            Document query = new Document("_id", queue.getId());
            collection.deleteOne(query);
            System.out.println("Queue track removed successfully.");
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

    public String get_Artist(Collection i){
        String nam = (i.getPublisher_metadata() != null && i.getPublisher_metadata().getArtist() != null)
                ? i.getPublisher_metadata().getArtist()
                : i.getUser().getUsername();
        return nam;
    }
}