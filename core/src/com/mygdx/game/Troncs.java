package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Ruben on 23/4/15.
 */
public class Troncs {

    public static final int FRAME_COLS = 1;
    public static final int FRAME_ROWS = 1;
    /**
     * Detectar el moviment
     */
    private boolean moureEsquerra;
    private boolean moureDreta;

    public boolean isDestruit() {
        return destruit;
    }

    private boolean destruit;

    private World world;                // Refer�ncia al mon on est� definit el personatge
    private Body cos;                   // per definir les propietats del cos
    private Sprite spriteBarra;    // sprite associat al personatge
    private AnimatedSprite spriteAnimat;// animaci� de l'sprite
    private Texture stoppedTexture, stoppedTextureE, animatedTexture;     // la seva textura
    private BodyDef defCos;
    private float posicioX, posicioY;

    public int getDestruir() {
        return destruir;
    }

    public void setDestruir(int destruir) {
        this.destruir = destruir;
    }

    private int destruir;

    private String pathImg;

    public String getNom() {
        return nom;
    }

    private String nom;


    public Troncs(World world, String nom, float posicioX, float posicioY, String pathImg) {
        moureEsquerra = moureDreta = false;
        this.world = world;
        this.posicioX = posicioX;
        this.posicioY = posicioY;
        this.pathImg = pathImg;
        this.destruir = 0;
        this.destruit = false;
        this.nom = nom;
        crearProtagonista();
    }


    private void crearProtagonista() {
        stoppedTexture = new Texture(Gdx.files.internal(pathImg));
        spriteBarra = new Sprite(stoppedTexture);
       // spriteAnimat = new AnimatedSprite(stoppedTextureE);

        // Definir el tipus de cos i la seva posici�
        defCos = new BodyDef();
        defCos.gravityScale = 0;
        defCos.type = BodyDef.BodyType.KinematicBody;
        defCos.position.set(this.posicioX, this.posicioY);
        cos = world.createBody(defCos);
        cos.setUserData(nom);
        /**
         * Definir les vores de l'sprite
         */
        PolygonShape requadre = new PolygonShape();
        requadre.setAsBox((spriteBarra.getWidth() / FRAME_COLS) / (2 * JocDeTrons.PIXELS_PER_METRE),
                (spriteBarra.getHeight() / FRAME_ROWS) / (2 * JocDeTrons.PIXELS_PER_METRE));

        /**
         * La densitat i fricci� del protagonista. Si es modifiquen aquests
         * valor anir� m�s r�pid o m�s lent.
         */
        FixtureDef propietats = new FixtureDef();
        propietats.shape = requadre;
        propietats.density = 1.0f;
        propietats.friction = 0.1f;

        cos.setFixedRotation(true);
        cos.createFixture(propietats);
        requadre.dispose();
    }


    public void updatePosition() {
        spriteBarra.setPosition(
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                        - spriteBarra.getWidth() / FRAME_COLS / 2,
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                        - spriteBarra.getHeight() / FRAME_ROWS / 2);
    }


    public void dibuixar(SpriteBatch batch) {
        spriteBarra.draw(batch);
    }

    /**
     * Fer que el personatge es mogui
     * <p/>
     * Canvia la posici� del protagonista
     * Es tracta de forma separada el salt perqu� es vol que es pugui moure si salta
     * al mateix temps..
     * <p/>
     * Els impulsos s'apliquen des del centre del protagonista
     */
    public void moure() {

    }

    public boolean isMoureEsquerra() {
        return moureEsquerra;
    }

    public void setMoureEsquerra(boolean moureEsquerra) {
        this.moureEsquerra = moureEsquerra;
    }

    public boolean isMoureDreta() {
        return moureDreta;
    }

    public void setMoureDreta(boolean moureDreta) {
        this.moureDreta = moureDreta;
    }

    public Vector2 getPositionBody() {
        return this.cos.getPosition();
    }

    public Vector2 getPositionSprite() {
        return new Vector2().set(this.spriteBarra.getX(), this.spriteBarra.getY());
    }


    public Texture getTextura() {
        return stoppedTexture;
    }

    public void setTextura(Texture textura) {
        this.stoppedTexture = textura;
    }


    public void dispose() {
        stoppedTexture.dispose();
    }
}