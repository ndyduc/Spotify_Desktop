package org.music.Func;

import javazoom.jl.player.advanced.AdvancedPlayer;
import org.music.Activity.Home;
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
    Home home;

    public Stream(Home home) {
        this.home = home;
    }

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

                // Kiểm tra nếu đã đến thời gian tối đa (duration)
                if (seconds >= duration) {
                    Boolean isNext = home.next_song();

                    stopTimer();
                    elapsedTime = 0;
                    if (isNext) {
                        home.Pausebtn.doClick();
                        home.Pausebtn.doClick();
                    } else { System.out.println("Phát xong"); }
                } else { // Cập nhật thời gian và slider
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

    public void Play(String file_name) {
        if (playerThread != null && playerThread.isAlive()) { playerThread.interrupt();}

        playerThread = new Thread(() -> {
            try {
                File file = new File("./mp3_playlist/" + file_name);

                while (!file.exists()) {
                    try {
                        File check = new File("./mp3_queue/" + file_name);
                        if (!check.exists()) {
                            System.out.println("File không tìm thấy. Đang thử lại sau 1 giây...");
                            Thread.sleep(1000);  // Chờ 1 giây // Kiểm tra lại sự tồn tại của file
                        } else file = check;

                    } catch (InterruptedException e) {return; }
                }
                mp3Stream = new BufferedInputStream(new FileInputStream(file));

                if (elapsedTime > 0) { skipBytes(mp3Stream, calculateBytesToSkip(elapsedTime)); }
                player = new AdvancedPlayer(mp3Stream);
                startTime = System.currentTimeMillis(); // Ghi lại thời gian bắt đầu phát

                player.play();
                System.out.println("luong player dang choi "+file_name);
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
                stopTimer(); // Dừng Timer
                player.close(); // Đóng player
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (playerThread != null && playerThread.isAlive()) {
            playerThread.interrupt(); // Dừng thread phát nhạc
        }

        if (mp3Stream != null) {
            try {
                mp3Stream.close(); // Đóng luồng âm thanh
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        elapsedTime = 0; // Đặt lại thời gian đã trôi qua
        startTime = 0;   // Đặt lại thời gian bắt đầu
    }

    public void pause() {
        if (player != null) {
            stopTimer(); // Dừng Timer
            elapsedTime += System.currentTimeMillis() - startTime; // Lưu thời gian đã trôi qua
            try {
                if (playerThread != null && playerThread.isAlive()) {
                    playerThread.interrupt(); // Dừng thread đang phát nhạc
                }
                if (mp3Stream != null) {
                    mp3Stream.close(); // Đóng luồng âm thanh
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.close(); // Đóng player
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