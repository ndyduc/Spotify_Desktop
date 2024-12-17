package org.music.Activity;

import org.music.Components.Border_Radius;
import org.music.Components.Rounded_Label;
import org.music.Func.Stream;
import org.music.MongoDB;
import org.music.getAPI.Soundcloud;
import org.music.models.DB.Playlists;
import org.music.models.DB.Queue_Tracks;
import org.music.models.Queue_Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.music.Activity.Home.createButton;
import static org.music.Activity.Home.loadIcon;

public class Playlist extends Border_Radius {
    private static final Logger log = LoggerFactory.getLogger(Playlist.class);
    Soundcloud sc = new Soundcloud();
    MongoDB mongo = new MongoDB();
    Frame window;
    Stream stream;
    Playlists pl;
    Boolean full = false;
    Boolean chance_ing = false;

    Home home ;
    JPanel lmao = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
    Boolean isplaying = false;
    Boolean is_edit = false;
    CardLayout center_pl = new CardLayout();
    JPanel pl_center = new JPanel(center_pl);
    int width = 860;

    Rounded_Label img = new Rounded_Label(new ImageIcon(), 30);
    JLabel title = new JLabel();
    JTextArea txt = new JTextArea();
    Rounded_Label img_e = new Rounded_Label(new ImageIcon(), 30);
    JTextField name_edit = new JTextField();
    JTextArea txtedit = new JTextArea();

    JButton Playbtn = home.createButton("src/main/resources/pngs/player-play.png",40,40);
    JButton shuffle ;
    JButton downl ;
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
        this.window = window;
        if (pl.getIs_shuffle()) shuffle = createButton("src/main/resources/pngs/arrows-shuffle-green.png",30,30);
        else shuffle = createButton("src/main/resources/pngs/arrows-shuffle.png",30,30);
        if(pl.getIs_dl()) downl = createButton("src/main/resources/pngs/circle-arrow-down-green.png",30,30);
        else downl = createButton("src/main/resources/pngs/circle-arrow-down.png",30,30);
        setBackground(new Color(101, 145, 126));
        setBorder(new EmptyBorder(10,0,0,0));

        JPanel header = gethead(pl);
        center_pl.show(pl_center, "show");

        JPanel body = get_main();
        lmao.add(header);

        Border_Radius flo = new Border_Radius(30);
        flo.setLayout(new BorderLayout());
        flo.setBackground(Color.decode("#1a1a1a"));
        flo.add(lmao, BorderLayout.NORTH);
        flo.add(body, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(flo);
        scroll.setPreferredSize(new Dimension((int)(window.getWidth()*0.7), (int)(window.getHeight()-160)));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scroll.setBorder(null);

        add(scroll, BorderLayout.CENTER);

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

        downl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (pl.getIs_dl()){
                    List<Queue_Item> item = mongo.getSongsByWhere(pl.getId());
                    for (Queue_Item i : item) { home.Delete_filepl(i.getFileName()); }
                    pl.setIs_dl(false);
                }else {
                    new Thread(() ->{
                        List<Queue_Item> item = mongo.getSongsByWhere(pl.getId());
                        for (Queue_Item i : item) {
                            home.Dl_for_pl(i.getLink(), i.getFileName());
                        }
                    }).start();
                    pl.setIs_dl(true);
                }
                mongo.DL_Playlist(pl);
                reload_pl(pl);
            }
        });

        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension windowSize = window.getSize();
                if (windowSize.width == screenSize.width && windowSize.height == screenSize.height |
                        window.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    scroll.setPreferredSize(new Dimension(1170,850));
                    div_btn.setPreferredSize(new Dimension(1170,60));
                    cxz.setPreferredSize(new Dimension(1150,20));
                    full = true;
                    width = 1150;
                    refresh_zehn();

                } else if (window.getExtendedState() == JFrame.NORMAL) {
                    scroll.setPreferredSize(new Dimension(window.getWidth()-250,440));
                    div_btn.setPreferredSize(new Dimension(window.getWidth()-250,60));
                    cxz.setPreferredSize(new Dimension(580,20));
                    full = false;
                    width = 860;
                    refresh_zehn();
                }
                scroll.revalidate();
                scroll.repaint();
                zehn.revalidate();
                zehn.repaint();
                div_btn.revalidate();
                div_btn.repaint();
                cxz.revalidate();
                cxz.repaint();
            }
        });
    }

    public void reload_pl(Playlists i){
        this.pl = i;
        chance_ing = false;
        if(i.getIs_dl()) downl.setIcon(loadIcon("src/main/resources/pngs/circle-arrow-down-green.png",30,30));
        else downl.setIcon(loadIcon("src/main/resources/pngs/circle-arrow-down.png",30,30));
        new Thread(() -> {
            try {
                String a = i.getImage();
                ImageIcon imageIcon;

                if (a != null && !a.isEmpty()) {
                    BufferedImage img = ImageIO.read(new File(a));
                    imageIcon = new ImageIcon(img);
                } else {
                    String fallbackImagePath = "src/main/resources/pngs/me.png";
                    BufferedImage img = ImageIO.read(new File(fallbackImagePath));
                    imageIcon = new ImageIcon(img);
                }
                SwingUtilities.invokeLater(() -> {
                    img.setIcon(imageIcon);
                    img_e.setIcon(imageIcon);
                });
            } catch (Exception e) {
                e.printStackTrace();
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
                String a = playlist.getImage();
                ImageIcon imageIcon = new ImageIcon(a);
                SwingUtilities.invokeLater(() -> { img.setIcon(imageIcon); });
            } catch (NullPointerException e) {
//                e.printStackTrace();
                SwingUtilities.invokeLater(() -> { img.setIcon(new ImageIcon("src/main/resources/pngs/me.png")); });
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
                    Playbtn.setIcon(loadIcon("src/main/resources/pngs/player-play.png", 40, 40));
                } else {
                    Playbtn.setIcon(loadIcon("src/main/resources/pngs/player-pause.png", 40, 40));
                }
                isplaying = !isplaying;
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
                String a = playlist.getImage();
                ImageIcon imageIcon = new ImageIcon(a);
                SwingUtilities.invokeLater(() -> { img_e.setIcon(imageIcon); });
            } catch (NullPointerException e) {
//                e.printStackTrace();
                    SwingUtilities.invokeLater(() -> { img.setIcon(new ImageIcon("src/main/resources/pngs/me.png")); });
            }
        }).start();
        head_main.add(img_e);
        final String[] path = new String[1];
        img_e.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    String script = "set chosenFile to choose file with prompt \"Select an image file\" of type {\"public.image\"}\n" +
                                    "return POSIX path of chosenFile";

                    Process process = Runtime.getRuntime().exec(new String[]{"osascript", "-e", script});
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String imagePath = reader.readLine();

                    if (imagePath != null) {
                        String resourcesDir = "src/main/resources/pngs/";
                        String extension = imagePath.substring(imagePath.lastIndexOf("."));
                        path[0] = resourcesDir + pl.getId() + extension;
                        File destFile = new File(path[0]);
                        Files.copy(Paths.get(imagePath), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        ImageIcon imageIcon = new ImageIcon(path[0]);
                        Image img = imageIcon.getImage().getScaledInstance(
                                img_e.getWidth(), img_e.getHeight(), Image.SCALE_SMOOTH);
                        img_e.setIcon(new ImageIcon(img));
                        chance_ing = true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        name_edit.setMaximumSize(new Dimension( 300,20));
        name_edit.setText(playlist.getName());
        name_edit.setFont(new Font("Serif", Font.PLAIN , 16));
        name_edit.setBorder(new EmptyBorder(5,20,10,20));
        name_edit.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                name_edit.setBackground(Color.decode("#3a3a3a"));
                name_edit.setForeground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                name_edit.setBackground(Color.white);
                name_edit.setForeground(Color.BLACK);
            }
        });

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
                txtedit.setBackground(Color.decode("#3a3a3a"));
                txtedit.setForeground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtedit.setBackground(Color.white);
                txtedit.setForeground(Color.black);
            }
        });

        Border_Radius head_edit = new Border_Radius(30);
        head_edit.setLayout(new BoxLayout(head_edit, BoxLayout.Y_AXIS));
        head_edit.setBackground(Color.decode("#1a1a1a"));
        head_edit.setBorder(new EmptyBorder(30,0,30,0));


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
                            path[0],
                            pl.getStatus(),
                            pl.getCreated_at(),
                            pl.getIs_pin(),
                            pl.getIs_dl()
                    );
                    mongo.Update_Playlist(update, chance_ing);
                    reload_pl(playlist);
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

        head_edit.add(name_edit);
        head_edit.add(txtedit);
        Border_Radius trashPanel = new Border_Radius(30);
        trashPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        trashPanel.setBackground(Color.decode("#1a1a1a"));
        trashPanel.add(trash);
        head_edit.add(trashPanel);


        edit.add(img_e);
        edit.add(head_edit);

        Border_Radius album = new Border_Radius(30);
        album.setBackground(new Color(101,145,126));
        album.setLayout(new FlowLayout(FlowLayout.LEFT));

        Rounded_Label img_ab = new Rounded_Label(new ImageIcon(),30);
        img_ab.setPreferredSize(new Dimension(200,200));
        new Thread(() -> {
            try {
                String a = playlist.getImage();
                ImageIcon imageIcon = new ImageIcon(a);
                SwingUtilities.invokeLater(() -> { img_e.setIcon(imageIcon); });
            } catch (NullPointerException e) {
                SwingUtilities.invokeLater(() -> { img.setIcon(new ImageIcon("src/main/resources/pngs/me.png")); });
            }
        }).start();

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

    private JPanel get_main(){
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.decode("#1a1a1a"));
        body.setBorder(new EmptyBorder(10,10,10,10));

        get_head();

        cxz.setLayout(new BorderLayout()); // chuyen sang flow
        cxz.setBackground(Color.decode("#1a1a1a"));
        cxz.setPreferredSize(new Dimension(580, 20));
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

        if (width == 860) {
            alb.setPreferredSize(new Dimension(250, 30));
            head.setPreferredSize(new Dimension(600,20));
        } else {
            head.setPreferredSize(new Dimension(1150,20));
            alb.setPreferredSize(new Dimension(540, 30));
        }

        SwingWorker<List<JPanel>, JPanel> worker = new SwingWorker<>() {
            @Override
            protected List<JPanel> doInBackground() {
                List<JPanel> panels = new ArrayList<>();
                List<Queue_Item> tracks = mongo.getSongsByWhere(pl.getId());
                new Thread(() ->{
                    for(Queue_Item item : tracks){
                        if(!mongo.checkFile(item.getFileName()) && pl.getIs_dl()) home.Dl_for_pl(item.getLink(), item.getFileName());
                    }
                }).start();
                int z = 1;

                for (Queue_Item track : tracks) {
                    JPanel songItem = get_item(track, z, width); // Tạo item trong luồng nền
                    panels.add(songItem); // Lưu lại để thêm vào giao diện sau
                    z++;
                }
                return panels;
            }

            @Override
            protected void process(List<JPanel> chunks) {
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

    private Border_Radius get_item(Queue_Item track, int number, int width) {
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
                URI uri = new URI(sc.IMG500x500(track.getImgCover()));
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
        if(mongo.checkFile(track.getFileName())) clue.add(dl);

        JLabel ar = new JLabel(track.getArtist());
        ar.setFont(new Font("Serif", Font.PLAIN, 12));
        ar.setForeground(Color.gray);
        clue.add(ar);

        ar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ar.setForeground(new Color(101, 145, 126));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                ar.setForeground(Color.GRAY);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                home.reload_Artist(track.getArtist_id(), width == 1150 ? 1150 : 860);
            }
        });

        info.add(name, BorderLayout.NORTH);
        info.add(clue, BorderLayout.SOUTH);
        bor.add(info);

        JLabel album = new JLabel(track.getAlbum());
        album.setFont(new Font("Serif", Font.PLAIN, 12));
        album.setForeground(Color.GRAY);
        album.setPreferredSize(new Dimension((int)((width-250)*0.2),50));
        bor.add(album);


        JLabel genre = new JLabel(track.getGenre());
        genre.setFont(new Font("Serif", Font.PLAIN, 12));
        genre.setForeground(Color.GRAY);
        genre.setPreferredSize(new Dimension((int)(width*0.2),50));

        if(full) {
            info.setPreferredSize(new Dimension((int) (width*0.45), 50));
            bor.add(genre);
        }else {
            info.setPreferredSize(new Dimension((int)(width*0.3),50));
            name.setPreferredSize(new Dimension((int)(width*0.3),20));
        }


        JLabel duration = new JLabel(mongo.get_duration(track.getDuration()));
        duration.setFont(new Font("Serif", Font.PLAIN, 12));
        duration.setForeground(Color.GRAY);
        duration.setPreferredSize(new Dimension((int)(width*0.05),50));
        bor.add(duration);

        JButton more = createButton("src/main/resources/pngs/dots.png", 30, 30);

        bor.add(more);

        JPopupMenu pop = home.popup_Album(track, more );
        home.createPopup(more, pop);

        set_item_clicked(bor, nu, number, track);
        return bor;
    }

    private void set_item_clicked(Border_Radius bor, JLabel nu, int number, Queue_Item track) {
        bor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bor.setBackground(Color.decode("#2a2a2a"));
                nu.setIcon(loadIcon("src/main/resources/pngs/player-play.png", 20, 20)); // Thay đổi icon của `nu`
                nu.setText("");
                nu.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                bor.setBackground(Color.decode("#1a1a1a"));
                nu.setIcon(null);
                nu.setText(String.valueOf(number));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                home.stop_stream();
                isplaying = true;
                Playbtn.setIcon(loadIcon("src/main/resources/pngs/player-pause.png", 40, 40));

                List<Queue_Item> inplaylist = mongo.getSongsByWhere(track.getWhere());
                LinkedList<Queue_Item> que = home.getQueueDL();
                boolean store = true;
                for (Queue_Item item : inplaylist) {
                    if(!mongo.checkFile(item.getFileName())) {
                        store = false;
                        System.out.println(item.getFileName()+"k thay !");
                        break;
                    }
                }

                if (!store) {
                    for (Queue_Item item : que) {
                        home.Delete_file(item.getFileName());
                    }

                    home.clearQueue();
                    for (Queue_Item item : inplaylist) { home.addToQueue(item); }
                } else {
                    home.QueueDL.clear();
                    for (Queue_Item item : inplaylist) { home.QueueDL.offer(item); }
                }

                que = home.getQueueDL();
                for (Queue_Item item : que) {
                    if ( Objects.equals(item.getFileName(), track.getFileName())) {
                        home.getAndRemoveFromQueue(item);
                        break;
                    }
                }

                if(!store) home.addToFront(track);
                else {
                    home.stream.stop();
                    home.QueueDL.offer(track);
                }
                home.refresh_Queue();
                home.setCurrentSong(track);
                home.Play_track();
            }
        });

    }
}
