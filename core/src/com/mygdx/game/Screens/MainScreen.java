package com.mygdx.game.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.Barra;
import com.mygdx.game.GestorContactes;
import com.mygdx.game.JocDeTrons;
import com.mygdx.game.MapBodyManager;
import com.mygdx.game.Monstre;
import com.mygdx.game.Personatge;
import com.mygdx.game.TiledMapHelper;

/**
 * Una pantalla del joc
 *
 * @author Marc
 *
 */
public class MainScreen extends AbstractScreen {

    /**
     * Estils
     */
    private final Skin skin;
    /**
	 * Variable d'instancia que permet gestionar i pintar el mapa a partir d'un
	 * TiledMap (TMX)
	 */
	private TiledMapHelper tiledMapHelper;

	// objecte que gestiona el protagonista del joc
	// ---->private PersonatgeBackup personatge;
    private Personatge personatge;

	Barra barra, barra2, barra3;
	/**
	 * Objecte que cont� tots els cossos del joc als quals els aplica la
	 * simulaci�
	 */
	private World world;

	/**
	 * Objecte que dibuixa elements per debugar. Dibuixa linies al voltant dels
	 * l�mits de les col�lisions. Va molt b� per comprovar que les
	 * col�lisions s�n les que desitgem. Cal tenir present, pe`ro, que �s
	 * m�s lent. Nom�s s'ha d'utitilitzar per debugar.
	 */
	private Box2DDebugRenderer debugRenderer;

	/**
	 * Musica i sons
	 */
	private Music musica;

    /**
     * Per debugar les col·lisions
     */
	private Box2DDebugRenderer box2DRenderer;

    /**
     * Per mostrar el títol
     */
    private Label title;
	private Label title2;

	private Table table = new Table();
	private Table table2 = new Table();

	private int vides;

	private Monstre monstre;

	/**
     * per indicar quins cossos s'han de destruir
     * @param joc
     */
    //private ArrayList<Body> bodyDestroyList;


	public MainScreen(JocDeTrons joc, int vides) {
		super(joc);

        // carregar el fitxer d'skins
        skin = new Skin(Gdx.files.internal("skins/skin.json"));
        title = new Label(joc.getTitol(),skin, "groc");
		title2 = new Label("",skin, "groc");
		/*
		 * Crear el mon on es desenvolupa el joc. S'indica la gravetat: negativa
		 * perquè indica cap avall
		 */
		world = new World(new Vector2(0.0f, -9.8f), true);
		comprovarMidesPantalla();
		carregarMapa();
		carregarObjectes();
		carregarMusica();

        // --- si es volen destruir objectes, descomentar ---
		//bodyDestroyList= new ArrayList<Body>();
		//world.setContactListener(new GestorContactes(bodyDestroyList));
		world.setContactListener(new GestorContactes());

		// crear el personatge
        personatge = new Personatge(world);

		personatge.setVides(vides);
		world.setContactListener(new GestorContactes(personatge, this));

		this.vides = vides;

        // objecte que permet debugar les col·lisions
		debugRenderer = new Box2DDebugRenderer();

		barra = new Barra(world, 12.6f, 1.3f,15.0f, 12.7f, "imatges/barra.png");
		barra2 = new Barra(world, 50.66f, 2.16f, 55.3f, 50.67f, "imatges/barraDoble.png");
		barra3 = new Barra(world,  59.66f, 2.16f, 55.6f, 59.65f, "imatges/barraDoble.png");

		monstre = new Monstre(world, 4.0f, 2.0f);
	}

    /**
     * Moure la càmera en funció de la posició del personatge
     */
	private void moureCamera() {
		// Posicionem la camera centran-la on hi hagi l'sprite del protagonista
		tiledMapHelper.getCamera().position.x = JocDeTrons.PIXELS_PER_METRE
				* personatge.getPositionBody().x;
		//Centrem la camara verticalment al personatge, permetent que la camara es mogui en vertical
 		tiledMapHelper.getCamera().position.y = JocDeTrons.PIXELS_PER_METRE
				* personatge.getPositionBody().y;

 		// Assegurar que la camera nomes mostra el mapa i res mes
		if (tiledMapHelper.getCamera().position.x <  joc.getScreenWidth() / 2) {
			tiledMapHelper.getCamera().position.x =  joc.getScreenWidth()/ 2;
		}
		if (tiledMapHelper.getCamera().position.x >= tiledMapHelper.getWidth()
				-  joc.getScreenWidth()/ 2) {
			tiledMapHelper.getCamera().position.x = tiledMapHelper.getWidth()
					- joc.getScreenWidth()/ 2;
		}

		if (tiledMapHelper.getCamera().position.y < joc.getScreenHeight() / 2) {
			tiledMapHelper.getCamera().position.y = joc.getScreenHeight()/ 2;
		}
		if (tiledMapHelper.getCamera().position.y >= tiledMapHelper.getHeight()
				- joc.getScreenHeight() / 2) {
			tiledMapHelper.getCamera().position.y = tiledMapHelper.getHeight()
					- joc.getScreenHeight() / 2;
		}
		// actualitzar els nous valors de la càmera
		tiledMapHelper.getCamera().update();
	}
/*
    @Override
    public boolean keyUp(int keycode) {
        personatge.inicialitzarMoviments();
        if(keycode == Input.Keys.DPAD_RIGHT) {
            personatge.setMoureDreta(true);
        } else if(keycode == Input.Keys.DPAD_LEFT) {
            personatge.setMoureEsquerra(true);
        } else if(keycode == Input.Keys.DPAD_UP) {
            personatge.setFerSalt(true);
        }

        //personatge.moure();
        //personatge.updatePosition();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        personatge.inicialitzarMoviments();

        if (screenX > Gdx.graphics.getWidth() * 0.80f) {
            personatge.setMoureDreta(true);
        } else if(screenX > Gdx.graphics.getWidth() * 0.20f) {
            personatge.setMoureEsquerra(true);
        } else if(screenY < Gdx.graphics.getHeight() * 0.20f) {
            personatge.setFerSalt(true);
        }
        //personatge.moure();
        //personatge.updatePosition();
        return true;
    }
    */
    /**
     * tractar els events de l'entrada
     */
	private void tractarEventsEntrada() {
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
			personatge.setMoureDreta(true);
		} else {
			for (int i = 0; i < 2; i++) {
				if (Gdx.input.isTouched(i)
						&& Gdx.input.getX() > Gdx.graphics.getWidth() * 0.80f) {
					personatge.setMoureDreta(true);
				}
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
			personatge.setMoureEsquerra(true);
		} else {
			for (int i = 0; i < 2; i++) {
				if (Gdx.input.isTouched(i)
						&& Gdx.input.getX() < Gdx.graphics.getWidth() * 0.20f) {
					personatge.setMoureEsquerra(true);
				}
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
			personatge.setFerSalt(true);
		} else {
			for (int i = 0; i < 2; i++) {
				if (Gdx.input.isTouched(i)
						&& Gdx.input.getY() < Gdx.graphics.getHeight() * 0.20f) {
					personatge.setFerSalt(true);
				}
			}
		}
	}

    /**
     * comprovar les mides de la pantalla
     */
	private void comprovarMidesPantalla() {
		/**
		 * Si la mida de la finestra de dibuix no està definida, la
		 * inicialitzem
		 */
		if (joc.getScreenWidth() == -1) {
			joc.setScreenWidth(JocDeTrons.WIDTH);
			joc.setScreenHeight(JocDeTrons.HEIGHT);
		}
	}


	/**
	 * Carrega el mapa del joc a partir d'un fitxer TMX
	 */
	private void carregarMapa() {
		tiledMapHelper = new TiledMapHelper();
		tiledMapHelper.setPackerDirectory("world/level1/packer");
		tiledMapHelper.loadMap("world/level1/packer/level.tmx");
		tiledMapHelper.prepareCamera(joc.getScreenWidth(),
				joc.getScreenHeight());
	}

	/**
	 * Carregar i reproduir l'arxiu de música de fons
	 */
	public void carregarMusica() {
		musica = Gdx.audio.newMusic(Gdx.files
				.internal("sons/gameOfThrones.mp3"));
		musica.setLooping(true);
		musica.setVolume(0.5f);
		musica.play();
	}

	/**
	 * Càrrega dels objectes que defineixen les col·lisions
	 */
	private void carregarObjectes() {
		MapBodyManager mapBodyManager = new MapBodyManager(world,
				JocDeTrons.PIXELS_PER_METRE,
				Gdx.files.internal("world/level1/materials.json"), 1);
		mapBodyManager.createPhysics(tiledMapHelper.getMap(), "Box2D");
	}

	// ----------------------------------------------------------------------------------
	// MÈTODES SOBREESCRITS DE AbstractScreen
	// ----------------------------------------------------------------------------------

	@Override
	public void render(float delta) {
		 personatge.inicialitzarMoviments();
		 tractarEventsEntrada();
	     personatge.moure();
         personatge.updatePosition();

		monstre.updatePosition();
		monstre.moure();

		barra.inicialitzarMoviments();
		barra.moure();
		barra.updatePosition();

		barra2.inicialitzarMoviments();
		barra2.moure();
		barra2.updatePosition();

		barra3.inicialitzarMoviments();
		barra3.moure();
		barra3.updatePosition();

        /**
         * Cal actualitzar les posicions i velocitats de tots els objectes. El
         * primer paràmetre és la quanitat de frames/segon que dibuixaré
         * El segon i tercer paràmetres indiquen la quantitat d'iteracions per
         * la velocitat i per tractar la posició. Un valor alt és més
         * precís però més lent.
         */
		world.step(Gdx.app.getGraphics().getDeltaTime(), 6, 2);

		/*
		 * per destruir cossos marcats per ser eliminats
		 */
        /*	for(int i = bodyDestroyList.size()-1; i >=0; i-- ) {
		    	world.destroyBody(bodyDestroyList.get(i));
		}
		bodyDestroyList.clear();
        */

		// Esborrar la pantalla
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Color de fons marro
		Gdx.gl.glClearColor(185f / 255f, 122f / 255f, 87f / 255f, 0);

		title2.setText("Vides: " +(String.valueOf(personatge.getVides())));

		moureCamera();
		// pintar el mapa
		tiledMapHelper.render();
		// Preparar l'objecte SpriteBatch per dibuixar la resta d'elements
		batch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
		// iniciar el lot
		batch.begin();
		personatge.dibuixar(batch);
		monstre.dibuixar(batch);
		barra.dibuixar(batch);
		barra2.dibuixar(batch);
		//barra3.dibuixar(batch);
	    	// finalitzar el lot: a partir d'aquest moment es dibuixa tot el que
		    // s'ha indicat entre begin i end
		batch.end();

        // dibuixar els controls de pantalla
        stage.act();
        stage.draw();

        debugRenderer.render(world, tiledMapHelper.getCamera().combined.scale(
				JocDeTrons.PIXELS_PER_METRE, JocDeTrons.PIXELS_PER_METRE,
				JocDeTrons.PIXELS_PER_METRE));

		if (personatge.getVides() == 0){
			joc.setScreen(new MainMenuScreen(joc));
		}
		else if (personatge.getVides() != vides){
			vides = personatge.getVides();
			joc.setScreen(new MainScreen(joc, vides));
		}
	}

	@Override
	public void dispose() {
		musica.stop();
		musica.dispose();
		world.dispose();
		personatge.dispose();
	}

	public void show() {
		// Els elements es mostren en l'ordre que s'afegeixen.
		// El primer apareix a la part superior, el darrer a la part inferior.

		table2.center().top().right();
		table.center().top();
		table.add(title).padTop(5);
		table2.add(title2).padTop(5).padRight(5);

		//cell2 = table.add(title2).padTop(5);
		table.setFillParent(true);
		table2.setFillParent(true);
		stage.addActor(table);
		stage.addActor(table2);
	}
}
