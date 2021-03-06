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
 * Classe Boles de foc del monstre
 * Aquesta classe ens permet disparar projectils
 * Simulant el llençament d'una bola de foc
 * Pot matar a l'heroi i destruir objectes del mapa
 * Es poden esquivar però no matar
 */
public class BolesFocMonstre {
    public static final int FRAME_COLS = 8;
    public static final int FRAME_ROWS = 2;
    private final float VELOCITAT = 0.8f;

    /**
     * Detectar el moviment
     */
    private float posX;
    private float posY;
    private float posMax;
    private float altura;

    private String nom, imatge;
    private boolean orientacio;

    private World world;                // Referència al mon on estè definit el personatge

    public Body getCos() {
        return cos;
    }

    private Body cos;                   // per definir les propietats del cos
    private Sprite spritePersonatge;    // sprite associat al personatge
    private AnimatedSprite spriteAnimat;// animació de l'sprite
    private Texture animatedTexture;


    public BolesFocMonstre(World world,String nom, float posX, float posY, float posMax, boolean orientacio, String imatge){
        this.nom = nom;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.posMax = posMax;
        this.orientacio = orientacio;
        this.altura = 0.6f;
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
        defCos.type = BodyDef.BodyType.DynamicBody;
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
         * valor anirà més ràpid o més lent.
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
     * Fer que les boles es moguin
     */
    public void moure() {
        if (this.getNom().substring(0,5).equals("Dreta")) {
            if (!orientacio) {
                cos.setLinearVelocity(VELOCITAT, altura);
                spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);
            }else{
                cos.setLinearVelocity(VELOCITAT, altura);
                spriteAnimat.setDirection(AnimatedSprite.Direction.LEFT);
                altura -= 0.002f;
            }
        }

        else if (!this.getNom().substring(0,5).equals("Dreta") && !this.getNom().substring(0,5).equals("Esque")) {
                cos.setLinearVelocity(0f, -1.6f);
                spriteAnimat.setDirection(AnimatedSprite.Direction.LEFT);
        }

        else if (this.getNom().substring(0,5).equals("Esque")){
            if (!orientacio) {
                cos.setLinearVelocity(-VELOCITAT, altura);
                spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);
            }else{
                cos.setLinearVelocity(-VELOCITAT, altura);
                spriteAnimat.setDirection(AnimatedSprite.Direction.LEFT);
                altura -= 0.002f;
            }
        }

        if (this.getPositionBody().y > this.posMax){
            orientacio = true;
        }
    }

    public Vector2 getPositionBody() {
        return this.cos.getPosition();
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
