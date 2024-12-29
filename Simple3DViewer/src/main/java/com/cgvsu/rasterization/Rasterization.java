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

            drawTriangle(pixelWriter, arrX, arrY, arrZ, colors, zBuffer);
        }
    }

    private static void drawTriangle(
            PixelWriter pixelWriter,
            int[] arrX, int[] arrY, double[] arrZ,
            Color[] colors,
            ZBuffer zBuffer) {

        sortVertices(arrX, arrY, arrZ, colors);

        for (int y = arrY[0]; y <= arrY[2]; y++) {
            if (y < arrY[1]) {
                fillScanline(pixelWriter, arrX, arrY, arrZ, colors, zBuffer, y, 0, 1);
            } else {
                fillScanline(pixelWriter, arrX, arrY, arrZ, colors, zBuffer, y, 1, 2);
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
        return arrX[i] + (y - arrY[i]) * (arrX[2] - arrX[i]) / (arrY[2] - arrY[i]);
    }

    private static double interpolateZ(int[] arrX, int[] arrY, double[] arrZ, int i, int y) {
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

    private static void sortVertices(int[] x, int[] y, double[] z, Color[] c) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2 - i; j++) {
                if (y[j] > y[j + 1]) {
                    swap(x, y, z, c, j, j + 1);
                }
            }
        }
    }
    private static void swap(int[] x, int[] y, double[] z, Color[] c, int i, int j) {
        int tempY = y[i]; y[i] = y[j]; y[j] = tempY;
        int tempX = x[i]; x[i] = x[j]; x[j] = tempX;
        double tempZ = z[i]; z[i] = z[j]; z[j] = tempZ;
        Color tempC = c[i]; c[i] = c[j]; c[j] = tempC;
    }
}