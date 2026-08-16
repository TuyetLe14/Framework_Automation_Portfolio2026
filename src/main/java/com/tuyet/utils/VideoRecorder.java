package com.tuyet.utils;

import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class VideoRecorder extends ScreenRecorder {
    private static final ThreadLocal<VideoRecorder> CURRENT_RECORDER =
            new ThreadLocal<>();
    public final String name;

    public VideoRecorder(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
            Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
            throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        if (!movieFolder.exists() && !movieFolder.mkdirs()) {
            throw new IOException(
                    "Unable to create video directory: "
                            + movieFolder.getAbsolutePath()
            );
        }
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
        return new File(movieFolder, name + "_" + timeStamp + "." + Registry.getInstance().getExtension(fileFormat));
    }

    public static void startRecording(String testCaseName) {
        try {
            stopAndDeleteVideo();
            String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            File videoDirectory = new File(
                    "Test_Reports/"
                            + dateFolder
                            + "/Videos/"
            );

            if (!videoDirectory.exists()
                    && !videoDirectory.mkdirs()) {

                throw new IOException(
                        "Unable to create video directory: "
                                + videoDirectory.getAbsolutePath()
                );
            }

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle captureSize = new Rectangle(0, 0, screenSize.width, screenSize.height);

            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

            VideoRecorder recorder = new VideoRecorder(gc, captureSize,
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, 24,
                            FrameRateKey, Rational.valueOf(15),
                            QualityKey, 1.0f,
                            KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                    null, videoDirectory, testCaseName);

            CURRENT_RECORDER.set(recorder);

            recorder.start();
            System.out.println("🎥 Đang quay video cho Test Case: " + testCaseName);

        } catch (Exception e) {
            CURRENT_RECORDER.remove();
            System.err.println("❌ Lỗi khi khởi động quay video: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String stopAndKeepVideo() {
        VideoRecorder recorder = CURRENT_RECORDER.get();
        if (recorder == null) {
            return "";
        }
        try {
            recorder.stop();

            List<File> createdMovieFiles =
                    recorder.getCreatedMovieFiles();
            if (createdMovieFiles != null && !createdMovieFiles.isEmpty()) {
                File videoFile = createdMovieFiles.get(0);
                if (videoFile.exists()) {
                    String videoPath = videoFile.getAbsolutePath();
                    System.out.println("🎥 Video saved at: " + videoPath);
                    return videoPath; 
                }
            }
        } catch (IOException e) {
            System.err.println(
                    "❌ Lỗi khi dừng quay video: "
                            + e.getMessage()
            );
            e.printStackTrace();
        } finally {

            CURRENT_RECORDER.remove();
    }
        return "";
    }

    public static void stopAndDeleteVideo() {
        VideoRecorder recorder = CURRENT_RECORDER.get();
        if (recorder == null) {
            return;
        }
        try {
            recorder.stop();
            List<File> createdMovieFiles =
                    recorder.getCreatedMovieFiles();
            
            if (createdMovieFiles != null) {
                for (File movie : createdMovieFiles) {
                    if (movie != null && movie.exists()){
                        if (!movie.delete()) {
                            System.err.println(
                                    "⚠️ Không thể xóa video: "
                                            + movie.getAbsolutePath()
                            );
                        }    
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(
                    "❌ Lỗi khi dừng/xóa video: "
                            + e.getMessage()
            );
            e.printStackTrace();
        }finally { CURRENT_RECORDER.remove();}
    }
}
