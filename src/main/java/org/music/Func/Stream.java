package org.music.Func;

import javazoom.jl.player.advanced.AdvancedPlayer;
import org.music.models.Queue_Item;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.util.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Stream {
    private AdvancedPlayer player;
    private Thread playerThread;
    private BufferedInputStream mp3Stream;
    public long startTime; // Thời gian tiep tuc phat
    public long elapsedTime; // Tổng thời gian đã trôi qua khi tạm dừng
    Timer timer;

    private long calculateBytesToSkip(long elapsedTime) {
        final int BITRATE = 128; // tính bằng kbps
        long bytesPerSecond = BITRATE * 1024 / 8; // Chuyển đổi sang byte mỗi giây
        return (bytesPerSecond * elapsedTime) / 1000; // Tính toán số byte cần bỏ qua
    }

    public void startTimer(JLabel position, JSlider positionSlider, int duration) {
        timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long currentTime = System.currentTimeMillis();
                long totalElapsed = elapsedTime + (currentTime - startTime);
                long seconds = totalElapsed / 1000;

                if (seconds >= duration) {
                    position.setText(formatTime(duration));
                    positionSlider.setValue(duration);
                    stopTimer();
                    elapsedTime = 0;
                } else {
                    position.setText(formatTime(seconds));
                    positionSlider.setValue((int) seconds);
                }
            }
        });
        timer.start();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    public String formatTime(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public void Play_Link(String url) {
        if (playerThread != null && playerThread.isAlive()) {
            stop(); // Dừng phát nhạc hiện tại
        }

        playerThread = new Thread(() -> {
            try {
                URI uri = new URI(url);
                mp3Stream = new BufferedInputStream(uri.toURL().openStream());

                if (elapsedTime > 0) {
                    mp3Stream.skip(calculateBytesToSkip(elapsedTime)); // Bỏ qua đến vị trí mong muốn
                }

                player = new AdvancedPlayer(mp3Stream);
                startTime = System.currentTimeMillis(); // Ghi lại thời gian bắt đầu phát

                // Chạy nhạc
                player.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        playerThread.start();
    }

    public void Play(String file_name) {
        if (playerThread != null && playerThread.isAlive()) { playerThread.interrupt();}

        playerThread = new Thread(() -> {
            try {
                File file = new File("./mp3_playlist" + "/" + file_name);

                while (!file.exists()) {
                    try {
                        File check = new File("./mp3_queue" + "/" + file_name);
                        if (!check.exists()) {
                            System.out.println("File không tìm thấy. Đang thử lại sau 2 giây...");
                            Thread.sleep(1000);  // Chờ 1 giây
                            file = new File("./mp3_queue" + "/" + file_name);  // Kiểm tra lại sự tồn tại của file
                        }

                    } catch (InterruptedException e) {return; }
                }
                mp3Stream = new BufferedInputStream(new FileInputStream(file));

                if (elapsedTime > 0) { skipBytes(mp3Stream, calculateBytesToSkip(elapsedTime)); }
                player = new AdvancedPlayer(mp3Stream);
                startTime = System.currentTimeMillis(); // Ghi lại thời gian bắt đầu phát

                player.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        playerThread.start();
    }

    private void skipBytes(BufferedInputStream stream, long bytesToSkip) throws IOException {
        long skipped = 0;
        while (skipped < bytesToSkip) {
            long remaining = bytesToSkip - skipped;
            long skippedNow = stream.skip(remaining);
            if (skippedNow == 0) {
                // Nếu không thể skip thêm byte nào, kiểm tra EOF
                if (stream.read() == -1) {
                    throw new IOException("Reached end of stream before skipping desired bytes");
                }
                skippedNow = 1; // Đã đọc một byte và bỏ qua
            }
            skipped += skippedNow;
        }
    }

    public void stop() {
        if (player != null) {
            try {
                player.close(); // Đóng player
                stopTimer();
                elapsedTime = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mp3Stream != null) {
            try {
                mp3Stream.close(); // Đóng luồng âm thanh
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        elapsedTime = 0; // Đặt lại thời gian đã trôi qua
    }

    public void pause() {
        if (player != null) {
            stopTimer();
            elapsedTime += System.currentTimeMillis() - startTime; // Lưu thời gian đã trôi qua
            player.close();
        }
    }

    public void setSystemVolume(float volumePercentage){
        int volume = Math.round(volumePercentage);
        String script = "set volume output volume " + volume;
        try {
            ProcessBuilder pb = new ProcessBuilder("osascript", "-e", script);
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}