package es.deusto.prog3.captureflag.screen;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.ScreenUtils;

import es.deusto.prog3.captureflag.CaptureTheFlag;
import es.deusto.prog3.captureflag.actor.MapActor;
import es.deusto.prog3.captureflag.actor.PanelActor;
import es.deusto.prog3.captureflag.controller.GameController;
import es.deusto.prog3.captureflag.controller.GameListener;
import es.deusto.prog3.captureflag.controller.GameController.Direction;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GameScreen extends ScreenAdapter implements GameListener {

	class KeyboardProcessor extends InputAdapter {

		@Override
		public boolean keyUp(int key) {
			switch (key) {
				case Keys.DOWN:		gameController.move(Direction.DOWN);
									break;
				
				case Keys.UP:		gameController.move(Direction.UP);
									break;

				case Keys.RIGHT:	gameController.move(Direction.RIGHT);
									break;

				case Keys.LEFT:		gameController.move(Direction.LEFT);
									break;

				case Keys.ESCAPE:	showReturnDialog();
									break;

				case Keys.F:		toogleFullScreen();
									break;

				default:			break;
			}

			return false;
		}

		// muestra un diálogo para volver a la pantalla principal
		private void showReturnDialog() {
		
			Dialog dialog = new Dialog("Warning", game.getDefaultSkin(), "dialog") {
				
				@Override
				public void result(Object obj) {
					// se elimina el diálogo del stage
					this.remove();

					// se comprueba la opción elegida por el usuario
					boolean result = (boolean) obj;
					if (result) {
						toMainScreen();
					}
				}
	
			};
			
			dialog.text("Are you sure you want to return to main menu?");
			dialog.button("Yes", true); //sends "true" as the result
			dialog.button("No", false); //sends "false" as the result
			dialog.show(stage);
		}

		// cambia el modo gráfico entre pantalla completa y ventana
		private void toogleFullScreen() {
			if (game.isFullScreen()) {
				game.setWindowed();
			} else {
				game.setFullscreen();
			}
		}
	}

	private static final String SCREEN_NAME = "Game Screen";

	private CaptureTheFlag game;

	private Stage stage;
	private MapActor mapActor;
	private PanelActor panelActor;
	private Group gameGroup;

	private Music music;
	private Sound flagPickSound;

	// mapa de juego
	private GameController gameController = new GameController();
	
	public GameScreen(CaptureTheFlag game) {
		this.game = game;

		Gdx.app.log(SCREEN_NAME, "Iniciando screen principal del juego");
		
		// se carga la información del mapa desde fichero 
		try {
			gameController.loadFile(Gdx.files.internal("gamescreen/map/mapinfo.txt").reader());
		} catch (IOException e) {
			Gdx.app.log(SCREEN_NAME, "Could not load map file information");
		}

		// creamos el stage para esta pantalla
		// se le añade el viewport por defecto para
		// toda la aplicación. Este viewport gestiona
		// como se transforman las coordenadas a pantalla.
		stage = new Stage(game.getViewport());

		// grupo para la pantalla de juego y el panel
		gameGroup = new Group();

		// se añade al grupo un actor que representa al mapa
		mapActor = new MapActor(gameController);
		gameGroup.addActor(mapActor);

		// se añade un actor que representa el panel de la derecha
		panelActor = new PanelActor(gameController);
		panelActor.setX(mapActor.getWidth());
		gameGroup.addActor(panelActor);

		// se añade el grupo al escenario
		stage.addActor(gameGroup);

		// cargamos el recurso de audio para el sonido de la bandera
		flagPickSound = Gdx.audio.newSound(Gdx.files.internal("sound/pick.mp3"));

		// carga y reproducción en bucle de la música de fondo
		music = Gdx.audio.newMusic(Gdx.files.internal("music/game-music.mp3"));
        music.setLooping(true);
        music.play();

		// establecemos un multiplexador para los eventos
		// queremos capturar eventos con el stage y con el listener
		// propio definido anteriormente
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(new KeyboardProcessor());

		// registramos el multiplexador de eventos como escuchador
		Gdx.input.setInputProcessor(multiplexer);

		gameController.restartMap();

		// establecemos esta clase como escuchadora de eventos
		// del controlador del juego
		gameController.setListener(this);
	}

	public void toMainScreen() {
		game.getScreen().dispose();
		game.setScreen(new MainScreen(game));
	}

	float rotSpeed = 20.0f;

	@Override
	public void render(float delta) {
		// borrado de pantalla para empezar a dibujar
		ScreenUtils.clear(0, 0, 0, 1);

		// en el método render se dice a los actores que
		// actuen.
		stage.act(delta);

		// se pinta la jerarquía del escenario
		stage.draw();
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		// liberamos los recursos utilizados
		mapActor.dispose();
		panelActor.dispose();
		stage.dispose();
		music.dispose();
		flagPickSound.dispose();
	}

	@Override
	public void flagPicked() {
		flagPickSound.play(1.0f);
	}

	@Override
	public void gameFinished() {
		Dialog dialog = new Dialog("Congratulations!", game.getDefaultSkin(), "dialog") {
				
			@Override
			public void result(Object obj) {
				// se elimina el diálogo del stage
				this.remove();

				// se comprueba la opción elegida por
				// el usuario
				boolean result = (boolean) obj;
				if (result) {
					gameController.restartMap();
				} else {
					toMainScreen();
				}
			}

		};
		
		dialog.text("You have finished the game! Restart?");
		dialog.button("Yes", true); //sends "true" as the result
		dialog.button("No", false); //sends "false" as the result
		dialog.show(stage);		
	}
}
