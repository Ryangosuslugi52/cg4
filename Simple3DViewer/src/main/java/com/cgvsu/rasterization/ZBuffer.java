package com.cgvsu.rasterization;

public class ZBuffer {
    private final double[][] buffer;

    public ZBuffer(int width, int height) {
        buffer = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                buffer[x][y] = Double.POSITIVE_INFINITY; // Инициализация бесконечным значением
            }
        }
    }

    public boolean isCloser(int x, int y, double depth) {
        return depth < buffer[x][y];
    }

    public void setDepth(int x, int y, double depth) {
        buffer[x][y] = depth;
    }
}
