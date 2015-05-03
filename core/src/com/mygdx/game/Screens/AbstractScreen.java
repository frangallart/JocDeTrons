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
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.JocDeTrons;

/**
 * Classe que abstreu les característiques bàsiques d'una pantalla del joc
 */
public class AbstractScreen implements Screen, InputProcessor {
    // atributs
    protected final JocDeTrons joc;
    /**
     * Objecte SpriteBatch Defineix un bloc en que es crea un lot d'ordres de
     * dibuix per enviar a la GPU
     */
    protected final SpriteBatch  batch;

    // escena
    protected final Stage stage;


    private FitViewport viewport;
    private Texture imatge;
    /**
     * Constructor
     *
     * @param joc
     *            Classe principal del joc
     */
    public AbstractScreen(JocDeTrons joc) {
        this.joc= joc;
        this.batch = new SpriteBatch();
        // definim l'stage amb un viewport de mida 0x0px i que mantingui la relació d'aspecte
        this.stage = new Stage(); //0, 0, true);
        Gdx.input.setInputProcessor(this);

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Pixmap pixels = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixels.setColor(1,1,1,1);
        pixels.fill();
        imatge = new Texture(pixels);
    }

    /**
     * @see com.badlogic.gdx.Screen#render(float)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // actualitzar els actors
        stage.act(delta);
        // dibuixar els actors.
        stage.draw();
    }


    public void calculRedimensionat() {

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        Gdx.gl.glViewport(0,0, screenWidth, screenHeight);
        batch.getProjectionMatrix().idt().setToOrtho2D(0,0,screenWidth, screenHeight);
        batch.getTransformMatrix().idt();
        batch.begin();
        float zonaEsquerra = viewport.getLeftGutterWidth();

        if(zonaEsquerra > 0) {
            batch.draw(imatge, 0, 0, zonaEsquerra, screenHeight);
            batch.draw(imatge, viewport.getRightGutterX(), 0, viewport.getRightGutterWidth(), screenHeight);
        }

        float zonaInferior = viewport.getBottomGutterHeight();

        if(zonaInferior > 0) {
            batch.draw(imatge, 0, 0, screenWidth, zonaInferior);
            batch.draw(imatge, 0, viewport.getTopGutterY(), screenWidth, viewport.getTopGutterHeight());
        }
        batch.end();
        viewport.update(screenWidth,screenHeight, true);
    }

    /**
     * @see com.badlogic.gdx.Screen#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
    }

    /**
     * @see com.badlogic.gdx.Screen#show()
     */
    @Override
    public void show() {
    }

    /**
     * @see com.badlogic.gdx.Screen#hide()
     */
    @Override
    public void hide() {

        // dispose de la pantalla quan deixem la pantalla.
        // Aquest mètode no es crida automàticament.
        dispose();
    }

    /**
     * @see com.badlogic.gdx.Screen#pause()
     */
    @Override
    public void pause() {
    }

    /**
     * @see com.badlogic.gdx.Screen#resume()
     */
    @Override
    public void resume() {
    }

    /**
     * @see com.badlogic.gdx.Screen#dispose()
     */
    @Override
    public void dispose() {
        Gdx.app.log(JocDeTrons.class.getSimpleName(), "Alliberant recursos de la pantalla: " + getName());

        stage.dispose();
        batch.dispose();
        imatge.dispose();

    }

    /**
     * per recuperar el nom de la pantalla
     *
     * @return
     */
    protected String getName() {
        return getClass().getSimpleName();
    }

    protected JocDeTrons getGame() {
        return joc;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}