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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.JocDeTrons;

public class SplashScreen extends AbstractScreen {

	private Texture splashTexture;
	private Image splashImage;
	private Music musica;

	public SplashScreen(JocDeTrons joc) {
		super(joc);
	}

	@Override
	public void show() {

		super.show();

		musica = Gdx.audio.newMusic(Gdx.files
				.internal("sons/gameOfThrones.mp3"));
		musica.setLooping(true);
		musica.setVolume(1f);
		musica.play();

		// carregar la imatge
		splashTexture = new Texture(
				Gdx.files.internal("imatges/logo.jpg"));
		// seleccionar Linear per millorar l'estirament
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		splashImage = new Image(splashTexture);
		splashImage.setFillParent(true);

		// aix� nom�s �s necessari perqu� funcioni correctament l'efecte fade-in
		// Nom�s fa la imatge completament transparent
		splashImage.getColor().a = 0f;

		// configuro l'efecte de fade-in/out de la imatge de splash
		// sequence indica que es faran de manera consecutiva.
		splashImage.addAction(Actions.sequence(Actions.fadeIn(1f),
				Actions.delay(4f), Actions.fadeOut(1f), new Action() {
					@Override
					public boolean act(float delta) {
						nextScreen();
						return true;
					}
				}));

		// finalment afegim l'actor a l'stage
		stage.addActor(splashImage);

	}

	/**
	 * canviar a la següent pantalla
	 */
	private void nextScreen() {
		musica.stop();
		// la darrera acci� ens porta cap a la seg�ent pantalla
		//joc.setScreen(new PantallaPrincipal(joc));
		joc.setScreen(new MainMenuScreen(joc));
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

	@Override
	public void dispose() {
		super.dispose();
		splashTexture.dispose();
	}
}


