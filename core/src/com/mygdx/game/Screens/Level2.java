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
import com.mygdx.game.Ascensor;
import com.mygdx.game.BolesFocMonstre;
import com.mygdx.game.Drac;
import com.mygdx.game.GestorContactes;
import com.mygdx.game.JocDeTrons;
import com.mygdx.game.MapBodyManager;
import com.mygdx.game.Monstre;
import com.mygdx.game.MonstreEstatic;
import com.mygdx.game.Personatge;
import com.mygdx.game.TiledMapHelper;
import com.mygdx.game.Troncs;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Arnau on 25/04/2015.
 */
public class Level2 extends AbstractScreen {



    /**
     * Variable d'instancia que permet gestionar i pintar el mapa a partir d'un
     * TiledMap (TMX)
     */
    private TiledMapHelper tiledMapHelper;


    private Ascensor ascensor;
    /**
     * Objecte que cont? tots els cossos del joc als quals els aplica la
     * simulaci?
     */
    private World world;

    /**
     * Objecte que dibuixa elements per debugar. Dibuixa linies al voltant dels
     * l?mits de les col?lisions. Va molt b? per comprovar que les
     * col?lisions s?n les que desitgem. Cal tenir present, pe`ro, que ?s
     * m?s lent. Nom?s s'ha d'utitilitzar per debugar.
     */
    private Box2DDebugRenderer debugRenderer;

    /**
     * Musica i sons
     */
    private Music musica;

    /**
     * Per debugar les col�lisions
     */
    private Box2DDebugRenderer box2DRenderer;

    /**
     * Per mostrar el t�tol
     */
    private Label title, labelVides, labelPunts;

    private Table table, table2;

    private int vides;

    private ArrayList<Monstre> monstres;
    private ArrayList<BolesFocMonstre> bolesDrac;
    private ArrayList<Troncs> troncs;

    private Texture splashTexture;
    private Image splashImage;
    private Personatge personatge;
    private MonstreEstatic noia;
    private Drac drac;

    /**
     * per indicar quins cossos s'han de destruir
     * @param joc
     */
    private ArrayList<Body> bodyDestroyList;


    public Level2(JocDeTrons joc, Personatge personatge) {
        super(joc);
        // carregar el fitxer d'joc.getSkin()s
        title = new Label(joc.getTitol(),joc.getSkin());
        labelVides = new Label("",joc.getSkin());
        labelPunts = new Label("",joc.getSkin());

        table = new Table();
        table2 = new Table();
		/*
		 * Crear el mon on es desenvolupa el joc. S'indica la gravetat: negativa
		 * perqu� indica cap avall
		 */
        world = new World(new Vector2(0.0f, -9.8f), true);
        comprovarMidesPantalla();
        carregarMapa();
        carregarObjectes();
        carregarMusica();

        // --- si es volen destruir objectes, descomentar ---
        bodyDestroyList= new ArrayList<Body>();

        // crear el personatge
        this.personatge = new Personatge(world, personatge.getVides(), personatge.getPunts(), personatge.getPathTextura(), personatge.getPathImatge(), personatge.getPathImatgeE(), personatge.getPathImatgeAtac());

        monstres = new ArrayList<Monstre>();
        monstres.add(new Monstre(world, "gegant1", 37.0f, 0.66f, 46f, 37.1f, "imatges/gegant.png","imatges/gegant.png", 6, 2));
        monstres.add(new Monstre(world, "gegant2", 72.66f, 0.66f, 77.66f, 72.67f,"imatges/whiteWalker.png", "imatges/gegant.png", 6, 2));
        monstres.add(new Monstre(world, "gegant3", 109f, 0.66f, 111f, 109.1f, "imatges/whiteWalker.png" , "imatges/gegant.png", 6, 2));
        monstres.add(new Monstre(world, "gegant4", 144.33f, 1.0f, 147f, 144.34f, "imatges/whiteWalker.png", "imatges/gegant.png", 6, 2));
        monstres.add(new Monstre(world, "gegant5", 156.33f, 1.66f, 160.33f, 156.34f, "imatges/gegant.png", "imatges/gegant.png", 6, 2));
        monstres.add(new Monstre(world, "caminant1", 122f, 2.33f, 127.33f, 122.1f, "imatges/whiteWalker.png", "imatges/whiteWalker.png", 6, 2));
        monstres.add(new Monstre(world, "caminant2", 169.33f, 1.0f, 172.33f, 169.34f, "imatges/whiteWalker.png", "imatges/whiteWalker.png", 6, 2));
        monstres.add(new Monstre(world, "caminant3", 203.33f, 1.33f, 210f, 203.34f, 1.5f, "imatges/whiteWalker.png", "imatges/whiteWalker.png", 6, 2));
        monstres.add(new Monstre(world, "caminant4", 236.33f, 1.33f, 240f, 236.34f, 1.5f, "imatges/whiteWalker.png", "imatges/whiteWalker.png", 6, 2));
        monstres.add(new Monstre(world, "caminant5", 249.66f, 2.66f, 252.33f, 249.67f, "imatges/whiteWalker.png", "imatges/whiteWalker.png", 6, 2));
        monstres.add(new Monstre(world, "caminant6", 266.66f, 1.0f, 271f, 266.67f, "imatges/whiteWalker.png" , "imatges/whiteWalker.png", 6, 2));
        monstres.add(new Monstre(world, "caminant7", 290.66f, 1.0f, 294f, 290.67f, "imatges/whiteWalker.png" ,"imatges/whiteWalker.png", 6, 2));

        drac = new Drac(world, "drac", 301.02f, 1.0f, 310, 301.01f,4f, "imatges/dracVolant.png" ,"imatges/drac.png", 12, 2);

        noia = new MonstreEstatic(world, "noia", 332.54f, 6.07f, true, "imatges/noiaNua.png");

        bolesDrac = new ArrayList<BolesFocMonstre>();
        bolesDrac.add(new BolesFocMonstre(world, "Gel", 301.02f, 3f, 5f, false));
        bolesDrac.add(new BolesFocMonstre(world, "Gel", 301.02f, 3f, 5f, false));

        this.vides = personatge.getVides();

        troncs = new ArrayList<Troncs>();
        troncs.add(new Troncs(world, "tronc1", 302.6f, 2.6f, "imatges/barra.png"));
        troncs.add(new Troncs(world, "tronc2", 303.7f, 2.6f, "imatges/barra.png"));

        world.setContactListener(new GestorContactes(bodyDestroyList, this.personatge, monstres, null, bolesDrac, troncs));


        // objecte que permet debugar les col·lisions
        debugRenderer = new Box2DDebugRenderer();

        /// Horitzontal: 49
        /// Vertical: 27 Arriba fins 5 1.66f

        ascensor = new Ascensor(world, 16.62f, 0.8f, 8.5f, 0.9f, "imatges/ascensor.png");


    }

    /**
     * Moure la c�mera en funci� de la posici� del personatge
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
        // actualitzar els nous valors de la c�mera
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

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            personatge.setFerAtac(true);
        }else{
            personatge.setFerAtac(false);
        }
    }

    /**
     * comprovar les mides de la pantalla
     */
    private void comprovarMidesPantalla() {
        /**
         * Si la mida de la finestra de dibuix no est� definida, la
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
        tiledMapHelper.loadMap("world/level1/packer/level2.tmx");
        tiledMapHelper.prepareCamera(joc.getScreenWidth(),
                joc.getScreenHeight());
    }

    /**
     * Carregar i reproduir l'arxiu de m�sica de fons
     */
    public void carregarMusica() {
        musica = Gdx.audio.newMusic(Gdx.files
                .internal("sons/gameOfThrones.mp3"));
        musica.setLooping(true);
        musica.setVolume(0.5f);
        musica.play();
    }

    /**
     * C�rrega dels objectes que defineixen les col�lisions
     */
    private void carregarObjectes() {
        MapBodyManager mapBodyManager = new MapBodyManager(world,
                JocDeTrons.PIXELS_PER_METRE,
                Gdx.files.internal("world/level1/materials.json"), 1);
        mapBodyManager.createPhysics(tiledMapHelper.getMap(), "Box2D");
    }

    // ----------------------------------------------------------------------------------
    // M�TODES SOBREESCRITS DE AbstractScreen
    // ----------------------------------------------------------------------------------

    @Override
    public void render(float delta) {

        personatge.inicialitzarMoviments();
        tractarEventsEntrada();
        personatge.moure();
        personatge.updatePosition();


        ascensor.inicialitzarMoviments();
        ascensor.moure();
        ascensor.updatePosition();

        /**
         * Cal actualitzar les posicions i velocitats de tots els objectes. El
         * primer par�metre �s la quanitat de frames/segon que dibuixar�
         * El segon i tercer par�metres indiquen la quantitat d'iteracions per
         * la velocitat i per tractar la posici�. Un valor alt �s m�s
         * prec�s per� m�s lent.
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

            for (int j = 0; j < bolesDrac.size(); j++) {
                if (bodyDestroyList.get(i).getUserData() == bolesDrac.get(j).getNom()) {
                    bolesDrac.get(j).dispose();
                    bolesDrac.remove(j);
                    break;
                }
            }

            for (int j = 0; j < troncs.size(); j++) {
                if (bodyDestroyList.get(i).getUserData() == troncs.get(j).getNom()) {
                    troncs.get(j).dispose();
                    troncs.remove(j);
                    break;
                }
            }

            world.destroyBody(bodyDestroyList.get(i));
        }
        bodyDestroyList.clear();

        for (int j = 0; j < troncs.size(); j++) {
            if (troncs.get(j).getDestruir() == 1){

            }
            if (troncs.get(j).getDestruir() == 2 && !troncs.get(j).isDestruit()) {
                //troncs.get(j).moure();
            }
        }

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
        this.personatge.dibuixar(batch);

        for(Iterator<Monstre> i = monstres.iterator(); i.hasNext(); ) {
            Monstre item = i.next();
            item.moure();
            item.updatePosition();
            item.dibuixar(batch);
        }

        for(Iterator<Troncs> i = troncs.iterator(); i.hasNext(); ) {
            Troncs item = i.next();
           // item.moure();
            item.updatePosition();
            item.dibuixar(batch);
        }

        noia.dibuixar(batch);
        noia.updatePosition();
        noia.moure();

        drac.moure(personatge);
        drac.updatePosition();
        drac.dibuixar(batch);

        ascensor.dibuixar(batch);

        if (personatge.getPositionBody().x > 301.02f && personatge.getPositionBody().x < 310f) {

            for (int i = 0; i < bolesDrac.size(); i++) {
                if (bolesDrac.get(i).getPositionBody().y > 3.0f) {
                    bolesDrac.get(i).dispose();
                    world.destroyBody(bolesDrac.get(i).getCos());
                    bolesDrac.remove(i);
                }else{
                    bolesDrac.get(i).moure();
                    bolesDrac.get(i).updatePosition();
                    bolesDrac.get(i).dibuixar(batch);
                }
            }
            if (drac.isAtacant() && bolesDrac.size() < 3) {
                bolesDrac.add(new BolesFocMonstre(world, "Gel", drac.getPositionBody().x + 1f, drac.getPositionBody().y + 0.7f, 2f, false));
            }
            drac.setAtacant(false);
        }

        // finalitzar el lot: a partir d'aquest moment es dibuixa tot el que
        // s'ha indicat entre begin i end
        batch.end();
        calculRedimensionat();
        // dibuixar els controls de pantalla
        stage.act();
        stage.draw();

        debugRenderer.render(world, tiledMapHelper.getCamera().combined.scale(
                JocDeTrons.PIXELS_PER_METRE, JocDeTrons.PIXELS_PER_METRE,
                JocDeTrons.PIXELS_PER_METRE));


        if (this.personatge.getPositionBody().x > 332.1f){
            joc.setScreen(new MainMenuScreen(joc));
        }else {
            if (personatge.getPositionBody().y < 0.38){
                personatge.setVides(personatge.getVides()-1);
                joc.setScreen(new Level2(joc, personatge));
            }

            if (this.personatge.getVides() == 0) {
                joc.setScreen(new MainMenuScreen(joc));
            } else if (this.personatge.getVides() != vides) {
                vides = this.personatge.getVides();
                joc.setScreen(new Level2(joc, this.personatge));
            }
        }
       // System.out.println(personatge.getPositionBody().x);
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


        table.center().top();
        table.add(title).padTop(5);
        table.setFillParent(true);


        table2.center().top().right();
        table2.add(labelVides).padTop(5).padRight(5).row();
        table2.add(labelPunts).padTop(20).padRight(5).row();
        table2.setFillParent(true);

        //cell2 = table.add(title2).padTop(5);

        stage.addActor(table);
        stage.addActor(table2);
    }
}
