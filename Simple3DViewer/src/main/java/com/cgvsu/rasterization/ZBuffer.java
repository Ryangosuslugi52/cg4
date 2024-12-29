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
        if (x < 0 || x >= buffer.length ||  y < 0 || y >= buffer[0].length) {
            return false; // Если координаты выходят за пределы, то возвращаем false
        }
        return depth < buffer[x][y]; // Проверяем, если глубина текущего пикселя меньше, чем глубина в буфере
    }

    public void setDepth(int x, int y, double depth) {
        if (x >= 0 && x < buffer.length && y >= 0 && y < buffer[0].length) {
            buffer[x][y] = depth;
        }
    }
}
