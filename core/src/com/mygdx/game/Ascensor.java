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
public class Ascensor {

    public static final int FRAME_COLS = 1;
    public static final int FRAME_ROWS = 20;
    /**
     * Detectar el moviment
     */
    private boolean moureAbaix, moureAdalt;

    private World world;                // Referència al mon on està definit el personatge
    private Body cos;                   // per definir les propietats del cos
    private Sprite spriteAscensor;    // sprite associat al personatge
    private AnimatedSprite spriteAnimat;// animació de l'sprite
    private Texture stoppedTexture;     // la seva textura
    private Texture animatedTexture;
    private BodyDef defCos;
    private float posicioX, posicioY;

    private float bloq_adalt;
    private float bloq_abaix;
    private String pathImg;


    public Ascensor(World world, float posicioX, float posicioY, float bloq_adalt, float bloq_abaix, String pathImg) {
        moureAbaix = moureAdalt = false;
        this.world = world;
        this.posicioX = posicioX;
        this.posicioY = posicioY;
        this.bloq_adalt = bloq_adalt;
        this.bloq_abaix = bloq_abaix;
        this.pathImg = pathImg;
        carregarTextures();
        crearProtagonista();
    }

    private void carregarTextures() {
        animatedTexture = new Texture(Gdx.files.internal(pathImg));
        animatedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        stoppedTexture = new Texture(Gdx.files.internal(pathImg));
        stoppedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }


    private void crearProtagonista() {
        spriteAscensor = new Sprite(animatedTexture);
        spriteAnimat = new AnimatedSprite(spriteAscensor, FRAME_COLS, FRAME_ROWS, stoppedTexture);

        // Definir el tipus de cos i la seva posició
        defCos = new BodyDef();
        defCos.gravityScale = 0;
        defCos.type = BodyDef.BodyType.KinematicBody;
        defCos.position.set(this.posicioX, this.posicioY);
        cos = world.createBody(defCos);
        cos.setUserData("Ascensor");
        /**
         * Definir les vores de l'sprite
         */
        PolygonShape requadre = new PolygonShape();
        requadre.setAsBox((spriteAscensor.getWidth() / FRAME_COLS) / (2 * JocDeTrons.PIXELS_PER_METRE),
                (spriteAscensor.getHeight() / FRAME_ROWS) / (2 * JocDeTrons.PIXELS_PER_METRE));

        /**
         * La densitat i fricció del protagonista. Si es modifiquen aquests
         * valor anirà més ràpid o més lent.
         */
        FixtureDef propietats = new FixtureDef();
        propietats.shape = requadre;
        propietats.density = 1.0f;
        propietats.friction = 0.1f;

        cos.setFixedRotation(true);
        cos.createFixture(propietats);
        requadre.dispose();
    }

    public void inicialitzarMoviments() {
        setmoureAdalt(false);
        setmoureAbaix(false);
        spriteAnimat.setDirection(AnimatedSprite.Direction.STOPPED);
    }

    /**
     * Actualitza la posició de l'sprite
     */
    public void updatePosition() {
        spriteAscensor.setPosition(
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                        - spriteAscensor.getWidth() / FRAME_COLS / 2,
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                        - spriteAscensor.getHeight() / FRAME_ROWS / 2);
        spriteAnimat.setPosition(spriteAscensor.getX(), spriteAscensor.getY());
    }

    public void dibuixar(SpriteBatch batch) {
        spriteAnimat.draw(batch);
    }

    /**
     * Fer que el personatge es mogui
     * <p/>
     * Canvia la posició del protagonista
     * Es tracta de forma separada el salt perquè es vol que es pugui moure si salta
     * al mateix temps..
     * <p/>
     * Els impulsos s'apliquen des del centre del protagonista
     */
    public void moure() {

            if (this.getPositionBody().y < this.bloq_abaix) {
                moureAdalt = true;
                moureAbaix = false;
            } else if (this.getPositionBody().y > this.bloq_adalt) {
                moureAdalt = false;
                moureAbaix = true;
            }
            if (moureAdalt) {
                cos.setLinearVelocity(0.0f, 1.0f);
                ;
                spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);
                spriteAscensor.flip(true, false);
                defCos.gravityScale = 0;
            } else if (moureAbaix) {
                cos.setLinearVelocity(0.0f, -1.0f);

                spriteAnimat.setDirection(AnimatedSprite.Direction.LEFT);

                spriteAscensor.flip(true, false);
                defCos.gravityScale = 0;
            }
    }

    public boolean ismoureAbaix() {
        return moureAbaix;
    }

    public void setmoureAbaix(boolean moureAbaix) {
        this.moureAbaix = moureAbaix;
    }

    public boolean ismoureAdalt() {
        return moureAdalt;
    }

    public void setmoureAdalt(boolean moureAdalt) {
        this.moureAdalt = moureAdalt;
    }

    public Vector2 getPositionBody() {
        return this.cos.getPosition();
    }

    public Vector2 getPositionSprite() {
        return new Vector2().set(this.spriteAscensor.getX(), this.spriteAscensor.getY());
    }


    public Texture getTextura() {
        return stoppedTexture;
    }

    public void setTextura(Texture textura) {
        this.stoppedTexture = textura;
    }


    public void dispose() {
        animatedTexture.dispose();
        stoppedTexture.dispose();
    }
}
