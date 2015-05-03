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

public class Troncs {

    public static final int FRAME_COLS = 1;
    public static final int FRAME_ROWS = 1;

    private World world;                                    // Referència al món on està definit el personatge
    private Body cos;                                       // per definir les propietats del cos
    private Sprite spriteBarra, spriteBarra2;               // sprite associat al personatge
    private Texture stoppedTexture, stoppedTextureCremat;   // la seva textura
    private BodyDef defCos;

    private float posicioX, posicioY;
    private int destruir;
    private String pathImg, pathImg2, nom;


    public Troncs(World world, String nom, float posicioX, float posicioY, String pathImg, String pathImg2) {
        this.world = world;
        this.posicioX = posicioX;
        this.posicioY = posicioY;
        this.pathImg = pathImg;
        this.destruir = 0;
        this.nom = nom;
        this.pathImg2 = pathImg2;
        crearProtagonista();
    }


    private void crearProtagonista() {
        stoppedTexture = new Texture(Gdx.files.internal(pathImg));
        spriteBarra = new Sprite(stoppedTexture);

        stoppedTextureCremat = new Texture(Gdx.files.internal(pathImg2));
        spriteBarra2 = new Sprite(stoppedTextureCremat);

        // Definir el tipus de cos i la seva posició
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


    public void updatePosition() {
        if (getDestruir()< 1) {
            spriteBarra.setPosition(
                    JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                            - spriteBarra.getWidth() / FRAME_COLS / 2,
                    JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                            - spriteBarra.getHeight() / FRAME_ROWS / 2);
        }else{
            spriteBarra2.setPosition(
                    JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                            - spriteBarra2.getWidth() / FRAME_COLS / 2,
                    JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                            - spriteBarra2.getHeight() / FRAME_ROWS / 2);
        }
    }

    public void dibuixar(SpriteBatch batch) {
        if (getDestruir() < 1) {
            spriteBarra.draw(batch);
        }else{
            spriteBarra2.draw(batch);
        }
    }

    public int getDestruir() {
        return destruir;
    }

    public void setDestruir(int destruir) {
        this.destruir = destruir;
    }

    public Vector2 getPositionBody() {
        return this.cos.getPosition();
    }

    public Vector2 getPositionSprite() {
        return new Vector2().set(this.spriteBarra.getX(), this.spriteBarra.getY());
    }

    public String getNom() {
        return nom;
    }

    public void dispose() {
        stoppedTexture.dispose();
    }
}