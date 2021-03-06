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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.Barra;
import com.mygdx.game.BolesFocMonstre;
import com.mygdx.game.Clau;
import com.mygdx.game.GestorContactes;
import com.mygdx.game.JocDeTrons;
import com.mygdx.game.MapBodyManager;
import com.mygdx.game.Monstre;
import com.mygdx.game.MonstreEstatic;
import com.mygdx.game.Personatge;
import com.mygdx.game.TiledMapHelper;
import com.mygdx.game.Vides;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Primer nivell del joc
 */
public class Level1 extends AbstractScreen {

	/**
	 * Variable d'instancia que permet gestionar i pintar el mapa a partir d'un
	 * TiledMap (TMX)
	 */
	private TiledMapHelper tiledMapHelper;

	// objecte que gestiona el protagonista del joc
	// ---->private PersonatgeBackup personatge;
	private Personatge personatge;

	private Barra barra, barra2, barra3;
	/**
	 * Objecte que conté tots els cossos del joc als quals els aplica la
	 * simulació
	 */
	private World world;

	/**
	 * Objecte que dibuixa elements per debugar. Dibuixa linies al voltant dels
	 * límits de les col·lisions. Va molt bé per comprovar que les
	 * col·lisions són les que desitgem. Cal tenir present, però, que és
	 * més lent. Només s'ha d'utitilitzar per debugar.
	 */
	//private Box2DDebugRenderer debugRenderer;

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
	private Label title, labelVides, labelPunts, labelNoPassarNivell;

	private Table table, table2, table3, table4;

	private Texture textVida;

	private int vides, punts;
	private boolean calculVides;

	private ArrayList<Monstre> monstres;
	private ArrayList<MonstreEstatic> monstresLava;
	private ArrayList<BolesFocMonstre> bolesFocMonstres;
	private Vides cor;
	private Clau clau;

	/**
	 * per indicar quins cossos s'han de destruir
	 * @param joc
	 */
	private ArrayList<Body> bodyDestroyList;

	private boolean vidaVista;

	public Level1(JocDeTrons joc, Personatge persona){
		super(joc);

		// carregar el fitxer d'skins
		title = new Label(joc.getTitol(),joc.getSkin());
		labelVides = new Label("",joc.getSkin());
		labelPunts = new Label("",joc.getSkin());
		labelNoPassarNivell = new Label("",joc.getSkin());

		textVida = new Texture(
				Gdx.files.internal("imatges/corSol.png"));

		this.vidaVista = false;

		table = new Table();
		table2 = new Table();
		table3 = new Table();
		table4 = new Table();
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

		// crear el personatge
		personatge = new Personatge(world, persona.getVides(), persona.getPunts(), persona.getPathTextura(), persona.getPathImatge(), persona.getPathImatgeE(), persona.getPathImatgeAtac(), persona.getPosX(), persona.getPosY(), persona.getPes());

		monstres = new ArrayList<Monstre>();
		monstres.add(new Monstre(world, "monstre1", 6.0f, 1.33f, 6.6f, 5.45f, "imatges/serp.png", 4, 2, 50));
		monstres.add(new Monstre(world, "monstre2", 17.0f, 2.0f, 17.2f, 15.8f, "imatges/serp.png", 4, 2, 50));
		monstres.add(new Monstre(world, "monstre3", 81.5f, 2.0f, 85.3f, 80.46f,2.5f, "imatges/serp.png", 4, 2, 50));

		monstresLava = new ArrayList<MonstreEstatic>();
		monstresLava.add(new MonstreEstatic(world, "monstreLava1", 50.64f, 0.64f, false, "imatges/lavaMonster.png"));
		monstresLava.add(new MonstreEstatic(world, "monstreLava2", 60.15f, 0.64f, true, "imatges/lavaMonster.png"));

		bolesFocMonstres = new ArrayList<BolesFocMonstre>();

		clau = new Clau(world, "clau", 58.36f, 3.50f, "imatges/clau.png");

		barra = new Barra(world, 12.6f, 1.3f,15.15f, 12.85f, "imatges/barra.png");
		barra2 = new Barra(world, 50.69f, 2.16f, 55.3f, 50.7f, "imatges/barraDoble.png");
		barra3 = new Barra(world,  60.41f, 2.16f, 60.40f, 55.8f, "imatges/barraDoble.png");

		world.setContactListener(new GestorContactes(bodyDestroyList, personatge, monstres, monstresLava, bolesFocMonstres, null));

		this.vides = persona.getVides();
		this.punts = 0;
		this.calculVides = true;

		// objecte que permet debugar les col·lisions
		//debugRenderer = new Box2DDebugRenderer();
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

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			personatge.setFerAtac(true);
		}else{
			for (int i = 0; i < 2; i++) {

				if (Gdx.input.isTouched(i)
						&& Gdx.input.getY() > Gdx.graphics.getHeight() * 0.80f) {
					personatge.setFerAtac(true);
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
	int boles = 0;
	@Override
	public void render(float delta) {

		personatge.inicialitzarMoviments();
		tractarEventsEntrada();
		personatge.moure();
		personatge.updatePosition();

		barra.moure();
		barra.updatePosition();

		barra2.moure();
		barra2.updatePosition();

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
					personatge.setPunts(personatge.getPunts() + monstres.get(j).getPunts());
					monstres.remove(j);
					break;
				}
			}

			for (int lava = 0; lava < bolesFocMonstres.size(); lava++) {
				if (bodyDestroyList.get(i).getUserData() == bolesFocMonstres.get(lava).getNom()) {
					bolesFocMonstres.get(lava).dispose();
					bolesFocMonstres.remove(lava);
					break;
				}
			}

			if (bodyDestroyList.get(i).getUserData().equals("Vida")) {
				cor = null;
				personatge.setVides(personatge.getVides() + 1);
				calculVides = true;
				this.vides = personatge.getVides();
			}

			if (bodyDestroyList.get(i).getUserData().equals("clau")) {
				clau = null;
				personatge.setPassarNivell(true);
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
			item.moure();
			item.updatePosition();
			item.dibuixar(batch);
		}

		for(Iterator<MonstreEstatic> i = monstresLava.iterator(); i.hasNext(); ) {
			MonstreEstatic items = i.next();
			items.moure();
			items.updatePosition();
			items.dibuixar(batch);
		}

		if (personatge.getPositionBody().x > 49.5f && personatge.getPositionBody().x < 61.5f) {
			if (bolesFocMonstres.size()< 2) {
				if (bolesFocMonstres.size() == 1) {
					Random rand = new Random();

					if (rand.nextInt() % 2 == 0) {
						bolesFocMonstres.add(new BolesFocMonstre(world, "Dreta" + boles, 50.58f, 1.3f, 2f, false, "imatges/bolaLava.png"));
						boles++;
					}else{
						bolesFocMonstres.add(new BolesFocMonstre(world, "Esque" + boles, 60.15f, 1.3f, 1.8f, false, "imatges/bolaLava.png"));
						boles++;
					}
				}else if (bolesFocMonstres.size() == 0){
					bolesFocMonstres.add(new BolesFocMonstre(world, "Dreta" + boles, 50.58f, 1.3f, 2f, false, "imatges/bolaLava.png"));
					boles++;
					bolesFocMonstres.add(new BolesFocMonstre(world, "Esque" + boles, 60.15f, 1.3f, 1.8f, false, "imatges/bolaLava.png"));
					boles++;
				}
			}
			for (Iterator<BolesFocMonstre> i = bolesFocMonstres.iterator(); i.hasNext(); ) {
				BolesFocMonstre items = i.next();
				items.moure();
				items.updatePosition();
				items.dibuixar(batch);
			}
		}

		if (personatge.getPositionBody().x > 86 && !vidaVista) {
			cor = new Vides(world, "Vida", 87.99f, 6.0f);
			vidaVista = true;
		}
		if (cor != null) {
			cor.moure();
			cor.updatePosition();
			cor.dibuixar(batch);
		}

		if (clau != null) {
			clau.moure();
			clau.updatePosition();
			clau.dibuixar(batch);
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

		/*debugRenderer.render(world, tiledMapHelper.getCamera().combined.scale(
				JocDeTrons.PIXELS_PER_METRE, JocDeTrons.PIXELS_PER_METRE,
				JocDeTrons.PIXELS_PER_METRE));*/

		if (calculVides) {
			table2.clear();
			for (int vides = 0; vides < personatge.getVides(); vides++) {
				table2.add(new Image(textVida)).padTop(5).padRight(5);
			}
			calculVides = false;
		}

		if (personatge.getPositionBody().x > 46f && personatge.getPositionBody().x < 47f){
			labelNoPassarNivell.setText("Punt de control");
			personatge.setPosX(46.5f);
			this.punts = personatge.getPunts();
		}
		else{
			labelNoPassarNivell.setText("");
		}

		if (personatge.getPositionBody().x > 96f){
			if ( personatge.isPassarNivell()) {
				personatge.setPosX(Personatge.POS_INICIAL_X);
				personatge.setPosY(Personatge.POS_INICIAL_Y);
				joc.setScreen(new NextLevel(joc, personatge, "Nivell 1"));//new Level2(joc,vides));
			}else{
				labelNoPassarNivell.setText("No has recollit la clau!");
			}
		}else {
			if (personatge.getPositionBody().y < 0.38) {
				personatge.setVides(personatge.getVides() - 1);
				personatge.setPunts(punts);
				joc.setScreen(new Level1(joc,personatge));
			}

			if (personatge.getVides() == 0) {
				joc.setScreen(new GameOver(joc, personatge));
			} else if (personatge.getVides() < vides) {
				vides = personatge.getVides();
				personatge.setPunts(punts);
				joc.setScreen(new Level1(joc,personatge));
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

		table.left().top();
		table.add(title).padTop(5).padLeft(15);
		//table2.add(labelVides).padTop(5).padRight(5).row();

		table2.top().right().padTop(10);

		table3.top().right().padTop(30);
		table3.add(labelPunts).padTop(30).padRight(5);

		table4.center();
		table4.add(labelNoPassarNivell).row();

		table.setFillParent(true);
		table2.setFillParent(true);
		table3.setFillParent(true);
		table4.setFillParent(true);
		stage.addActor(table);
		stage.addActor(table2);
		stage.addActor(table3);
		stage.addActor(table4);
	}
}