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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.JocDeTrons;
import com.mygdx.game.Personatge;

/**
 * Classe que mostra la pantalla de game over
 */
public class GameOver extends AbstractScreen {

    private Personatge jugador;

    private Table table = new Table();

    private TextButton buttonPlay, buttonExit;
    private Label labelNivell;
    private Label labelPuntuacio;


    public GameOver(final JocDeTrons joc, final Personatge jugador) {
        super(joc);
        this.jugador = jugador;
        buttonPlay = new TextButton("Reiniciar partida", joc.getSkin());
        buttonExit = new TextButton("Menu", joc.getSkin());
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go
                Personatge persona = new Personatge(3, 0, jugador.getPathTextura(), jugador.getPathImatge(), jugador.getPathImatgeE(), jugador.getPathImatgeAtac(), 1f, 5f, jugador.getPes());
                joc.setScreen(new Level1(getGame(), persona));
            }
        });
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                joc.setScreen(new MainMenuScreen(joc));
            }
        });
        labelNivell = new Label("Game Over", joc.getSkin());
        labelPuntuacio = new Label("Punts: " + String.valueOf(jugador.getPunts()), joc.getSkin());
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
        table.add(labelNivell).padBottom(40).row();
        table.add(labelPuntuacio).padBottom(40).row();

        table.add(buttonPlay).size(150,60).padBottom(20).row();
        table.add(buttonExit).size(150, 60).padBottom(20).row();
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
}
