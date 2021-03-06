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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.JocDeTrons;
import com.mygdx.game.Personatge;


public class PersonatgeSelectionScreen extends AbstractScreen {

    private Table table, tableImatges;

    private Texture textureHeroi, textureHeroina;
    private Image imatgeHeroi, imatgeHeroina;
    private Label infoEscollir;

    public PersonatgeSelectionScreen(JocDeTrons joc) {
        super(joc);
        table = new Table();
        tableImatges = new Table();

        // carregar la imatge
        textureHeroi = new Texture(
                Gdx.files.internal("imatges/caraHeroi.png"));

        imatgeHeroi = new Image(textureHeroi);

        // això només és necessari perquè funcioni correctament l'efecte fade-in
        // Només fa la imatge completament transparent
        imatgeHeroi.getColor().a = 0f;

        // configuro l'efecte de fade-in/out de la imatge de splash
        // sequence indica que es faran de manera consecutiva.
        imatgeHeroi.addAction(Actions.sequence(Actions.fadeIn(1f)));
        imatgeHeroi.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextScreen("imatges/heroiSpriteSheet.png", "imatges/heroi.png", "imatges/heroiE.png", "imatges/heroiSpriteAtacDret.png", 0.8f);
            }
        });


        textureHeroina = new Texture(
                Gdx.files.internal("imatges/caraHeroina.png"));

        imatgeHeroina = new Image(textureHeroina);

        // això només és necessari perquè funcioni correctament l'efecte fade-in
        // Només fa la imatge completament transparent
        imatgeHeroina.getColor().a = 0f;

        // configuro l'efecte de fade-in/out de la imatge de splash
        // sequence indica que es faran de manera consecutiva.
        imatgeHeroina.addAction(Actions.sequence(Actions.fadeIn(1f)));
        imatgeHeroina.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextScreen("imatges/heroinaSpriteSheet.png", "imatges/heroina.png", "imatges/heroinaE.png", "imatges/heroinaSpriteAtacDret.png", 1.0f);
            }
        });

        infoEscollir = new Label("Escull el teu heroi: ", joc.getSkin());
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
    private void nextScreen(String pathToTexture, String pathToImg, String pathToImgE, String pathToAtac, float pes) {
        Personatge persona = new Personatge(3, 0, pathToTexture, pathToImg, pathToImgE, pathToAtac, Personatge.POS_INICIAL_X, Personatge.POS_INICIAL_Y, pes);
        joc.setScreen(new Level1(getGame(), persona));
    }

}