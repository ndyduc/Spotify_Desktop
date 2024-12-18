package org.music.Activity;

import org.music.Components.*;
import org.music.MongoDB;
import org.music.getAPI.Soundcloud;
import org.music.Func.Stream;
import org.music.models.Albums;
import org.music.models.DB.Playlists;
import org.music.models.Queue_Item;
import org.music.models.Search_Album.Track;
import org.music.models.Search_User.Collection;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Home implements Queue_Lis, Artist_Lis{
    JFrame window = new JFrame("_ndyduc_");
    public final Stream stream;

    Soundcloud sc = new Soundcloud();
    public Queue_Item item;
    private boolean isplaying = false;
    private boolean isschuffle = false;
    private final JLabel crTitle = new JLabel();
    private final JLabel crArtist = new JLabel();
    private final Rounded_Label imgcrSong = new Rounded_Label(new ImageIcon(),30);
    private JPanel centerbot = new JPanel();
    private final JLabel least = new JLabel();
    private ShapeIcon playIcon;
    private ShapeIcon pauseIcon;
    public JButton Pausebtn;
    JButton Shuffle;
    JButton skipback;
    public JButton skipforward;
    JButton Repeat;

    private JSlider positionSlider;
    private JLabel position;
    private boolean isUserAdjusting = false;
    AtomicBoolean showqueue = new AtomicBoolean(false);
    AtomicBoolean showartist = new AtomicBoolean(false);

    private final JButton Artist = createButton("src/main/resources/pngs/user-scan.png", 25, 25);
    private final JButton Queue = createButton("src/main/resources/pngs/queue.png", 25, 25);
    private final JButton Volume = createButton("src/main/resources/pngs/volume.png", 25, 25);
    private Queue_L que;
    private Track_info tra;
    private final Library lib = new Library(30, item, this);

    CardLayout center_home = new CardLayout();
    JPanel main_center = new JPanel(center_home);
    private House house;
    private final Search_all s_all;
    private final Playlist playlist;
    private final Artist artist;
    private final Album album;
    private final MongoDB mongo = new MongoDB();
    int wit = 860;

    public final LinkedList<Queue_Item> QueueDL = new LinkedList<>();  // Hàng đợi lưu đường dẫn file
    static final String DIRECTORY = "./mp3_queue";
    static final String DOWNLOAD = "./mp3_playlist";

    private Timer searchTimer = new Timer(1000, null);

    public Home() {
        String name_track = "K. Cigarettes";
        stream = new Stream(this);
        item = sc.getQueue_Item(name_track);
        addToQueue(item);

        window.setSize(860, 600);
        window.setMinimumSize(new Dimension(860, 600));
        window.getContentPane().setBackground(Color.BLACK);
        window.setLayout(new BorderLayout(2, 5));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel header = new JPanel(new BorderLayout());
        getHeader(header, item);
        window.add(header, BorderLayout.NORTH);
        window.add(lib, BorderLayout.WEST);

        main_center.setBackground(Color.BLACK);
        house = new House(30, this, window);
        s_all = new Search_all(30, "cigarettes", window, this);
        List<Playlists> pla = mongo.get_Playlists("_ndyduc_");
        playlist = new Playlist(30, pla.getFirst(), stream, window, this);
        Collection user = sc.getBestUser("Cigarettes after sex");
        artist = new Artist(30, user, stream, window, this);
        Albums albums = sc.get_Albums("cry", 1);
        album = new Album(30, albums, stream, window, this);

        main_center.add(house, "house");
        main_center.add(s_all, "search_all");
        main_center.add(playlist, "playlist");
        main_center.add(artist, "artist");
        main_center.add(album, "album");

        window.add(main_center, BorderLayout.CENTER);

        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.X_AXIS));
        centerContainer.setBackground(Color.BLACK);

        centerContainer.add(Box.createHorizontalGlue());
        JPanel centerbot = new JPanel();
        getcenterbot(centerbot, item);
        centerContainer.add(centerbot);
        centerContainer.add(Box.createHorizontalGlue());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK);

        JPanel leftbot = new JPanel(new GridBagLayout());
        getleftbot(leftbot, item);
        setCurrentSong(item);
        bottomPanel.add(leftbot, BorderLayout.WEST);

        JPanel rightbot = new JPanel(new GridBagLayout());
        getRightbot(rightbot, item);
        bottomPanel.add(rightbot, BorderLayout.EAST);

        bottomPanel.add(centerContainer, BorderLayout.CENTER);

        window.add(bottomPanel, BorderLayout.SOUTH);

        Artist.addActionListener(e -> {
            if (!showartist.get()) {
                Artist.setIcon(loadIcon("src/main/resources/pngs/user-scan-green.png", 25, 25));
                showartist.set(true);
                tra = new Track_info(30, item, this, showqueue, Queue, showartist, Artist);
                tra.setShowArtistListener(this); // Thiết lập listener
                window.add(tra, BorderLayout.EAST);
                window.revalidate(); // Làm mới giao diện
                if (showqueue.get()) {
                    Queue.setIcon(loadIcon("src/main/resources/pngs/queue.png", 25, 25));
                    window.remove(que);
                    showqueue.set(false);
                    window.revalidate();
                    window.repaint();
                }
            } else {
                Artist.setIcon(loadIcon("src/main/resources/pngs/user-scan.png", 25, 25));
                showartist.set(false);
                window.remove(tra);
                tra.removeAll();
                tra = null;
                window.revalidate();
                window.repaint();
            }
        });

        que = new Queue_L(30, this, showqueue, Queue, showartist, Artist);
        Queue.addActionListener(e -> {
            if (!showqueue.get()) {
                // Tạo và thêm Queue_L khi showqueue đang là false
                Queue.setIcon(loadIcon("src/main/resources/pngs/queue-green.png", 25, 25));
                que = new Queue_L(30, this, showqueue, Queue, showartist, Artist);
                que.setShowQueueListener(this); // Thiết lập listener
                window.add(que, BorderLayout.EAST);
                window.revalidate(); // Refresh giao diện
                showqueue.set(true);

                // Đảm bảo trạng thái showartist là false nếu cần
                if (showartist.get()) {
                    Artist.setIcon(loadIcon("src/main/resources/pngs/user-scan.png", 25, 25));
                    showartist.set(false);
                    window.remove(tra);
                    window.revalidate();
                    window.repaint();
                }
            } else {
                Queue.setIcon(loadIcon("src/main/resources/pngs/queue.png", 25, 25));
                window.remove(que);
                window.revalidate(); // Refresh giao diện
                window.repaint(); // Làm mới lại giao diện để cập nhật thay đổi
                showqueue.set(false);
            }
        });

        Pausebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isplaying) {
                    stream.pause();
                    Pausebtn.setIcon(playIcon);  // Đổi icon thành Play
                } else {
                    stream.Play(item.getFileName());
                    Pausebtn.setIcon(pauseIcon);  // Đổi icon thành Pause
                    stream.startTimer(position, positionSlider, mongo.get_Duration(item.getDuration())); // Khởi động Timer
                }
                isplaying = !isplaying;  // Đổi trạng thái phát nhạc
            }
        });

        InputMap im = Pausebtn.getInputMap();
        im.put(KeyStroke.getKeyStroke("SPACE"), "pressed");
        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension windowSize = window.getSize();
                if (windowSize.width == screenSize.width && windowSize.height == screenSize.height | window.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    updateToFullScreenLayout();
                    wit = 1150;
                } else if (window.getExtendedState() == JFrame.NORMAL) {
                    updateToNormalLayout();
                    wit = 860;
                }
            }
        });


        window.setLocationRelativeTo(null);  // Center window on the screen
        window.setVisible(true);

        positionSlider.addChangeListener(e -> {
            int currentValue = positionSlider.getValue(); // Lấy giá trị hiện tại của slider
            int maxDuration = mongo.get_Duration(item.getDuration()); // Tổng thời lượng bài hát
            // Người dùng đang điều chỉnh slider
            if (positionSlider.getValueIsAdjusting()) {
                if (isplaying) { stream.pause(); }// Tạm dừng nhạc nếu đang phát
                isUserAdjusting = true; // Đánh dấu đang điều chỉnh
            } else { // Người dùng kết thúc việc điều chỉnh
                if (currentValue >= maxDuration) {  // Kiểm tra khi slider chạm giá trị tối đa
                    System.out.println("phát xong");
                    stop_stream(); // Dừng stream khi hết bài trong queue
                }
                if (isUserAdjusting) { // Giá trị giây từ slider
                    stream.elapsedTime = (long) currentValue * 1000; // Cập nhật elapsedTime
                    stream.startTime = System.currentTimeMillis() - stream.elapsedTime; // Đồng bộ thời gian
                    position.setText(stream.formatTime(currentValue)); // Cập nhật thời gian hiển thị
                    // Phát lại từ vị trí mới
                    stream.Play(item.getFileName());
                    Pausebtn.setIcon(pauseIcon);
                    // Khởi động lại timer
                    stream.startTimer(position, positionSlider, maxDuration);
                    isplaying = true;
                    isUserAdjusting = false; // Kết thúc điều chỉnh
                }
            }
        });


    }


    private void updateToFullScreenLayout() {
        centerbot.setPreferredSize(new Dimension(850, 90));
        centerbot.revalidate();
        centerbot.repaint();
        Artist.setIcon(loadIcon("src/main/resources/pngs/user-scan.png", 30, 30));
        Queue.setIcon(loadIcon("src/main/resources/pngs/queue.png", 30, 30));
        Volume.setIcon(loadIcon("src/main/resources/pngs/volume.png", 30, 30));
        crTitle.setPreferredSize(new Dimension(250, 10));
        crTitle.revalidate();
        crTitle.repaint();
    }

    private void updateToNormalLayout() {
        centerbot.setPreferredSize(new Dimension(500, 90));
        centerbot.revalidate();
        centerbot.repaint();
        Artist.setIcon(loadIcon("src/main/resources/pngs/user-scan.png", 20, 20));
        Queue.setIcon(loadIcon("src/main/resources/pngs/queue.png", 20, 20));
        Volume.setIcon(loadIcon("src/main/resources/pngs/volume.png", 20, 20));
        crTitle.setPreferredSize(new Dimension(100, 10));
        crTitle.revalidate();
        crTitle.repaint();
    }

    private void getHeader(JPanel header, Queue_Item item) {
        header.setBackground(Color.BLACK);
        header.setPreferredSize(new Dimension(800, 50));

        JPanel lefthead = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lefthead.setBackground(Color.BLACK);

        JButton back = createButton("src/main/resources/pngs/chevron-left.png", 40, 40);
        JButton forward = createButton("src/main/resources/pngs/chevron-right.png", 40, 40);

        lefthead.add(back);
        lefthead.add(forward);

        JPanel midhead = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        midhead.setBackground(Color.BLACK);
        midhead.setPreferredSize(new Dimension(450, 40));  // Hạn chế kích thước của midhead

        RoundedPanel mh1 = new RoundedPanel(40, Color.decode("#1a1a1a"), 5, 5);
        mh1.setPreferredSize(new Dimension(50, 50));
        JButton home = createButton("src/main/resources/pngs/home.png", 30, 30);

        home.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                center_home.show(main_center, "house");
            }
        });

        mh1.add(home);

        RoundedPanel mh2 = new RoundedPanel(40, Color.decode("#1a1a1a"), 5, 0);
        mh2.setPreferredSize(new Dimension(400, 40));
        mh2.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JButton search = createButton("src/main/resources/pngs/search.png", 30, 30);
        HintTextField txtSearch = new HintTextField("What are you looking for ?");
        txtSearch.setPreferredSize(new Dimension(340, 40));
        txtSearch.setBackground(Color.decode("#1a1a1a"));
        txtSearch.setOpaque(false);
        txtSearch.setCaretColor(Color.WHITE);

        search.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                center_home.show(main_center, "search_all");
            }
        });

        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                mh2.setBorderColor(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                mh2.setBorderColor(Color.decode("#1a1a1a"));
            }
        });


        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String currentText = txtSearch.getText();
                if (searchTimer.isRunning()) {
                    searchTimer.stop();
                }
                if (currentText.isEmpty()) {
                    center_home.show(main_center, "house");
                } else {
                    searchTimer = new Timer(1000, evt -> {
                        s_all.setKey(currentText);
                        center_home.show(main_center, "search_all");
                        window.revalidate();
                    });
                }
                searchTimer.setRepeats(false);
                searchTimer.start();
            }
        });

        home.addActionListener(e -> {
            txtSearch.setText("");
            house = new House(30, this, window);
            center_home.show(main_center, "house");
        });

        mh2.add(search);
        mh2.add(txtSearch);
        midhead.add(mh1);
        midhead.add(mh2);
        header.add(lefthead, BorderLayout.WEST);
        header.add(midhead, BorderLayout.CENTER);
    }

    private void getcenterbot(JPanel centerbot, Queue_Item item){
        centerbot.setBackground(Color.BLACK);
        centerbot.setMaximumSize(new Dimension(500, 90));
        centerbot.setLayout(new BoxLayout(centerbot, BoxLayout.Y_AXIS));

        JPanel centerbot1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerbot1.setBackground(Color.BLACK);
        centerbot1.setPreferredSize(new Dimension(500, 40));
        centerbot1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        Shuffle = createButton("src/main/resources/pngs/arrows-shuffle.png", 20, 20);
        skipback = createButton("src/main/resources/pngs/player-skip-back.png", 25, 25);

        final GeneralPath Pause = new GeneralPath();
        Pause.moveTo(1.5 / 5.0, 1.0 / 5.0);
        Pause.lineTo(1.5 / 5.0, 4 / 5.0);
        Pause.lineTo(5.0 / 6.0, 1.0 / 2.0);
        Pause.closePath();
        final Area playArea = new Area(Pause);

        GeneralPath pauseShape = new GeneralPath();
        pauseShape.append(new Rectangle2D.Double(0.3, 0.2, 0.15, 0.6), false);
        pauseShape.append(new Rectangle2D.Double(0.6, 0.2, 0.15, 0.6), false);

        playIcon = new ShapeIcon(playArea, Color.WHITE, 40);
        pauseIcon = new ShapeIcon(pauseShape, Color.WHITE, 40);

        Pausebtn = new JButton(playIcon);
        Pausebtn.setPreferredSize(new Dimension(40, 40));
        Pausebtn.setBorderPainted(false);
        Pausebtn.setFocusPainted(false);
        Pausebtn.setContentAreaFilled(false);

        skipforward = createButton("src/main/resources/pngs/player-skip-forward.png", 25, 25);
        Repeat = createButton("src/main/resources/pngs/repeat.png", 20, 20);

        Shuffle.addActionListener(e -> {
            if (!isschuffle){
                Shuffle.setIcon(loadIcon("src/main/resources/pngs/arrows-shuffle-green.png", 20, 20));
                isschuffle = true;
            } else {
                Shuffle.setIcon(loadIcon("src/main/resources/pngs/arrows-shuffle.png", 20, 20));
                isschuffle = false;
            }
        });

        final int[] state = {0}; // Dùng mảng để có thể thay đổi trạng thái bên trong ActionListener
        Repeat.addActionListener(e -> {
            state[0] = (state[0] + 1) % 3; // Tăng trạng thái và quay vòng về 0 khi vượt quá 2
            switch (state[0]) {
                case 0:
                    Repeat.setIcon(loadIcon("src/main/resources/pngs/repeat.png",20,20));
                    break;
                case 1:
                    Repeat.setIcon(loadIcon("src/main/resources/pngs/repeat-green.png",20,20)); // Trạng thái 1: repeat-green.png
                    break;
                case 2:
                    Repeat.setIcon(loadIcon("src/main/resources/pngs/repeat-once.png",20,20)); // Trạng thái 2: repeat-one.png
                    break;
            }
        });

        skipforward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean next = next_song();
                if (next) {
                    Pausebtn.doClick();
                    Pausebtn.doClick();
                }
            }
        });

        centerbot1.add(Shuffle);
        centerbot1.add(skipback);
        centerbot1.add(Pausebtn);
        centerbot1.add(skipforward);
        centerbot1.add(Repeat);

        JPanel centerbot2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerbot2.setBackground(Color.BLACK);
        centerbot2.setPreferredSize(new Dimension(400, 10));
        centerbot2.setLayout(new BoxLayout(centerbot2, BoxLayout.X_AXIS));

        JPanel slider = new JPanel(new BorderLayout(10, 0));
        slider.setBackground(Color.BLACK);

        positionSlider = getSlider(mongo.get_Duration(item.getDuration()));
        slider.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        slider.add(positionSlider, BorderLayout.CENTER);

        position = new JLabel("00:00");
        position.setForeground(new Color(178,178,178));
        least.setForeground(new Color(178, 178, 178));

        centerbot2.add(position);
        centerbot2.add(slider);
        centerbot2.add(least);

        centerbot.add(centerbot1, BorderLayout.NORTH);
        centerbot.add(centerbot2, BorderLayout.CENTER);
        centerbot.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
    }

    private void getleftbot(JPanel leftbot, Queue_Item item){
        leftbot.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa các thành phần

        gbc.gridx = 0; // Cột 0
        gbc.gridy = 0; // Hàng 0
        gbc.gridwidth = 1; // Chiếm 1 cột
        gbc.gridheight = 3; // Chiếm 2 hàng
        gbc.anchor = GridBagConstraints.CENTER; // Căn giữa

        imgcrSong.setPreferredSize(new Dimension(50,50));
        leftbot.add(imgcrSong, gbc);

        gbc.gridx = 1; // Cột 1
        gbc.gridy = 0; // Hàng 0
        gbc.gridwidth = 1; // Chiếm 1 cột
        gbc.gridheight = 1; // Chiếm 1 hàng
        gbc.anchor = GridBagConstraints.WEST; // Căn bên trái
        leftbot.add(new JLabel(), gbc);

        gbc.gridx = 1; // Cột 1
        gbc.gridy = 1; // Hàng 0
        gbc.gridwidth = 1; // Chiếm 1 cột
        gbc.gridheight = 1; // Chiếm 1 hàng
        gbc.anchor = GridBagConstraints.WEST; // Căn bên trái
        leftbot.add(crTitle, gbc);

        gbc.gridx = 1; // Cột 1
        gbc.gridy = 2; // Hàng 0
        gbc.gridwidth = 1; // Chiếm 1 cột
        gbc.gridheight = 1; // Chiếm 1 hàng
        gbc.anchor = GridBagConstraints.WEST; // Căn bên trái
        leftbot.add(crArtist, gbc);
    }

    private void getRightbot(JPanel rightbot, Queue_Item item){
        rightbot.setBackground(Color.BLACK);
        rightbot.setPreferredSize(new Dimension(200, 60));
        JSlider volumeSlider = getVolumeSlider();

        volumeSlider.addChangeListener(e -> {
            if (!volumeSlider.getValueIsAdjusting()) {
                int volumeValue = volumeSlider.getValue();
//                stream.debugAudioSystem();
                stream.setSystemVolume(volumeValue);
            }
        });

        rightbot.add(Artist);
        rightbot.add(new getSpace(5,1));
        rightbot.add(Queue);
        rightbot.add(new getSpace(5,1));
        rightbot.add(volumeSlider);
        rightbot.add(new getSpace(5,1));
        rightbot.add(Volume);
        rightbot.add(new getSpace(5,1));
    }

    public void setCurrentSong(Queue_Item item) {
        this.item = item;
        imgcrSong.setPreferredSize(new Dimension(50,50));
        new Thread(() -> {
            try {
                URI uri = new URI(sc.IMG500x500(item.getImgCover()));
                ImageIcon image = new ImageIcon(uri.toURL());
                SwingUtilities.invokeLater(() -> imgcrSong.setIcon(image));
            } catch (URISyntaxException | MalformedURLException | NullPointerException e) {
                SwingUtilities.invokeLater(() -> { imgcrSong.setIcon(new ImageIcon("src/main/resources/pngs/me.png")); });
            }
        }).start();

        crTitle.setText(item.getTitle());
        crTitle.setForeground(Color.WHITE);
        crTitle.setPreferredSize(new Dimension(100, 20));
        crTitle.setText("<html><body style='width: 100px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis'>"
                + crTitle.getText() + "</body></html>");
        crTitle.setFont(new Font("Arial", Font.PLAIN, 13));
        crArtist.setText(item.getArtist());
        crArtist.setFont(new Font("Arial", Font.PLAIN, 12));
        crArtist.setForeground(new Color(178,178,178));

        least.setText(mongo.get_duration(item.getDuration()));

        positionSlider.setMaximum(mongo.get_Duration(item.getDuration()));

        if(tra != null){
            tra.removeAll();
            tra = new Track_info(30, item, this, showqueue, Queue, showartist, Artist);
            tra.setShowArtistListener(this);
            tra.revalidate();
            tra.repaint();
            window.add(tra, BorderLayout.EAST);
            window.revalidate();
        }
    }

    private static JSlider getVolumeSlider() {
        JSlider volumeSlider = new JSlider(0, 100, 70);
        volumeSlider.setPreferredSize(new Dimension(100, 25));
        volumeSlider.setBackground(Color.BLACK);

        volumeSlider.setUI(new javax.swing.plaf.basic.BasicSliderUI(volumeSlider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Xác định chiều cao của thanh trượt và bo góc 15px
                int trackHeight = 5;
                int trackY = trackRect.y + (trackRect.height - trackHeight) / 2;
                int arcRadius = 5; // Bán kính bo góc

                // Vẽ phần đã di chuyển (màu trắng) với bo góc
                int fillWidth = (int) ((float) volumeSlider.getValue() / volumeSlider.getMaximum() * trackRect.width);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(trackRect.x, trackY, fillWidth, trackHeight, arcRadius, arcRadius);

                // Vẽ phần chưa di chuyển (màu xám) với bo góc
                g2d.setColor(Color.GRAY);
                g2d.fillRoundRect(trackRect.x + fillWidth, trackY, trackRect.width - fillWidth, trackHeight, arcRadius, arcRadius);
            }

            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Vẽ hình tròn cho con trỏ tại vị trí hiện tại
                int thumbRadius = 6; // Bán kính của hình tròn
                int x = thumbRect.x + thumbRect.width / 2 - thumbRadius;
                int y = trackRect.y + (trackRect.height - thumbRadius * 2) / 2;

                g2d.setColor(Color.WHITE);
                g2d.fillOval(x, y, thumbRadius * 2, thumbRadius * 2);
            }
        });
        return volumeSlider;
    }

    private static JSlider getSlider(int duration) {
        if(duration <= 0 ) { duration= 100;}
        JSlider positionSlider = new JSlider(0, duration, 0);
        positionSlider.setBackground(Color.BLACK);
        positionSlider.setForeground(Color.GRAY);
        positionSlider.setUI(new javax.swing.plaf.basic.BasicSliderUI(positionSlider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int trackHeight = 5;
                int trackY = trackRect.y + (trackRect.height - trackHeight) / 2;
                int arcRadius = 5;
                Color customGreen = new Color(101,145,126);
                int fillWidth = (int) ((float) positionSlider.getValue() / positionSlider.getMaximum() * trackRect.width);
                g2d.setColor(customGreen);
                g2d.fillRoundRect(trackRect.x, trackY, fillWidth, trackHeight, arcRadius, arcRadius);
                g2d.setColor(Color.GRAY);
                g2d.fillRoundRect(trackRect.x + fillWidth, trackY, trackRect.width - fillWidth, trackHeight, arcRadius, arcRadius);
            }

            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int thumbRadius = 6;
                int x = thumbRect.x + thumbRect.width / 2 - thumbRadius;
                int y = trackRect.y + (trackRect.height - thumbRadius * 2) / 2;
                g2d.setColor(Color.WHITE);
                g2d.fillOval(x, y, thumbRadius * 2, thumbRadius * 2);
            }
        });

        return positionSlider;
    }

    public static JButton createButton(String imagePath, int width, int height) {
        JButton button = new JButton(loadIcon(imagePath, width, height));
        button.setBackground(new Color(0,0,0,0));
        button.setPreferredSize(new Dimension(width, height));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        return button;
    }

    public static ImageIcon loadIcon(String imagePath, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onShowQueueChanged(AtomicBoolean show) {
        if (!show.get()) {
            window.remove(que);  // Xóa `Queue_L` khỏi giao diện
            window.revalidate();
            window.repaint();
        }
    }

    @Override
    public void onShowArtistChanged(AtomicBoolean show) {
        if (!show.get()) {
            window.remove(tra);
            window.revalidate();
            window.repaint();
        }
    }

    public void reload_playlist(){
        lib.Refesh_Library();
    }

    public void show_home(){
        center_home.show(main_center,"house");
    }
    public void set_Lib(Playlists i){
        playlist.reload_pl(i);
        center_home.show(main_center,"playlist");
    }

    public void reload_album(org.music.models.Search_Album.Collection track){
        album.reload_album(track);
        center_home.show(main_center, "album");
    }

    public void createPopup(JButton button, JPopupMenu popup) {
        button.addActionListener(e -> popup.show(button, 0, button.getHeight()));
    }

    public JPopupMenu popup_Search(org.music.models.Search_Tracks.Collection i, JButton button) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Color.decode("#1a1a1a"));
        popupMenu.setForeground(Color.WHITE);

        JMenuItem option1 = new JMenuItem("Add to Playlist");
        JMenuItem option2 = new JMenuItem("Add to Queue");
        option1.setBackground(Color.decode("#1a1a1a"));
        option1.setForeground(Color.WHITE);
        option2.setBackground(Color.decode("#1a1a1a"));
        option2.setForeground(Color.WHITE);

        popupMenu.add(option1);
        popupMenu.add(option2);

        JPopupMenu playlistPopup = popup_playlist(i);

        option1.addActionListener(e -> {
            popupMenu.setVisible(false);
            Component invoker = popupMenu.getInvoker();
            if (invoker != null) {
                playlistPopup.show(invoker, 0, button.getHeight());
            }
        });

        option2.addActionListener(e -> {
            addToQueue(cv_track_to_queuei(i));
        });

        return popupMenu;
    }


    public JPopupMenu popup_playlist(org.music.models.Search_Tracks.Collection i) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Color.decode("#1a1a1a"));
        popupMenu.setForeground(Color.WHITE);

        List<Playlists> playlists = mongo.get_Playlists("_ndyduc_");
        for (Playlists playlist : playlists) {
            JMenuItem menuItem = new JMenuItem(playlist.getName());
            menuItem.setBackground(Color.decode("#1a1a1a"));
            menuItem.setForeground(Color.WHITE);
            menuItem.addActionListener(e -> {
                Queue_Item ne = cv_track_to_queuei(i);
                ne.setWhere(playlist.getId());
                if (mongo.isSongExists(i.getPermalink_url(), playlist.getId())) JOptionPane.showMessageDialog(null, "This song is already add to "+ playlist.getName());
                else mongo.Insert_Song(ne);
            });
            popupMenu.add(menuItem);
        }

        return popupMenu;
    }

    public JPopupMenu popup_Album(org.music.models.Search_Album.Track i, JButton button) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Color.decode("#1a1a1a"));
        popupMenu.setForeground(Color.WHITE);

        JMenuItem option1 = new JMenuItem("Add to Playlist");
        JMenuItem option2 = new JMenuItem("Add to Queue");
        option1.setBackground(Color.decode("#1a1a1a"));
        option1.setForeground(Color.WHITE);
        option2.setBackground(Color.decode("#1a1a1a"));
        option2.setForeground(Color.WHITE);

        popupMenu.add(option1);
        popupMenu.add(option2);

        JPopupMenu playlistPopup = popup_al(i);

        option1.addActionListener(e -> {
            popupMenu.setVisible(false);
            Component invoker = popupMenu.getInvoker();
            if (invoker != null) {
                playlistPopup.show(invoker, 0, button.getHeight());
            }
        });

        option2.addActionListener(e -> {
            addToQueue(album.track_to_queit(i));
        });

        return popupMenu;
    }


    public JPopupMenu popup_al(org.music.models.Search_Album.Track i) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Color.decode("#1a1a1a"));
        popupMenu.setForeground(Color.WHITE);

        List<Playlists> playlists = mongo.get_Playlists("_ndyduc_");
        for (Playlists playlist : playlists) {
            JMenuItem menuItem = new JMenuItem(playlist.getName());
            menuItem.setBackground(Color.decode("#1a1a1a"));
            menuItem.setForeground(Color.WHITE);
            menuItem.addActionListener(e -> {
                Queue_Item ne = album.track_to_queit(i);
                ne.setWhere(playlist.getId());
                if (mongo.isSongExists(i.getPermalink_url(), playlist.getId())) JOptionPane.showMessageDialog(null, "This song is already add to "+ playlist.getName());
                else mongo.Insert_Song(ne);
            });
            popupMenu.add(menuItem);
        }

        return popupMenu;
    }


    public JPopupMenu popup_Album(Queue_Item i, JButton button) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Color.decode("#1a1a1a"));
        popupMenu.setForeground(Color.WHITE);

        JMenuItem op1 = new JMenuItem("Add to orther Playlist");
        JMenuItem op2 = new JMenuItem("remove from playlist");
        JMenuItem op3 = new JMenuItem("Add to Queue");

        op1.setBackground(Color.decode("#1a1a1a"));
        op1.setForeground(Color.WHITE);
        op2.setBackground(Color.decode("#1a1a1a"));
        op2.setForeground(Color.WHITE);
        op3.setBackground(Color.decode("#1a1a1a"));
        op3.setForeground(Color.WHITE);

        popupMenu.add(op1);
        popupMenu.add(op2);
        popupMenu.add(op3);

        JPopupMenu playlistPopup = list_playlist(i);

        op1.addActionListener(e -> {
            popupMenu.setVisible(false);
            Component invoker = popupMenu.getInvoker();
            if (invoker != null) {
                playlistPopup.show(invoker, 0, button.getHeight());
            }
        });

        op2.addActionListener(e -> {
            mongo.Remove_Song(i.getMongoID());
            playlist.refresh_zehn();
        });

        op3.addActionListener(e -> {
            addToQueue(i);
        });

        return popupMenu;
    }

    public JPopupMenu list_playlist(Queue_Item i) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Color.decode("#1a1a1a"));
        popupMenu.setForeground(Color.WHITE);

        List<Playlists> playlists = mongo.get_Playlists("_ndyduc_");
        for (Playlists playlist : playlists) {
            JMenuItem menuItem = new JMenuItem(playlist.getName());
            menuItem.setBackground(Color.decode("#1a1a1a"));
            menuItem.setForeground(Color.WHITE);
            menuItem.addActionListener(e -> {
                if (mongo.isSongExists(i.getLink(), playlist.getId())) JOptionPane.showMessageDialog(null, "This song is already add to "+ playlist.getName());
                else mongo.Insert_Song(i);
            });
            popupMenu.add(menuItem);
        }

        return popupMenu;
    }

    public void reload_Artist(int id, int full){
        Collection user = sc.get_user_by_id(id);
        artist.refresh_neun(user, full);
        center_home.show(main_center, "artist");
    }

    public Queue_Item cv_track_to_queuei(org.music.models.Search_Tracks.Collection i) {
        Queue_Item ne = new Queue_Item();
        ne.setImgCover(i.getArtwork_url());
        ne.setTitle(i.getTitle());
        String nam = (i.getPublisher_metadata() != null && i.getPublisher_metadata().getArtist() != null)
                ? i.getPublisher_metadata().getArtist()
                : i.getUser().getUsername();
        ne.setArtist(nam);
        String album = (i.getPublisher_metadata().getAlbum_title() != null) ? i.getPublisher_metadata().getAlbum_title():"unknown";
        ne.setAlbum(album);
        ne.setFileName(i.getTitle()+" - "+nam);
        ne.setLink(i.getPermalink_url());
        ne.setArtist_id(i.getUser_id());
        ne.setDuration(i.getDuration());
        ne.setGenre(i.getGenre());
        return ne;
    }

    public void refresh_House(){
        house.refesh_playlists();
        house.refesh_artist();
    }
    public void refresh_Queue(){
        que.refresh_que();
    }

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    public void addToQueue(Queue_Item item) {
        QueueDL.offer(item);
        executor.submit(() -> {
            DLFile(item.getLink(), item.getFileName());
        });
    }
    public Queue_Item peekQueue() {
        return QueueDL.peek();
    }  // Lấy phần tử đầu nhưng không xóa
    public Queue_Item pollFromQueue() {
        return QueueDL.poll();
    } // Lấy và xóa phần tử đầu
    public boolean isQueueEmpty() {
        return QueueDL.isEmpty();
    }
    public void addToFront(Queue_Item item) {
        QueueDL.addFirst(item);
        stream.stop();
        executor.submit(() -> {
            DLFile(item.getLink(), item.getFileName());
        });
    }
    public LinkedList<Queue_Item> getQueueDL() { return QueueDL; }
    public void clearQueue() {
        for (Queue_Item song : QueueDL) {
            File file = new File(DIRECTORY + "/" + song.getFileName());
            if (file.exists()) { Delete_file(song.getFileName());  }
        }
        QueueDL.clear();
        System.out.println("Hàng đợi đã được xóa.");
    }
    public Queue_Item getAndRemoveFromQueue(Queue_Item i) {
        for (Queue_Item item : QueueDL) {
            if (item.getFileName().equals(i.getFileName())) {
                QueueDL.remove(item); // Xóa phần tử tìm thấy
                return item;          // Trả về phần tử đã xóa
            }
        }
        return null; // Không tìm thấy, trả về null
    }
    public void Delete_file(String filename) {
        File file = new File(DIRECTORY + "/" + filename);
        if (file.exists()) {
            file.delete();
        };
    }
    public void DLFile(String link_file, String file_name) {
        try {
            File directory = new File(DIRECTORY);
            if (!directory.exists()) { directory.mkdirs();  }

            File newFile = new File(DIRECTORY + "/" + file_name);
            File oldFile = new File(DOWNLOAD + "/" + file_name);
            if (newFile.exists() || oldFile.exists()) {
                System.out.println("File đã tồn tại: " + file_name);
                return; // Không tải nếu file đã tồn tại
            }

            System.out.println("Bắt đầu tải file: " + file_name);

            // Tải file với yt-dlp
            ProcessBuilder pb = new ProcessBuilder(
                    "yt-dlp",
                    "-f", "bestaudio[ext=mp3]",
                    "--output", newFile.getAbsolutePath(),
                    link_file
            );
            Process process = pb.start();
            process.waitFor();

            System.out.println("Đã tải file: " + newFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi tải file: " + e.getMessage());
        }
    }
    public void Dl_for_pl(String link_file, String file_name) {
        executor.submit(() -> {
            try {
                File directory = new File(DOWNLOAD);
                if (!directory.exists()) { directory.mkdirs();  }

                File newFile = new File(DOWNLOAD + "/" + file_name);
                if (newFile.exists()) { return; }
                // Tải file với yt-dlp
                ProcessBuilder pb = new ProcessBuilder(
                        "yt-dlp",
                        "-f", "bestaudio[ext=mp3]",
                        "--output", newFile.getAbsolutePath(),
                        link_file
                );
                Process process = pb.start();
                process.waitFor();

                System.out.println("Đã tải file: " + newFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Lỗi khi tải file: " + e.getMessage());
            }
        });
    }
    public void Delete_filepl(String filename) {
        File file = new File(DOWNLOAD + "/" + filename);
        if (file.exists()) {
            file.delete();

        };
    }


    public void Play_track(){
        isplaying = true;
        positionSlider.setValue(0);
        Pausebtn.setIcon(pauseIcon);
        stream.stop();
        stream.Play(item.getFileName());
        stream.startTimer(position, positionSlider, mongo.get_Duration(item.getDuration()));
    }

    public void stop_stream(){
        stream.stop();
        Pausebtn.setIcon(playIcon);
        isplaying = false;
    }
    public int get_wit(){return wit;}

    public Boolean next_song(){
        pollFromQueue(); // Xóa bài hiện tại khỏi queue
        LinkedList<Queue_Item> queue = getQueueDL();

        Queue_Item next = queue.isEmpty() ? null : queue.getFirst(); // Lấy bài tiếp theo nếu có

        if (next != null) {
            refresh_Queue(); // Làm mới giao diện queue
            setCurrentSong(next); // Cập nhật bài hát hiện tại
            Play_track();
            return true;
        } else {
            System.out.println("Run out of queue");
            stop_stream(); // Dừng stream khi hết bài trong queue
            return false;
        }
    }
}


