package org.music.Activity;

import org.music.Components.Border_Radius;
import org.music.Components.Button_Radius;
import org.music.Components.RoundedPanel;
import org.music.Components.Rounded_Label;
import org.music.Func.Stream;
import org.music.getAPI.Soundcloud;
import org.music.models.Albums;
import org.music.models.Queue_Item;
import org.music.models.Search_User.Collection;
import org.music.models.Songs;
import org.music.models.Users;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.music.Activity.Home.createButton;
import static org.music.Activity.Home.loadIcon;

public class Search_all extends Border_Radius {
    Soundcloud sc = new Soundcloud();
    CardLayout layout = new CardLayout();
    JPanel main = new JPanel(layout);
    private String key;
    Frame frame;
    Stream stream;
    JPanel S_all = new JPanel(new BorderLayout());
    JPanel S_Track = new JPanel(new BorderLayout());
    JPanel S_Album = new JPanel(new BorderLayout());
    JPanel S_Artist = new JPanel(new BorderLayout());
    JPanel S_Playlist = new JPanel(new BorderLayout());
    Button_Radius all;
    Button_Radius profiles;
    Button_Radius songs;
    Button_Radius albums;
    Button_Radius playlists;
    JPanel ein;
    JPanel head_right = new JPanel(new BorderLayout());
    JPanel head_left = new JPanel(new BorderLayout());
    JPanel cxz = new JPanel(new BorderLayout());
    JPanel head = new JPanel(new BorderLayout(50,0));

    JPanel zwei;
    JPanel drei = new JPanel(new GridLayout(0,1));
    JLabel alb = new JLabel("Album");
    JPanel vier = new JPanel(new GridBagLayout());
    JPanel funf = new JPanel(new GridBagLayout());
    Songs sogs = sc.get_n_Tracks(this.key, 100);
    Home home;
    int width;

    public Search_all(int radius, String keyword, Stream stream, JFrame window, Home home) {
        super(radius);
        this.key = keyword;
        this.stream = stream;
        this.frame = window;
        this.home = home;
        setLayout(new BorderLayout());
        setBackground(Color.decode("#1a1a1a"));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        this.sogs = sc.get_n_Tracks(this.key, 100);

        Panel kinds = new Panel(new FlowLayout(FlowLayout.LEFT));
        kinds.setBackground(Color.decode("#1a1a1a"));
        all = new Button_Radius("All", 30);
        profiles = new Button_Radius("Artists", 30);
        songs = new Button_Radius("Songs", 30);
        albums = new Button_Radius("Albums", 30);
        playlists = new Button_Radius("Playlists", 30);

        all.setBackground(Color.WHITE);
        all.setForeground(Color.BLACK);
        kinds.add(all);
        kinds.add(profiles);
        kinds.add(songs);
        kinds.add(albums);
        kinds.add(playlists);

        add(kinds, BorderLayout.NORTH);

        main.setBackground(Color.decode("#1a1a1a"));
        main.add(S_all, "Search_all");
        main.add(S_Artist, "Search_Artist");
        main.add(S_Track, "Search_Song");
        main.add(S_Album, "Search_Album");
        main.add(S_Playlist, "Search_Playlist");

        all.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layout.show(main, "Search_all");
                all.setForeground(Color.BLACK);
            }
        });

        profiles.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layout.show(main,"Search_Artist");
                profiles.setBackground(Color.white);
            }
        });

        songs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layout.show(main,"Search_Song");
                songs.setBackground(Color.white);
            }
        });

        albums.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layout.show(main,"Search_Album");
                albums.setBackground(Color.white);
            }
        });

        playlists.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layout.show(main,"Search_Playlist");
                playlists.setBackground(Color.white);
            }
        });

        JPanel abc = new JPanel(new BorderLayout());
        abc.setBackground(Color.decode("#1a1a1a"));
        abc.add(main);
        JScrollPane main_all = new JScrollPane(abc);
        main_all.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        main_all.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        main_all.getVerticalScrollBar().setUnitIncrement(16);
        main_all.setBorder(null);
        add(main_all, BorderLayout.CENTER);

        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension windowSize = window.getSize();
                if (windowSize.width == screenSize.width && windowSize.height == screenSize.height |
                        window.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    head_left.setPreferredSize(new Dimension(550, 310));
                    head_right.setPreferredSize(new Dimension(600,310));
                    width = 1150;
                    refresh_drei(700, sogs);

                } else if (window.getExtendedState() == JFrame.NORMAL) {
                    head_left.setPreferredSize(new Dimension(300, 310));
                    head_right.setPreferredSize(new Dimension(300,310));
                    width = 860;
                    refresh_drei(250, sogs);
                }
                head_left.revalidate();
                head_right.revalidate();
                head_right.revalidate();
                head_right.repaint();
                cxz.revalidate();
                cxz.repaint();

            }
        });
    }

    public void setKey(String key) {
        this.key = key;
        panel_all(S_all, stream);
        panel_Artists(S_Artist, stream);
        panel_Album(S_Album, vier, true);
        panel_Album(S_Playlist, funf,  false);

        this.sogs = sc.get_n_Tracks(this.key, 100);
        panel_Tracks(S_Track);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = frame.getSize();
        if (windowSize.width == screenSize.width && windowSize.height == screenSize.height |
                frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            head_left.setPreferredSize(new Dimension(550, 310));
            head_right.setPreferredSize(new Dimension(600,310));
            refresh_drei(700, sogs);

        } else if (frame.getExtendedState() == JFrame.NORMAL) {
            head_left.setPreferredSize(new Dimension(300, 310));
            head_right.setPreferredSize(new Dimension(300,310));
            refresh_drei(250, this.sogs);
        }
        revalidate();
        repaint();
    }

    private void panel_all(JPanel div,Stream stream) {
        div.removeAll();
        Collection best_user = sc.getBestUser(key);
        Albums albums = sc.get_Albums(key, 100);
        Songs trac = sc.get_n_Tracks(key,4);
        List<org.music.models.Search_Tracks.Collection> tracks = trac.getCollection();

        ein = new JPanel(new BorderLayout(20,10));
        ein.setBackground(Color.decode("#1a1a1a"));

        get_head_all(best_user, tracks);
        JPanel asd = new JPanel(new BorderLayout(5,0));
        asd.setBackground(Color.decode("#1a1a1a"));
        asd.add(head_left, BorderLayout.WEST);
        asd.add(head_right, BorderLayout.EAST);
        ein.add(asd, BorderLayout.WEST);
        JPanel cen = new JPanel(new BorderLayout());
        JPanel bot = new JPanel(new BorderLayout());
        get_cen_bot(cen, bot, albums);

        div.setBorder(new EmptyBorder(20,0,0,0));
        div.setBackground(Color.decode("#1a1a1a"));
        JPanel stift = new JPanel(new BorderLayout());
        stift.add(cen, BorderLayout.NORTH);
        stift.add(bot, BorderLayout.SOUTH);
        JPanel fdf = new JPanel(new BorderLayout());
        fdf.setBackground(Color.decode("#1a1a1a"));

        fdf.add(ein, BorderLayout.NORTH);
        fdf.add(stift, BorderLayout.SOUTH);
        div.add(fdf, BorderLayout.NORTH);
    }

    private void get_head_all(Collection best_user, List<org.music.models.Search_Tracks.Collection> tracks) {
        head_left.removeAll();
        head_left.setBackground(Color.decode("#1a1a1a"));
        head_left.setPreferredSize(new Dimension(300, 310));
        head_left.setBorder(new EmptyBorder(0,0,40,0));
        JLabel l_north = new JLabel("Top Artist");
        l_north.setBorder(new EmptyBorder(0,0,0,20));
        l_north.setForeground(Color.WHITE);
        l_north.setFont(new Font("Serif", Font.BOLD, 20));
        JPanel l_north_div = new JPanel(new BorderLayout());
        l_north_div.setBackground(Color.decode("#1a1a1a"));
        l_north_div.add(l_north, BorderLayout.WEST);

        Border_Radius div_artist = new Border_Radius(30);
        div_artist.setLayout(new BorderLayout(0,5));
        div_artist.setBorder(new EmptyBorder(15, 15, 15, 15));
        div_artist.setBackground(Color.decode("#2a2a2a"));

        Rounded_Label img_ar = new Rounded_Label(new ImageIcon(), 1000);
        img_ar.setPreferredSize(new Dimension(100, 100));
        JPanel img_ar_div = new JPanel(new BorderLayout());
        img_ar_div.setBackground(Color.decode("#2a2a2a"));
        img_ar_div.add(img_ar, BorderLayout.WEST);


        div_artist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                div_artist.setBackground(Color.decode("#3a3a3a"));
                img_ar_div.setBackground(Color.decode("#3a3a3a"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                div_artist.setBackground(Color.decode("#2a2a2a"));
                img_ar_div.setBackground(Color.decode("#2a2a2a"));
            }
        });


        JPanel name_ar_div = new JPanel(new BorderLayout());
        name_ar_div.setBackground(new Color(0, 0, 0, 0));
        JLabel name_ar = new JLabel(best_user.getUsername());
        name_ar.setPreferredSize(new Dimension(270, 30));
        name_ar.setForeground(Color.WHITE);
        name_ar.setBorder(new EmptyBorder(10,0,0,0));
        name_ar.setFont(new Font("Serif", Font.PLAIN, 18));
        name_ar_div.add(name_ar, BorderLayout.WEST);

        JTextArea description_ar = new JTextArea(best_user.getDescription());
        description_ar.setLineWrap(true);
        description_ar.setWrapStyleWord(true);
        description_ar.setEditable(false);
        description_ar.setPreferredSize(new Dimension(270, 70));
        description_ar.setBackground(new Color(0, 0, 0, 0));
        description_ar.setForeground(Color.GRAY);
        description_ar.setBorder(null);
        JPanel description_ar_div = new JPanel();
        description_ar_div.setLayout(new BoxLayout(description_ar_div, BoxLayout.Y_AXIS));
        description_ar_div.setBackground(new Color(0, 0, 0, 0));
        description_ar_div.add(description_ar);

        JPanel xyz = new JPanel(new BorderLayout());
        xyz.setBackground(new Color(0, 0, 0, 0));
        xyz.add(img_ar_div, BorderLayout.NORTH);
        xyz.add(name_ar_div, BorderLayout.SOUTH);
        div_artist.add(xyz, BorderLayout.NORTH);
        div_artist.add(description_ar_div, BorderLayout.SOUTH);

        head_left.add(l_north_div, BorderLayout.NORTH);
        head_left.add(div_artist, BorderLayout.SOUTH);

        div_artist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                home.reload_Artist(best_user.getId(), width == 1150 ? 1150 : 860);
            }
        });

        new Thread(() -> {
            try {
                URI uri = new URI(sc.IMG500x500(best_user.getAvatar_url()));
                ImageIcon originalIcon = new ImageIcon(uri.toURL());
                Image scaledImage = originalIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(scaledImage);
                img_ar.setIcon(resizedIcon);
            } catch (URISyntaxException | MalformedURLException e) {
                e.printStackTrace();
                img_ar.setIcon(new ImageIcon("src/main/resources/pngs/x.png"));
            }
        }).start();

        head_right.removeAll();
        head_right.setPreferredSize(new Dimension(300,310));
        head_right.setBackground(Color.decode("#1a1a1a"));
        JLabel r_north = new JLabel("Top Tracks");
        r_north.setBorder(new EmptyBorder(0,0,30,0));
        r_north.setPreferredSize(new Dimension(100,50));
        r_north.setForeground(Color.WHITE);
        r_north.setFont(new Font("Serif", Font.BOLD, 20));

        JPanel right_head = new JPanel(new GridLayout(5,1));
        right_head.setBackground(Color.decode("#1a1a1a"));
        right_head.setBorder(new EmptyBorder(20,0,0,20));
        for (org.music.models.Search_Tracks.Collection track : tracks) {
            right_head.add(get_track_item(track));
        }

        head_right.add(r_north, BorderLayout.NORTH);
        head_right.add(right_head, BorderLayout.CENTER);
    }

    private JPanel get_track_item(org.music.models.Search_Tracks.Collection i){
        Border_Radius a = new Border_Radius(15);
        a.setBackground(Color.decode("#1a1a1a"));
        a.setBorder(new EmptyBorder(5,5,5,5));
        a.setLayout(new BorderLayout(10,5));

        Rounded_Label img_track = new Rounded_Label(new ImageIcon(),10);
        img_track.setPreferredSize(new Dimension(45,45));

        new Thread(() ->{
            try {
                URI uri = new URI(sc.IMG500x500(i.getArtwork_url()));
                ImageIcon imageIcon = new ImageIcon(uri.toURL());
                SwingUtilities.invokeLater(() -> {
                    img_track.setIcon(imageIcon);
                });
            }  catch (URISyntaxException | MalformedURLException | NullPointerException e) {
                SwingUtilities.invokeLater(() -> {
                    img_track.setIcon(new ImageIcon("src/main/resources/pngs/x.png"));
                });
            }
        }).start();

        JPanel b = new JPanel(new BorderLayout());
        b.setBackground(new Color(0, 0, 0, 0));
        JLabel name_track = new JLabel(i.getTitle());
        name_track.setForeground(Color.WHITE);
        name_track.setFont(new Font("Serif", Font.PLAIN, 17));
        JLabel artist_track = new JLabel();
        String nam = (i.getPublisher_metadata() != null && i.getPublisher_metadata().getArtist() != null)
                ? i.getPublisher_metadata().getArtist()
                : i.getUser().getUsername();
        artist_track.setText(nam);
        artist_track.setForeground(Color.GRAY);
        artist_track.setFont(new Font("Serif", Font.PLAIN, 15));
        b.add(name_track, BorderLayout.NORTH);
        b.add(artist_track, BorderLayout.SOUTH);

        JButton add = createButton("src/main/resources/pngs/circle-plus.png",25,25);
        a.add(img_track, BorderLayout.WEST);
        a.add(b, BorderLayout.CENTER);
        a.add(add, BorderLayout.EAST);

        a.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                a.setBackground(Color.decode("#2a2a2a"));
            }


            @Override
            public void mouseExited(MouseEvent e) {
                a.setBackground(Color.decode("#1a1a1a"));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Track_Stream(stream, i.getId());
            }
        });

        JPopupMenu pop = home.popup_Search(i, add);
        home.createPopup(add, pop);

        return a;
    }

    private void get_cen_bot(JPanel cen, JPanel bot, Albums albums){
        List<org.music.models.Search_Album.Collection> alb = albums.getCollection();

        cen.removeAll();
        cen.setBackground(Color.decode("#1a1a1a"));
        cen.setBorder(new EmptyBorder(0,0,20,0));
        cen.setPreferredSize(new Dimension(610,320));
        JLabel Album = new JLabel("Albums");
        Album.setBorder(new EmptyBorder(0,0,10,0));
        Album.setForeground(Color.WHITE);
        Album.setFont(new Font("Serif", Font.BOLD, 20));
        JPanel Album_div = new JPanel(new GridLayout(0,5));
        Album_div.setBackground(Color.decode("#1a1a1a"));

        int count = 0;
        for (org.music.models.Search_Album.Collection album : alb) {
            if(count >= 5) break;
            JPanel a = get_playlist_item(album, true);
            if(a == null) continue;
            else {
                Album_div.add(a);
                count++;
            }
        }

        cen.add(Album, BorderLayout.NORTH);
        cen.add(Album_div, BorderLayout.SOUTH);

        bot.removeAll();
        bot.setBackground(Color.decode("#1a1a1a"));
        bot.setPreferredSize(new Dimension(610,300));
        JLabel Playlist = new JLabel("Playlists");
        Playlist.setForeground(Color.WHITE);
        Playlist.setFont(new Font("Serif", Font.BOLD, 20));
        JPanel Playlist_div = new JPanel(new GridLayout(0,5));
        Playlist_div.setBackground(Color.decode("#1a1a1a"));

        int counter = 0;
        for (org.music.models.Search_Album.Collection album : alb) {
            if(counter >= 5) break;
            JPanel a = get_playlist_item(album, false);
            if(a == null) continue;
            else {
                Playlist_div.add(a);
                counter++;
            }
        }


        bot.add(Playlist, BorderLayout.NORTH);
        bot.add(Playlist_div, BorderLayout.SOUTH);
    }

    private JPanel get_playlist_item(org.music.models.Search_Album.Collection i, boolean kind){
        if(i.isIs_album() != kind) return null;
        else{
            JPanel b = new JPanel(new BorderLayout(5,15));
            b.setBackground(Color.decode("#1a1a1a"));
            b.setPreferredSize(new Dimension(200,260));
            b.setBorder(new EmptyBorder(5,5,5,5));

            Rounded_Label img_ab = new Rounded_Label(new ImageIcon(),10);
            img_ab.setPreferredSize(new Dimension(200,200));
            new Thread(() ->{
                try {
                    URI uri = new URI(sc.IMG500x500(i.getTracks().getFirst().getArtwork_url()));
                    ImageIcon imageIcon = new ImageIcon(uri.toURL());
                    SwingUtilities.invokeLater(() -> {
                        img_ab.setIcon(imageIcon);
                    });
                }  catch (URISyntaxException | MalformedURLException | NullPointerException e) {
                    SwingUtilities.invokeLater(() -> {
                        img_ab.setIcon(new ImageIcon("src/main/resources/pngs/x.png"));
                    });
                }
            }).start();

            JPanel inf = new JPanel(new BorderLayout(5,5));
            inf.setBackground(new Color(0, 0, 0, 0));
            JLabel title = new JLabel(i.getTitle());
            title.setBorder(new EmptyBorder(10,0,0,0));
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Serif", Font.PLAIN, 14));
            title.setMaximumSize(new Dimension(200,8));
            JLabel asd = new JLabel("From Soundcloud");
            asd.setForeground(new Color(255, 253, 208));
            asd.setFont(new Font("Serif", Font.PLAIN, 12));

            inf.add(title, BorderLayout.NORTH);
            inf.add(asd, BorderLayout.SOUTH);

            b.add(img_ab, BorderLayout.NORTH);
            b.add(inf, BorderLayout.SOUTH);

            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    b.setBackground(Color.decode("#2a2a2a"));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    b.setBackground(Color.decode("#1a1a1a"));
                }
            });

            return b;
        }

    }

    private void panel_Artists(JPanel art, Stream stream) {
        art.removeAll();
        Users users = sc.getUsers(key, 30);

        zwei = new JPanel(new GridBagLayout());
        zwei.setBackground(Color.decode("#1a1a1a"));

        List<org.music.models.Search_User.Collection> us = users.getCollection();
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 15, 10, 15); // Khoảng cách giữa các ô
        gbc.anchor = GridBagConstraints.CENTER; // Canh giữa nội dung trong ô
        gbc.weightx = 0; // Trọng số theo chiều ngang (đồng đều)
        gbc.weighty = 1; // kéo dãn theo chiều dọc



        art.setBackground(Color.decode("#1a1a1a"));
        art.add(zwei, BorderLayout.NORTH);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                int cols = 6;
                for (int i = 0; i < us.size(); i++) {
                    gbc.gridx = i % cols; // Cột hiện tại
                    gbc.gridy = i / cols; // Hàng hiện tại
                    zwei.add(get_artist_item(us.get(i)), gbc);
                }
                return null;
            }

            @Override
            protected void done() {
                art.revalidate();  // Cập nhật layout
                art.repaint();  // Vẽ lại panel
            }
        };
        worker.execute();
    }

    private Border_Radius get_artist_item(org.music.models.Search_User.Collection i) {
        Border_Radius a = new Border_Radius(30);
        a.setLayout(new BorderLayout(5, 5));
        a.setBackground(Color.decode("#1a1a1a"));
        a.setPreferredSize(new Dimension(160, 200));
        a.setBorder(new EmptyBorder(10, 10, 10, 10));

        Rounded_Label img_ar = new Rounded_Label(new ImageIcon(), 1000);
        img_ar.setPreferredSize(new Dimension(150, 150));

        // Sử dụng executor để tải ảnh
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            try {
                URI uri = new URI(sc.IMG500x500(i.getAvatar_url()));
                ImageIcon imageIcon = new ImageIcon(uri.toURL());
                SwingUtilities.invokeLater(() -> img_ar.setIcon(imageIcon));
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> img_ar.setIcon(new ImageIcon("src/main/resources/pngs/x.png")));
            }
        });

        JLabel name = new JLabel(i.getUsername());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Serif", Font.PLAIN, 14));
        name.setBorder(new EmptyBorder(10, 0, 0, 0));

        a.add(img_ar, BorderLayout.NORTH);
        a.add(name, BorderLayout.SOUTH);

        a.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                a.setBackground(Color.decode("#2a2a2a"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                a.setBackground(Color.decode("#1a1a1a"));
            }
        });

        return a;
    }

    private void panel_Tracks(JPanel tra) {
        tra.removeAll();
        tra.setBackground(Color.decode("#1a1a1a"));

        head.removeAll();
        head.setBackground(Color.decode("#1a1a1a"));
        JPanel left = new JPanel(new BorderLayout(5,0));
        left.setBackground(Color.decode("#1a1a1a"));
        left.setPreferredSize(new Dimension(70,20));
        JLabel sharp = new JLabel("   #");
        sharp.setForeground(Color.WHITE);
        sharp.setFont(new Font("Serif", Font.PLAIN, 16));
        JLabel tit = new JLabel("Titel");
        tit.setForeground(Color.WHITE);
        tit.setFont(new Font("Serif", Font.PLAIN, 16));
        left.add(sharp, BorderLayout.WEST);
        left.add(tit, BorderLayout.EAST);

        alb.setForeground(Color.WHITE);
        alb.setFont(new Font("Serif", Font.PLAIN, 16));

        head.setPreferredSize(new Dimension(600,20));
        alb.setPreferredSize(new Dimension(230,30));

        head.add(left, BorderLayout.WEST);
        head.add(alb, BorderLayout.EAST);
        cxz.setBackground(Color.decode("#1a1a1a"));
        cxz.setPreferredSize(new Dimension(600, 20));
        cxz.add(head, BorderLayout.WEST);

        drei.removeAll();
        drei.setBackground(Color.decode("#1a1a1a"));
        drei.setBorder(new EmptyBorder(5,0,0,20));

        drei.add(cxz, BorderLayout.NORTH);
        tra.add(drei, BorderLayout.WEST);

    }

    private void refresh_drei(int wit, Songs sons) {
        drei.removeAll();

        // Thiết lập kích thước
        if (wit == 250) {
            cxz.setPreferredSize(new Dimension(600, 20));
            head.setPreferredSize(new Dimension(600, 20));
            alb.setPreferredSize(new Dimension(230, 30));
        } else {
            cxz.setPreferredSize(new Dimension(1150, 20));
            head.setPreferredSize(new Dimension(1150, 20));
            alb.setPreferredSize(new Dimension(320, 30));
        }

        drei.add(cxz, BorderLayout.NORTH);

        // Sử dụng SwingWorker để xử lý việc tạo các item
        SwingWorker<List<JPanel>, JPanel> worker = new SwingWorker<>() {
            @Override
            protected List<JPanel> doInBackground() {
                List<JPanel> panels = new ArrayList<>();
                List<org.music.models.Search_Tracks.Collection> tracks = sons.getCollection();
                int z = 1;

                for (org.music.models.Search_Tracks.Collection track : tracks) {
                    JPanel songItem = get_song_item(track, z, wit); // Tạo item trong luồng nền
                    panels.add(songItem); // Lưu lại để thêm vào giao diện sau
                    z++;
                }
                return panels;
            }

            @Override
            protected void process(List<JPanel> chunks) {
                // Thêm các item vào giao diện trong EDT
                for (JPanel panel : chunks) {
                    drei.add(panel);
                }
                drei.revalidate();
                drei.repaint();
            }

            @Override
            protected void done() {
                try {
                    // Lấy toàn bộ các item đã tạo và thêm chúng vào giao diện
                    List<JPanel> panels = get();
                    for (JPanel panel : panels) {
                        drei.add(panel);
                    }
                    drei.revalidate();
                    drei.repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private JPanel get_song_item(org.music.models.Search_Tracks.Collection i, int number, int wit){
        Border_Radius a = new Border_Radius(15);
        a.setBackground(Color.decode("#1a1a1a"));
        a.setBorder(new EmptyBorder(5,5,5,30));
        a.setLayout(new BorderLayout(10,5));
        a.setPreferredSize(new Dimension(600, 55));

        Rounded_Label img_track = new Rounded_Label(new ImageIcon(),10);
        img_track.setPreferredSize(new Dimension(45,45));

        new Thread(() ->{
            try {
                URI uri = new URI(sc.IMG500x500(i.getArtwork_url()));
                ImageIcon imageIcon = new ImageIcon(uri.toURL());
                SwingUtilities.invokeLater(() -> {
                    img_track.setIcon(imageIcon);
                });
            }  catch (URISyntaxException | MalformedURLException | NullPointerException e) {
                SwingUtilities.invokeLater(() -> {
                    img_track.setIcon(new ImageIcon("src/main/resources/pngs/x.png"));
                });
            }
        }).start();

        JPanel jkl = new JPanel(new BorderLayout());
        jkl.setBackground(new Color(0, 0, 0, 0));
        jkl.setPreferredSize(new Dimension(wit,45));
        JLabel name_track = new JLabel(i.getTitle());
        name_track.setForeground(Color.WHITE);
        name_track.setFont(new Font("Serif", Font.PLAIN, 17));
        JLabel artist_track = new JLabel();
        String nam = (i.getPublisher_metadata() != null && i.getPublisher_metadata().getArtist() != null)
                ? i.getPublisher_metadata().getArtist()
                : i.getUser().getUsername();
        artist_track.setText(nam);
        artist_track.setForeground(Color.GRAY);
        artist_track.setFont(new Font("Serif", Font.PLAIN, 15));
        jkl.add(name_track, BorderLayout.NORTH);
        jkl.add(artist_track, BorderLayout.SOUTH);

        JLabel albu = new JLabel();
        String al ;
        try{
            al = i.getPublisher_metadata().getAlbum_title();
        } catch (NullPointerException e) {
            al = "Unkown";
        }
        albu.setText(al);
        albu.setForeground(Color.GRAY);
        albu.setFont(new Font("Serif", Font.PLAIN, 15));

        JButton add = createButton("src/main/resources/pngs/circle-plus.png",25,25);
        JPopupMenu pop = home.popup_Search(i, add);
        home.createPopup(add, pop);

        JPanel img_name = new JPanel(new BorderLayout(10,0));
        img_name.setBackground(new Color(0,0,0,0));
        img_name.add(img_track, BorderLayout.WEST);
        img_name.add(jkl, BorderLayout.EAST);

        JPanel album_btn = new JPanel(new BorderLayout(10,0));
        album_btn.setBackground(Color.decode("#1a1a1a"));
        album_btn.add(albu, BorderLayout.WEST);
        album_btn.add(add, BorderLayout.EAST);
        if(wit == 250) {
            albu.setPreferredSize(new Dimension(180,45));
            album_btn.setPreferredSize(new Dimension(200,45));
        } else {
            albu.setPreferredSize(new Dimension(250,45));
            album_btn.setPreferredSize(new Dimension(280,45));
        }

        JPanel cent = new JPanel(new BorderLayout(20,0));
        cent.setBackground(new Color(0,0,0,0));
        cent.add(img_name, BorderLayout.WEST);
        cent.add(album_btn, BorderLayout.EAST);

        JLabel nu = new JLabel(String.valueOf(number));
        nu.setPreferredSize(new Dimension(30,45));
        nu.setForeground(Color.white);
        nu.setFont(new Font("Serif", Font.PLAIN, 16));

        a.add(nu, BorderLayout.WEST);
        a.add(cent, BorderLayout.CENTER);

        a.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                a.setBackground(Color.decode("#2a2a2a"));
                album_btn.setBackground(Color.decode("#2a2a2a"));
                nu.setIcon(loadIcon("src/main/resources/pngs/player-play.png", 20, 20)); // Thay đổi icon của `nu`
                nu.setText("");
                nu.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                a.setBackground(Color.decode("#1a1a1a"));
                album_btn.setBackground(Color.decode("#1a1a1a"));
                nu.setIcon(null);
                nu.setText(String.valueOf(number));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Track_Stream(stream, i.getId());
            }
        });

        return a;
    }

    private void panel_Album(JPanel alb, JPanel num, boolean list) {
        alb.removeAll();
        num.removeAll();
        alb.setBackground(Color.decode("#1a1a1a"));
        num.setBackground(Color.decode("#1a1a1a"));
        alb.add(num, BorderLayout.NORTH);

        SwingWorker<Void, org.music.models.Search_Album.Collection> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                Albums album = sc.get_Albums(key, 200);
                List<org.music.models.Search_Album.Collection> us = album.getCollection();

                List<org.music.models.Search_Album.Collection> items = list
                        ? us.stream().filter(org.music.models.Search_Album.Collection::isIs_album).toList()
                        : us.stream().filter(item -> !item.isIs_album()).toList();

                int count = 0;
                for (org.music.models.Search_Album.Collection item : items) {
                    if (count > 60) break;
                    publish(item); // Đưa mục vào luồng cập nhật
                    count++;
                }
                return null;
            }

            @Override
            protected void process(List<org.music.models.Search_Album.Collection> chunks) {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 15, 10, 15);
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.weightx = 0;
                gbc.weighty = 1;
                int cols = 6;

                for (org.music.models.Search_Album.Collection item : chunks) {
                    int index = num.getComponentCount();
                    gbc.gridx = index % cols;
                    gbc.gridy = index / cols;
                    num.add(get_album_item(item), gbc);
                }
                num.revalidate();
                num.repaint();
            }

            @Override
            protected void done() {
                alb.revalidate();
                alb.repaint();
            }
        };
        worker.execute();
    }

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    private Border_Radius get_album_item(org.music.models.Search_Album.Collection i) {
        Border_Radius a = new Border_Radius(30);
        a.setLayout(new BorderLayout(5, 5));
        a.setBackground(Color.decode("#1a1a1a"));
        a.setPreferredSize(new Dimension(160, 230));
        a.setBorder(new EmptyBorder(10, 10, 10, 10));

        Rounded_Label img_ar = new Rounded_Label(new ImageIcon(), 50);
        img_ar.setPreferredSize(new Dimension(150, 150));

        executor.submit(() -> {
            try {
                String artworkUrl = i.getTracks().stream()
                        .map(org.music.models.Search_Album.Track::getArtwork_url)
                        .filter(url -> url != null && !url.isEmpty())
                        .findFirst()
                        .orElse(null);

                if (artworkUrl != null) {
                    URI uri = new URI(sc.IMG500x500(artworkUrl));
                    ImageIcon imageIcon = new ImageIcon(uri.toURL());
                    SwingUtilities.invokeLater(() -> img_ar.setIcon(imageIcon));
                } else {
                    throw new NullPointerException();
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> img_ar.setIcon(new ImageIcon("src/main/resources/pngs/x.png")));
            }
        });

        JLabel name = new JLabel(i.getTitle());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Serif", Font.PLAIN, 14));
        name.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel tracks = new JLabel("Tracks: " + i.getTrack_count());
        tracks.setForeground(Color.GRAY);
        tracks.setFont(new Font("Serif", Font.PLAIN, 12));
        tracks.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel alb_bot = new JPanel(new BorderLayout());
        alb_bot.setBackground(new Color(0, 0, 0, 0));
        alb_bot.add(name, BorderLayout.NORTH);
        alb_bot.add(tracks, BorderLayout.SOUTH);

        a.add(img_ar, BorderLayout.NORTH);
        a.add(alb_bot, BorderLayout.SOUTH);
        a.setOpaque(true);
        a.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                a.setBackground(Color.decode("#2a2a2a"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                a.setBackground(Color.decode("#1a1a1a"));
            }
        });

        return a;
    }

    private void Track_Stream(Stream stream, int id){
        org.music.models.Search_Tracks.Collection track = sc.get_track_by_id(id);
        Queue_Item qi = home.cv_track_to_queuei(track);
        home.addToFront(qi);
        home.setCurrentSong(qi);
        home.Play_track();
    }
}



