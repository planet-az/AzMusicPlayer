package com.example.az;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Az Music Player - とても単純なミュージックプレーヤー
 * 
 * @author Azumi Hirata
 *
 */
public class AzMusicPlayer extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // iTunesのミュージックファイル(.m4a)のリスト
        List<Path> musicFiles = new ArrayList<>();

        // iTunes Mediaフォルダ
        // Windowsの場合はここ
        Path start = Paths.get(System.getProperty("user.home"), "Music", "iTunes", "iTunes Media");

        // ファイルの探索
        // サブフォルダを含めてミュージックファイルだけ取り出す
        FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".m4a")) {
                    musicFiles.add(file);
                }
                return FileVisitResult.CONTINUE;
            }

        };

        Files.walkFileTree(start, visitor);

        // ミュージックファイルがなければ終了
        if (musicFiles.isEmpty()) {
            Platform.exit();
        }

        // 再生する曲を決める(ランダム)
        int i = new Random().nextInt(musicFiles.size());
        Path path = musicFiles.get(i);

        // MediaPlayer
        // ミュージックの再生・停止・音量調節など
        Media media = new Media(path.toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

        // MediaView
        // MediaPlayerを画面に埋め込む
        MediaView mediaView = new MediaView(mediaPlayer);

        // Image
        // 適当に五線譜と音符の画像を用意する
        Image image = new Image("/publicdomainq-0002180mqg.jpg");

        // ImageView
        // Imageを画面に埋め込む
        ImageView imageView = new ImageView(image);

        // 曲名を取得する
        String name = path.getFileName().toString();

        // ラベル : 曲名
        Label label = new Label("Now playing: " + name);
        label.setFont(new Font(24.0));

        // レイアウト
        VBox vbox = new VBox(imageView, label, mediaView);

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("Az Music Player - " + name);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
