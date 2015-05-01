/*************************************************************************************
 *                                                                                   *
 *  Joc de Trons por Java Norriors se distribuye bajo una                            *
 *  Licencia Creative Commons Atribución-NoComercial-SinDerivar 4.0 Internacional.   *
 *                                                                                   *
 *  http://creativecommons.org/licenses/by-nc-nd/4.0/                                *
 *                                                                                   *
 *  @author: Arnau Roma Vidal  - aroma@infoboscoma.net                               *
 *  @author: Rubén Garcia Torres - rgarcia@infobosccoma.net                          *
 *  @author: Francesc Gallart Vila - fgallart@infobosccoma.net                       *
 *                                                                                   *
/************************************************************************************/

package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.JocDeTrons;

public class SplashScreen extends AbstractScreen {
	private Texture texture = new Texture(Gdx.files.internal("imatges/logo.jpg"));
	private Image splashImage = new Image(texture);
	private Stage stage = new Stage();
	private Music musica;


	public SplashScreen(JocDeTrons joc) {
		super(joc);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		musica = Gdx.audio.newMusic(Gdx.files
				.internal("sons/gameOfThrones.mp3"));
		musica.setLooping(true);
		musica.setVolume(1f);
		musica.play();
		stage.addActor(splashImage);

		splashImage.addAction(Actions.sequence(Actions.alpha(0)
				,Actions.fadeIn(1f),Actions.delay(4f), Actions.fadeOut(1f), Actions.run(new Runnable() {
			@Override
			public void run() {
				nextScreen();
			}
		})));
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		musica.dispose();
		texture.dispose();
		stage.dispose();
	}

	/**
	 * en aixecar el botó del mouse després de fer clic o bé en aixecar el dit després de fer
	 * touch
	 *
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 * @param button
	 * @return
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		nextScreen();
		return true;
	}

	private void nextScreen() {
		// la darrera acció ens porta cap a la següent pantalla
		//joc.setScreen(new PantallaPrincipal(joc));
		joc.setScreen(new MainMenuScreen(joc));
	}
}
