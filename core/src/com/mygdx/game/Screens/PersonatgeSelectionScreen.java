package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.JocDeTrons;


public class PersonatgeSelectionScreen extends AbstractScreen {

    private Stage stage = new Stage();
    private Table table = new Table();

    private Skin skin;

    private TextButton buttonPlay, buttonExit;
    private Label namePlayerInfo;
    private TextField nomPlayer;
    private Texture splashTexture;
    private Image splashImage;

    /**
     * Constructor
     *
     * @param joc Classe principal del joc
     */
    public PersonatgeSelectionScreen(JocDeTrons joc) {
        super(joc);
        skin = new Skin(Gdx.files.internal("skins/skin.json"));

        // carregar la imatge
        splashTexture = new Texture(
                Gdx.files.internal("imatges/heroi.png"));

        splashImage = new Image(splashTexture);

        // aix� nom�s �s necessari perqu� funcioni correctament l'efecte fade-in
        // Nom�s fa la imatge completament transparent
        splashImage.getColor().a = 0f;

        // configuro l'efecte de fade-in/out de la imatge de splash
        // sequence indica que es faran de manera consecutiva.
        splashImage.addAction(Actions.sequence(Actions.fadeIn(1f)));

        buttonPlay = new TextButton("Entrar", skin);
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
               nextScreen();
            }
        });
        namePlayerInfo = new Label("Escull el teu nom",skin);
        nomPlayer = new TextField("", skin);
        nomPlayer.setText("Jugador 1");
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
        table.add(namePlayerInfo).padTop(5).padLeft(5).row();
        table.add(nomPlayer).padTop(20).padLeft(5).row();
        table.add(splashImage).size(150,60).padTop(10).padLeft(5).row();
        //table.add(buttonPlay).size(150,60).padBottom(20).row();
        table.setFillParent(true);
        stage.setKeyboardFocus(nomPlayer);
        
        stage.addActor(table);
        //stage.addActor(splashImage);

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
    private void nextScreen() {
        // la darrera acci� ens porta cap a la seg�ent pantalla
        //joc.setScreen(new PantallaPrincipal(joc));
        joc.setScreen(new MainMenuScreen(joc));
    }

}