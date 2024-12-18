package org.music.Activity;

import org.music.Components.Border_Radius;
import org.music.Components.Rounded_Label;
import org.music.Func.Stream;
import org.music.MongoDB;
import org.music.getAPI.Soundcloud;
import org.music.models.Albums;
import org.music.models.DB.Playlists;
import org.music.models.Queue_Item;
import org.music.models.Search_Album.Collection;
import org.music.models.Search_Album.Track;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.music.Activity.Home.createButton;
import static org.music.Activity.Home.loadIcon;

public class Album extends Border_Radius {
    Soundcloud sc = new Soundcloud();
    MongoDB mongo = new MongoDB();
    Frame window;
    Stream stream;
    Boolean full = false;
    Collection album;
    int width = 860;

    Home home ;
    Boolean isplaying = false;
    JLabel Name_ab;

    JLabel alb = new JLabel("Album");
    JPanel head = new JPanel(new BorderLayout(50,0));
    Border_Radius cxz = new Border_Radius(30);
    JPanel dreizehn = new JPanel(new GridLayout(0,1));

    Rounded_Label img = new Rounded_Label(new ImageIcon(), 30);

    public Album(int radius, Albums album, Stream stream, Frame window, Home home) {
        super(radius);
        this.home  = home;
        this.stream = stream;
        this.window = window;
        this.album = album.getCollection().getFirst();

        setBackground(new Color(98, 153, 209));
        setBorder(new EmptyBorder(10,0,0,0));

        JPanel header = gethead();

        JPanel body = get_main();

        Border_Radius flo = new Border_Radius(30);
        flo.setLayout(new BorderLayout());
        flo.setBackground(Color.decode("#1a1a1a"));
        flo.add(header, BorderLayout.NORTH);
        flo.add(body, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(flo);
        scroll.setPreferredSize(new Dimension((int)(window.getWidth()*0.7), (int)(window.getHeight()-160)));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scroll.setBorder(null);

        add(scroll, BorderLayout.CENTER);

        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension windowSize = window.getSize();
                if (windowSize.width == screenSize.width && windowSize.height == screenSize.height |
                        window.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    scroll.setPreferredSize(new Dimension(1170,760));
                    cxz.setPreferredSize(new Dimension(1150,20));
                    full = true;
                    width = 1150;
                    refresh_dreizehn();

                } else if (window.getExtendedState() == JFrame.NORMAL) {
                    scroll.setPreferredSize(new Dimension(window.getWidth()-250,420));
                    cxz.setPreferredSize(new Dimension(580,20));
                    full = false;
                    width = 860;
                    refresh_dreizehn();
                }
                scroll.revalidate();
                scroll.repaint();
                dreizehn.revalidate();
                dreizehn.repaint();
                cxz.revalidate();
                cxz.repaint();
            }
        });
    }

    private JPanel gethead() {
        JPanel head_main = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        head_main.setBorder(new EmptyBorder(0,0,30,0));
        head_main.setBackground(new Color(98, 153, 209));

        img.setPreferredSize(new Dimension(200, 200));
        img.setBorder(new EmptyBorder(20, 0, 20, 50));
        reload_img(album);
        Name_ab = new JLabel("<html>" + album.getTitle().replaceAll("(.{30})", "$1<br>") + "</html>");
        Name_ab.setForeground(Color.white);
        Name_ab.setFont(new Font("Serif", Font.BOLD, 26));
        Name_ab.setBorder(new EmptyBorder(0,30,0,0));

        head_main.add(img);
        head_main.add(Name_ab);
        return head_main;
    }

    private void reload_img(Collection album) {
        new Thread(() -> {
            try {
                String url = album.getArtwork_url();
                if(url==null){
                    url = album.getTracks().getFirst().getArtwork_url();
                    int i = 1;
                    while (url == null){
                        url = album.getTracks().get(i).getArtwork_url();
                        i++;
                        if(i>10) break;
                    }
                }
                URI uri = new URI(sc.IMG500x500(url));
                ImageIcon image = new ImageIcon(uri.toURL());
                SwingUtilities.invokeLater(() -> img.setIcon(image));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> { img.setIcon(new ImageIcon("src/main/resources/pngs/me.png")); });
            }
        }).start();
    }

    private JPanel get_main(){
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.decode("#1a1a1a"));
        body.setBorder(new EmptyBorder(10,10,10,10));

        get_cxz();

        cxz.setLayout(new BorderLayout()); // chuyen sang flow
        cxz.setBackground(Color.decode("#1a1a1a"));
        cxz.setPreferredSize(new Dimension(580, 20));
        cxz.add(head, BorderLayout.WEST);

        body.add(cxz, BorderLayout.NORTH);
        body.add(dreizehn, BorderLayout.CENTER);

        refresh_dreizehn();
        return body;
    }

    private void get_cxz(){
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

    public void reload_album(Collection album) {
        this.album = album;
        reload_img(album);
        Name_ab.setText(album.getTitle());
        refresh_dreizehn();
    }

    public void refresh_dreizehn(){
        dreizehn.removeAll();
        dreizehn.setBackground(Color.decode("#1a1a1a"));

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
                List<Track> tracks = album.getTracks();
                int z = 1;
                for (Track track : tracks) {
                    JPanel songItem = get_item(track, z, width); // Tạo item trong luồng nền
                    panels.add(songItem); // Lưu lại để thêm vào giao diện sau
                    z++;
                }
                return panels;
            }

            @Override
            protected void process(java.util.List<JPanel> chunks) {
                for (JPanel panel : chunks) {
                    dreizehn.add(panel);
                }
                dreizehn.revalidate();
                dreizehn.repaint();
            }

            @Override
            protected void done() {
                try {
                    java.util.List<JPanel> panels = get();
                    for (JPanel panel : panels) {
                        dreizehn.add(panel);
                    }
                    dreizehn.revalidate();
                    dreizehn.repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private Border_Radius get_item(Track track, int number, int width) {
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

        String nam = (track.getPublisher_metadata() != null && track.getPublisher_metadata().getArtist() != null)
                ? track.getPublisher_metadata().getArtist()
                : album.getUser().getUsername();
        JLabel ar = new JLabel(nam);
        ar.setFont(new Font("Serif", Font.PLAIN, 12));
        ar.setForeground(Color.gray);

        ar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ar.setForeground(new Color(98, 153, 209));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                ar.setForeground(Color.GRAY);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                home.reload_Artist(track.getUser_id(), width == 1150 ? 1150 : 860);
            }
        });

        info.add(name, BorderLayout.NORTH);
        info.add(ar, BorderLayout.SOUTH);
        bor.add(info);


        JLabel title_album = new JLabel(track.getTitle());
        title_album.setFont(new Font("Serif", Font.PLAIN, 12));
        title_album.setForeground(Color.GRAY);
        title_album.setPreferredSize(new Dimension((int)((width-250)*0.2),50));
        bor.add(title_album);


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

        Queue_Item song = track_to_queit(track);

        JPopupMenu pop = home.popup_Album(track, more);
        home.createPopup(more, pop);

        set_item_clicked(bor, nu, number, song);
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
                    home.QueueDL.addFirst(track);
                }
                home.refresh_Queue();
                home.setCurrentSong(track);
                home.Play_track();
            }
        });

    }

    public Queue_Item track_to_queit(Track i){
        Queue_Item ne = new Queue_Item();
        ne.setImgCover(i.getArtwork_url());
        ne.setTitle(i.getTitle());
        String nam = (i.getPublisher_metadata() != null && i.getPublisher_metadata().getArtist() != null)
                ? i.getPublisher_metadata().getArtist()
                : album.getUser().getUsername();
        ne.setArtist(nam);
        String album = ( i.getPermalink_url() != null &&i.getPublisher_metadata().getAlbum_title() != null) ? i.getPublisher_metadata().getAlbum_title():"unknown";
        ne.setAlbum(album);
        ne.setFileName(i.getTitle()+" - "+nam);
        ne.setLink(i.getPermalink_url());
        ne.setArtist_id(i.getUser_id());
        ne.setDuration(i.getDuration());
        ne.setGenre(i.getGenre());
        return ne;
    }
}
