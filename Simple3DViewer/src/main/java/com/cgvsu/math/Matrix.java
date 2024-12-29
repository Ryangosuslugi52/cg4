package com.cgvsu.math;

public class Matrix {
    public float[][] elements = new float[4][4];

    // Конструктор для единичной матрицы
    public Matrix() {
        for (int i = 0; i < 4; i++) {
            elements[i][i] = 1.0f; // Единичная матрица
        }
    }

    // Матрица переноса
    public static Matrix translation(float tx, float ty, float tz) {
        Matrix result = new Matrix();
        result.elements[0][3] = tx;
        result.elements[1][3] = ty;
        result.elements[2][3] = tz;
        return result;
    }

    // Матрица масштабирования
    public static Matrix scaling(float sx, float sy, float sz) {
        Matrix result = new Matrix();
        result.elements[0][0] = sx;
        result.elements[1][1] = sy;
        result.elements[2][2] = sz;
        return result;
    }

    // Матрица поворота вокруг оси X
    public static Matrix rotationX(float angle) {
        Matrix result = new Matrix();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        result.elements[1][1] = cos;
        result.elements[1][2] = -sin;
        result.elements[2][1] = sin;
        result.elements[2][2] = cos;
        return result;
    }

    // Матрица поворота вокруг оси Y
    public static Matrix rotationY(float angle) {
        Matrix result = new Matrix();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        result.elements[0][0] = cos;
        result.elements[0][2] = sin;
        result.elements[2][0] = -sin;
        result.elements[2][2] = cos;
        return result;
    }

    // Матрица поворота вокруг оси Z
    public static Matrix rotationZ(float angle) {
        Matrix result = new Matrix();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        result.elements[0][0] = cos;
        result.elements[0][1] = -sin;
        result.elements[1][0] = sin;
        result.elements[1][1] = cos;
        return result;
    }

    // Умножение матрицы на другую матрицу
    public Matrix multiply(Matrix other) {
        Matrix result = new Matrix();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.elements[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                    result.elements[i][j] += this.elements[i][k] * other.elements[k][j];
                }
            }
        }
        return result;
    }

    // Преобразование матрицы в строку (для отладки)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append("[");
            for (int j = 0; j < 4; j++) {
                sb.append(String.format("%6.2f ", elements[i][j]));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}
