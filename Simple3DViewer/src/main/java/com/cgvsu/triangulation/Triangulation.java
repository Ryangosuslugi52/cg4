package com.cgvsu.triangulation;

import com.cgvsu.model.Polygon;
import com.cgvsu.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Triangulation {

    // Триангуляция одного полигона
    public static ArrayList<Polygon> triangulatePolygon(Polygon poly, ArrayList<Vector3f> allVertices) {
        int vertexCount = poly.getVertexIndices().size();
        ArrayList<Polygon> triangles = new ArrayList<>();

        // Если в полигоне меньше 3 вершин, его невозможно триангулировать
        if (vertexCount < 3) {
            return triangles;
        }

        // Если полигон уже треугольник, возвращаем его как есть
        if (vertexCount == 3) {
            triangles.add(poly);
            return triangles;
        }

        // Список индексов всех вершин в полигона
        ArrayList<Integer> remainingVertices = new ArrayList<>(poly.getVertexIndices());

        // Алгоритм "Ear Clipping"
        while (remainingVertices.size() > 3) {
            boolean earFound = false;

            // Ищем ушко
            for (int i = 0; i < remainingVertices.size(); i++) {
                int prevIndex = (i == 0) ? remainingVertices.size() - 1 : i - 1;
                int nextIndex = (i == remainingVertices.size() - 1) ? 0 : i + 1;

                // Проверяем, является ли треугольник ушком
                if (isEar(remainingVertices, prevIndex, i, nextIndex, poly, allVertices)) {
                    // Создаем новый треугольник и добавляем его в список
                    ArrayList<Integer> earVertices = new ArrayList<>();
                    earVertices.add(remainingVertices.get(prevIndex));
                    earVertices.add(remainingVertices.get(i));
                    earVertices.add(remainingVertices.get(nextIndex));

                    Polygon earTriangle = new Polygon();
                    earTriangle.setVertexIndices(earVertices);
                    triangles.add(earTriangle);

                    // Удаляем вершину, образующую ушко
                    remainingVertices.remove(i);
                    earFound = true;
                    break;
                }
            }

            // Если не нашли ушко, прерываем процесс (невозможно триангулировать)
            if (!earFound) {
                break;
            }
        }

        // Добавляем последний треугольник
        if (remainingVertices.size() == 3) {
            Polygon lastTriangle = new Polygon();
            lastTriangle.setVertexIndices(remainingVertices);
            triangles.add(lastTriangle);
        }

        return triangles;
    }

    // Метод для проверки, является ли вершина ушком
    private static boolean isEar(ArrayList<Integer> remainingVertices, int prev, int v, int next, Polygon poly, ArrayList<Vector3f> allVertices) {
        // Получаем координаты вершин полигона
        Vector3f p = allVertices.get(remainingVertices.get(prev));
        Vector3f vertex = allVertices.get(remainingVertices.get(v));
        Vector3f q = allVertices.get(remainingVertices.get(next));

        // Проверка на выпуклость треугольника
        if (!isConvex(p, vertex, q)) {
            return false;
        }

        // Проверка, что нет других вершин внутри треугольника
        for (int i = 0; i < remainingVertices.size(); i++) {
            if (i == prev || i == v || i == next) continue;
            Vector3f other = allVertices.get(remainingVertices.get(i));

            if (isPointInTriangle(p, vertex, q, other)) {
                return false; // Если точка внутри треугольника, это не ушко
            }
        }

        return true;
    }

    // Проверка на выпуклость (если угол между сторонами треугольника направлен наружу)
    private static boolean isConvex(Vector3f p, Vector3f vertex, Vector3f q) {
        Vector3f edge1 = new Vector3f(vertex.x - p.x, vertex.y - p.y, vertex.z - p.z);
        Vector3f edge2 = new Vector3f(q.x - vertex.x, q.y - vertex.y, q.z - vertex.z);

        float crossProduct = edge1.x * edge2.y - edge1.y * edge2.x; // Плоскость перпендикулярна
        return crossProduct > 0; // Если скалярное произведение положительное, угол выпуклый
    }

    // Проверка, лежит ли точка внутри треугольника
    private static boolean isPointInTriangle(Vector3f p, Vector3f vertex, Vector3f q, Vector3f point) {
        float denominator = (q.y - vertex.y) * (p.x - vertex.x) + (vertex.x - q.x) * (p.y - vertex.y);
        float a = ((q.y - vertex.y) * (point.x - vertex.x) + (vertex.x - q.x) * (point.y - vertex.y)) / denominator;
        float b = ((vertex.y - p.y) * (point.x - vertex.x) + (p.x - vertex.x) * (point.y - vertex.y)) / denominator;
        float c = 1.0f - a - b;

        return a > 0 && b > 0 && c > 0; // Если все коэффициенты положительные, точка внутри
    }

    // Триангуляция модели
    public static ArrayList<Polygon> triangulateModel(ArrayList<Polygon> polygons, ArrayList<Vector3f> allVertices) {
        ArrayList<Polygon> triangulatedPolygons = new ArrayList<>();

        for (Polygon poly : polygons) {
            triangulatedPolygons.addAll(triangulatePolygon(poly, allVertices));
        }
        return triangulatedPolygons;
    }
}