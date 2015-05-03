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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.JocDeTrons;

/**
 * Classe Main Menu Screen
 *  Pantalla principal que ens permet iniciar el joc o en el seu defecte
 *  Sortir d'ell
 */
public class MainMenuScreen extends AbstractScreen{

    private Table table;

    private TextButton buttonPlay, buttonExit, buttonInstruccions, buttonCredits;
    private Texture texturaTitol;
    private Image imatgeTitol;
    /**
     * Constructor
     *
     * @param joc Classe principal del joc
     */
    public MainMenuScreen(JocDeTrons joc) {
        super(joc);
        table =  new Table();
        buttonPlay = new TextButton("Jugar", joc.getSkin());
        buttonExit = new TextButton("Sortir", joc.getSkin());
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go
                ((Game)Gdx.app.getApplicationListener()).setScreen(new PersonatgeSelectionScreen(getGame()));
            }
        });
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                // or System.exit(0);
            }
        });

        buttonInstruccions = new TextButton("Instruccions", joc.getSkin());
        buttonInstruccions.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                ((Game)Gdx.app.getApplicationListener()).setScreen(new InstruccionsScreen(getGame()));
            }
        });

        buttonCredits = new TextButton("Creadors", joc.getSkin());
        buttonCredits.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                ((Game)Gdx.app.getApplicationListener()).setScreen(new AboutUsScreen(getGame()));
            }
        });
        texturaTitol = new Texture(
                Gdx.files.internal("imatges/ImatgeTitol.png"));

        imatgeTitol = new Image(texturaTitol);
    }


    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.

        table.add(imatgeTitol).size(510 * Gdx.graphics.getDensity(),
                66.5f * Gdx.graphics.getDensity()).padTop(15 * Gdx.graphics.getDensity()).row();
        table.add(buttonPlay).padTop(20 * Gdx.graphics.getDensity()).row();
        table.add(buttonExit).padTop(35 * Gdx.graphics.getDensity()).row();
        table.add(buttonInstruccions).padTop(10 * Gdx.graphics.getDensity()).row();
        table.add(buttonCredits).padTop(10 * Gdx.graphics.getDensity()).row();
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