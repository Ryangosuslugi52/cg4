package com.cgvsu.math;

import static com.cgvsu.math.EPS.EPS;

public class Vector4f {
    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public Vector4f(){}
    public boolean equals(Vector4f other) {
        return Math.abs(x - other.x) < EPS && Math.abs(y - other.y) < EPS && Math.abs(z - other.z) < EPS && Math.abs(w - other.w) < EPS;
    }

    public float x, y, z, w;
}
