package com.cgvsu.triangulation;

import com.cgvsu.model.Polygon;

import java.util.ArrayList;

public class Triangulation {
    public static ArrayList<Polygon> triangulatePolygon (Polygon poly) {
        int vertexNum = poly.getVertexIndices().size(); // число точек в исходном полигоне
        ArrayList<Polygon> polygons = new ArrayList<Polygon>(); // список треугольных полигонов

        if (poly.getVertexIndices().size() == 3) { // если передан треугольник, то возвращаем его
            polygons.add(poly);
            return polygons;
        }

        for (int i = 2; i < vertexNum - 1; i++) {
            ArrayList<Integer> vertex = new ArrayList<>(); // список точек в новом треугольнике
            vertex.add(poly.getVertexIndices().get(0));
            vertex.add(poly.getVertexIndices().get(i - 1));
            vertex.add(poly.getVertexIndices().get(i));

            Polygon currPoly = new Polygon(); //
            currPoly.setVertexIndices(vertex);
            polygons.add(currPoly);
        }
        if (vertexNum > 3) { // последний треугольник
            ArrayList<Integer> vertex = new ArrayList<>();
            vertex.add(poly.getVertexIndices().get(0));
            vertex.add(poly.getVertexIndices().get(vertexNum - 2));
            vertex.add(poly.getVertexIndices().get(vertexNum - 1));

            Polygon currPoly = new Polygon();
            currPoly.setVertexIndices(vertex);
            polygons.add(currPoly);
        }
        return polygons;
    }
    public static ArrayList<Polygon> triangulateModel (ArrayList<Polygon> polygons){
        ArrayList<Polygon> newModelPoly = new ArrayList<Polygon>();

        for (int i = 0; i < polygons.size(); i++) {
//            if (polygons.get(i).getVertexIndices().size() < 4) {
//                newModelPoly.add(polygons.get(i));
//                continue;
//            }
            newModelPoly.addAll(
                    triangulatePolygon(polygons.get(i))
            );
        }
        return newModelPoly;
    }
}
