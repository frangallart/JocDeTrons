package com.mygdx.game.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.Barra;
import com.mygdx.game.BolesFocMonstre;
import com.mygdx.game.GestorContactes;
import com.mygdx.game.JocDeTrons;
import com.mygdx.game.MapBodyManager;
import com.mygdx.game.Monstre;
import com.mygdx.game.MonstreLava;
import com.mygdx.game.Personatge;
import com.mygdx.game.TiledMapHelper;
import com.mygdx.game.Vides;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Una pantalla del joc
 *
 * @author Marc
 *
 */
public class Level1 extends AbstractScreen {

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
    private Label title, labelVides, labelPunts, labelNomJugador;

	private Table table, table2, table3;

	private int vides;

	private ArrayList<Monstre> monstres;
	private ArrayList<MonstreLava> monstresLava;
	private ArrayList<BolesFocMonstre> bolesFocMonstres;
	private Vides cor;

	private String pathTexturaPj, pathImgPj;

	/**
     * per indicar quins cossos s'han de destruir
     * @param joc
     */
    private ArrayList<Body> bodyDestroyList;


	public Level1(JocDeTrons joc, int vides, String pathTexturaPj, String pathImgPj, String nomJugador) {
		super(joc);
        // carregar el fitxer d'skins
        skin = new Skin(Gdx.files.internal("skins/skin.json"));
        title = new Label(joc.getTitol(),skin, "groc");
		labelVides = new Label("",skin, "groc");
		labelPunts = new Label("",skin, "groc");
		labelNomJugador = new Label(nomJugador, skin, "groc");

		this.pathTexturaPj = pathTexturaPj;
		this.pathImgPj = pathImgPj;

		table = new Table();
		table2 = new Table();
		table3 = new Table();
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
		bodyDestroyList= new ArrayList<Body>();
		//world.setContactListener(new GestorContactes(bodyDestroyList));
		//world.setContactListener(new GestorContactes());

		// crear el personatge
        personatge = new Personatge(world, this.pathTexturaPj, this.pathImgPj);

		monstres = new ArrayList<Monstre>();
		monstres.add(new Monstre(world, "monstre1", 6.0f, 2.0f, 6.6f, 5.45f));
		monstres.add(new Monstre(world, "monstre2", 17.0f, 3.0f, 17.2f, 15.8f));

		monstresLava = new ArrayList<MonstreLava>();
		monstresLava.add(new MonstreLava(world, "monstreLava1", 50.64f, 0.64f, false));
		monstresLava.add(new MonstreLava(world, "monstreLava2", 60.15f, 0.64f, true));

		bolesFocMonstres = new ArrayList<BolesFocMonstre>();
		bolesFocMonstres.add(new BolesFocMonstre(world, "Lava1", 50.44f, 1f, 2f, false));
		bolesFocMonstres.add(new BolesFocMonstre(world, "Lava2", 60.15f, 1f, 1.8f, false));

		personatge.setVides(vides);
		cor = new Vides(world, "Vida", 87.99f, 6.0f);
		world.setContactListener(new GestorContactes(bodyDestroyList, personatge, monstres, monstresLava, bolesFocMonstres));

		this.vides = vides;

        // objecte que permet debugar les col·lisions
		debugRenderer = new Box2DDebugRenderer();

		barra = new Barra(world, 12.6f, 1.3f,15.0f, 12.7f, "imatges/barra.png");
		barra2 = new Barra(world, 50.69f, 2.16f, 55.3f, 50.7f, "imatges/barraDoble.png");
		barra3 = new Barra(world,  60.41f, 2.16f, 60.40f, 55.8f, "imatges/barraDoble.png");

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
        	for(int i = bodyDestroyList.size()-1; i >=0; i-- ) {

				for (int j = 0; j < monstres.size(); j++) {
					if (bodyDestroyList.get(i).getUserData() == monstres.get(j).getNom()) {
						monstres.get(j).dispose();
						personatge.setPunts(personatge.getPunts() + monstres.get(j).getPUNTS());
						monstres.remove(j);
						break;
					}
				}

				for (int lava = 0; lava < bolesFocMonstres.size(); lava++) {
					if (bodyDestroyList.get(i).getUserData() == bolesFocMonstres.get(lava).getNom()) {
						bolesFocMonstres.get(lava).dispose();
						if (bodyDestroyList.get(i).getUserData().equals("Lava1")) {
							bolesFocMonstres.add(new BolesFocMonstre(world, bolesFocMonstres.get(lava).getNom(), 50.44f, 1f, 2f, false));
						} else if (bodyDestroyList.get(i).getUserData().equals("Lava2")) {
							bolesFocMonstres.add(new BolesFocMonstre(world, bolesFocMonstres.get(lava).getNom(), 60.15f, 1f, 1.8f, false));
						}
						bolesFocMonstres.remove(lava);
						break;
					}
				}

				if (bodyDestroyList.get(i).getUserData().equals("Vida")) {
					cor = null;
					//personatge.setVides(personatge.getVides() + 1);
					//this.vides = personatge.getVides();
					//break;
				}
				world.destroyBody(bodyDestroyList.get(i));
			}
			bodyDestroyList.clear();

		// Esborrar la pantalla
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Color de fons marro
		Gdx.gl.glClearColor(185f / 255f, 122f / 255f, 87f / 255f, 0);

		labelVides.setText("Vides: " + (String.valueOf(personatge.getVides())));
		labelPunts.setText("Punts: " + (String.valueOf(personatge.getPunts())));


		moureCamera();
		// pintar el mapa
		tiledMapHelper.render();
		// Preparar l'objecte SpriteBatch per dibuixar la resta d'elements
		batch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
		// iniciar el lot
		batch.begin();
		personatge.dibuixar(batch);

		for(Iterator<Monstre> i = monstres.iterator(); i.hasNext(); ) {
			Monstre item = i.next();
			item.dibuixar(batch);
			item.updatePosition();
			item.moure();
		}

		for(Iterator<MonstreLava> i = monstresLava.iterator(); i.hasNext(); ) {
			MonstreLava items = i.next();
			items.dibuixar(batch);
			items.updatePosition();
			items.moure();
		}

		if (personatge.getPositionBody().x > 49.5f && personatge.getPositionBody().x < 61.5f) {

			for (Iterator<BolesFocMonstre> i = bolesFocMonstres.iterator(); i.hasNext(); ) {
				BolesFocMonstre items = i.next();
				items.dibuixar(batch);
				items.updatePosition();
				items.moure();
			}
		}

		if (cor != null) {
			cor.dibuixar(batch);
			cor.updatePosition();
			cor.moure();
		}

		barra.dibuixar(batch);
		barra2.dibuixar(batch);
		barra3.dibuixar(batch);
	    	// finalitzar el lot: a partir d'aquest moment es dibuixa tot el que
		    // s'ha indicat entre begin i end
		batch.end();


        // dibuixar els controls de pantalla
				stage.act();
				stage.draw();

				debugRenderer.render(world, tiledMapHelper.getCamera().combined.scale(
						JocDeTrons.PIXELS_PER_METRE, JocDeTrons.PIXELS_PER_METRE,
				JocDeTrons.PIXELS_PER_METRE));

		if (personatge.getPositionBody().x > 96f){
			joc.setScreen(new NextLevel(joc, personatge, "Nivell 1", labelNomJugador.getText().toString()));//new Level2(joc,vides));
		}else {

			if (personatge.getPositionBody().y < 0.38) {
				personatge.setVides(personatge.getVides() - 1);
				joc.setScreen(new Level1(joc, vides, personatge.getPathTextura(), personatge.getPathImatge(), labelNomJugador.getText().toString()));
			}

			if (personatge.getVides() == 0) {
				joc.setScreen(new MainMenuScreen(joc));
			} else if (personatge.getVides() < vides) {
				vides = personatge.getVides();
				joc.setScreen(new Level1(joc, vides, personatge.getPathTextura(), personatge.getPathImatge(), labelNomJugador.getText().toString()));
			}
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
		table2.add(labelVides).padTop(5).padRight(5).row();
		table2.add(labelPunts).padTop(20).padRight(5).row();


		table3.center().top().left();
		table3.add(labelNomJugador).padTop(5).padLeft(5).row();

		//cell2 = table.add(title2).padTop(5);
		table.setFillParent(true);
		table2.setFillParent(true);
		stage.addActor(table);
		stage.addActor(table2);
	}
}
