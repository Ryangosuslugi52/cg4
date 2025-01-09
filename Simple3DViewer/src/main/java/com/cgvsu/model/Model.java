package com.cgvsu.model;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.normalize.NormalCalculator;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Model {

    public List<Vector3f> vertices = new ArrayList<>();
    public List<Vector2f> textureVertices = new ArrayList<>();
    public List<Vector3f> normals = new ArrayList<>();
    public List<Polygon> polygons = new ArrayList<>();
    public List<Polygon> polygonsWithoutTriangulation = new ArrayList<>();
    public boolean isActive = true;
    public boolean isActiveGrid = false;
    public boolean isActiveTexture = false;
    public String pathTexture = null;
    public boolean isActiveLighting = false;
    public Color color;
    private Vector3f translation = new Vector3f(0, 0, 0);
    private Vector3f rotation = new Vector3f(0, 0, 0);
    private Vector3f scale = new Vector3f(1, 1, 1);

    public void normalize() {
        try {
            normals = (ArrayList<Vector3f>) NormalCalculator.normale(vertices, polygons);
        } catch (Exception e) {
            System.err.println("Error during normalization: " + e.getMessage());
        }
    }
}
