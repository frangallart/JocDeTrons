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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.Ascensor;
import com.mygdx.game.Barra;
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
import com.mygdx.game.Vides;

import java.util.ArrayList;
import java.util.Iterator;

public class Level2 extends AbstractScreen {

    /**
     * Variable d'instancia que permet gestionar i pintar el mapa a partir d'un
     * TiledMap (TMX)
     */
    private TiledMapHelper tiledMapHelper;


    private Ascensor ascensor;

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
     * Música i sons
     */
    private Music musica;

    /**
     * Per debugar les col·lisions
     */
    //private Box2DDebugRenderer box2DRenderer;

    /**
     * Per mostrar el títol
     */
    private Label title, labelVides, labelPunts, labelPuntControl;

    private Table table, table2, table3, table4;

    private int vides, numBoles, punts;
    private boolean calculVides;

    private Texture textVida;

    private ArrayList<Monstre> monstres;
    private ArrayList<BolesFocMonstre> bolesDrac;
    private ArrayList<Troncs> troncs;

    private Personatge personatge;
    private MonstreEstatic noia;
    private Drac drac;
    private Barra barra;
    private Vides cor1, cor2;

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
        labelPuntControl = new Label("",joc.getSkin());

        textVida = new Texture(
                Gdx.files.internal("imatges/corSol.png"));

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
        this.personatge = new Personatge(world, personatge.getVides(), personatge.getPunts(), personatge.getPathTextura(), personatge.getPathImatge(), personatge.getPathImatgeE(), personatge.getPathImatgeAtac(), personatge.getPosX(), personatge.getPosY(), personatge.getPes());

        monstres = new ArrayList<Monstre>();
        monstres.add(new Monstre(world, "gegant1", 37.0f, 0.66f, 46f, 37.1f, "imatges/gegant.png", 6, 2, 150));
        monstres.add(new Monstre(world, "gegant2", 72.66f, 0.66f, 77.66f, 72.67f,"imatges/gegant.png", 6, 2, 150));
        monstres.add(new Monstre(world, "gegant3", 109f, 0.66f, 111f, 109.1f, "imatges/gegant.png", 6, 2, 150));
        monstres.add(new Monstre(world, "gegant4", 144.33f, 1.0f, 147f, 144.34f, "imatges/gegant.png", 6, 2, 150));
        monstres.add(new Monstre(world, "gegant5", 156.33f, 1.66f, 160.33f, 156.34f, "imatges/gegant.png", 6, 2, 150));
        monstres.add(new Monstre(world, "caminant1", 122f, 2.33f, 127.33f, 122.1f, "imatges/whiteWalker.png", 6, 2, 100));
        monstres.add(new Monstre(world, "caminant2", 169.33f, 1.0f, 172.33f, 169.34f, "imatges/whiteWalker.png", 6, 2, 100));
        monstres.add(new Monstre(world, "caminant3", 203.33f, 1.33f, 210f, 203.34f, 1.5f, "imatges/whiteWalker.png", 6, 2, 100));
        monstres.add(new Monstre(world, "caminant4", 236.33f, 1.33f, 240f, 236.34f, 1.5f, "imatges/whiteWalker.png", 6, 2, 100));
        monstres.add(new Monstre(world, "caminant5", 249.66f, 2.66f, 252.33f, 249.67f, "imatges/whiteWalker.png", 6, 2, 100));
        monstres.add(new Monstre(world, "caminant6", 266.66f, 1.0f, 271f, 266.67f, "imatges/whiteWalker.png", 6, 2, 100));
        monstres.add(new Monstre(world, "caminant7", 290.66f, 1.0f, 294f, 290.67f, "imatges/whiteWalker.png", 6, 2, 100));

        drac = new Drac(world, "drac", 300.8f, 0.3f, "imatges/dracVolant.png" ,"imatges/drac.png", 12, 2);

        noia = new MonstreEstatic(world, "noia", 332.54f, 6.07f, true, "imatges/noiaNua.png");

        barra = new Barra(world, 52.52f, 2.95f, 55.44f, 52.53f, "imatges/barra.png");

        cor1 = new Vides(world, "Vida1", 130.51f, 3.0f);
        cor2 = new Vides(world, "Vida2", 296.50f, 3.0f);

        bolesDrac = new ArrayList<BolesFocMonstre>();

        this.vides = personatge.getVides();
        this.punts = personatge.getPunts();

        troncs = new ArrayList<Troncs>();
        troncs.add(new Troncs(world, "tronc1", 304.6f, 0.7f, "imatges/torreFusta.png", "imatges/torreFustaCremada.png"));
        troncs.add(new Troncs(world, "tronc2", 307.7f, 0.7f, "imatges/torreFusta.png", "imatges/torreFustaCremada.png"));
        troncs.add(new Troncs(world, "tronc3", 310.7f, 0.7f, "imatges/torreFusta.png", "imatges/torreFustaCremada.png"));
        troncs.add(new Troncs(world, "tronc4", 313.7f, 0.7f, "imatges/torreFusta.png", "imatges/torreFustaCremada.png"));
        troncs.add(new Troncs(world, "tronc5", 316.7f, 0.7f, "imatges/torreFusta.png", "imatges/torreFustaCremada.png"));
        troncs.add(new Troncs(world, "tronc6", 319.7f, 0.7f, "imatges/torreFusta.png", "imatges/torreFustaCremada.png"));

        world.setContactListener(new GestorContactes(bodyDestroyList, this.personatge, monstres, null, bolesDrac, troncs));


        // objecte que permet debugar les col·lisions
        //debugRenderer = new Box2DDebugRenderer();

        ascensor = new Ascensor(world, 16.62f, 8.6f, 8.5f, 0.9f, "imatges/ascensor.png");

        this.numBoles = 0;
        this.calculVides = true;
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
        // actualitzar els nous valors de la c�mera
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
        tiledMapHelper.loadMap("world/level1/packer/level2.tmx");
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
    // MèTODES SOBREESCRITS DE AbstractScreen
    // ----------------------------------------------------------------------------------
    @Override
    public void render(float delta) {
        personatge.inicialitzarMoviments();
        tractarEventsEntrada();
        personatge.moure();
        personatge.updatePosition();

        ascensor.moure();
        ascensor.updatePosition();

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

            if (bodyDestroyList.get(i).getUserData().equals("Vida1")) {
                cor1 = null;
                personatge.setVides(personatge.getVides() + 1);
                calculVides = true;
                this.vides = personatge.getVides();
            }
            if (bodyDestroyList.get(i).getUserData().equals("Vida2")) {
                cor2 = null;
                personatge.setVides(personatge.getVides() + 1);
                calculVides = true;
                this.vides = personatge.getVides();
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
        this.personatge.dibuixar(batch);

        for(Iterator<Monstre> i = monstres.iterator(); i.hasNext(); ) {
            Monstre item = i.next();
            item.moure();
            item.updatePosition();
            item.dibuixar(batch);
        }

        for(Iterator<Troncs> i = troncs.iterator(); i.hasNext(); ) {
            Troncs item = i.next();
            item.updatePosition();
            item.dibuixar(batch);
        }

        noia.moure();
        noia.updatePosition();
        noia.dibuixar(batch);

        drac.moure(personatge);
        drac.updatePosition();
        drac.dibuixar(batch);

        ascensor.dibuixar(batch);

        if (personatge.getPositionBody().x > 301.02f && personatge.getPositionBody().x < 323.3f) {
            if (drac.isAtacant() && bolesDrac.size() < 3) {
                drac.getFoc().play();
                bolesDrac.add(new BolesFocMonstre(world, "BolaGel" + numBoles, drac.getPositionBody().x + 1.6f, drac.getPositionBody().y - 0.6f, 0f, false, "imatges/bolaGel.png"));
                numBoles++;
                bolesDrac.add(new BolesFocMonstre(world, "BolaGel" + numBoles, drac.getPositionBody().x + 1.8f, drac.getPositionBody().y + -0.3f, 0f, false, "imatges/bolaGel.png"));
                numBoles++;
                bolesDrac.add(new BolesFocMonstre(world, "BolaGel" + numBoles, drac.getPositionBody().x + 1.4f, drac.getPositionBody().y + 0.0f, 0f, false, "imatges/bolaGel.png"));
                numBoles++;
            }

            for (int i = 0; i < bolesDrac.size(); i++) {
                if (bolesDrac.get(i).getPositionBody().y > 5.0f) {
                    bolesDrac.get(i).dispose();
                    world.destroyBody(bolesDrac.get(i).getCos());
                    bolesDrac.remove(i);
                } else {
                    bolesDrac.get(i).moure();
                    bolesDrac.get(i).updatePosition();
                    bolesDrac.get(i).dibuixar(batch);
                }
            }
        }

        barra.moure();
        barra.updatePosition();

        if (cor1 != null) {
            cor1.moure();
            cor1.updatePosition();
            cor1.dibuixar(batch);
        }

        if (cor2 != null) {
            cor2.moure();
            cor2.updatePosition();
            cor2.dibuixar(batch);
        }

        barra.dibuixar(batch);

        // finalitzar el lot: a partir d'aquest moment es dibuixa tot el que
        // s'ha indicat entre begin i end
        batch.end();
        calculRedimensionat();
        // dibuixar els controls de pantalla
        stage.act();
        stage.draw();

        /*debugRenderer.render(world, tiledMapHelper.getCamera().combined.scale(
                JocDeTrons.PIXELS_PER_METRE, JocDeTrons.PIXELS_PER_METRE,
                JocDeTrons.PIXELS_PER_METRE));*/

        if (calculVides) {
            table4.clear();
            for (int vides = 0; vides < personatge.getVides(); vides++) {
                table4.add(new Image(textVida)).padTop(5).padRight(5);
            }
            calculVides = false;
        }

        if (personatge.getPositionBody().x > 69.3f && personatge.getPositionBody().x < 70.5f){
            labelPuntControl.setText("Punt de control");
            personatge.setPosX(70f);
            this.punts = personatge.getPunts();
        }else if (personatge.getPositionBody().x > 178f && personatge.getPositionBody().x < 179f){
            labelPuntControl.setText("Punt de control");
            personatge.setPosX(178.5f);
            this.punts = personatge.getPunts();
        }else if (personatge.getPositionBody().x > 263.34f && personatge.getPositionBody().x < 264.5f){
            labelPuntControl.setText("Punt de control");
            personatge.setPosX(263.9f);
            this.punts = personatge.getPunts();
        }
        else{
            labelPuntControl.setText("");
        }

        if (this.personatge.getPositionBody().x > 332.08f){
            joc.setScreen(new Win(joc, personatge));
        }else {
            if (personatge.getPositionBody().y < 0.38){
                personatge.setVides(personatge.getVides()-1);
                personatge.setPunts(punts);
                joc.setScreen(new Level2(joc, personatge));
            }

            if (this.personatge.getVides() == 0) {
                joc.setScreen(new GameOver(joc, personatge));
            } else if (this.personatge.getVides() != vides) {
                vides = this.personatge.getVides();
                personatge.setPunts(punts);
                joc.setScreen(new Level2(joc, this.personatge));
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
        table.setFillParent(true);

        table4.top().right().padTop(10);

        table2.center().top().right().padTop(30);
        table2.add(labelPunts).padTop(30).padRight(5);
        table2.setFillParent(true);

        table3.center();
        table3.add(labelPuntControl).row();
        table3.setFillParent(true);
        table4.setFillParent(true);

        stage.addActor(table);
        stage.addActor(table2);
        stage.addActor(table3);
        stage.addActor(table4);
    }
}
