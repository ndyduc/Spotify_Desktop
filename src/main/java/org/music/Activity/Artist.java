package org.music.Activity;

import org.checkerframework.checker.units.qual.C;
import org.music.Components.Border_Radius;
import org.music.Components.ImagePanel;
import org.music.Components.RoundedPanel;
import org.music.Components.Rounded_Label;
import org.music.Func.Stream;
import org.music.MongoDB;
import org.music.getAPI.Soundcloud;
import org.music.models.Queue_Item;
import org.music.models.Search_User.Collection;
import org.music.models.Songs;
import org.music.models.Users;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.music.Activity.Home.createButton;
import static org.music.Activity.Home.loadIcon;

public class Artist extends Border_Radius {
    Collection user;
    MongoDB mongo = new MongoDB();
    Home home;
    Stream stream;
    Soundcloud sc = new Soundcloud();
    Frame window;
    Boolean full = false;

    JLabel fav = new JLabel("Popular Song");
    Rounded_Label img_artist = new Rounded_Label(new ImageIcon(),30);
    JPanel neun = new JPanel(new GridLayout(0,1));
    JLabel Name_artist = new JLabel();
    JLabel Listener = new JLabel();

    JButton Playbtn = home.createButton("src/main/resources/pngs/player-play.png",40,40);


    public Artist(int radius, Collection user, Stream stream, Frame window, Home home) {
        super(radius);
        refresh_neun(user, 860);
        this.user = user;
        this.home = home;
        this.window = window;

        setBackground(Color.decode("#1a1a1a"));
        fav.setFont(new Font("Serif", Font.BOLD, 28));
        fav.setBorder(new EmptyBorder(5,30,5,0));
        fav.setForeground(Color.WHITE);
        img_artist.setPreferredSize(new Dimension(590,300));
        loadimg(user);


        Border_Radius btn = new Border_Radius(30);
        btn.setBackground(Color.decode("#2a2a2a"));
        btn.setPreferredSize(new Dimension(550, 60));
        btn.setLayout(new FlowLayout(FlowLayout.LEFT));
        btn.setBorder(new EmptyBorder(5,30,5,50));
        btn.add(Playbtn);

        JPanel inf = new JPanel(new BorderLayout());
        inf.setBackground(new Color(0,0,0,0));
        inf.setPreferredSize(new Dimension(300,50));
        inf.setBorder(new EmptyBorder(0,50,0,50));
        Name_artist.setText(user.getUsername());
        Name_artist.setFont(new Font("Serif", Font.BOLD, 26));
        Name_artist.setForeground(Color.white);
        Listener.setText(String.valueOf(user.getFollowers_count()) + " Followers");
        Listener.setFont(new Font("Serif", Font.PLAIN, 14));
        Listener.setForeground(Color.white);
        inf.add(Name_artist, BorderLayout.NORTH);
        inf.add(Listener, BorderLayout.SOUTH);
        btn.add(inf);

        JButton fol = createButton("src/main/resources/pngs/fl_gray.png", 50,30);
        btn.add(fol);

        JPanel abc = new JPanel(new BorderLayout());
        abc.setLayout(new BorderLayout(0,15));
        abc.setBackground(Color.decode("#1a1a1a"));

        abc.add(img_artist, BorderLayout.NORTH);
        abc.add(btn, BorderLayout.CENTER);
        abc.add(neun, BorderLayout.SOUTH);


        JScrollPane main_all = new JScrollPane(abc);
        main_all.setPreferredSize(new Dimension((int)(window.getWidth()*0.7), (int)(window.getHeight()-160)));
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
                    main_all.setPreferredSize(new Dimension(1150,850));
                    full = true;
                    refresh_neun(user, 1150);

                } else if (window.getExtendedState() == JFrame.NORMAL) {
                    main_all.setPreferredSize(new Dimension(window.getWidth()-250,440));
                    full = false;
                    refresh_neun(user, 860);
                }
                main_all.revalidate();
                main_all.repaint();
                neun.revalidate();
                neun.repaint();

            }
        });
    }

    public void refresh_neun(Collection user, int width) {
        loadimg(user);
        neun.removeAll();
        neun.setBackground(Color.decode("#1a1a1a"));

        neun.add(fav);

        SwingWorker<java.util.List<JPanel>, JPanel> worker = new SwingWorker<>() {
            @Override
            protected java.util.List<JPanel> doInBackground() {
                List<JPanel> panels = new ArrayList<>();
                Songs tra = sc.get_n_Tracks(user.getUsername(), 200);
                List<org.music.models.Search_Tracks.Collection> tracks = tra.getCollection().stream()
                        .filter(collection -> collection.getUser() != null && collection.getUser().getId() == user.getId())
                        .collect(Collectors.toList());


                int z = 1;
                for (org.music.models.Search_Tracks.Collection track : tracks) {
                    JPanel songItem = get_item(track, z, width);
                    panels.add(songItem);
                    z++;
                }
                return panels;
            }

            @Override
            protected void process(java.util.List<JPanel> chunks) {
                for (JPanel panel : chunks) {
                    neun.add(panel);
                }
                neun.revalidate();
                neun.repaint();
            }

            @Override
            protected void done() {
                try {
                    List<JPanel> panels = get();
                    for (JPanel panel : panels) {
                        neun.add(panel);
                    }
                    neun.revalidate();
                    neun.repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void loadimg(Collection user){
        new Thread(() -> {
            try {
                URI uri = new URI(sc.IMG500x500(user.getAvatar_url()));
                ImageIcon image = new ImageIcon(uri.toURL());
                SwingUtilities.invokeLater(() -> img_artist.setIcon(image));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> { img_artist.setIcon(new ImageIcon("src/main/resources/pngs/me.png")); });
            }
        }).start();
    }

    private Border_Radius get_item(org.music.models.Search_Tracks.Collection track, int number, int width) {
        Border_Radius bor  = new Border_Radius(30);
        bor.setBackground(Color.decode("#1a1a1a"));
        bor.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel nu = new JLabel(String.valueOf(number));
        nu.setPreferredSize(new Dimension(30,45));
        nu.setForeground(Color.white);
        nu.setFont(new Font("Serif", Font.PLAIN, 16));
        nu.setBorder(new EmptyBorder(0,10,0,0));

        bor.add(nu);

        Rounded_Label img = new Rounded_Label(new ImageIcon(), 30);
        img.setPreferredSize(new Dimension(40,40));
        img.setBorder(new EmptyBorder(5,5,5,5));

        bor.add(img);

        new Thread(() -> {
            try {
                URI uri = new URI(sc.IMG500x500(track.getArtwork_url()));
                ImageIcon image = new ImageIcon(uri.toURL());
                SwingUtilities.invokeLater(() -> img.setIcon(image));
            } catch (URISyntaxException | MalformedURLException | NullPointerException e) {
                SwingUtilities.invokeLater(() -> { img.setIcon(new ImageIcon("src/main/resources/pngs/me.png")); });
            }
        }).start();

        Border_Radius info = new Border_Radius(30);
        info.setBackground(new Color(0,0,0,0));
        info.setLayout(new BorderLayout(10,5));
        info.setBorder(new EmptyBorder(0,15,0,0));

        JLabel name = new JLabel(track.getTitle());
        name.setFont(new Font("Serif", Font.PLAIN, 14));
        name.setForeground(Color.white);

        Border_Radius clue = new Border_Radius(30);
        clue.setBackground(new Color(0,0,0,0));
        clue.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton dl = createButton("src/main/resources/pngs/circle-arrow-down-green.png", 25, 25);
        if(checkFile(mongo.File_name(track))) clue.add(dl);

        JLabel ar = new JLabel(mongo.get_Artist(track));
        ar.setFont(new Font("Serif", Font.PLAIN, 12));
        ar.setForeground(Color.white);
        clue.add(ar);

        info.add(name, BorderLayout.NORTH);
        info.add(clue, BorderLayout.SOUTH);
        bor.add(info);

        String abl = (track.getPublisher_metadata().getAlbum_title() == null)? "Unknown" : track.getPublisher_metadata().getAlbum_title();
        JLabel album = new JLabel(abl);
        album.setFont(new Font("Serif", Font.PLAIN, 12));
        album.setForeground(Color.GRAY);
        album.setPreferredSize(new Dimension((int)((width-250)*0.2),50));
        if(full) {
            info.setPreferredSize(new Dimension((int) (width*0.45), 50));
            bor.add(album);
        }else {
            info.setPreferredSize(new Dimension((int)(width*0.3),50));
            name.setPreferredSize(new Dimension((int)(width*0.3),20));
        }


        JLabel lis = new JLabel(Spaces(track.getPlayback_count())+" Streamed");
        lis.setFont(new Font("Serif", Font.PLAIN, 12));
        lis.setForeground(Color.GRAY);
        lis.setPreferredSize(new Dimension((int)(width*0.18),50));

        bor.add(lis);

        JLabel duration = new JLabel(get_duration(track.getDuration()));
        duration.setFont(new Font("Serif", Font.PLAIN, 12));
        duration.setForeground(Color.GRAY);
        duration.setPreferredSize(new Dimension((int)(width*0.05),50));

        bor.add(duration);

        JButton more = createButton("src/main/resources/pngs/dots.png", 30, 30);

        bor.add(more);

        JPopupMenu pop = home.popup_Search(track, more);
        home.createPopup(more, pop);

        bor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bor.setBackground(Color.decode("#2a2a2a"));
                nu.setIcon(loadIcon("src/main/resources/pngs/player-play.png", 15, 15));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                bor.setBackground(Color.decode("#1a1a1a"));
                nu.setIcon(null);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                Playbtn.setIcon(home.loadIcon("src/main/resources/pngs/player-pause.png", 40, 40));
                home.setCurrentSong(home.cv_track_to_queuei(track));
                home.Play_track();
            }
        });

        return bor;
    }

    public boolean checkFile(String fileName) {
        File folder = new File("./mp3_queue");
        if (!folder.exists() || !folder.isDirectory()) {
            return false; // Thư mục không tồn tại hoặc không phải là thư mục.
        }

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().equals(fileName)) {
                    return true; // Tìm thấy file "abc.mp3".
                }
            }
        }
        return false;
    }

    public static String get_duration(int durationMillis) {
        int totalSeconds = durationMillis / 1000; // Chuyển sang giây
        int minutes = totalSeconds / 60;         // Tính số phút
        int seconds = totalSeconds % 60;         // Tính số giây còn lại

        // Trả về chuỗi định dạng "m:ss"
        return String.format("%d:%02d", minutes, seconds);
    }

    public String Spaces(int number) {
        return String.format("%,d", number).replace(",", " ");
    }
}
