package com.cgvsu.rasterization;

public class ZBuffer {
    private final double[][] buffer;

    public ZBuffer(int width, int height) {
        buffer = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                buffer[x][y] = Double.POSITIVE_INFINITY;
            }
        }
    }

    public boolean isCloser(int x, int y, double depth) {
        if (x < 0 || x >= buffer.length ||  y < 0 || y >= buffer[0].length) {
            return false;
        }
        return depth < buffer[x][y];
    }

    public void setDepth(int x, int y, double depth) {
        if (x >= 0 && x < buffer.length && y >= 0 && y < buffer[0].length) {
            buffer[x][y] = depth;
        }
    }
}
