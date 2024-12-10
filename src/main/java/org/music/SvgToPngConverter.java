package org.music;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class SvgToPngConverter {
    public static void main(String[] args) {
        String svgDirectory = "src/main/resources/images"; // Đường dẫn đến thư mục chứa tệp SVG
        String pngDirectory = "src/main/resources/pngs"; // Đường dẫn đến thư mục lưu trữ tệp PNG đầu ra

        File pngDir = new File(pngDirectory);
        if (!pngDir.exists()) {
            pngDir.mkdir();
        }

        try {
            convertAllSvgToPng(svgDirectory, pngDirectory);
            System.out.println("Converted SVG to PNG successfully!");
        } catch (IOException | TranscoderException e) {
            e.printStackTrace();
        }
    }

    public static void convertAllSvgToPng(String svgDirectory, String pngDirectory) throws IOException, TranscoderException {
        File dir = new File(svgDirectory);
        File[] svgFiles = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".svg")); // Lọc các tệp SVG

        List<String> filesToConvert = Arrays.asList("circle-arrow-down-green.svg",
                "pin.svg",
                "square-rounded-plus.png");

        if (svgFiles != null) {
            for (File svgFile : svgFiles) {
                if (filesToConvert.contains(svgFile.getName())) {
                    String pngFileName = pngDirectory + "/" + svgFile.getName().replace(".svg", ".png"); // Đường dẫn tệp PNG
                    convertSvgToPng(svgFile.getAbsolutePath(), pngFileName, 20f);
                    System.out.println("Converted: " + svgFile.getName() + " to " + pngFileName);
                } else {
                    String pngFileName = pngDirectory + "/" + svgFile.getName().replace(".svg", ".png"); // Đường dẫn tệp PNG
                    convertSvgToPng(svgFile.getAbsolutePath(), pngFileName, 25f);
                    System.out.println("Converted: " + svgFile.getName() + " to " + pngFileName);
                }
            }
        } else {
            System.out.println("No SVG files found in the directory.");
        }
    }

    public static void convertSvgToPng(String svgFile, String pngFile, float size) throws IOException, TranscoderException {
        // Tạo đối tượng PNGTranscoder
        PNGTranscoder transcoder = new PNGTranscoder();

        // Cài đặt thông số cho PNGTranscoder
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, size);
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, size);

        // Tạo TranscoderInput từ tệp SVG
        TranscoderInput input = new TranscoderInput(new FileInputStream(svgFile));

        // Tạo TranscoderOutput cho tệp PNG đầu ra
        OutputStream os = new FileOutputStream(pngFile);
        TranscoderOutput output = new TranscoderOutput(os);

        // Thực hiện chuyển đổi
        transcoder.transcode(input, output);

        // Đóng các luồng
        os.flush();
        os.close();
    }
}