package com.cgvsu.triangulation;

import com.cgvsu.model.Polygon;
import java.util.ArrayList;

public class Triangulation {

    // Триангуляция одного полигона
    public static ArrayList<Polygon> triangulatePolygon(Polygon poly) {
        int vertexCount = poly.getVertexIndices().size();
        ArrayList<Polygon> triangles = new ArrayList<>();

        // Если в полигоне меньше 3 вершин, его невозможно триангулировать
        if (vertexCount < 3) {
            return triangles;
        }

        // Преобразование полигона с 3 вершинами не требуется
        if (vertexCount == 3) {
            triangles.add(poly);
            return triangles;
        }

        // Фан-триангуляция (triangle fan)
        for (int i = 1; i < vertexCount - 1; i++) {
            ArrayList<Integer> vertexIndices = new ArrayList<>();
            vertexIndices.add(poly.getVertexIndices().get(0));   // Центральная вершина
            vertexIndices.add(poly.getVertexIndices().get(i));   // Текущая вершина
            vertexIndices.add(poly.getVertexIndices().get(i + 1)); // Следующая вершина

            Polygon triangle = new Polygon();
            triangle.setVertexIndices(vertexIndices);
            triangles.add(triangle);
        }
        return triangles;
    }

    // Триангуляция списка полигонов
    public static ArrayList<Polygon> triangulateModel(ArrayList<Polygon> polygons) {
        ArrayList<Polygon> triangulatedPolygons = new ArrayList<>();

        for (Polygon poly : polygons) {
            triangulatedPolygons.addAll(triangulatePolygon(poly));
        }
        return triangulatedPolygons;
    }
}