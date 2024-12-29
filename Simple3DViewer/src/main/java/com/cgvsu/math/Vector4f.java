//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cgvsu.math;

import java.util.ArrayList;

public class Vector3d {
    public double x;
    public double y;
    public double z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Vector3d other) {
        float eps = 1.0E-4F;
        return Math.abs(this.x - other.x) < 9.999999747378752E-5 && Math.abs(this.y - other.y) < 9.999999747378752E-5 && Math.abs(this.z - other.z) < 9.999999747378752E-5;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public Vector3d multiplication(double num) {
        return new Vector3d(this.getX() * num, this.getY() * num, this.getZ() * num);
    }

    public static Vector3d sum(Vector3d... vectors) {
        double x = vectors[0].getX();
        double y = vectors[0].getY();
        double z = vectors[0].getZ();

        for(int i = 1; i < vectors.length; ++i) {
            x += vectors[i].getX();
            y += vectors[i].getY();
            z += vectors[i].getZ();
        }

        return new Vector3d(x, y, z);
    }

    public static Vector3d sum(ArrayList<Vector3d> vectors) {
        double x = ((Vector3d)vectors.get(0)).getX();
        double y = ((Vector3d)vectors.get(0)).getY();
        double z = ((Vector3d)vectors.get(0)).getZ();

        for(int i = 1; i < vectors.size(); ++i) {
            x += ((Vector3d)vectors.get(i)).getX();
            y += ((Vector3d)vectors.get(i)).getY();
            z += ((Vector3d)vectors.get(i)).getZ();
        }

        return new Vector3d(x, y, z);
    }

    public Vector3d divide(float num) {
        float eps = 1.0E-7F;
        if (num - 0.0F < 1.0E-7F) {
            throw new ArithmeticException("Division by zero");
        } else {
            return new Vector3d(this.x / (double)num, this.y / (double)num, this.z / (double)num);
        }
    }

    public static Vector3d calculateCrossProduct(Vector3d vector1, Vector3d vector2) {
        double x = vector1.getY() * vector2.getZ() - vector1.getZ() * vector2.getY();
        double y = vector1.getZ() * vector2.getX() - vector1.getX() * vector2.getZ();
        double z = vector1.getX() * vector2.getY() - vector1.getY() * vector2.getX();
        return new Vector3d(x, y, z);
    }

    public static Vector3d fromTwoPoints(float x1, float y1, float z1, float x2, float y2, float z2) {
        return new Vector3d((double)(x2 - x1), (double)(y2 - y1), (double)(z2 - z1));
    }

    public static Vector3d fromTwoPoints(Vector3d vertex1, Vector3d vertex2) {
        return new Vector3d(vertex2.getX() - vertex1.getX(), vertex2.getY() - vertex1.getY(), vertex2.getZ() - vertex1.getZ());
    }

    public void normalize() {
        double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        if (length != 0.0) {
            this.x /= length;
            this.y /= length;
            this.z /= length;
        }

    }
}
