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
 * Classe Monstre Estatic
 * Aquesta classe ens permet crear monstres estatics
 * Els quals son inmortals(Inmatables)
 * Fan referència als monstres de lava del nivell 1 (riu de lava)
 */
public class MonstreEstatic {
    public static final int FRAME_COLS = 4;
    public static final int FRAME_ROWS = 2;
    /**
     * Detectar el moviment
     */
    private boolean moureEsquerra;
    private boolean moureDreta;
    private boolean personatgeCaraDreta;
    private float posX;
    private float posY;

    private String nom, imatge;

    private World world;                // Referència al mon on està definit el personatge
    private Body cos;                   // per definir les propietats del cos
    private Sprite spritePersonatge;    // sprite associat al personatge
    private AnimatedSprite spriteAnimat;// animació de l'sprite
    private Texture stoppedTexture;     // la seva textura
    private Texture animatedTexture;


    public MonstreEstatic(World world, String nom, float posX, float posY, boolean orientacio, String imatge){
        this.moureEsquerra = orientacio;
        this.nom = nom;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.imatge = imatge;
        carregarTextures();
        crearProtagonista();
    }

    private void carregarTextures() {
        animatedTexture = new Texture(Gdx.files.internal(imatge));
        animatedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private void crearProtagonista() {
        spritePersonatge = new Sprite(animatedTexture);
        spriteAnimat = new AnimatedSprite(spritePersonatge, FRAME_COLS, FRAME_ROWS);

        // Definir el tipus de cos i la seva posició
        BodyDef defCos = new BodyDef();
        defCos.type = BodyDef.BodyType.StaticBody;
        defCos.position.set(posX, posY);

        cos = world.createBody(defCos);
        cos.setUserData(nom);

        /**
         * Definir les vores de l'sprite
         */
        PolygonShape requadre = new PolygonShape();
        requadre.setAsBox((spritePersonatge.getWidth() / FRAME_COLS) / (2 * JocDeTrons.PIXELS_PER_METRE),
                (spritePersonatge.getHeight() / FRAME_ROWS) / (2 * JocDeTrons.PIXELS_PER_METRE));

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
     * Fer que el monstre executi l'animació de la dreta o l'esquerra segons
     * la posició on estigui
     */

    public void moure() {
        if (!moureEsquerra) {
            spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);
        }else {
            spriteAnimat.setDirection(AnimatedSprite.Direction.LEFT);
        }
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void dispose() {
        animatedTexture.dispose();
    }
}
