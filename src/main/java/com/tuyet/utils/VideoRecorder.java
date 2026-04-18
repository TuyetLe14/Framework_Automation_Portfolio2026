package com.tuyet.utils;

import org.monte.media.Format;
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
    public static ScreenRecorder screenRecorder;
    public String name;

    public VideoRecorder(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
            Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
            throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        if (!movieFolder.exists())
            movieFolder.mkdirs();
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
        return new File(movieFolder, name + "_" + timeStamp + "." + Registry.getInstance().getExtension(fileFormat));
    }

    public static void startRecording(String testCaseName) {
        try {
            String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            File file = new File("videos/" + dateFolder);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle captureSize = new Rectangle(0, 0, screenSize.width, screenSize.height);
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                    .getDefaultConfiguration();

            screenRecorder = new VideoRecorder(gc, captureSize,
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                            Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                    null, file, testCaseName);
            screenRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopAndKeepVideo() {
        try {
            if (screenRecorder != null)
                screenRecorder.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopAndDeleteVideo() {
        try {
            if (screenRecorder != null) {
                screenRecorder.stop();
                List<File> createdMovieFiles = screenRecorder.getCreatedMovieFiles();
                for (File movie : createdMovieFiles) {
                    if (movie.exists())
                        movie.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}