package org.music.Activity;

import org.music.Components.Border_Radius;
import org.music.Components.Rounded_Label;
import org.music.models.Queue_Item;
import org.music.models.Track;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.music.Activity.Home.createButton;
import static org.music.Activity.Home.loadIcon;

public class Queue_L extends Border_Radius {
    public Queue_Lis listener;
    public Queue_L(int radius, Queue_Item item, AtomicBoolean showqueue, JButton Queue, AtomicBoolean showartist, JButton Artist) {
        super(radius);
        setLayout(new BorderLayout(0, 10));
        setPreferredSize(new Dimension(250, 0));
        setBackground(Color.decode("#1a1a1a"));
        setBorder(new EmptyBorder(10, 0, 5, 5));

        JPanel resizeHandle = new JPanel();
        resizeHandle.setPreferredSize(new Dimension(2, 0)); // Độ rộng thanh kéo
        resizeHandle.setBackground(Color.decode("#1a1a1a"));
        add(resizeHandle, BorderLayout.WEST);
        resizeHandle.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));

        resizeHandle.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int newWidth = getWidth() - e.getX();
                if (newWidth > 200 && newWidth < 300) {
                    setPreferredSize(new Dimension(newWidth, getHeight()));
                    revalidate();
                    repaint();
                }
            }
        });

        JPanel queue_top = new JPanel(new BorderLayout());
        queue_top.setBackground(Color.decode("#1a1a1a"));
        JLabel qu = new JLabel("Queue");
        qu.setBorder(new EmptyBorder(0,10,0,0));
        qu.setForeground(Color.WHITE);
        qu.setFont(new Font("Serif", Font.BOLD,16));
        JButton close = createButton("src/main/resources/pngs/x.png",30,30);
        queue_top.add(qu, BorderLayout.WEST);
        queue_top.add(close, BorderLayout.EAST);
        add(queue_top, BorderLayout.NORTH);

        JPanel quescroll = new JPanel(new FlowLayout());
        quescroll.setBackground(Color.decode("#1a1a1a"));
        JPanel Libs = new JPanel(new GridLayout(0, 1));
        Libs.setBackground(Color.decode("#1a1a1a"));

        Track l = new Track();
        JPanel newque1 = getQueue(l, item);
        JPanel newque2 = getQueue(l, item);

        Libs.add(newque1);
        Libs.add(newque2);
        quescroll.add(Libs);
        JScrollPane playlistScroll = new JScrollPane(quescroll);
        playlistScroll.setBorder(null);
        playlistScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        playlistScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(playlistScroll, BorderLayout.CENTER);


        close.addActionListener(e -> {
            if (!showqueue.get()) {
                Queue.setIcon(loadIcon("src/main/resources/pngs/queue-green.png", 25, 25));
                showqueue.set(true);
                if (showartist.get()) {
                    Artist.setIcon(loadIcon("src/main/resources/pngs/user-scan.png", 25, 25));
                    showartist.set(false);
                }
            } else {
                Queue.setIcon(loadIcon("src/main/resources/pngs/queue.png", 25, 25));
                showqueue.set(false);
            }

            // Kích hoạt listener nếu có
            if (listener != null) {
                listener.onShowQueueChanged(showqueue);
            }
        });
    }

    private JPanel getQueue(Track i, Queue_Item item) {
        Border_Radius panel = new Border_Radius(15);
        panel.setBackground(Color.decode("#1a1a1a"));
        panel.setBorder(new LineBorder(new Color(0,0,0), 5));
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.setLayout(new BorderLayout(10,5));

        Rounded_Label queimg = new Rounded_Label(new ImageIcon("src/main/resources/pngs/x.png"),10);
        queimg.setPreferredSize(new Dimension(40, 40));

        new Thread(() -> {
            try {
                URI uri = new URI(item.getImgCover());
                ImageIcon imageIcon = new ImageIcon(uri.toURL());

                SwingUtilities.invokeLater(() -> {
                    queimg.setIcon(imageIcon);
                });

            } catch (URISyntaxException | MalformedURLException e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    queimg.setIcon(new ImageIcon("src/main/resources/pngs/x.png"));
                });
            }
        }).start();

        JPanel rque = new JPanel(new BorderLayout());
        rque.setBackground(new Color(0,0,0,0));

        JLabel nameque = new JLabel(item.getFileName());
        nameque.setFont(new Font("Serif", Font.PLAIN, 14));
        nameque.setBorder(new EmptyBorder(5,0,0,0));
        nameque.setForeground(Color.WHITE);

        JPanel botque = new JPanel(new BorderLayout());
        botque.setBackground(new Color(0,0,0,0));
        if (i.isIsdl()) botque.add(new JLabel(new ImageIcon("src/main/resources/pngs/download.png")));
        JLabel queartist = new JLabel(item.getArtist());
        queartist.setForeground(new Color(178,178,178));
        botque.add(queartist);

        rque.add(nameque, BorderLayout.NORTH);
        rque.add(botque, BorderLayout.SOUTH);

        panel.add(queimg, BorderLayout.WEST);
        panel.add(rque, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(73,73,73));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.decode("#1a1a1a"));
            }
        });

        return panel;
    }

    public void setShowQueueListener(Queue_Lis listener) {
        this.listener = listener;
    }
}
