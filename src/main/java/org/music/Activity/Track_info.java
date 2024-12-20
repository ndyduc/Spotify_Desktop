package org.music.Activity;

import org.music.Components.Border_Radius;
import org.music.Components.RoundedPanel;
import org.music.Components.Rounded_Label;
import org.music.MongoDB;
import org.music.getAPI.Soundcloud;
import org.music.models.DB.Love_Artists;
import org.music.models.Queue_Item;
import org.music.models.Search_User.Collection;
import org.music.models.Track;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.music.Activity.Home.createButton;
import static org.music.Activity.Home.loadIcon;

public class Track_info extends Border_Radius {
    public Artist_Lis listener;
    Soundcloud sc = new Soundcloud();
    MongoDB mongo = new MongoDB();
    Queue_Item song;
    Home home;
    public Track_info(int radius, Queue_Item song, Home home, AtomicBoolean showqueue, JButton Queue, AtomicBoolean showartist, JButton Artist){
        super(radius);
        this.song = song;
        this.home = home;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(260, 0));
        setBackground(Color.decode("#1a1a1a"));
        setBorder(new EmptyBorder(10, 0, 10, 5));

        JPanel resizeHandle = new JPanel();
        resizeHandle.setPreferredSize(new Dimension(2, 0)); // Độ rộng thanh kéo
        resizeHandle.setBackground(Color.decode("#1a1a1a"));
        add(resizeHandle, BorderLayout.WEST);
        resizeHandle.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));

        resizeHandle.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int newWidth = getWidth() - e.getX();
                if (newWidth > 200 && newWidth < 360) {
                    setPreferredSize(new Dimension(newWidth, getHeight()));
                    revalidate();
                    repaint();
                }
            }
        });

        JPanel track_top = new JPanel(new BorderLayout());
        track_top.setBackground(Color.decode("#1a1a1a"));
        JButton close = createButton("src/main/resources/pngs/x.png",30,30);
        JButton more = createButton("src/main/resources/pngs/dots.png",30,30);
        track_top.add(more, BorderLayout.EAST);
        track_top.add(close, BorderLayout.EAST);
        add(track_top, BorderLayout.NORTH);

        close.addActionListener(e -> {
            if (!showartist.get()) {
                Artist.setIcon(loadIcon("src/main/resources/pngs/user-scan-green.png", 25, 25));
                showartist.set(true);
                if (showqueue.get()) {
                    Queue.setIcon(loadIcon("src/main/resources/pngs/queue.png", 25, 25));
                    showqueue.set(false);
                }
            } else {
                Artist.setIcon(loadIcon("src/main/resources/pngs/user-scan.png", 25, 25));
                showartist.set(false);
            }
            if (listener != null) {
                listener.onShowArtistChanged(showartist);
            }
        });

        JPanel info_scroll = new JPanel(new FlowLayout());
        info_scroll.setBorder(null);
        info_scroll.setBackground(Color.decode("#1a1a1a"));
        JPanel Libs = new JPanel(new BorderLayout());
        Libs.setBackground(Color.decode("#1a1a1a"));

        JScrollPane info_track_scroll = new JScrollPane(info_scroll);
        info_track_scroll.setBorder(null);
        info_track_scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        info_track_scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        info_track_scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(info_track_scroll, BorderLayout.CENTER);

        Rounded_Label img_cover = new Rounded_Label(new ImageIcon(),30);
        Libs.add(img_cover, BorderLayout.NORTH);

        new Thread(() -> {
            try {
                URI uri = new URI(sc.IMG500x500(song.getImgCover()));
                ImageIcon originalIcon = new ImageIcon(uri.toURL());
                Image scaledImage = originalIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(scaledImage);
                img_cover.setIcon(resizedIcon);
            } catch (URISyntaxException | MalformedURLException e) {
                e.printStackTrace();
                img_cover.setIcon(new ImageIcon("src/main/resources/pngs/x.png"));
            }
        }).start();

        JPanel Song_status = new JPanel(new BorderLayout());
        Song_status.setBorder(new EmptyBorder(0,0,0,10));
        Song_status.setBackground(new Color(0,0,0,0));

        JPanel Song_info = new JPanel(new BorderLayout(0,5));
        Song_info.setBackground(new Color(0,0,0,0));
        Song_info.setBorder(new EmptyBorder(10,5,10,5));
        JLabel Name_song = new JLabel(song.getTitle());
        Name_song.setPreferredSize(new Dimension(200, 20));
        Name_song.setFont(new Font("Serif", Font.PLAIN, 18));
        Name_song.setForeground(Color.WHITE);
        Name_song.setBorder(new EmptyBorder(5,0,0,0));
        JLabel Artist_song = new JLabel();
        Artist_song.setText(song.getArtist());
        Artist_song.setFont(new Font("Serif", Font.PLAIN, 16));
        Artist_song.setForeground(Color.LIGHT_GRAY );
        Song_info.add(Name_song, BorderLayout.NORTH);
        Song_info.add(Artist_song, BorderLayout.SOUTH);
        Song_status.add(Song_info, BorderLayout.WEST);

        JButton status = createButton("src/main/resources/pngs/circle-plus.png",20,20);
        Song_status.add(status, BorderLayout.EAST);
        Libs.add(Song_status, BorderLayout.CENTER);

        RoundedPanel div_artist = new RoundedPanel(30,Color.decode("#1a1a1a"),0,10);
        JLabel img_artist = new JLabel(new ImageIcon());
        div_artist.add(img_artist, BorderLayout.NORTH);
        Collection user = sc.get_user_by_id(song.getArtist_id());
        new Thread(() -> {
            try {
                URI uri = new URI(sc.IMG500x500(user.getAvatar_url()));
                ImageIcon originalIcon = new ImageIcon(uri.toURL());
                Image scaledImage = originalIcon.getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(scaledImage);
                img_artist.setIcon(resizedIcon);
            } catch (URISyntaxException | MalformedURLException e) {
                e.printStackTrace();
                img_artist.setIcon(new ImageIcon("src/main/resources/pngs/x.png"));
            }
        }).start();

        JLabel name_artist = new JLabel();
        name_artist.setForeground(Color.WHITE);
        name_artist.setText(Artist_song.getText());
        name_artist.setFont(new Font("Serif", Font.PLAIN, 22));

        JPanel info_artist = new JPanel(new BorderLayout());
        info_artist.setBackground(Color.decode("#1a1a1a"));
        JPanel fl_artist = new JPanel(new BorderLayout());
        fl_artist.setBackground(Color.decode("#1a1a1a"));
        JLabel fl_count = new JLabel();
        fl_count.setFont(new Font("Serif", Font.PLAIN, 16));
        fl_count.setText(user.getFollowers_count()+" follower");
        fl_count.setForeground(Color.WHITE);
        JButton follow = createButton("src/main/resources/pngs/fl_grey.png",100,40);
        fl_artist.add(fl_count, BorderLayout.WEST);
        fl_artist.add(follow, BorderLayout.EAST);

        name_artist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                name_artist.setForeground(new Color(101,145,126));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                name_artist.setForeground(Color.WHITE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                home.reload_Artist(song.getArtist_id(), home.get_wit() ==  1150 ? 1150 : 860);
            }
        });

        info_artist.add(name_artist, BorderLayout.NORTH);
        info_artist.add(fl_artist, BorderLayout.SOUTH);
        div_artist.add(info_artist, BorderLayout.CENTER);

        follow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mongo.Insert_Love_Artist(new Love_Artists(null, "_ndyduc_", song.getArtist_id(), song.getArtist(), user.getAvatar_url()));
                follow.setIcon(null);
                follow.setText("Unfollow");
                follow.setPreferredSize(new Dimension(100,40));
                follow.setForeground(Color.WHITE);
            }
        });

        JTextArea bio_artist = new JTextArea();
        bio_artist.setForeground(Color.WHITE);
        bio_artist.setText("    "+user.getDescription());
//        bio_artist.setCaretPosition(0);
        bio_artist.setLineWrap(true); // Cho phép xuống dòng tự động
        bio_artist.setWrapStyleWord(true); // Xuống dòng theo từ (không cắt giữa từ)
        bio_artist.setEditable(false); // Chế độ chỉ đọc
        bio_artist.setBackground(Color.decode("#2a2a2a"));
        bio_artist.setBorder(null);
        div_artist.add(bio_artist, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> {
            info_track_scroll.getVerticalScrollBar().setValue(0);
        });
        Libs.add(div_artist, BorderLayout.SOUTH);

        info_scroll.add(Libs);
    }

    public void setShowArtistListener(Artist_Lis listener) {
        this.listener = listener;
    }
}
