package com.cgvsu.triangulation;

import com.cgvsu.model.Polygon;
import com.cgvsu.math.Vector3f;

import java.util.ArrayList;

public class Triangulation {

    public static ArrayList<Polygon> triangulatePolygon(Polygon poly, ArrayList<Vector3f> allVertices) {
        int vertexCount = poly.getVertexIndices().size();
        ArrayList<Polygon> triangles = new ArrayList<>();

        if (vertexCount < 3) {
            return triangles;
        }

        if (vertexCount == 3) {
            triangles.add(poly);
            return triangles;
        }

        ArrayList<Integer> remainingVertices = new ArrayList<>(poly.getVertexIndices());

        while (remainingVertices.size() > 3) {
            boolean earFound = false;

            for (int i = 0; i < remainingVertices.size(); i++) {
                int prevIndex = (i == 0) ? remainingVertices.size() - 1 : i - 1;
                int nextIndex = (i == remainingVertices.size() - 1) ? 0 : i + 1;

                if (isEar(remainingVertices, prevIndex, i, nextIndex, poly, allVertices)) {
                    ArrayList<Integer> earVertices = new ArrayList<>();
                    earVertices.add(remainingVertices.get(prevIndex));
                    earVertices.add(remainingVertices.get(i));
                    earVertices.add(remainingVertices.get(nextIndex));

                    Polygon earTriangle = new Polygon();
                    earTriangle.setVertexIndices(earVertices);
                    triangles.add(earTriangle);

                    remainingVertices.remove(i);
                    earFound = true;
                    break;
                }
            }

            if (!earFound) {
                break;
            }
        }

        if (remainingVertices.size() == 3) {
            Polygon lastTriangle = new Polygon();
            lastTriangle.setVertexIndices(remainingVertices);
            triangles.add(lastTriangle);
        }

        return triangles;
    }

    private static boolean isEar(ArrayList<Integer> remainingVertices, int prev, int v, int next, Polygon poly, ArrayList<Vector3f> allVertices) {
        Vector3f p = allVertices.get(remainingVertices.get(prev));
        Vector3f vertex = allVertices.get(remainingVertices.get(v));
        Vector3f q = allVertices.get(remainingVertices.get(next));

        if (!isConvex(p, vertex, q)) {
            return false;
        }

        for (int i = 0; i < remainingVertices.size(); i++) {
            if (i == prev || i == v || i == next) continue;
            Vector3f other = allVertices.get(remainingVertices.get(i));

            if (isPointInTriangle(p, vertex, q, other)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isConvex(Vector3f p, Vector3f vertex, Vector3f q) {
        Vector3f edge1 = new Vector3f(vertex.x - p.x, vertex.y - p.y, vertex.z - p.z);
        Vector3f edge2 = new Vector3f(q.x - vertex.x, q.y - vertex.y, q.z - vertex.z);

        float crossProduct = edge1.x * edge2.y - edge1.y * edge2.x;
        return crossProduct > 0;
    }

    private static boolean isPointInTriangle(Vector3f p, Vector3f vertex, Vector3f q, Vector3f point) {
        float denominator = (q.y - vertex.y) * (p.x - vertex.x) + (vertex.x - q.x) * (p.y - vertex.y);
        float a = ((q.y - vertex.y) * (point.x - vertex.x) + (vertex.x - q.x) * (point.y - vertex.y)) / denominator;
        float b = ((vertex.y - p.y) * (point.x - vertex.x) + (p.x - vertex.x) * (point.y - vertex.y)) / denominator;
        float c = 1.0f - a - b;

        return a > 0 && b > 0 && c > 0;
    }

    public static ArrayList<Polygon> triangulateModel(ArrayList<Polygon> polygons, ArrayList<Vector3f> allVertices) {
        ArrayList<Polygon> triangulatedPolygons = new ArrayList<>();

        for (Polygon poly : polygons) {
            triangulatedPolygons.addAll(triangulatePolygon(poly, allVertices));
        }
        return triangulatedPolygons;
    }
}