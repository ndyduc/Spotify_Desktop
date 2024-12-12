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
    public static String getFile(String url) {
        String audioUrl = "";
        try {
            ProcessBuilder pb = new ProcessBuilder("youtube-dl", "-f", "best", "-g", url);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            process.waitFor();

            audioUrl = output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return audioUrl;
    }


    private long calculateBytesToSkip(long elapsedTime) {
        final int BITRATE = 128; // tính bằng kbps
        long bytesPerSecond = BITRATE * 1024 / 8; // Chuyển đổi sang byte mỗi giây
        return (bytesPerSecond * elapsedTime) / 1000; // Tính toán số byte cần bỏ qua
    }

    public void startTimer(JLabel position, JSlider positionSlider, int duration) {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long currentTime = System.currentTimeMillis();
                long totalElapsed = elapsedTime + (currentTime - startTime);
                long seconds = totalElapsed / 1000;

                if (seconds >= duration) {
                    position.setText(formatTime(duration));
                    positionSlider.setValue(duration);
                    stopTimer();
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
        File file = new File("./mp3_queue" + "/" + file_name);
        if (playerThread != null && playerThread.isAlive()) {stop();}
        playerThread = new Thread(() -> {
            try {
                mp3Stream = new BufferedInputStream(new FileInputStream(file));

                if (elapsedTime > 0) { mp3Stream.skip(calculateBytesToSkip(elapsedTime));}
                player = new AdvancedPlayer(mp3Stream);
                startTime = System.currentTimeMillis(); // Ghi lại thời gian bắt đầu phát

                player.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        playerThread.start();
    }

    public void stop() {
        if (player != null) {
            player.close(); // Dừng phát nhạc
            elapsedTime = 0; // Đặt lại thời gian đã trôi qua
        }
        if (mp3Stream != null) {
            try {
                mp3Stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public void debugAudioSystem() {
        try {
            Mixer.Info[] mixers = AudioSystem.getMixerInfo();
            for (Mixer.Info mixerInfo : mixers) {
                Mixer mixer = AudioSystem.getMixer(mixerInfo);
                System.out.println("Mixer: " + mixerInfo.getName());

                // Liệt kê tất cả các Line khả dụng
                Line.Info[] targetLines = mixer.getTargetLineInfo();
                for (Line.Info targetLine : targetLines) {
                    System.out.println("  Target Line: " + targetLine.toString());
                }

                Line.Info[] sourceLines = mixer.getSourceLineInfo();
                for (Line.Info sourceLine : sourceLines) {
                    System.out.println("  Source Line: " + sourceLine.toString());
                }

                // Kiểm tra các Port
                if (mixer.isLineSupported(Port.Info.SPEAKER)) {
                    Port port = (Port) mixer.getLine(Port.Info.SPEAKER);
                    port.open();
                    System.out.println("  Port opened: " + port.toString());

                    Control[] controls = port.getControls();
                    for (Control control : controls) {
                        System.out.println("    Control: " + control.toString());
                    }
                    port.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}