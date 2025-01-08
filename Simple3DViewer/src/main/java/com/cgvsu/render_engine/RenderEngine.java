package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.normalize.NormalCalculator;
import com.cgvsu.rasterization.Rasterization;
import com.cgvsu.rasterization.ZBuffer;
import com.cgvsu.triangulation.Triangulation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point2f;
import java.util.ArrayList;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height)
    {
        mesh.polygons = Triangulation.triangulate(mesh.polygons);
        NormalCalculator.calculateNormals(mesh);
        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);

        ZBuffer zBuffer = new ZBuffer(width, height);
        PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        ArrayList<double[]> vertices = new ArrayList<>();
        ArrayList<Color> vertexColors = new ArrayList<>();

        for (Vector3f vertex : mesh.vertices) {
            javax.vecmath.Vector3f vertexVecmath = new javax.vecmath.Vector3f(vertex.x, vertex.y, vertex.z);
            javax.vecmath.Vector3f transformedVertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertexVecmath);

            Point2f point = vertexToPoint(transformedVertex, width, height);
            vertices.add(new double[]{point.x, point.y, transformedVertex.z});

            vertexColors.add(Color.RED);
        }

        Rasterization.renderPolygons(graphicsContext, mesh.polygons, vertices.toArray(new double[0][]), vertexColors.toArray(new Color[0]), zBuffer);
    }
}