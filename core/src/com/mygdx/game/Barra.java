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
 * Classe barra
 * Ens permet posar objectes del tipus barra
 * Permetran a l'heroi sobrepassar diversos obstacles que s'anirà trobant
 * Referències al joc: Nivell 1: 3 barres (1 de "doble" i dues de curtes)
 *
 */
public class Barra {

    public static final int FRAME_COLS = 1;
    public static final int FRAME_ROWS = 2;

    private boolean moureEsquerra, moureDreta;

    private World world;                // Referència al mon on està definit el personatge
    private Body cos;                   // per definir les propietats del cos
    private Sprite spriteBarra;    // sprite associat al personatge
    private AnimatedSprite spriteAnimat;// animació de l'sprite
    private Texture stoppedTexture;     // la seva textura
    private Texture animatedTexture;
    private BodyDef defCos;
    private float posicioX, posicioY, bloq_esquerra, bloq_dreta;
    private String pathImg;


    public Barra(World world, float posicioX, float posicioY, float bloq_esquerra, float bloq_dreta, String pathImg) {
        moureEsquerra = moureDreta = false;
        this.world = world;
        this.posicioX = posicioX;
        this.posicioY = posicioY;
        this.bloq_esquerra = bloq_esquerra;
        this.bloq_dreta = bloq_dreta;
        this.pathImg = pathImg;
        carregarTextures();
        crearProtagonista();
    }

    private void carregarTextures() {
        animatedTexture = new Texture(Gdx.files.internal(pathImg));
        animatedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }


    private void crearProtagonista() {
        spriteBarra = new Sprite(animatedTexture);

        // Definir el tipus de cos i la seva posició
        defCos = new BodyDef();
        defCos.gravityScale = 0;
        defCos.type = BodyDef.BodyType.KinematicBody;
        defCos.position.set(this.posicioX, this.posicioY);
        cos = world.createBody(defCos);
        cos.setUserData("Barra");

        /**
         * Definir les vores de l'sprite
         */
        PolygonShape requadre = new PolygonShape();
        requadre.setAsBox((spriteBarra.getWidth() / FRAME_COLS) / (2 * JocDeTrons.PIXELS_PER_METRE),
                (spriteBarra.getHeight() / FRAME_ROWS) / (2 * JocDeTrons.PIXELS_PER_METRE));

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

    /**
     * Actualitza la posició de l'sprite
     */
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
     * Fer que les barres es moguin de costat
     */
    public void moure() {
            if (this.getPositionBody().x < this.bloq_dreta) {
                moureDreta = true;
                moureEsquerra = false;
            } else if (this.getPositionBody().x > this.bloq_esquerra) {
                moureDreta = false;
                moureEsquerra = true;
            }
            if (moureDreta) {
                cos.setLinearVelocity(1.0f, 0.0f);
                defCos.gravityScale = 0;
            } else if (moureEsquerra) {
                cos.setLinearVelocity(-1.0f, 0.0f);
                defCos.gravityScale = 0;
            }
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
        animatedTexture.dispose();
    }
}
