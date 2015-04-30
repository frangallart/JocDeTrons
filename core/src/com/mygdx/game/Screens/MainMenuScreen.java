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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.JocDeTrons;

public class MainMenuScreen extends AbstractScreen {

    private Stage stage = new Stage();
    private Table table = new Table();

    private TextButton buttonPlay, buttonExit;
    //private Label title;
    private Texture texturaTitol;
    private Image imatgeTitol;

    /**
     * Constructor
     *
     * @param joc Classe principal del joc
     */
    public MainMenuScreen(JocDeTrons joc) {
        super(joc);

        buttonPlay = new TextButton("Play", joc.getSkin());
        buttonExit = new TextButton("Exit", joc.getSkin());
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go
                ((Game) Gdx.app.getApplicationListener()).setScreen(new PersonatgeSelectionScreen(getGame()));
            }
        });
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                // or System.exit(0);
            }
        });

        texturaTitol = new Texture(
                Gdx.files.internal("imatges/ImatgeTitol.png"));

        imatgeTitol = new Image(texturaTitol);
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

        table.add(imatgeTitol).size(510 * Gdx.graphics.getDensity(),
                66.5f * Gdx.graphics.getDensity()).padBottom(30 * Gdx.graphics.getDensity()).row();
        table.add(buttonPlay).size(150 * Gdx.graphics.getDensity(),
                60 * Gdx.graphics.getDensity()).padBottom(20 * Gdx.graphics.getDensity()).row();
        table.add(buttonExit).size(150 * Gdx.graphics.getDensity(),
                60 * Gdx.graphics.getDensity()).padBottom(20 * Gdx.graphics.getDensity()).row();
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