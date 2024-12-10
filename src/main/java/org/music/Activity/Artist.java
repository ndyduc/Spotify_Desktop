package org.music.Activity;

import org.music.Components.Border_Radius;
import org.music.Components.ImagePanel;
import org.music.Components.RoundedPanel;
import org.music.Components.Rounded_Label;
import org.music.MongoDB;
import org.music.getAPI.Soundcloud;
import org.music.models.Queue_Item;
import org.music.models.Search_User.Collection;
import org.music.models.Songs;
import org.music.models.Users;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.music.Activity.Home.createButton;

public class Artist extends Border_Radius {
    Collection user;
    MongoDB mongo = new MongoDB();
    Home home;
    Soundcloud sc = new Soundcloud();
    Frame window;


    Border_Radius img_artist = new Border_Radius(30);
    JPanel neun = new JPanel(new GridLayout(0,1));
    JLabel Name_artist = new JLabel();
    JLabel Listener = new JLabel();

    JButton Playbtn = home.createButton("src/main/resources/pngs/player-play.png",40,40);


    public Artist(int radius, Collection user, Frame window, Home home) {
        super(radius);
        this.user = user;
        this.home = home;
        this.window = window;

        setBackground(Color.decode("#1a1a1a"));

        img_artist.setPreferredSize(new Dimension(600, 310));
        ImagePanel backgroundPanel = new ImagePanel(30);
        backgroundPanel.setBorder(new EmptyBorder(150, 30,10,30));
        backgroundPanel.setPreferredSize(new Dimension(600, 300));
        img_artist.add(backgroundPanel, BorderLayout.CENTER);

        new Thread(() -> {
            try {
                URI uri = new URI(sc.IMG500x500(user.getAvatar_url()));
                Image image = Toolkit.getDefaultToolkit().createImage(uri.toURL());
                SwingUtilities.invokeLater(() -> backgroundPanel.setBackgroundImage(image));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    Image fallbackImage = Toolkit.getDefaultToolkit().getImage("src/main/resources/pngs/me.png");
                    backgroundPanel.setBackgroundImage(fallbackImage);
                });
            }
        }).start();

        Name_artist.setFont(new Font("Serif", Font.BOLD, 32));
        Name_artist.setForeground(Color.white);
        Listener.setFont(new Font("Serif", Font.PLAIN, 16));
        Listener.setForeground(Color.white);

        backgroundPanel.add(Name_artist, BorderLayout.CENTER);
        backgroundPanel.add(Listener, BorderLayout.SOUTH);

        Border_Radius btn = new Border_Radius(30);
        btn.setBackground(Color.decode("#2a2a2a"));
        btn.setPreferredSize(new Dimension(550, 60));
        btn.setLayout(new FlowLayout(FlowLayout.LEFT));
        btn.setBorder(new EmptyBorder(10,30,10,0));
        btn.add(Playbtn);


        Border_Radius abc = new Border_Radius(30);
        abc.setLayout(new BorderLayout(0,15));
//        abc.setBackground(Color.decode("#1a1a1a"));

        abc.add(img_artist, BorderLayout.NORTH);
        abc.add(btn, BorderLayout.CENTER);


        JScrollPane main_all = new JScrollPane(abc);
        main_all.setBackground(Color.decode("#1a1a1a"));
        main_all.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        main_all.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        main_all.getVerticalScrollBar().setUnitIncrement(16);
        main_all.setBorder(null);
        add(main_all, BorderLayout.CENTER);
        refresh_neun(user);
    }

    public void refresh_neun(Collection user) {
        neun.removeAll();
        neun.setBackground(Color.decode("#1a1a1a"));

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
                    JPanel songItem = get_item(track, z, window.getWidth());
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


    private Border_Radius get_item(org.music.models.Search_Tracks.Collection track, int number, int width) {
        Border_Radius bor  = new Border_Radius(30);
        bor.setBackground(Color.decode("#1a1a1a"));
        bor.setLayout(new FlowLayout(FlowLayout.LEFT));

        Rounded_Label nu = new Rounded_Label(new ImageIcon(), 15);
        nu.setText(String.valueOf(number));
        nu.setPreferredSize(new Dimension(30,30));
        nu.setBorder(new EmptyBorder(10,10,10,10));

        Rounded_Label img = new Rounded_Label(new ImageIcon(), 30);
        img.setPreferredSize(new Dimension(40,40));
        img.setBorder(new EmptyBorder(5,5,5,5));

        new Thread(() -> {
            try {
                URI uri = new URI(sc.IMG500x500(user.getAvatar_url()));
                ImageIcon image = new ImageIcon(uri.toURL());
                SwingUtilities.invokeLater(() -> img.setIcon(image));
            } catch (URISyntaxException | MalformedURLException | NullPointerException e) {
                SwingUtilities.invokeLater(() -> { img.setIcon(new ImageIcon("src/main/resources/pngs/me.png")); });
            }
        }).start();

        Border_Radius info = new Border_Radius(30);
        info.setBackground(Color.decode("#1a1a1a"));
        info.setLayout(new BorderLayout(10,5));
        info.setPreferredSize(new Dimension((int)(width*0.25),50));

        JLabel name = new JLabel(track.getTitle());
        name.setFont(new Font("Serif", Font.PLAIN, 14));
        name.setForeground(Color.white);

        Border_Radius clue = new Border_Radius(30);
        clue.setBackground(new Color(0,0,0,0));
        clue.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton dl = createButton("src/main/resources/pngs/circle-arrow-down-green.png", 25, 25);
        if(checkFile(mongo.get_Artist(track))) clue.add(dl);

        JLabel ar = new JLabel(mongo.get_Artist(track));
        ar.setFont(new Font("Serif", Font.PLAIN, 12));
        ar.setForeground(Color.white);
        clue.add(ar);

        info.add(name, BorderLayout.NORTH);
        info.add(clue, BorderLayout.SOUTH);
        bor.add(info);

        JLabel lis = new JLabel(String.valueOf(track.getPlayback_count()));
        lis.setFont(new Font("Serif", Font.PLAIN, 12));
        lis.setForeground(Color.darkGray);
        lis.setPreferredSize(new Dimension((int)(width*0.1),50));

        bor.add(lis);

        JLabel duration = new JLabel(get_duration(track.getDuration()));
        duration.setFont(new Font("Serif", Font.PLAIN, 12));
        duration.setForeground(Color.darkGray);
        duration.setPreferredSize(new Dimension((int)(width*0.05),50));

        bor.add(duration);

        JButton more = createButton("src/main/resources/pngs/dots.png", 30, 30);

        bor.add(more);

        JPopupMenu pop = home.popup_Search(track, more);
        home.createPopup(more, pop);

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
}
