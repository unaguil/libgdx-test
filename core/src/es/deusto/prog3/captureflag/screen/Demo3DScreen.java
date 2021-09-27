package es.deusto.prog3.captureflag.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import es.deusto.prog3.captureflag.CaptureTheFlag;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

public class Demo3DScreen extends ScreenAdapter {

    // campo de visión de la cámara en grados
    private static final float FOV_Y = 60;
    private static final float NEAR  = 1.0f;
    private static final float FAR = 300.0f;

    // cámara para visualizar la escena
    // con perspectiva
    private PerspectiveCamera camera;

    // modelo con la información de renderizado
    private Model model;
    
    // este objeto contiene la información sobre
    // posición y transformaciones para el modelo
    private ModelInstance modelInstance;

    //objeto para renderizar los modelos 3d
    private ModelBatch modelBatch;

    // contiene información sobre configuración de la escena
    private Environment environment;

    // viewport de la escena que determina el ajuste a pantalla
    private Viewport viewport;
    
    public Demo3DScreen() {
        // creación y configuración de la cámara de la escena
        camera = new PerspectiveCamera(FOV_Y, CaptureTheFlag.DEFAULT_WIDTH, CaptureTheFlag.DEFAULT_HEIGHT);
        camera.position.set(10f, 10f, 10f);
		camera.lookAt(0,0,0);
		camera.near = NEAR;
		camera.far = FAR;
        camera.update();

        // creamos el viewport para la pantalla asignando la cámara en perspectiva
        viewport = new FitViewport(CaptureTheFlag.DEFAULT_WIDTH, CaptureTheFlag.DEFAULT_HEIGHT, camera);

        // creamos una caja de ejemplo utilizando la
        // utilidad ModelBuilder
        ModelBuilder modelBuilder = new ModelBuilder();
        Material material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
        // el atributo Usage.Position es siempre obligatorio
        // además se añade información de normales al cubo para la iluminación
        model = modelBuilder.createBox(5f, 5f, 5f, material, Usage.Position | Usage.Normal);

        // creamos el objeto para controlar la posición y transformaciones de la geometría
        modelInstance = new ModelInstance(model);

        // creamos el objeto usado para el rederizado de los modelos
        modelBatch = new ModelBatch();

        // creamos el objeto para la configuración de la escena
        // y añadimos una luz ambiental y una luz direccional
        environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    float rotSpeed = 15f;

    @Override
    public void render(float delta) {
        // iniciamos el renderizado
		modelBatch.begin(camera);        
        
        // establecemos la cámara
        modelBatch.setCamera(camera);

        // limpiamos la pantalla y el buffer de profundidad
        ScreenUtils.clear(0, 0, 0, 1, true);

        // aplicamos un rotación al nodo del modelo
        modelInstance.transform.rotate(new Vector3(0f, 1f, 0f), rotSpeed * delta);

        // renderizamos el cubo con la información de escena de luces
		modelBatch.render(modelInstance, environment);

        // terminamos el renderizado
		modelBatch.end();  
    }

    @Override
    public void dispose() {
        model.dispose();
        modelBatch.dispose();
    }

    @Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
