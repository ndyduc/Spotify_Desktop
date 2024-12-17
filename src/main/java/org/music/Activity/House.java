package org.music.Activity;

import org.music.Components.Border_Radius;
import org.music.Components.Rounded_Label;
import org.music.MongoDB;
import org.music.getAPI.Soundcloud;
import org.music.models.Albums;
import org.music.models.DB.Love_Artists;
import org.music.models.DB.Playlists;
import org.music.models.Queue_Item;
import org.music.models.Search_Album.Collection;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class House extends Border_Radius {
    Home home;
    MongoDB mongo = new MongoDB();
    Soundcloud sc = new Soundcloud();

    Border_Radius elf = new Border_Radius(30);
    JPanel zwolf = new JPanel(new BorderLayout());
    JPanel artists = new JPanel(new FlowLayout(FlowLayout.LEFT));
    Border_Radius Suggest = new Border_Radius(30);
    JPanel suggest = new JPanel(new FlowLayout(FlowLayout.LEFT));
    Border_Radius Album = new Border_Radius(30);
    JPanel album = new JPanel(new FlowLayout(FlowLayout.LEFT));

    int wit;
    public House(int radius, Home home, Frame window) {
        super(radius);
        this.home = home;
        setBackground(Color.decode("#1a1a1a"));
        setBorder(new EmptyBorder(10,10,10,10));
        setLayout(new BorderLayout(0,10));

        JLabel ndyduc = new JLabel("_ndyduc_");
        ndyduc.setFont(new Font("Serif", Font.BOLD, 26));
        ndyduc.setForeground(Color.WHITE);

        Border_Radius top = new Border_Radius(30);
        top.setLayout(new BorderLayout(0,10));
        top.setBackground(Color.decode("#1a1a1a"));
        top.add(ndyduc, BorderLayout.NORTH);
        top.add(elf, BorderLayout.CENTER);
        get_Playlist();

        Border_Radius center = new Border_Radius(30);
        center.setLayout(new BorderLayout(0,10));
        center.setBackground(Color.decode("#1a1a1a"));

        get_Artirts();
        get_Suggest(Suggest, suggest, "playlist", "Pop american");
        get_Suggest(Album, album, "album", "Cigarettes after sex");

        center.add(zwolf, BorderLayout.NORTH);
        center.add(Suggest, BorderLayout.CENTER);
        center.add(Album, BorderLayout.SOUTH);

        JPanel main = new JPanel(new BorderLayout(0,10));
        main.setPreferredSize(new Dimension(580, 850));
        main.setBackground(Color.decode("#1a1a1a"));
        main.add(top, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);

        Border_Radius empty = new Border_Radius(30);
        empty.setBackground(Color.decode("#1a1a1a"));
        empty.setPreferredSize(new Dimension(600, 200));
        main.add(empty, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(main);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension windowSize = window.getSize();
                if (windowSize.width == screenSize.width && windowSize.height == screenSize.height |
                        window.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
//                    refesh_artist();
                } else if (window.getExtendedState() == JFrame.NORMAL) {
//                    refesh_artist();
                }

            }
        });
    }

    private void get_Suggest(Border_Radius a, JPanel b, String kind, String title){
        a.removeAll();
        a.setLayout(new BorderLayout());
        a.setPreferredSize(new Dimension(580, 150));
        a.setBackground(Color.decode("#1a1a1a"));

        JLabel ar = new JLabel("Popular "+kind);
        ar.setFont(new Font("Serif", Font.PLAIN, 16));
        ar.setForeground(Color.WHITE);
        a.add(ar, BorderLayout.NORTH);

        b.setBackground(Color.decode("#1a1a1a"));

        JScrollPane scroll = new JScrollPane(b);
        scroll.getHorizontalScrollBar().setUnitIncrement(16); // Tốc độ cuộn ngang
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); // Cho phép cuộn ngang
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0)); // Thiết lập chiều rộng thanh cuộn ngang bằng 0

        SwingWorker<java.util.List<JPanel>, JPanel> worker = new SwingWorker<>() {
            @Override
            protected List<JPanel> doInBackground() {
                List<JPanel> panels = new ArrayList<>();
                Albums playlis = sc.get_Albums(title, 100);
                List<Collection> albums = playlis.getCollection();
                int i=0;
                for (Collection album : albums) {
                    if(i >= 20) break;
                    if(!Objects.equals(kind, "playlist")){
                        if(album.isIs_album()) panels.add(get_Sugest_item(album));
                    }else {
                        if(!album.isIs_album()) panels.add(get_Sugest_item(album));
                    }
                    i++;
                }
                return panels;
            }

            @Override
            protected void process(java.util.List<JPanel> chunks) {
                for (JPanel panel : chunks) {
                    b.add(panel);
                }
                b.revalidate();
                b.repaint();
            }

            @Override
            protected void done() {
                try {
                    List<JPanel> panels = get();
                    for (JPanel panel : panels) {
                        b.add(panel);
                    }
                    b.revalidate();
                    b.repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();

        a.add(scroll, BorderLayout.CENTER);
    }

    private Border_Radius get_Sugest_item(Collection i){
        Border_Radius item = new Border_Radius(30);
        item.setBackground(Color.decode("#1a1a1a"));
        item.setLayout(new BorderLayout(0,10));
        item.setBorder(new EmptyBorder(5,5,5,5));
        item.setPreferredSize(new Dimension(110,130));

        Rounded_Label img_ar = new Rounded_Label(new ImageIcon(), 30);
        img_ar.setPreferredSize(new Dimension(100,100));

        new Thread(() ->{
            try {
                URI uri = new URI(sc.IMG500x500(i.getTracks().getFirst().getArtwork_url()));
                ImageIcon imageIcon = new ImageIcon(uri.toURL());
                SwingUtilities.invokeLater(() -> { img_ar.setIcon(imageIcon); });
            }  catch (URISyntaxException | MalformedURLException | NullPointerException e) {
                SwingUtilities.invokeLater(() -> { img_ar.setIcon(new ImageIcon("src/main/resources/pngs/x.png")); });
            }
        }).start();


        JLabel name_ar = new JLabel(i.getTitle());
        name_ar.setFont(new Font("Serif", Font.PLAIN, 12));
        name_ar.setForeground(new Color(255, 253, 208));

        item.add(img_ar, BorderLayout.NORTH);
        item.add(name_ar, BorderLayout.SOUTH);

        return item;
    }

    private void get_Playlist(){
        elf.setBackground(Color.decode("#1a1a1a"));
        elf.setLayout(new FlowLayout(FlowLayout.LEFT));
        refesh_playlists();
    }

    public void refesh_playlists() {
        elf.removeAll();
        List<Playlists> playlists = mongo.get_Playlists("_ndyduc_");
        elf.setPreferredSize(new Dimension(600, (playlists.size() / 2 + 1) * 60));

        SwingWorker<java.util.List<JPanel>, JPanel> worker = new SwingWorker<>() {
            @Override
            protected java.util.List<JPanel> doInBackground() {
                java.util.List<JPanel> panels = new ArrayList<>();
                for (Playlists playlist : playlists) {
                    panels.add(get_Playlist_item(playlist));
                }
                return panels;
            }

            @Override
            protected void process(java.util.List<JPanel> chunks) {
                for (JPanel panel : chunks) {
                    elf.add(panel);
                }
                elf.revalidate();
                elf.repaint();
            }

            @Override
            protected void done() {
                try {
                    List<JPanel> panels = get();
                    for (JPanel panel : panels) {
                        elf.add(panel);
                    }
                    elf.revalidate();
                    elf.repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private Border_Radius get_Playlist_item(Playlists playlist){
        Border_Radius item = new Border_Radius(30);
        item.setBackground(Color.decode("#2a2a2a"));
        item.setLayout(new FlowLayout(FlowLayout.LEFT));
        item.setPreferredSize(new Dimension(285,50));

        Rounded_Label img = new Rounded_Label(new ImageIcon("src/main/resources/pngs/me.png"),30);
        img.setPreferredSize(new Dimension(40,40));

        new Thread(() -> {
            try {
                String a = playlist.getImage();
                ImageIcon imageIcon;

                if (a != null && !a.isEmpty()) {
                    BufferedImage imgx = ImageIO.read(new File(a));
                    imageIcon = new ImageIcon(imgx);
                } else {
                    BufferedImage imgx = ImageIO.read(new File("src/main/resources/pngs/me.png"));
                    imageIcon = new ImageIcon(imgx);
                }
                SwingUtilities.invokeLater(() -> img.setIcon(imageIcon));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        JLabel name = new JLabel(playlist.getName());
        name.setFont(new Font("Serif", Font.PLAIN, 16));
        name.setForeground(Color.WHITE);
        name.setPreferredSize(new Dimension(220,40));

        item.add(img);
        item.add(name);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                home.set_Lib(playlist);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(Color.decode("#3a3a3a"));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(Color.decode("#2a2a2a"));
            }
        });

        return item;
    }

    private void get_Artirts(){
        zwolf.setPreferredSize(new Dimension(550, 160));
        zwolf.setBorder(new EmptyBorder(0,0,0,30));
        zwolf.setBackground(Color.decode("#1a1a1a"));

        JLabel ar = new JLabel("Favorist Artists");
        ar.setFont(new Font("Serif", Font.PLAIN, 16));
        ar.setForeground(Color.WHITE);
        zwolf.add(ar, BorderLayout.NORTH);

        artists.setBackground(Color.decode("#1a1a1a"));
        artists.setLayout(new FlowLayout(FlowLayout.LEFT));

        JScrollPane scroll = new JScrollPane(artists);
        scroll.getHorizontalScrollBar().setUnitIncrement(16); // Tốc độ cuộn ngang
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); // Cho phép cuộn ngang
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0)); // Thiết lập chiều rộng thanh cuộn ngang bằng 0

        refesh_artist();


        zwolf.add(scroll, BorderLayout.SOUTH);
    }

    public void refesh_artist(){
        artists.removeAll();

        SwingWorker<java.util.List<JPanel>, JPanel> worker = new SwingWorker<>() {
            @Override
            protected List<JPanel> doInBackground() {
                List<JPanel> panels = new ArrayList<>();
                List<Love_Artists> artists = mongo.Get_Love_Artists_By_Owner("_ndyduc_");

                for (Love_Artists artist : artists) {
                    panels.add(get_Artists_item(artist));
                }
                return panels;
            }

            @Override
            protected void process(java.util.List<JPanel> chunks) {
                for (JPanel panel : chunks) {
                    artists.add(panel);
                }
                artists.revalidate();
                artists.repaint();
            }

            @Override
            protected void done() {
                try {
                    List<JPanel> panels = get();
                    for (JPanel panel : panels) {
                        artists.add(panel);
                    }
                    artists.revalidate();
                    artists.repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private Border_Radius get_Artists_item(Love_Artists artist){
        Border_Radius item = new Border_Radius(30);
        item.setBackground(Color.decode("#1a1a1a"));
        item.setLayout(new BorderLayout(0,10));
        item.setBorder(new EmptyBorder(5,5,5,5));
        item.setPreferredSize(new Dimension(110,130));

        Rounded_Label img_ar = new Rounded_Label(new ImageIcon(), 30);
        img_ar.setPreferredSize(new Dimension(100,100));

        new Thread(() ->{
            try {
                URI uri = new URI(sc.IMG500x500(artist.getArtist_img()));
                ImageIcon imageIcon = new ImageIcon(uri.toURL());
                SwingUtilities.invokeLater(() -> { img_ar.setIcon(imageIcon); });
            }  catch (URISyntaxException | MalformedURLException | NullPointerException e) {
                SwingUtilities.invokeLater(() -> { img_ar.setIcon(new ImageIcon("src/main/resources/pngs/x.png")); });
            }
        }).start();


        JLabel name_ar = new JLabel(artist.getArtist_name());
        name_ar.setFont(new Font("Serif", Font.PLAIN, 12));
        name_ar.setForeground(new Color(255, 253, 208));

        item.add(img_ar, BorderLayout.NORTH);
        item.add(name_ar, BorderLayout.SOUTH);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                home.reload_Artist(artist.getArtist_id(), wit == 1150 ? 1150 : 860);
            }
            @Override
            public void mouseEntered(MouseEvent e) {item.setBackground(Color.decode("#2a2a2a"));}
            @Override
            public void mouseExited(MouseEvent e) {item.setBackground(Color.decode("#1a1a1a"));}
        });

        return item;
    }
}
