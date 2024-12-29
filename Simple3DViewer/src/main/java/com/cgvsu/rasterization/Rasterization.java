package com.cgvsu.rasterization;

import com.cgvsu.model.Polygon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.List;

public class Rasterization {

    public static void renderPolygons(
            GraphicsContext graphicsContext,
            List<Polygon> polygons,
            double[][] vertices,
            Color[] vertexColors,
            ZBuffer zBuffer) {

        PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        for (Polygon polygon : polygons) {
            int[] arrX = new int[3];
            int[] arrY = new int[3];
            double[] arrZ = new double[3];
            Color[] colors = new Color[3];

            for (int i = 0; i < 3; i++) {
                int vertexIndex = polygon.getVertexIndices().get(i);
                arrX[i] = (int) vertices[vertexIndex][0];
                arrY[i] = (int) vertices[vertexIndex][1];
                arrZ[i] = vertices[vertexIndex][2];
                colors[i] = vertexColors[vertexIndex];
            }

            // Вычисление ограничивающего прямоугольника
            int minX = (int) Math.min(Math.min(arrX[0], arrX[1]), arrX[2]);
            int maxX = (int) Math.max(Math.max(arrX[0], arrX[1]), arrX[2]);
            int minY = (int) Math.min(Math.min(arrY[0], arrY[1]), arrY[2]);
            int maxY = (int) Math.max(Math.max(arrY[0], arrY[1]), arrY[2]);

            // Ограничиваем растеризацию в пределах экрана
            minX = Math.max(0, minX);
            maxX = Math.min((int) graphicsContext.getCanvas().getWidth(), maxX);
            minY = Math.max(0, minY);
            maxY = Math.min((int) graphicsContext.getCanvas().getHeight(), maxY);

            // Отрисовка треугольника внутри ограничивающего прямоугольника
            for (int y = minY; y <= maxY; y++) {
                if (y < arrY[1]) {
                    fillScanline(pixelWriter, arrX, arrY, arrZ, colors, zBuffer, y, 0, 1);
                } else {
                    fillScanline(pixelWriter, arrX, arrY, arrZ, colors, zBuffer, y, 1, 2);
                }
            }
        }
    }

    private static void fillScanline(
            PixelWriter pixelWriter,
            int[] arrX, int[] arrY, double[] arrZ,
            Color[] colors,
            ZBuffer zBuffer,
            int y, int i1, int i2) {

        int x1 = interpolateX(arrX, arrY, i1, y);
        int x2 = interpolateX(arrX, arrY, i2, y);
        double z1 = interpolateZ(arrX, arrY, arrZ, i1, y);
        double z2 = interpolateZ(arrX, arrY, arrZ, i2, y);
        Color color1 = interpolateColor(arrX, arrY, colors, i1, y);
        Color color2 = interpolateColor(arrX, arrY, colors, i2, y);

        if (x1 > x2) {
            int tempX = x1; x1 = x2; x2 = tempX;
            double tempZ = z1; z1 = z2; z2 = tempZ;
            Color tempColor = color1; color1 = color2; color2 = tempColor;
        }

        for (int x = x1; x <= x2; x++) {
            double z = interpolate(x1, x2, z1, z2, x);
            if (zBuffer.isCloser(x, y, z)) {
                zBuffer.setDepth(x, y, z);
                Color color = interpolateColor(x1, x2, color1, color2, x);
                pixelWriter.setColor(x, y, color);
            }
        }
    }

    private static int interpolateX(int[] arrX, int[] arrY, int i, int y) {
        if (arrY[2] == arrY[i]) return arrX[i];  // Защита от деления на 0
        return arrX[i] + (y - arrY[i]) * (arrX[2] - arrX[i]) / (arrY[2] - arrY[i]);
    }

    private static double interpolateZ(int[] arrX, int[] arrY, double[] arrZ, int i, int y) {
        if (arrY[2] == arrY[i]) return arrZ[i];  // Защита от деления на 0
        return arrZ[i] + (y - arrY[i]) * (arrZ[2] - arrZ[i]) / (arrY[2] - arrY[i]);
    }

    private static Color interpolateColor(int[] arrX, int[] arrY, Color[] colors, int i, int y) {
        double t = (double) (y - arrY[i]) / (arrY[2] - arrY[i]);
        return colors[i].interpolate(colors[2], t);
    }
    private static double interpolate(int x1, int x2, double v1, double v2, int x) {
        return v1 + (x - x1) * (v2 - v1) / (x2 - x1);
    }

    private static Color interpolateColor(int x1, int x2, Color c1, Color c2, int x) {
        double t = (double) (x - x1) / (x2 - x1);
        return c1.interpolate(c2, t);
    }
}