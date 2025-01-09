package com.cgvsu.rasterization;

import com.cgvsu.model.Polygon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import static com.cgvsu.math.EPS.EPS;

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
            int[] coordX = new int[3];
            int[] coordY = new int[3];
            double[] deepZ = new double[3];
            Color[] colors = new Color[3];

            for (int i = 0; i < 3; i++) {
                int vertexIndex = polygon.getVertexIndices().get(i);

                if (vertexIndex >= vertices.length || vertexIndex < 0) {
                    System.err.println("Invalid vertex index: " + vertexIndex);
                    continue;
                }

                coordX[i] = (int) vertices[vertexIndex][0];
                coordY[i] = (int) vertices[vertexIndex][1];
                deepZ[i] = vertices[vertexIndex][2];
                colors[i] = vertexColors[vertexIndex];
            }

            sort(coordX, coordY, deepZ, colors);

            rasterizeTrianglePart(graphicsContext, coordX, coordY, deepZ, colors, zBuffer, 0, 1);

            rasterizeTrianglePart(graphicsContext, coordX, coordY, deepZ, colors, zBuffer, 1, 2);
        }
    }


    private static void rasterizeTrianglePart(
            GraphicsContext graphicsContext,
            int[] coordX, int[] coordY, double[] deepZ,
            Color[] colors, ZBuffer zBuffer,
            int startIdx, int endIdx) {

        PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int yStart = coordY[startIdx];
        int yEnd = coordY[endIdx];

        for (int y = yStart; y <= yEnd; y++) {
            int xl = interpolateX(y, coordY[startIdx], coordX[startIdx], coordY[endIdx], coordX[endIdx]);
            int xr = interpolateX(y, coordY[0], coordX[0], coordY[2], coordX[2]);

            if (xl > xr) {
                int temp = xl;
                xl = xr;
                xr = temp;
            }

            for (int x = xl; x <= xr; x++) {
                double[] baryCoords = calculateBarycentricCoordinates(x, y, coordX, coordY);

                if (isValidBarycentricCoord(baryCoords)) {
                    double z = interpolateZ(baryCoords, deepZ);

                    if (zBuffer.isCloser(x, y, z)) {
                        Color pixelColor = interpolateColor(baryCoords, colors);
                        zBuffer.setDepth(x, y, z);
                        pixelWriter.setColor(x, y, pixelColor);
                    }
                }
            }
        }
    }

    private static void sort(int[] coordX, int[] coordY, double[] deepZ, Color[] colors) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2 - i; j++) {
                if (coordY[j] > coordY[j + 1]) {
                    swap(coordX, j, j + 1);
                    swap(coordY, j, j + 1);
                    swap(deepZ, j, j + 1);
                    swap(colors, j, j + 1);
                }
            }
        }
    }

    private static int interpolateX(int y, int y1, int x1, int y2, int x2) {
        if (y1 == y2) return x1;
        return (y - y1) * (x2 - x1) / (y2 - y1) + x1;
    }

    private static double[] calculateBarycentricCoordinates(int x, int y, int[] coordX, int[] coordY) {
        double det = ((coordY[1] - coordY[2]) * (coordX[0] - coordX[2]) + (coordX[2] - coordX[1]) * (coordY[0] - coordY[2]));
        double l1 = ((coordY[1] - coordY[2]) * (x - coordX[2]) + (coordX[2] - coordX[1]) * (y - coordY[2])) / det;
        double l2 = ((coordY[2] - coordY[0]) * (x - coordX[2]) + (coordX[0] - coordX[2]) * (y - coordY[2])) / det;
        double l3 = 1 - l1 - l2;
        return new double[]{l1, l2, l3};
    }

    private static boolean isValidBarycentricCoord(double[] baryCoords) {
        return !Double.isNaN(baryCoords[0]) && !Double.isNaN(baryCoords[1]) && !Double.isNaN(baryCoords[2]) &&
                Math.abs(baryCoords[0] + baryCoords[1] + baryCoords[2] - 1) < EPS;
    }

    private static double interpolateZ(double[] baryCoords, double[] deepZ) {
        return baryCoords[0] * deepZ[0] + baryCoords[1] * deepZ[1] + baryCoords[2] * deepZ[2];
    }

    private static Color interpolateColor(double[] baryCoords, Color[] colors) {
        double r = clamp(baryCoords[0] * colors[0].getRed() + baryCoords[1] * colors[1].getRed() + baryCoords[2] * colors[2].getRed(), 0, 1);
        double g = clamp(baryCoords[0] * colors[0].getGreen() + baryCoords[1] * colors[1].getGreen() + baryCoords[2] * colors[2].getGreen(), 0, 1);
        double b = clamp(baryCoords[0] * colors[0].getBlue() + baryCoords[1] * colors[1].getBlue() + baryCoords[2] * colors[2].getBlue(), 0, 1);
        return Color.color(r, g, b);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }


    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static void swap(double[] arr, int i, int j) {
        double temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static void swap(Color[] arr, int i, int j) {
        Color temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
