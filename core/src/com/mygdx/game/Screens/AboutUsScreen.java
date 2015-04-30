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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.JocDeTrons;


public class AboutUsScreen extends AbstractScreen {

    private Stage stage;
    private Table table;

    private Skin skin;
    private Image credits;
    private Texture textureCredits;

    /**
     * Constructor
     *
     * @param joc Classe principal del joc
     */
    public AboutUsScreen(JocDeTrons joc) {
        super(joc);
        skin = joc.getSkin();
        stage = new Stage();
        table =  new Table();

        // carregar la imatge
        textureCredits = new Texture(
                Gdx.files.internal("imatges/caraHeroi.png"));

        credits = new Image(textureCredits);

        // Només fa la imatge completament transparent
        credits.getColor().a = 0f;

        // configuro l'efecte de fade-in/out de la imatge de splash
        // sequence indica que es faran de manera consecutiva.
        credits.addAction(Actions.sequence(Actions.fadeIn(1f)));
        credits.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    nextScreen();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

        table.center();
        table.row().center();
        table.add(credits).size(90, 110).center().padTop(15).padRight(50);

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
        skin.dispose();
    }

    /**
     * canviar a la següent pantalla
     */
    private void nextScreen(){
        joc.setScreen(new MainMenuScreen(joc));

    }

}