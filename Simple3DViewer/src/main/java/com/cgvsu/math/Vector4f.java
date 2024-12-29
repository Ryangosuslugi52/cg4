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
        return Math.abs(this.x - other.x) < EPS && Math.abs(this.y - other.y) < EPS && Math.abs(this.z - other.z) < EPS && Math.abs(this.w - other.w) < EPS;
    }
}

