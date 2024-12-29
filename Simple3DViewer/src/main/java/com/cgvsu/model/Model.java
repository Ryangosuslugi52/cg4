package com.cgvsu.model;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import javafx.scene.paint.Color;

import java.util.*;

public class Model {

    public ArrayList<Vector3f> vertices = new ArrayList<>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<>();
    public ArrayList<Vector3f> normals = new ArrayList<>();
    public ArrayList<Polygon> polygons = new ArrayList<>();
//    public ArrayList<Polygon> polygonsWithoutTriangulation = new ArrayList<>();
    public boolean isActive = true;
    public boolean isActiveGrid = false;
    public boolean isActiveTexture = false;
    public String pathTexture = null;
    public boolean isActiveLighting = false;
    public Color color;
//    public ImageToText imageToText = null;

//    public void triangulate() {
//        polygonsWithoutTriangulation = polygons;
//
//        polygons = (ArrayList<Polygon>) Triangulation.triangulate(polygons);
//    }

//    public void normalize() {
//        normals = (ArrayList<Vector3f>) Normalize.normale(vertices, polygons);
//    }
}
