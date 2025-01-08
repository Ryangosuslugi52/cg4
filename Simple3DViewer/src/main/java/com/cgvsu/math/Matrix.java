//package com.cgvsu.math;
//
//public class Matrix {
//    public float[][] elements = new float[4][4];
//
//    public Matrix() {
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                elements[i][j] = (i == j) ? 1.0f : 0.0f;
//            }
//        }
//    }
//
//    public static Matrix translation(float tx, float ty, float tz) {
//        Matrix result = new Matrix();
//        result.elements[0][3] = tx;
//        result.elements[1][3] = ty;
//        result.elements[2][3] = tz;
//        return result;
//    }
//
//    public static Matrix scaling(float sx, float sy, float sz) {
//        Matrix result = new Matrix();
//        result.elements[0][0] = sx;
//        result.elements[1][1] = sy;
//        result.elements[2][2] = sz;
//        return result;
//    }
//
//    public static Matrix rotationX(float angle) {
//        Matrix result = new Matrix();
//        float cos = (float) Math.cos(angle);
//        float sin = (float) Math.sin(angle);
//        result.elements[1][1] = cos;
//        result.elements[1][2] = -sin;
//        result.elements[2][1] = sin;
//        result.elements[2][2] = cos;
//        return result;
//    }
//
//    public static Matrix rotationY(float angle) {
//        Matrix result = new Matrix();
//        float cos = (float) Math.cos(angle);
//        float sin = (float) Math.sin(angle);
//        result.elements[0][0] = cos;
//        result.elements[0][2] = sin;
//        result.elements[2][0] = -sin;
//        result.elements[2][2] = cos;
//        return result;
//    }
//
//    public static Matrix rotationZ(float angle) {
//        Matrix result = new Matrix();
//        float cos = (float) Math.cos(angle);
//        float sin = (float) Math.sin(angle);
//        result.elements[0][0] = cos;
//        result.elements[0][1] = -sin;
//        result.elements[1][0] = sin;
//        result.elements[1][1] = cos;
//        return result;
//    }
//
//    public Matrix multiply(Matrix other) {
//        Matrix result = new Matrix();
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                for (int k = 0; k < 4; k++) {
//                    result.elements[i][j] += this.elements[i][k] * other.elements[k][j];
//                }
//            }
//        }
//        return result;
//    }
//
//    public Vector4f multiply(Vector4f vec) {
//        return new Vector4f(
//                elements[0][0] * vec.x + elements[0][1] * vec.y + elements[0][2] * vec.z + elements[0][3] * vec.w,
//                elements[1][0] * vec.x + elements[1][1] * vec.y + elements[1][2] * vec.z + elements[1][3] * vec.w,
//                elements[2][0] * vec.x + elements[2][1] * vec.y + elements[2][2] * vec.z + elements[2][3] * vec.w,
//                elements[3][0] * vec.x + elements[3][1] * vec.y + elements[3][2] * vec.z + elements[3][3] * vec.w
//        );
//    }
//
//    public Matrix transpose() {
//        Matrix transposedMatrix = new Matrix();
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                transposedMatrix.elements[j][i] = this.elements[i][j];
//            }
//        }
//        return transposedMatrix;
//    }
//}
