package com.cgvsu.normalize;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class NormalCalculator {

    public static void calculateNormals(Model model) {
        List<Vector3f> vertices = model.vertices;
        List<Polygon> polygons = model.polygons;

        // Инициализация массива нормалей нулевыми векторами
        List<Vector3f> normals = new ArrayList<>(vertices.size());
        for (int i = 0; i < vertices.size(); i++) {
            normals.add(Vector3f.zero());
        }

        // Проход по каждому полигону для вычисления нормалей граней
        for (Polygon polygon : polygons) {
            Vector3f normal = computeFaceNormal(
                    vertices.get(polygon.getVertexIndices().get(0)),
                    vertices.get(polygon.getVertexIndices().get(1)),
                    vertices.get(polygon.getVertexIndices().get(2))
            );

            // Добавление нормали ко всем вершинам полигона
            for (int vertexIndex : polygon.getVertexIndices()) {
                normals.get(vertexIndex).add(normal);
            }
        }

        // Нормализация всех вершинных нормалей
        for (Vector3f normal : normals) {
            try {
                normal.normalize();
            } catch (ArithmeticException e) {
                // В случае нулевого вектора - оставить его как есть
            }
        }

        // Установка нормалей обратно в модель
        model.normals = new ArrayList<>(normals);
    }

    private static Vector3f computeFaceNormal(Vector3f v1, Vector3f v2, Vector3f v3) {
        // Векторы ребер
        Vector3f edge1 = new Vector3f(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
        Vector3f edge2 = new Vector3f(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);

        // Векторное произведение для вычисления нормали
        Vector3f normal = new Vector3f(0, 0, 0);
        normal.mul(edge1, edge2);  // Результат сохраняется в normal

        try {
            normal.normalize();
        } catch (ArithmeticException e) {
            // Если нормаль нулевая, оставляем ее как есть
        }

        return normal;
    }
}