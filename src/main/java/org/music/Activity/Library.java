package org.music.Activity;

import org.music.Components.Border_Radius;
import org.music.Components.RoundedPanel;
import org.music.Components.Rounded_Label;
import org.music.MongoDB;
import org.music.getAPI.Soundcloud;
import org.music.models.DB.Playlists;
import org.music.models.Queue_Item;
import org.music.models.Track;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.music.Activity.Home.createButton;

class Library extends Border_Radius {
    Soundcloud sc = new Soundcloud();
    MongoDB mongo = new MongoDB();
    Home home ;
    JPanel Libs = new JPanel(new GridLayout(0, 1));


    public Library(int radius, Queue_Item item, Home home) {
        super(radius);
        this.home = home;
        setLayout(new BorderLayout(0,10));
        setPreferredSize(new Dimension(250, 0));
        setBackground(Color.decode("#1a1a1a"));
        setBorder(new EmptyBorder(10, 5, 5, 5));

        JPanel topLib = new JPanel(new BorderLayout());
        JLabel li = new JLabel(new ImageIcon("src/main/resources/pngs/layout-list.png"));
        JButton addli = createButton("src/main/resources/pngs/square-rounded-plus.png",25,25);
        JLabel softli = new JLabel(new ImageIcon("src/main/resources/pngs/list.png"));
        JLabel libraryLabel = new JLabel("Library");
        libraryLabel.setFont(new Font("Serif", Font.BOLD, 16));
        libraryLabel.setForeground(Color.WHITE);

        addli.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = mongo.Count_Playlists();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String currentDate = LocalDateTime.now().format(formatter);

                Playlists p = new Playlists(null, "Playlist number " + (i+1), "_ndyduc_", null, false, null, "undownload", currentDate, false, false);
                mongo.Insert_into_playlist(p);
                home.refresh_House();
                Refesh_Library();
            }

        });

        topLib.setBackground(Color.decode("#1a1a1a"));
        JPanel leftLibtop = new JPanel(new BorderLayout(5,0));
        leftLibtop.setBackground(Color.decode("#1a1a1a"));
        leftLibtop.add(li , BorderLayout.WEST);
        leftLibtop.add(libraryLabel, BorderLayout.EAST);
        JPanel rightLibtop = new JPanel(new BorderLayout(5,0));
        rightLibtop.setBackground(Color.decode("#1a1a1a"));
        rightLibtop.add(addli, BorderLayout.WEST);
        rightLibtop.add(softli, BorderLayout.EAST);
        topLib.add(leftLibtop, BorderLayout.WEST);
        topLib.add(rightLibtop, BorderLayout.EAST);
        add(topLib, BorderLayout.NORTH);

        JPanel resizeHandle = new JPanel();
        resizeHandle.setPreferredSize(new Dimension(2, 0));  // Độ rộng thanh kéo là 10px
        resizeHandle.setBackground(Color.decode("#1a1a1a"));
        add(resizeHandle, BorderLayout.EAST);
        resizeHandle.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));

        resizeHandle.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int newWidth = e.getXOnScreen() - getLocationOnScreen().x;
                if (newWidth > 200 && newWidth < 400) {
                    setPreferredSize(new Dimension(newWidth, getHeight()));
                    revalidate();
                    repaint();
                }
            }
        });

        JPanel libscroll = new JPanel(new FlowLayout());
        libscroll.setBackground(Color.decode("#1a1a1a"));

        Libs.setBackground(Color.decode("#1a1a1a"));

        List<Playlists> playlists = mongo.get_Playlists("_ndyduc_");

        for (Playlists playlist : playlists) {
            JPanel newlib = getLibrary(playlist);
            Libs.add(newlib);
        }

        libscroll.add(Libs);
        JScrollPane playlistScroll = new JScrollPane(libscroll);
        playlistScroll.setBorder(null);
        playlistScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        playlistScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        add(playlistScroll, BorderLayout.CENTER);

    }

    public void Refesh_Library() {
        Libs.removeAll();
        List<Playlists> playlists = mongo.get_Playlists("_ndyduc_");

        for (Playlists playlist : playlists) {
            JPanel newlib = getLibrary(playlist);
            Libs.add(newlib);
        }
        Libs.revalidate();
        Libs.repaint();
    }

    private JPanel getLibrary(Playlists item) {
        Border_Radius panel = new Border_Radius(15);
        panel.setBackground(Color.decode("#1a1a1a"));
        panel.setBorder(new LineBorder(new Color(0,0,0), 5));
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.setLayout(new BorderLayout(10,5));
        panel.setMinimumSize(new Dimension(200,50));

        Rounded_Label libimg = new Rounded_Label(new ImageIcon("src/main/resources/pngs/me.png"),10);
        libimg.setPreferredSize(new Dimension(50, 50));

        new Thread(() -> {
            try {
                String a = item.getImage();
                ImageIcon imageIcon;

                if (a != null && !a.isEmpty()) {
                    BufferedImage img = ImageIO.read(new File(a));
                    imageIcon = new ImageIcon(img);
                } else {
                    imageIcon = new ImageIcon("src/main/resources/pngs/me.png");
                }
                SwingUtilities.invokeLater(() -> libimg.setIcon(imageIcon));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        JPanel rlib = new JPanel(new BorderLayout(0,5));
        rlib.setBackground(new Color(0,0,0,0));
        JLabel namelib = new JLabel();
        namelib.setText("<html><body style='width: 100px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis'>"
                + item.getName() + "</body></html>");
        namelib.setFont(new Font("Arial", Font.PLAIN, 14));
        namelib.setBorder(new EmptyBorder(10,0,0,0));
        namelib.setMinimumSize(new Dimension(200,50));
        namelib.setForeground(Color.WHITE);

        JPanel botlib = new JPanel(new BorderLayout());
        botlib.setBackground(new Color(0,0,0,0));
        if (item.getIs_pin()) botlib.add(new JLabel(new ImageIcon("src/main/resources/pngs/pin.png")));
        if (item.getIs_dl()) botlib.add(new JLabel(new ImageIcon("src/main/resources/pngs/download.png")));
        JLabel plist = new JLabel(item.getOwner());
        plist.setForeground(new Color(178,178,178));
        botlib.add(plist);

        rlib.add(namelib, BorderLayout.NORTH);
        rlib.add(botlib, BorderLayout.SOUTH);

        panel.add(libimg, BorderLayout.WEST);
        panel.add(rlib, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(73,73,73));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.decode("#1a1a1a"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                home.set_Lib(item);
            }
        });

        return panel;
    }
}

