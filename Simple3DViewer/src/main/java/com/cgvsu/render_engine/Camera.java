package com.cgvsu.render_engine;

import javax.vecmath.Vector3f;
import javax.vecmath.Matrix4f;

public class Camera {

    private Vector3f position;
    private Vector3f target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;
    private boolean isActive;

    public Camera(
            final Vector3f position,
            final Vector3f target,
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane,
            final boolean isActive
    ) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
        this.isActive = isActive;
    }

    public void setPosition(final Vector3f position) {
        this.position = position;
    }

    public void setAspectRatio(final float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getTarget() {
        return target;
    }

    Matrix4f getViewMatrix() {
        return GraphicConveyor.lookAt(position, target);
    }

    Matrix4f getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    public void zoom(float amount, float modelSize) {
        float zoomFactor = amount * (modelSize / 10f);
        Vector3f toCamera = new Vector3f();
        toCamera.sub(position, target);
        toCamera.scale(1 - zoomFactor);
        position.add(target, toCamera);
    }

    public void setTarget(Vector3f target) {
        this.target = target;
    }
}
