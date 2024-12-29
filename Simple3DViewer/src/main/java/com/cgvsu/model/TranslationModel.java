package com.cgvsu.model;

import com.cgvsu.math.Matrix;

public class TranslationModel {

    private Matrix transformationMatrix;

    public TranslationModel() {
        transformationMatrix = new Matrix(); // Единичная матрица
    }

    public void translate(float tx, float ty, float tz) {
        Matrix translationMatrix = Matrix.translation(tx, ty, tz);
        transformationMatrix = transformationMatrix.multiply(translationMatrix);
    }

    public void scale(float sx, float sy, float sz) {
        Matrix scalingMatrix = Matrix.scaling(sx, sy, sz);
        transformationMatrix = transformationMatrix.multiply(scalingMatrix);
    }

    public void rotateX(float angle) {
        Matrix rotationMatrixX = Matrix.rotationX(angle);
        transformationMatrix = transformationMatrix.multiply(rotationMatrixX);
    }

    public void rotateY(float angle) {
        Matrix rotationMatrixY = Matrix.rotationY(angle);
        transformationMatrix = transformationMatrix.multiply(rotationMatrixY);
    }

    public void rotateZ(float angle) {
        Matrix rotationMatrixZ = Matrix.rotationZ(angle);
        transformationMatrix = transformationMatrix.multiply(rotationMatrixZ);
    }

    public void rotate(float angleX, float angleY, float angleZ) {
        rotateX(angleX);
        rotateY(angleY);
        rotateZ(angleZ);
    }

    public Matrix getTransformationMatrix() {
        return transformationMatrix;
    }

    public void resetTransformations() {
        transformationMatrix = new Matrix(); // Сброс к единичной матрице
    }
}
