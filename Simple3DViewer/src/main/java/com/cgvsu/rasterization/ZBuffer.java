package com.cgvsu.rasterization;

public class ZBuffer {
    private final double[][] buffer;

    public ZBuffer(int width, int height) {
        buffer = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                buffer[x][y] = Float.MAX_VALUE;
            }
        }
    }

    public boolean isCloser(int x, int y, double depth) {
        if (x < 0 || x >= buffer.length || y < 0 || y >= buffer[0].length) {
            return false;
        }
        return depth < buffer[x][y];
    }

    public void setDepth(int x, int y, double depth) {
        if (x >= 0 && x < buffer.length && y >= 0 && y < buffer[0].length) {
            buffer[x][y] = depth;
        }
    }

    public void clear() {
        for (int x = 0; x < buffer.length; x++) {
            for (int y = 0; y < buffer[0].length; y++) {
                buffer[x][y] = Float.MAX_VALUE;
            }
        }
    }

    public double getDepth(int x, int y) {
        if (x >= 0 && x < buffer.length && y >= 0 && y < buffer[0].length) {
            return buffer[x][y];
        }
        return Float.MAX_VALUE;
    }
}
