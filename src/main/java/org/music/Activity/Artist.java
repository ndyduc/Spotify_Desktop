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


    Border_Radius img_artist = new Border_Radius(30);
    JPanel neun = new JPanel(new GridLayout(0,1));
    JLabel Name_artist = new JLabel();
    JLabel Listener = new JLabel();

    JButton Playbtn = home.createButton("src/main/resources/pngs/player-play.png",40,40);


    public Artist(int radius, Collection user, Home home) {
        super(radius);
        this.user = user;
        this.home = home;



        img_artist.setPreferredSize(new Dimension(550, 300));
        ImagePanel backgroundPanel = new ImagePanel();
        backgroundPanel.setBorder(new EmptyBorder(150, 30,10,30));
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
        btn.setBackground(Color.decode("#1a1a1a"));
        btn.setPreferredSize(new Dimension(550, 60));
        btn.setLayout(new FlowLayout(FlowLayout.LEFT));
        btn.setBorder(new EmptyBorder(10,30,10,0));
        btn.add(Playbtn);


        Border_Radius abc = new Border_Radius(30);
        abc.setLayout(new BorderLayout(0,15));
        abc.setBackground(Color.decode("#1a1a1a"));

        abc.add(img_artist, BorderLayout.NORTH);
        abc.add(btn, BorderLayout.CENTER);


        JScrollPane main_all = new JScrollPane(abc);
        main_all.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        main_all.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        main_all.getVerticalScrollBar().setUnitIncrement(16);
        main_all.setBorder(null);
        add(main_all, BorderLayout.CENTER);
    }

    private void refresh_neun(Collection user) {
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
                    JPanel songItem = get_item(track); // Tạo item trong luồng nền
                    panels.add(songItem); // Lưu lại để thêm vào giao diện sau
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


    private Border_Radius get_item(org.music.models.Search_Tracks.Collection track) {
        Border_Radius bor  = new Border_Radius(30);
        bor.setBackground(Color.decode("#1a1a1a"));

        return bor;
    }
}
