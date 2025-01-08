package com.cgvsu;

import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Matrix4f;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GuiController {

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;


    @FXML
    private Button selectTextureButton; // Поле для кнопки выбора текстуры

    private Model mesh = null;

    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100,
            true);

    private Timeline timeline;
    private boolean isMousePressedForModel = false;
    private double lastMouseXForModel, lastMouseYForModel;
    private final float rotationSensitivity = 0.2f;
    private final float zoomSpeed = 0.3f;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (mesh != null) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();

        setupMouseControls();

        selectTextureButton.setOnAction(event -> handleSelectTextureButtonClick());

    }
    private void handleSelectTextureButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        fileChooser.setTitle("Выберите текстуру");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file != null) {
            String texturePath = file.getAbsolutePath();
            // Предполагается, что у вас есть метод для установки текстуры модели
            if (mesh != null) {
                mesh.pathTexture = texturePath; // Установка пути к текстуре в модели
                System.out.println("Текстура установлена: " + texturePath);
            } else {
                System.out.println("Модель не загружена.");
            }
        }
    }



    private void setupMouseControls() {
        canvas.setOnMousePressed(this::handleMousePress);
        canvas.setOnMouseDragged(this::handleMouseDrag);
        canvas.setOnMouseReleased(this::handleMouseRelease);
        canvas.setOnScroll(this::handleScroll);
    }

    @FXML
    public void handleMousePress(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            isMousePressedForModel = true;
            lastMouseXForModel = event.getSceneX();
            lastMouseYForModel = event.getSceneY();
        }
        canvas.requestFocus();
    }

    @FXML
    public void handleMouseDrag(MouseEvent event) {
        if (isMousePressedForModel) {
            double currentX = event.getSceneX();
            double currentY = event.getSceneY();

            double deltaX = currentX - lastMouseXForModel;
            double deltaY = currentY - lastMouseYForModel;

            float angleY = (float) (deltaX * rotationSensitivity * 0.01);
            float angleX = (float) (deltaY * rotationSensitivity * 0.01);

            // Вместо вращения модели, вращаем камеру вокруг цели
            Vector3f cameraPosition = camera.getPosition();
            Vector3f cameraTarget = camera.getTarget();

            // Вращение вокруг вертикальной оси (Y)
            Matrix4f rotationY = new Matrix4f();
            rotationY.rotY(angleY);
            rotationY.transform(cameraPosition);

            // Вращение вокруг горизонтальной оси (X)
            Vector3f right = new Vector3f();
            right.cross(new Vector3f(0, 1, 0), new Vector3f(cameraTarget.x - cameraPosition.x, cameraTarget.y - cameraPosition.y, cameraTarget.z - cameraPosition.z));
            right.normalize();
            Matrix4f rotationX = new Matrix4f();
            rotationX.setIdentity();
            rotationX.setRotation(new AxisAngle4f(right, angleX));
            rotationX.transform(cameraPosition);

            camera.setPosition(cameraPosition);

            lastMouseXForModel = currentX;
            lastMouseYForModel = currentY;
        }
    }

    @FXML
    public void handleMouseRelease(MouseEvent event) {
        if (isMousePressedForModel && !event.isPrimaryButtonDown()) {
            isMousePressedForModel = false;
        }
    }

    @FXML
    public void handleScroll(ScrollEvent event) {
        float zoom = (float) event.getDeltaY() * zoomSpeed;
        float modelSize = mesh != null ? 0.1f : 1f;
        camera.zoom(zoom, modelSize);
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            // todo: обработка ошибок
        } catch (IOException exception) {
            // Handle exception
        }
    }
}
