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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.JocDeTrons;
import com.mygdx.game.Personatge;


public class PersonatgeSelectionScreen extends AbstractScreen {

    private Stage stage;
    private Table table, tableImatges;

    //private Skin skin;

    private Texture textureHeroi, textureHeroina;
    private Image imatgeHeroi, imatgeHeroina;
    private Label  infoEscollir;

    /**
     * Constructor
     *
     * @param joc Classe principal del joc
     */
    public PersonatgeSelectionScreen(JocDeTrons joc) {
        super(joc);
        //skin = new Skin(Gdx.files.internal("skins/skin.json"));
        stage = new Stage();
        table =  new Table();
        tableImatges = new Table();

        // carregar la imatge
        textureHeroi = new Texture(
                Gdx.files.internal("imatges/caraHeroi.png"));

        imatgeHeroi = new Image(textureHeroi);

        // aix� nom�s �s necessari perqu� funcioni correctament l'efecte fade-in
        // Nom�s fa la imatge completament transparent
        imatgeHeroi.getColor().a = 0f;

        // configuro l'efecte de fade-in/out de la imatge de splash
        // sequence indica que es faran de manera consecutiva.
        imatgeHeroi.addAction(Actions.sequence(Actions.fadeIn(1f)));
        imatgeHeroi.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextScreen("imatges/heroiSpriteSheet.png", "imatges/heroi.png", "imatges/heroiE.png","imatges/heroiSpriteAtacDret.png", 0.8f);
            }
        });


        textureHeroina = new Texture(
                Gdx.files.internal("imatges/caraHeroina.png"));

        imatgeHeroina = new Image(textureHeroina);

        // aix� nom�s �s necessari perqu� funcioni correctament l'efecte fade-in
        // Nom�s fa la imatge completament transparent
        imatgeHeroina.getColor().a = 0f;

        // configuro l'efecte de fade-in/out de la imatge de splash
        // sequence indica que es faran de manera consecutiva.
        imatgeHeroina.addAction(Actions.sequence(Actions.fadeIn(1f)));
        imatgeHeroina.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextScreen("imatges/heroinaSpriteSheet.png", "imatges/heroina.png", "imatges/heroinaE.png", "imatges/heroinaSpriteAtacDret.png", 1.0f);           }
        });

        //namePlayerInfo = new Label("Introdueix el teu nom: ",skin);
        infoEscollir = new Label("Escull el teu heroi: ", joc.getSkin());
       // nomPlayer = new TextField("", skin);
       // nomPlayer.setText("Jugador 1");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        calculRedimensionat();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.


        table.add(infoEscollir).padBottom(40 * Gdx.graphics.getDensity()).row();
        tableImatges.add(imatgeHeroi).size(90 * Gdx.graphics.getDensity(), 110 * Gdx.graphics.getDensity()).padRight(125 * Gdx.graphics.getDensity());
        tableImatges.add(imatgeHeroina).size(90 * Gdx.graphics.getDensity(), 110 * Gdx.graphics.getDensity());
        table.add(tableImatges);
        table.setFillParent(true);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
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
        stage.dispose();
    }

    /**
     * canviar a la següent pantalla
     */
    private void nextScreen(String pathToTexture, String pathToImg, String pathToImgE, String pathToAtac, float pes){
        Personatge persona = new Personatge(3 , 0, pathToTexture, pathToImg, pathToImgE, pathToAtac, 1,5, pes);
        joc.setScreen(new Level1(getGame(), persona));
    }

}