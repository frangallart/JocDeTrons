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

/**
 *
 * Classe per gestionar l'objecte Vides.
 * Aquesta classe ens crea una vida que permetrà jugar al jugador
 * diverses vegades tot i morir
 */
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


public class Vides {

    public static final int FRAME_COLS = 2;
    public static final int FRAME_ROWS = 2;
    /**
     * Detectar el moviment
     */
    private float posX, posY;

    private String nom;

    private World world;                // Refer?ncia al mon on est? definit el personatge
    private Body cos;                   // per definir les propietats del cos
    private Sprite spriteVida;    // sprite associat al personatge
    private AnimatedSprite spriteAnimat;// animaci? de l'sprite
    private Texture stoppedTexture;     // la seva textura
    private Texture animatedTexture;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Vides(World world,String nom, float posX, float posY){
        this.nom = nom;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        carregarTextures();
        crearProtagonista();
    }

    private void carregarTextures() {
        animatedTexture = new Texture(Gdx.files.internal("imatges/cor.png"));
        animatedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private void crearProtagonista() {
        spriteVida = new Sprite(animatedTexture);
        spriteAnimat = new AnimatedSprite(spriteVida, FRAME_COLS, FRAME_ROWS);

        // Definir el tipus de cos i la seva posici?
        BodyDef defCos = new BodyDef();
        defCos.type = BodyDef.BodyType.DynamicBody;
        defCos.position.set(posX, posY);

        cos = world.createBody(defCos);
        cos.setUserData(nom);
        /**
         * Definir les vores de l'sprite
         */
        PolygonShape requadre = new PolygonShape();
        requadre.setAsBox((spriteVida.getWidth() / FRAME_COLS) / (2 * JocDeTrons.PIXELS_PER_METRE),
                (spriteVida.getHeight() / FRAME_ROWS) / (2 * JocDeTrons.PIXELS_PER_METRE));

        /**
         * La densitat i fricci? del protagonista. Si es modifiquen aquests
         * valor anir? m?s r?pid o m?s lent.
         */
        FixtureDef propietats = new FixtureDef();
        propietats.shape = requadre;
        propietats.density = 1.0f;
        propietats.friction = 3.0f;

        cos.setFixedRotation(true);
        cos.createFixture(propietats);
        requadre.dispose();
    }

    /**
     * Actualitza la posici? de l'sprite
     */
    public void updatePosition() {
        spriteVida.setPosition(
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                        - spriteVida.getWidth() / FRAME_COLS / 2,
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                        - spriteVida.getHeight() / FRAME_ROWS / 2);
    }

    public void dibuixar(SpriteBatch batch) {
        spriteAnimat.draw(batch);
    }

    public void moure() {
        spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);
    }

    public Vector2 getPositionBody() {
        return this.cos.getPosition();
    }

    public Vector2 getPositionSprite() {
        return new Vector2().set(this.spriteVida.getX(), this.spriteVida.getY());
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