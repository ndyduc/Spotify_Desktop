package org.music.Activity;

import jdk.jfr.Description;
import org.music.Components.Border_Radius;
import org.music.Components.RoundedPanel;
import org.music.Components.Rounded_Label;
import org.music.Func.Stream;
import org.music.MongoDB;
import org.music.getAPI.Soundcloud;
import org.music.models.DB.Playlists;
import org.music.models.Queue_Item;
import org.music.models.Search_Tracks.Collection;
import org.music.models.Songs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Name;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.music.Activity.Home.createButton;
import static org.music.Activity.Home.loadIcon;

public class Playlist extends Border_Radius {
    private static final Logger log = LoggerFactory.getLogger(Playlist.class);
    Soundcloud sc = new Soundcloud();
    MongoDB mongo = new MongoDB();
    int wit = 250;
    Stream stream;
    Playlists pl;
    Home home ;
    JPanel lmao = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
    Boolean isplaying = false;
    Boolean is_edit = false;
    CardLayout center_pl = new CardLayout();
    JPanel pl_center = new JPanel(center_pl);

    Rounded_Label img = new Rounded_Label(new ImageIcon(), 30);
    JLabel title = new JLabel();
    JTextArea txt = new JTextArea();
    Rounded_Label img_e = new Rounded_Label(new ImageIcon(), 30);
    JTextField img_edit = new JTextField();
    JTextField name_edit = new JTextField();
    JTextArea txtedit = new JTextArea();

    JButton Playbtn = home.createButton("src/main/resources/pngs/player-play.png",40,40);
    JButton shuffle ;
    JButton downl = home.createButton("src/main/resources/pngs/circle-arrow-down.png",30,30);
    JButton dots = home.createButton("src/main/resources/pngs/pencil.png",30,30);
    JButton sort = home.createButton("src/main/resources/pngs/playlist.png",30,30);
    JButton trash = home.createButton("src/main/resources/pngs/trash.png",30,30);

    Border_Radius div_btn = new Border_Radius(30);

    JLabel alb = new JLabel("Album");
    JPanel head = new JPanel(new BorderLayout(50,0));
    Border_Radius cxz = new Border_Radius(30);
    JPanel zehn = new JPanel(new GridLayout(0,1));

    public Playlist(int radius, Playlists playlist, Stream stream, Frame window, Home home) {
        super(radius);
        this.home  = home;
        this.stream = stream;
        this.pl = playlist;
        if (pl.getIs_shuffle())
            shuffle = home.createButton("src/main/resources/pngs/arrows-shuffle-green.png",30,30);
        else
            shuffle = home.createButton("src/main/resources/pngs/arrows-shuffle.png",30,30);

        setBackground(new Color(101, 145, 126));

        JPanel header = gethead(pl);
        center_pl.show(pl_center, "show");

        Border_Radius body = get_main();

//        lmao.setBackground(new Color(101, 145, 126));
        lmao.add(header);

        Border_Radius flo = new Border_Radius(30);
        flo.setLayout(new BorderLayout());
        flo.setBackground(Color.decode("#1a1a1a"));
        flo.add(lmao, BorderLayout.NORTH);
        flo.add(body, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(flo);
        scrollPane.setPreferredSize(new Dimension(600,440));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        trash.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure to remove playlist "+pl.getName()+" ?",
                    "Remove Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                mongo.remove_Playlist(pl.getId());
                home.reload_playlist();
                home.show_home();
            }
        });

        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension windowSize = window.getSize();
                if (windowSize.width == screenSize.width && windowSize.height == screenSize.height |
                        window.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    scrollPane.setPreferredSize(new Dimension(1150,900));
                    div_btn.setPreferredSize(new Dimension(1150,60));
                    set_wit(700);
                    refresh_zehn();

                } else if (window.getExtendedState() == JFrame.NORMAL) {
                    System.out.println(window.getWidth());
                    scrollPane.setPreferredSize(new Dimension(600,440));
                    div_btn.setPreferredSize(new Dimension(600,60));
                    set_wit(250);
                    refresh_zehn();
                }
                scrollPane.revalidate();
                scrollPane.repaint();
                zehn.revalidate();
                zehn.repaint();
                div_btn.revalidate();
                div_btn.repaint();
                cxz.revalidate();
                cxz.repaint();

            }
        });
    }

    private void set_wit(int wit){this.wit = wit;}

    public void reload_pl(Playlists i){
        this.pl = i;
        new Thread(() -> {
            try {
                String a = sc.IMG500x500(i.getImage());
                if (a != null && !a.isEmpty()) {
                    URI uri = new URI(a);
                    ImageIcon imageIcon = new ImageIcon(uri.toURL());
                    SwingUtilities.invokeLater(() -> {
                        img.setIcon(imageIcon);
                        img_e.setIcon(imageIcon);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        img.setIcon(new ImageIcon("src/main/resources/pngs/me.png"));
                        img_e.setIcon(new ImageIcon("src/main/resources/pngs/me.png"));
                    });
                }

            } catch (URISyntaxException | MalformedURLException e) {
//                e.printStackTrace();
            }
        }).start();
        title.setText(i.getName());
        txt.setText(i.getDescription());
        name_edit.setText(i.getName());
        txtedit.setText(i.getDescription());
        is_edit = false;
        center_pl.show(pl_center,"show");
        dots.setIcon(home.loadIcon("src/main/resources/pngs/pencil.png", 30, 30));
        refresh_zehn();
    }

    private JPanel gethead(Playlists playlist) {
        JLabel pla = new JLabel("Öffentliche Playlist");
        pla.setBorder(new EmptyBorder(10,250,0,0));
        pla.setForeground(Color.white);
        pla.setFont(new Font("Serif", Font.PLAIN , 16));

        JPanel head_main = new JPanel(new FlowLayout(FlowLayout.LEFT,20,0));
        head_main.setBackground(new Color(101,145,126));

        img.setPreferredSize(new Dimension(200,200));
        img.setBorder(new EmptyBorder(20,0,20,20));

        new Thread(() -> {
            try {
                String a = sc.IMG500x500(playlist.getImage());
                if (a != null && !a.isEmpty()) {
                    URI uri = new URI(a);
                    ImageIcon imageIcon = new ImageIcon(uri.toURL());
                    SwingUtilities.invokeLater(() -> {
                        img.setIcon(imageIcon);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        img.setIcon(new ImageIcon("src/main/resources/pngs/me.png"));
                    });
                }

            } catch (URISyntaxException | MalformedURLException e) {
//                e.printStackTrace();
            }
        }).start();
        head_main.add(img);

        JPanel right_main = new JPanel(new BorderLayout(0,10));
        right_main.setBackground(new Color(101,145,126));

        title.setText(playlist.getName());
        title.setBackground(new Color(101,145,126));
        title.setForeground(Color.white);
        title.setFont(new Font("Serif", Font.BOLD , 32));

        txt.setEditable(false);
        txt.setFocusable(false);
        txt.setBackground(new Color(101,145,126));
        txt.setForeground(Color.white);
        txt.setPreferredSize(new Dimension(300,100));
        txt.setFont(new Font("Serif", Font.PLAIN , 16));
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setBorder(new EmptyBorder(20,20,20,20));
        if(playlist.getDescription() != null) txt.setText(playlist.getDescription());

        div_btn.setLayout(new FlowLayout(FlowLayout.LEFT,30,0));
        div_btn.setBackground(new Color(2, 2, 2, 128));
        div_btn.setBorder(new EmptyBorder(10,10,10,10));

        Playbtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isplaying) {
                    Playbtn.setIcon(home.loadIcon("src/main/resources/pngs/player-play.png", 40, 40));
                } else {
                    Playbtn.setIcon(home.loadIcon("src/main/resources/pngs/player-pause.png", 40, 40));
                }
                isplaying = !isplaying; // Thay đổi trạng thái chơi
            }
        });
        sort.setBorder(new EmptyBorder(0,400,0,0));

        div_btn.add(Playbtn);
        div_btn.add(shuffle);
        div_btn.add(downl);
        div_btn.add(dots);
        div_btn.add(sort);

        right_main.add(title, BorderLayout.NORTH);
        right_main.add(txt, BorderLayout.CENTER);
        head_main.add(right_main);

        JPanel edit = new JPanel(new FlowLayout(FlowLayout.LEFT,20,0));
        edit.setBackground(new Color(101,145,126));

        img_e.setPreferredSize(new Dimension(200,200));
        img_e.setMaximumSize(new Dimension(200,200));

        new Thread(() -> {
            try {
                String a = sc.IMG500x500(playlist.getImage());
                if (a != null && !a.isEmpty()) {
                    URI uri = new URI(a);
                    ImageIcon imageIcon = new ImageIcon(uri.toURL());
                    SwingUtilities.invokeLater(() -> {
                        img_e.setIcon(imageIcon);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        img_e.setIcon(new ImageIcon("src/main/resources/pngs/me.png"));
                    });
                }

            } catch (URISyntaxException | MalformedURLException e) {
//                e.printStackTrace();
            }
        }).start();
        head_main.add(img_e);

        JLabel linkimg = new JLabel("Enter link to picture");
        linkimg.setForeground(new Color(253, 124, 141));
        linkimg.setFont(new Font("Serif", Font.BOLD , 16));

        img_edit.setMaximumSize(new Dimension(340,20));
        img_edit.setForeground(new Color(253, 124, 141));
        img_edit.setBackground(new Color(101, 145, 126));
        if(playlist.getImage() != null) img_edit.setText(playlist.getImage());
        else img_edit.setText("Add link Image here !");
        img_edit.setBorder(new EmptyBorder(5,5,5,20));
        img_edit.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                img_edit.setBackground(new Color(253, 124, 141));
                img_edit.setForeground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                img_edit.setBackground(new Color(101,145, 126));
                img_edit.setForeground(Color.WHITE);

                new Thread(() -> {
                    try {
                        String a = sc.IMG500x500(img_edit.getText());
                        if (a != null && !a.isEmpty()) {
                            URI uri = new URI(a);
                            ImageIcon imageIcon = new ImageIcon(uri.toURL());
                            SwingUtilities.invokeLater(() -> {
                                img_e.setIcon(imageIcon);
                            });
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                img_e.setIcon(new ImageIcon("src/main/resources/pngs/me.png"));
                            });
                        }

                    } catch (URISyntaxException | MalformedURLException se) {
//                e.printStackTrace();
                    }
                }).start();
            }
        });


        name_edit.setMaximumSize(new Dimension(340,20));
        name_edit.setBackground(new Color(101,145,126));
        name_edit.setText(playlist.getName());
        name_edit.setForeground(new Color(255, 182, 193));
        name_edit.setFont(new Font("Serif", Font.PLAIN , 16));
        name_edit.setBorder(new EmptyBorder(5,20,10,20));
        name_edit.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                name_edit.setBackground(new Color(253, 124, 141));
                name_edit.setForeground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                name_edit.setBackground(new Color(101,145, 126));
                name_edit.setForeground(Color.WHITE);
            }
        });

        txtedit.setBackground(new Color(0,0,0,0));
        txtedit.setForeground(new Color(255, 182, 193));
        txtedit.setPreferredSize(new Dimension(340,100));
        txtedit.setMaximumSize(new Dimension(300,100));
        txtedit.setFont(new Font("Serif", Font.PLAIN , 16));
        txtedit.setLineWrap(true);
        txtedit.setWrapStyleWord(true);
        txtedit.setBorder(new EmptyBorder(20,20,20,20));
        if(playlist.getDescription() != null) txtedit.setText(playlist.getDescription());
        else txtedit.setText("Add Your Imagination !");

        txtedit.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtedit.setBackground(new Color(253, 124, 141));
                txtedit.setForeground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtedit.setBackground(new Color(101,145, 126));
                txtedit.setForeground(Color.WHITE);
            }
        });

        JPanel div_edit = new JPanel(new FlowLayout(FlowLayout.LEFT,20,0));
        div_edit.setBackground(new Color(0,0,0,0));

        JPanel head_edit = new JPanel();
        head_edit.setLayout(new BoxLayout(head_edit, BoxLayout.Y_AXIS));
        head_edit.setBackground(new Color(101, 145, 126));


        dots.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(is_edit){
                    is_edit = false;
                    center_pl.show(pl_center,"show");
                    dots.setIcon(home.loadIcon("src/main/resources/pngs/pencil.png", 30, 30));

                    Playlists update = new Playlists(
                            pl.getId(),
                            name_edit.getText() != null && !name_edit.getText().isEmpty()
                                    ? name_edit.getText()
                                    : pl.getName(),
                            pl.getOwner(),
                            txtedit.getText(),
                            pl.getIs_shuffle(),
                            img_edit.getText(),
                            pl.getStatus(),
                            pl.getCreated_at(),
                            pl.getIs_pin(),
                            pl.getIs_dl()
                    );
                    mongo.Update_Playlist(update);
                    title.setText(name_edit.getText());
                    txt.setText(txtedit.getText());
                    home.reload_playlist();
                } else {
                    is_edit = true;
                    center_pl.show(pl_center, "edit");
                    dots.setIcon(home.loadIcon("src/main/resources/pngs/rosette-discount-check.png", 30, 30));
                }
            }
        });

        head_edit.add(linkimg);
        head_edit.add(img_edit);
        head_edit.add(name_edit);
        head_edit.add(txtedit);
        JPanel trashPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        trashPanel.setBackground(new Color(101, 145, 126));
        trashPanel.add(trash);
        head_edit.add(trashPanel);


        edit.add(img_e);
        edit.add(head_edit);

        pl_center.add(head_main, "show");
        pl_center.add(edit, "edit");

        JPanel header = new JPanel(new BorderLayout(10,10));
        header.setBackground(new Color(101, 145, 126));
        header.setBorder(new EmptyBorder(0,0,10,0));
        header.add(pla, BorderLayout.NORTH);
        header.add(pl_center, BorderLayout.CENTER);
        header.add(div_btn, BorderLayout.SOUTH);

        return header;
    }

    private Border_Radius get_main(){
        Border_Radius body = new Border_Radius(30);
        body.setBackground(Color.decode("#1a1a1a"));
        body.setLayout(new BorderLayout());
        body.setBorder(new EmptyBorder(10,10,10,10));

        get_head();

        cxz.setLayout(new BorderLayout()); // chuyen sang flow
        cxz.setBackground(Color.decode("#1a1a1a"));
        cxz.setPreferredSize(new Dimension(600, 20));
        cxz.add(head, BorderLayout.WEST);

        body.add(cxz, BorderLayout.NORTH);
        body.add(zehn, BorderLayout.CENTER);

        refresh_zehn();
        return body;
    }

    private void get_head(){
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
    }

    public void refresh_zehn(){
        zehn.removeAll();
        zehn.setBackground(Color.decode("#1a1a1a"));

        if (wit == 250) {
            cxz.setPreferredSize(new Dimension(600, 20));
            head.setPreferredSize(new Dimension(600, 20));
            alb.setPreferredSize(new Dimension(230, 30));
        } else {
            cxz.setPreferredSize(new Dimension(1150, 20));
            head.setPreferredSize(new Dimension(1150, 20));
            alb.setPreferredSize(new Dimension(320, 30));
        }

        SwingWorker<java.util.List<JPanel>, JPanel> worker = new SwingWorker<>() {
            @Override
            protected java.util.List<JPanel> doInBackground() {
                java.util.List<JPanel> panels = new ArrayList<>();
                java.util.List<Queue_Item> tracks = mongo.getSongsByWhere(pl.getId());
                int z = 1;

                for (Queue_Item track : tracks) {
                    JPanel songItem = add_song_item(track, z, wit); // Tạo item trong luồng nền
                    panels.add(songItem); // Lưu lại để thêm vào giao diện sau
                    z++;
                }
                return panels;
            }

            @Override
            protected void process(java.util.List<JPanel> chunks) {
                for (JPanel panel : chunks) {
                    zehn.add(panel);
                }
                zehn.revalidate();
                zehn.repaint();
            }

            @Override
            protected void done() {
                try {
                    List<JPanel> panels = get();
                    for (JPanel panel : panels) {
                        zehn.add(panel);
                    }
                    zehn.revalidate();
                    zehn.repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private JPanel add_song_item(Queue_Item i, int number, int wit){
        Border_Radius a = new Border_Radius(15);
        a.setBackground(Color.decode("#1a1a1a"));
        a.setBorder(new EmptyBorder(5,5,5,30));
        a.setLayout(new BorderLayout(10,5));
        a.setPreferredSize(new Dimension(600, 55));

        Rounded_Label img_track = new Rounded_Label(new ImageIcon(),10);
        img_track.setPreferredSize(new Dimension(45,45));

        new Thread(() ->{
            try {
                URI uri = new URI(sc.IMG500x500(i.getImgCover()));
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
        artist_track.setText(i.getArtist());
        artist_track.setForeground(Color.GRAY);
        artist_track.setFont(new Font("Serif", Font.PLAIN, 15));
        jkl.add(name_track, BorderLayout.NORTH);
        jkl.add(artist_track, BorderLayout.SOUTH);

        artist_track.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                artist_track.setForeground(new Color(101, 145, 126));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                artist_track.setForeground(Color.GRAY);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                home.reload_Artist(i.getArtist_id());
            }
        });

        JLabel albu = new JLabel(i.getAlbum());
        albu.setForeground(Color.GRAY);
        albu.setFont(new Font("Serif", Font.PLAIN, 15));

        JButton add = createButton("src/main/resources/pngs/dots.png",25,25);
        JPopupMenu pop = home.popup_Album(i, add, pl);
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
                isplaying = true;
                Playbtn.setIcon(home.loadIcon("src/main/resources/pngs/player-pause.png", 40, 40));
                stream.addToFront(i);
                home.setCurrentSong(i);
                home.Play_track();
            }
        });

        return a;
    }

}
