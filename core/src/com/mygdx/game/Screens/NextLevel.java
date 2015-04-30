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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.JocDeTrons;
import com.mygdx.game.Personatge;

/**
 * Classe Next Level
 * Pantalla  entre nivells que ens mostra la possibilitat de continuar,
 * de sortir del joc; ademés ens mostra la puntuació i les vides restants
 */
public class NextLevel extends AbstractScreen {

    private Personatge jugador;
    private String nivell;

    private Stage stage = new Stage();
    private Table table = new Table();

    private TextButton buttonPlay, buttonExit;
    private Label labelNivell;
    private Label labelPuntuacio;
    private Label labelVides;
    /**
     * Constructor
     *
     * @param joc Classe principal del joc
     */
    public NextLevel(final JocDeTrons joc, final Personatge jugador, String nivell) {
        super(joc);
        this.jugador = jugador;
        this.nivell = nivell;
        buttonPlay = new TextButton("Next Level", joc.getSkin());
        buttonExit = new TextButton("Menu", joc.getSkin());
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go
                joc.setScreen(new Level2(getGame(), jugador));
            }
        });
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                joc.setScreen(new MainMenuScreen(joc));
                //Gdx.app.exit();
                // or System.exit(0);
            }
        });
        labelNivell = new Label("Enhorabona t'has passat el " + nivell, joc.getSkin());
        labelPuntuacio = new Label("Punts: " + String.valueOf(jugador.getPunts()), joc.getSkin());
        labelVides = new Label("Vides: " + String.valueOf(jugador.getVides()),joc.getSkin());
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
        table.add(labelNivell).padBottom(40).row();
        table.add(labelPuntuacio).padBottom(40).row();
        table.add(labelVides).padBottom(40).row();

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
