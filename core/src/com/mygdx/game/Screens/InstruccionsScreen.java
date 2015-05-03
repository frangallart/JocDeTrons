/*************************************************************************************
 *                                                                                   *
 *  Joc de Trons por Java Norriors se distribuye bajo una                            *
 *  Licencia Creative Commons Atribuci�n-NoComercial-SinDerivar 4.0 Internacional.   *
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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.JocDeTrons;

/**
 * Classe instruccionsScreen
 */
public class InstruccionsScreen extends AbstractScreen {

    private Image instruccions;
    private Texture textureInstruccions;


    public InstruccionsScreen(JocDeTrons joc) {
        super(joc);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.

        // carregar la imatge
        textureInstruccions = new Texture(
                Gdx.files.internal("imatges/instruccions.png"));

        instruccions = new Image(textureInstruccions);

        instruccions.setFillParent(true);


        // Només fa la imatge completament transparent
        instruccions.getColor().a = 0f;

        // configuro l'efecte de fade-in/out de la imatge de splash
        // sequence indica que es faran de manera consecutiva.
        instruccions.addAction(Actions.sequence(Actions.fadeIn(1f)));
        stage.addActor(instruccions);
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
    private void nextScreen(){
        joc.setScreen(new MainMenuScreen(joc));
    }

}