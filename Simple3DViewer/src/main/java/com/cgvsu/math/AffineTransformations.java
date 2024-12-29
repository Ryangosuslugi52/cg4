package com.cgvsu.math;

public class AffineTransformations {

    private Matrix transformationMatrix;

    public AffineTransformations() {
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

    public void rotate(float angleX, float angleY, float angleZ) {
        Matrix rotationX = Matrix.rotationX(angleX);
        Matrix rotationY = Matrix.rotationY(angleY);
        Matrix rotationZ = Matrix.rotationZ(angleZ);

        Matrix rotationMatrix = rotationX.multiply(rotationY).multiply(rotationZ);
        transformationMatrix = transformationMatrix.multiply(rotationMatrix);
    }

    public Matrix getTransformationMatrix() {
        return transformationMatrix;
    }

    public Vector4f applyToVector(Vector4f vector) {
        return vector.transform(transformationMatrix);
    }
}
