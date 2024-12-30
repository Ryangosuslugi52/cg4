package com.cgvsu.math;

import static com.cgvsu.math.EPS.EPS;

public class Vector3f {
    public float x, y, z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public boolean equals(Vector3f other) {
        return Math.abs(this.x - other.x) < EPS && Math.abs(this.y - other.y) < EPS && Math.abs(this.z - other.z) < EPS;
    }

    public Vector3f add(Vector3f other) {
        return new Vector3f(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3f subtract(Vector3f other) {
        return new Vector3f(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public void mul(Vector3f var1, Vector3f var2) {
        this.x = var1.y * var2.z - var1.z * var2.y;
        this.y = -(var1.x * var2.z - var1.z * var2.x);
        this.z = var1.x * var2.y - var1.y * var2.x;
    }

    public void divide(float n) {
        if (n == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        } else {
            this.x /= n;
            this.y /= n;
            this.z /= n;
        }
    }

    public Vector3f scalar(float scalar) {
        return new Vector3f(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vector4f toColumnVector() {
        return new Vector4f(this.x, this.y, this.z, 1.0f);
    }

    public float dot(Vector3f other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector3f transform(Matrix matrix) {
        float[] result = new float[4];
        float[] vector = {x, y, z, 1};
        for (int i = 0; i < 4; i++) {
            result[i] = 0;
            for (int j = 0; j < 4; j++) {
                result[i] += matrix.elements[i][j] * vector[j];
            }
        }
        return new Vector3f(result[0], result[1], result[2]);
    }

    public Vector3f normalize() {
        float length = length();
        if (length == 0) {
            throw new ArithmeticException("Cannot normalize a zero-length vector");
        }
        return new Vector3f(this.x / length, this.y / length, this.z / length);
    }

    public Vector3f cross(Vector3f other) {
        return new Vector3f(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public static Vector3f zero() {
        return new Vector3f(0, 0, 0);
    }

    public static Vector3f unitX() {
        return new Vector3f(1, 0, 0);
    }

    public static Vector3f unitY() {
        return new Vector3f(0, 1, 0);
    }

    public static Vector3f unitZ() {
        return new Vector3f(0, 0, 1);
    }

    @Override
    public String toString() {
        return "Vector3f{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}