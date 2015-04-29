package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Screens.SplashScreen;



public class JocDeTrons extends Game {
	
	/**
	 * Box2D treballa millor amb valors petits. NO s'han d'utilitzar unitats de
	 * p�xels. Es recomana utilitzar una constant per convertir p�xels a metres
	 * i a la inversa
	 */
	public static final float PIXELS_PER_METRE = 96.0f;
	
	public static final int WIDTH = 900;
	public static final int HEIGHT = 620;

	// cont� el t�tol del joc
	private String titol;
	// skin utilitzat en el joc
	private Skin skin;

	public Skin getSkin() {
		return skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	/**
	 * Mides de la pantalla en p�xels
	 */
	private int screenWidth;
	private int screenHeight;



	/**
	 * Constructor per defecte
	 */
	public JocDeTrons() {
		setScreenWidth(-1);
		setScreenHeight(-1);
	}

	/**
	 * Constructor amb par�metres
	 * 
	 * @param width
	 *            Amplada de la finestra
	 * @param height
	 *            Al�ada de la finestra
	 */
	public JocDeTrons(int width, int height, String titol) {
		this(titol);
		setScreenWidth(width);
		setScreenHeight(height);
	}

	public JocDeTrons(String titol) {
		this();
		this.setTitol(titol);
	}



	@Override
	public void create() {
		// càrrega de l'skin
		skin = new Skin(Gdx.files.internal("skins/skin.json"));
		// si està en un dispositiu Android, escalar la font segons la densitat de pantalla
		if(Gdx.app.getType() == Application.ApplicationType.Android) {
			skin.getFont("default-font").setScale(Gdx.graphics.getDensity(), Gdx.graphics.getDensity());
			//skin.getFont("groc").setScale(Gdx.graphics.getDensity(), Gdx.graphics.getDensity());
		}
		// començar el joc amb la SplashScreen
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		skin.dispose();

	}
	@Override
	public void resume() {

	}

	@Override
	public void render() {
		super.render();

	}



	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
	}


	public String getTitol() {
		return titol;
	}

	public void setTitol(String titol) {
		this.titol = titol;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
}
