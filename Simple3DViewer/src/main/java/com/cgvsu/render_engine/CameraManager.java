package com.cgvsu.render_engine;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class CameraManager {
    private List<Camera> cameras;
    private int activeCameraIndex;

    public CameraManager() {
        cameras = new ArrayList<>();
        activeCameraIndex = 0; // По умолчанию активная камера первая
    }

    public void addCamera(Camera camera) {
        cameras.add(camera);
    }

    public void removeCamera(int index) {
        if (index >= 0 && index < cameras.size()) {
            cameras.remove(index);
            if (activeCameraIndex >= cameras.size()) {
                activeCameraIndex = cameras.size() - 1; // Смена активной камеры, если удалена текущая
            }
        }
    }

    public void setActiveCamera(int index) {
        if (index >= 0 && index < cameras.size()) {
            activeCameraIndex = index;
        }
    }

    public Camera getActiveCamera() {
        return cameras.get(activeCameraIndex);
    }

    public List<Camera> getCameras() {
        return cameras;
    }
}
