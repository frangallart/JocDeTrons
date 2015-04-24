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
public class Barra {

    public static final int FRAME_COLS = 9;
    public static final int FRAME_ROWS = 2;
    /**
     * Detectar el moviment
     */
    private boolean moureEsquerra;
    private boolean moureDreta;
    private boolean personatgeCaraDreta;

    private World world;                // Referència al mon on està definit el personatge
    private Body cos;                   // per definir les propietats del cos
    private Sprite spritePersonatge;    // sprite associat al personatge
    private AnimatedSprite spriteAnimat;// animació de l'sprite
    private Texture stoppedTexture;     // la seva textura
    private Texture animatedTexture;
    private BodyDef defCos;
    private float posicioX, posicioY;

    private static final float BLOQ_ESQUERRE = 16.0f;
    private static final float BLOQ_DRETA = 14.4f;


    public Barra(World world, float posicioX, float posicioY) {
        moureEsquerra = moureDreta = false;
        this.world = world;
        this.posicioX = posicioX;
        this.posicioY = posicioY;
        carregarTextures();
        crearProtagonista();

    }

    private void carregarTextures() {
        animatedTexture = new Texture(Gdx.files.internal("imatges/warriorSpriteSheet.png"));
        animatedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        stoppedTexture = new Texture(Gdx.files.internal("imatges/warrior.png"));
        stoppedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }


    private void crearProtagonista() {
        spritePersonatge = new Sprite(animatedTexture);
        spriteAnimat = new AnimatedSprite(spritePersonatge, FRAME_COLS, FRAME_ROWS, stoppedTexture);

        // Definir el tipus de cos i la seva posició
        defCos = new BodyDef();
        defCos.gravityScale = 0;
        defCos.type = BodyDef.BodyType.KinematicBody;
        System.out.println("Posicio X " + this.posicioX + " Posicio Y " + this.posicioY);
        defCos.position.set(this.posicioX, this.posicioY);
        cos = world.createBody(defCos);
        cos.setUserData("Personatge");
        /**
         * Definir les vores de l'sprite
         */
        PolygonShape requadre = new PolygonShape();
        requadre.setAsBox((spritePersonatge.getWidth() / FRAME_COLS) / (2 * JocDeTrons.PIXELS_PER_METRE),
                (spritePersonatge.getHeight() / FRAME_ROWS) / (2 * JocDeTrons.PIXELS_PER_METRE));

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
        setMoureDreta(false);
        setMoureEsquerra(false);
        spriteAnimat.setDirection(AnimatedSprite.Direction.STOPPED);
    }

    /**
     * Actualitza la posició de l'sprite
     */
    public void updatePosition() {
        spritePersonatge.setPosition(
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                        - spritePersonatge.getWidth() / FRAME_COLS / 2,
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                        - spritePersonatge.getHeight() / FRAME_ROWS / 2);
        spriteAnimat.setPosition(spritePersonatge.getX(), spritePersonatge.getY());
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

        if (this.getPositionBody().x < BLOQ_DRETA) {
            moureDreta = true;
            moureEsquerra = false;
        } else if (this.getPositionBody().x > BLOQ_ESQUERRE) {
            moureDreta = false;
            moureEsquerra = true;
        }
        if (moureDreta) {
            cos.setLinearVelocity(1.0f, 0.0f);
            ;
            spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);
            spritePersonatge.flip(true, false);
            defCos.gravityScale = 0;
        } else if(moureEsquerra){
            cos.setLinearVelocity(-1.0f, 0.0f);
            ;
            spriteAnimat.setDirection(AnimatedSprite.Direction.LEFT);

            spritePersonatge.flip(true, false);
            defCos.gravityScale = 0;
        }
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

    public boolean isCaraDreta() {
        return this.personatgeCaraDreta;
    }

    public void setCaraDreta(boolean caraDreta) {
        this.personatgeCaraDreta = caraDreta;

    }

    public Vector2 getPositionBody() {
        return this.cos.getPosition();
    }

    public Vector2 getPositionSprite() {
        return new Vector2().set(this.spritePersonatge.getX(), this.spritePersonatge.getY());
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
