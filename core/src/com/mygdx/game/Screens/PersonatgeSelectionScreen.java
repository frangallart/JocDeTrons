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

    private Stage stage;
    private Table table;

    private Skin skin;

    private TextButton buttonPlay, buttonExit;
    private Label namePlayerInfo, escullirPj;
    private TextField nomPlayer;
    private Texture textureHeroi, textureHeroina;
    private Image imatgeHeroi, imatgeHeroina;

    /**
     * Constructor
     *
     * @param joc Classe principal del joc
     */
    public PersonatgeSelectionScreen(JocDeTrons joc) {
        super(joc);
        skin = new Skin(Gdx.files.internal("skins/skin.json"));
        stage = new Stage();
        table =  new Table();

        // carregar la imatge
        textureHeroi = new Texture(
                Gdx.files.internal("imatges/heroi.png"));

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
                nextScreen("imatges/heroiSpriteSheet.png", "imatges/heroi.png", nomPlayer.getText());
            }
        });


        textureHeroina = new Texture(
                Gdx.files.internal("imatges/heroina.png"));

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
                nextScreen("imatges/heroinaSpriteSheet.png", "imatges/heroina.png", nomPlayer.getText());
            }
        });

        namePlayerInfo = new Label("Introdueix el teu nom: ",skin);
        escullirPj = new Label("Escoll el teu heroi: ", skin);
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

        table.center().top();
        table.add(namePlayerInfo);
        table.add(nomPlayer);
        table.row();
        table.add(escullirPj);
        table.row();
        table.add(imatgeHeroi).size(150,60).padTop(10);
        table.add(imatgeHeroina).size(150,60);
        table.row();
        //table.add(buttonPlay).size(150,60).padBottom(20).row();
        table.setFillParent(true);
        stage.setKeyboardFocus(nomPlayer);

        stage.addActor(table);
        //stage.addActor(imatgeHeroi);

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
    private void nextScreen(String pathToTexture, String pathToImg, String nomJugador) {
        // la darrera acci� ens porta cap a la seg�ent pantalla
        //joc.setScreen(new PantallaPrincipal(joc));
        joc.setScreen(new MainScreen(getGame(), 3, pathToTexture, pathToImg, nomJugador));
    }

}