package com.cgvsu.math;

import static com.cgvsu.math.EPS.EPS;

public class Vector4f {
    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public boolean equals(Vector4f other) {
        return Math.abs(x - other.x) < EPS && Math.abs(y - other.y) < EPS && Math.abs(z - other.z) < EPS && Math.abs(w - other.w) < EPS;
    }

    public Vector4f add(Vector4f other) {
        return new Vector4f(x + other.x, y + other.y, z + other.z, w + other.w);
    }

    public Vector4f subtract(Vector4f other) {
        return new Vector4f(x - other.x, y - other.y, z - other.z, w - other.w);
    }

    public Vector4f mul(float scalar) {
        return new Vector4f(x * scalar, y * scalar, z * scalar, w * scalar);
    }

    public Vector4f divide(float scalar) {
        if (Math.abs(scalar) < EPS) {
            throw new ArithmeticException("Division by zero");
        }
        return new Vector4f(x / scalar, y / scalar, z / scalar, w / scalar);
    }

    public void normalize() {
        float length = length();
        if (length > EPS) {
            x /= length;
            y /= length;
            z /= length;
            w /= length;
        } else {
            throw new ArithmeticException("Cannot normalize a zero vector");
        }
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public float dot(Vector4f other) {
        return x * other.x + y * other.y + z * other.z + w * other.w;
    }

    public Vector4f transform(Matrix matrix) {
        float[] result = new float[4];
        for (int i = 0; i < 4; i++) {
            result[i] = x * matrix.elements[i][0] +
                    y * matrix.elements[i][1] +
                    z * matrix.elements[i][2] +
                    w * matrix.elements[i][3];
        }
        return new Vector4f(result[0], result[1], result[2], result[3]);
    }

    @Override
    public String toString() {
        return "Vector4f{" + "x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + '}';
    }
}
