package com.cgvsu;

import com.cgvsu.render_engine.CameraManager;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
import java.util.Objects;
import javafx.scene.input.KeyEvent;

public class GuiController {

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    @FXML
    private Button selectTextureButton;
    @FXML
    private Button addCameraButton; // Кнопка для добавления камеры
    @FXML
    private Button removeCameraButton; // Кнопка для удаления камеры
    @FXML
    private Button switchCameraButton; // Кнопка для переключения камеры
    @FXML
    private TextField cameraPosX;
    @FXML
    private TextField cameraPosY;
    @FXML
    private TextField cameraPosZ;
    @FXML
    private TextField cameraTargetX;
    @FXML
    private TextField cameraTargetY;
    @FXML
    private TextField cameraTargetZ;

    @FXML
    private ComboBox<Camera> cameraComboBox;

    private int cameraCount = 0; // Счетчик для камер

    private final Scene scene = new Scene();

    private Model mesh = null;
    private CameraManager cameraManager; // Менеджер камер

    public TextField sx;
    public TextField sy;
    public TextField sz;
    public TextField tx;
    public TextField ty;
    public TextField tz;
    public TextField rx;
    public TextField ry;
    public TextField rz;
    public Button convert;

    Alert messageError = new Alert(Alert.AlertType.ERROR);

    private Timeline timeline;
    private boolean isMousePressedForModel = false;
    private double lastMouseXForModel, lastMouseYForModel;
    private final float rotationSensitivity = 0.2f;
    private final float zoomSpeed = 0.3f;

    @FXML
    private void initialize() {
        cameraManager = new CameraManager();
        cameraManager.addCamera(new Camera(new Vector3f(0, 0, 100),
                new Vector3f(0, 0, 0),
                1.0F, 1, 0.01F, 100, true,1));
        updateCameraComboBox();

        switchCameraButton.setOnAction(event -> handleSwitchCamera());
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        addCameraButton.setOnAction(event -> handleAddCamera());
        removeCameraButton.setOnAction(event -> handleRemoveCamera());
        switchCameraButton.setOnAction(event -> handleSwitchCamera());

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            cameraManager.getActiveCamera().setAspectRatio((float) (width / height));

            if (mesh != null) {
                RenderEngine.render(canvas.getGraphicsContext2D(), cameraManager, mesh, (int) width, (int) height);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();

        setupMouseControls();

        canvas.setOnKeyPressed(this::handleKeyPress);
        canvas.setFocusTraversable(true);

        selectTextureButton.setOnAction(event -> handleSelectTextureButtonClick());

    }
    private void updateCameraComboBox() {
        cameraComboBox.getItems().clear();
        cameraComboBox.getItems().addAll(cameraManager.getCameras());
        cameraComboBox.getSelectionModel().select(cameraManager.getActiveCamera()); // Устанавливаем активную камеру по умолчанию
    }


    private void handleAddCamera() {
        try {
            float posX = Float.parseFloat(cameraPosX.getText());
            float posY = Float.parseFloat(cameraPosY.getText());
            float posZ = Float.parseFloat(cameraPosZ.getText());
            float targetX = Float.parseFloat(cameraTargetX.getText());
            float targetY = Float.parseFloat(cameraTargetY.getText());
            float targetZ = Float.parseFloat(cameraTargetZ.getText());

            Camera newCamera = new Camera(
                    new Vector3f(posX, posY, posZ),
                    new Vector3f(targetX, targetY, targetZ),
                    1.0F,
                    (float) ((float) canvas.getWidth() / canvas.getHeight()),
                    0.01F,
                    100F,
                    true,
                    ++cameraCount); // Увеличиваем счетчик и передаем его

            cameraManager.addCamera(newCamera);
            updateCameraNames(); // Обновляем ComboBox с названиями камер

            System.out.println("Камера добавлена: " + newCamera);
        } catch (NumberFormatException e) {
            showMessage("Ошибка", "Введите корректные числовые значения для позиции и направления камеры.", messageError);
        }
    }


    private void updateCameraNames() {
        cameraComboBox.getItems().clear(); // Очищаем текущие элементы
        for (int i = 0; i < cameraManager.getCameras().size(); i++) {
            cameraComboBox.getItems().add(cameraManager.getCameras().get(i)); // Добавляем объект Camera
        }

        // Устанавливаем StringConverter для отображения названий камер
        cameraComboBox.setConverter(new javafx.util.StringConverter<Camera>() {
            @Override
            public String toString(Camera camera) {
                return "Camera " + (cameraManager.getCameras().indexOf(camera) + 1); // Возвращаем название камеры
            }

            @Override
            public Camera fromString(String string) {
                return null; // Не требуется для этого случая
            }
        });

        if (!cameraManager.getCameras().isEmpty()) {
            cameraComboBox.getSelectionModel().select(cameraManager.getActiveCamera()); // Устанавливаем активную камеру по умолчанию
        }
    }



    private void handleRemoveCamera() {
        int selectedIndex = cameraComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            cameraManager.removeCamera(selectedIndex); // Удаляем камеру из менеджера
            updateCameraNames(); // Обновляем названия в ComboBox
            System.out.println("Камера удалена: Camera " + (selectedIndex + 1));
        } else {
            showMessage("Ошибка", "Выберите камеру для удаления.", messageError);
        }
    }

    private void handleSwitchCamera() {
        int selectedIndex = cameraComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            cameraManager.setActiveCamera(selectedIndex);
            System.out.println("Переключение на камеру: Camera " + (selectedIndex + 1));
        }
    }



    private void handleSelectTextureButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        fileChooser.setTitle("Выберите текстуру");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file != null) {
            String texturePath = file.getAbsolutePath();
            if (mesh != null) {
                mesh.pathTexture = texturePath;
                System.out.println("Текстура установлена: " + texturePath);
            } else {
                System.out.println("Модель не загружена.");
            }
        }
    }

    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                rotateCamera(0, -rotationSensitivity);
                break;
            case S:
                rotateCamera(0, rotationSensitivity);
                break;
            case A:
                rotateCamera(-rotationSensitivity, 0);
                break;
            case D:
                rotateCamera(rotationSensitivity, 0);
                break;
            case UP:
                moveCamera(0, 0, -zoomSpeed);
                break;
            case DOWN:
                moveCamera(0, 0, zoomSpeed);
                break;
        }
    }

    private void rotateCamera(float angleY, float angleX) {
        Vector3f cameraPosition = cameraManager.getActiveCamera().getPosition();
        Vector3f cameraTarget = cameraManager.getActiveCamera().getTarget();

        Matrix4f rotationY = new Matrix4f();
        rotationY.rotY(angleY);
        rotationY.transform(cameraPosition);

        Vector3f right = new Vector3f();
        right.cross(new Vector3f(0, 1, 0), new Vector3f(cameraTarget.x - cameraPosition.x, cameraTarget.y - cameraPosition.y, cameraTarget.z - cameraPosition.z));
        right.normalize();
        Matrix4f rotationX = new Matrix4f();
        rotationX.setIdentity();
        rotationX.setRotation(new AxisAngle4f(right, angleX));
        rotationX.transform(cameraPosition);

        cameraManager.getActiveCamera().setPosition(cameraPosition);
    }

    private void moveCamera(float dx, float dy, float dz) {
        Vector3f cameraPosition = cameraManager.getActiveCamera().getPosition();
        Vector3f cameraTarget = cameraManager.getActiveCamera().getTarget();

        Vector3f direction = new Vector3f(cameraTarget);
        direction.sub(cameraPosition);
        direction.normalize();

        Vector3f right = new Vector3f();
        right.cross(direction, new Vector3f(0, 1, 0));
        right.normalize();

        Vector3f up = new Vector3f();
        up.cross(right, direction);

        Vector3f movement = new Vector3f();
        movement.scaleAdd(dx, right, movement);
        movement.scaleAdd(dy, up, movement);
        movement.scaleAdd(dz, direction, movement);

        cameraPosition.add(movement);
        cameraTarget.add(movement);

        cameraManager.getActiveCamera().setPosition(cameraPosition);
        cameraManager.getActiveCamera().setTarget(cameraTarget);
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

            Vector3f cameraPosition = cameraManager.getActiveCamera().getPosition();
            Vector3f cameraTarget = cameraManager.getActiveCamera().getTarget();

            Matrix4f rotationY = new Matrix4f();
            rotationY.rotY(angleY);
            rotationY.transform(cameraPosition);

            Vector3f right = new Vector3f();
            right.cross(new Vector3f(0, 1, 0), new Vector3f(cameraTarget.x - cameraPosition.x, cameraTarget.y - cameraPosition.y, cameraTarget.z - cameraPosition.z));
            right.normalize();
            Matrix4f rotationX = new Matrix4f();
            rotationX.setIdentity();
            rotationX.setRotation(new AxisAngle4f(right, angleX));
            rotationX.transform(cameraPosition);

            cameraManager.getActiveCamera().setPosition(cameraPosition);

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
        cameraManager.getActiveCamera().zoom(zoom, modelSize);
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
            scene.addModel(mesh);
            // todo: обработка ошибок
        } catch (IOException exception) {
        }
    }

    private void showMessage(String headText, String messageText, Alert alert) {
        alert.setHeaderText(headText);
        alert.setContentText(messageText);
        alert.showAndWait();
    }

    public void convert(MouseEvent mouseEvent) {
        if (Objects.equals(tx.getText(), "") || Objects.equals(ty.getText(), "") || Objects.equals(tz.getText(), "")
                || Objects.equals(sx.getText(), "") || Objects.equals(sy.getText(), "") || Objects.equals(sz.getText(), "")
                || Objects.equals(rx.getText(), "") || Objects.equals(ry.getText(), "") || Objects.equals(rz.getText(), "")) {
            showMessage("Ошибка", "Введите необходимые данные!", messageError);
        } else {
            try {
                float txVal = Float.parseFloat(tx.getText());
                float tyVal = Float.parseFloat(ty.getText());
                float tzVal = Float.parseFloat(tz.getText());
                float rxVal = Float.parseFloat(rx.getText());
                float ryVal = Float.parseFloat(ry.getText());
                float rzVal = Float.parseFloat(rz.getText());
                float sxVal = Float.parseFloat(sx.getText());
                float syVal = Float.parseFloat(sy.getText());
                float szVal = Float.parseFloat(sz.getText());
                scene.transformActiveModel(txVal, tyVal, tzVal, rxVal, ryVal, rzVal, sxVal, syVal, szVal);
            } catch (NumberFormatException e) {
                showMessage("Ошибка", "Неправильный формат чисел!", messageError);
            } catch (RuntimeException ex) {
                showMessage("Ошибка", ex.getMessage(), messageError);
            }
        }
    }
}
