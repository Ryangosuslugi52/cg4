package com.cgvsu;

import com.cgvsu.math.AffineTransformations;
import com.cgvsu.model.TranslationModel;
import com.cgvsu.model.Model;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final List<Model> meshes;

    public Scene() {
        this.meshes = new ArrayList<>();
    }

    public void addModel(Model model) {
        this.meshes.add(model);
    }

    public Model getActiveModel() {
        for (Model m : meshes) {
            if (m.isActive) {
                return m;
            }
        }
        return !meshes.isEmpty() ? meshes.get(0) : null;
    }

    public void transformActiveModel(
            float tx, float ty, float tz,
            float rx, float ry, float rz,
            float sx, float sy, float sz) {
        Model active = getActiveModel();
        if (active == null) {
            throw new RuntimeException("Нет активной модели для трансформации!");
        }
        Matrix4f transposeMatrix = AffineTransformations.modelMatrix(
                tx, ty, tz,
                rx, ry, rz,
                sx, sy, sz
        );
        TranslationModel.move(transposeMatrix, active);
    }
}
