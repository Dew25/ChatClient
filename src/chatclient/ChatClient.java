/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Melnikov
 */
public class ChatClient extends Application {
    
private BufferedReader in;
    private PrintWriter out;
    private TextArea messagesArea;

    @Override
    public void start(Stage primaryStage) {
        messagesArea = new TextArea();
        messagesArea.setEditable(false);
        TextField inputField = new TextField();

        inputField.setOnAction(event -> {
            out.println(inputField.getText());
            inputField.setText("");
        });

        VBox layout = new VBox(10, messagesArea, inputField);
        layout.setPrefSize(400, 600);

        connectToServer();

        primaryStage.setScene(new Scene(layout));
        primaryStage.setTitle("Chat Client");
        primaryStage.show();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 1234);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            Thread listenerThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        final String message = line;
                        javafx.application.Platform.runLater(() -> messagesArea.appendText(message + "\n"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            listenerThread.setDaemon(true);
            listenerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
