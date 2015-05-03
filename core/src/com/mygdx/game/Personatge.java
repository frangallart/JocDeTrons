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
import com.badlogic.gdx.audio.Sound;
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
 * Classe que implementa el protagonista del joc
 */
public class Personatge {
    public static final int FRAME_COLS = 8;
    public static final int FRAME_ROWS = 2;
    public static final float POS_INICIAL_X = 1f;
    public static final float POS_INICIAL_Y = 5f;
    /**
     * Detectar el moviment
     */
    private boolean moureDreta, moureEsquerra, personatgeCaraDreta, ferSalt, ferAtac, atac, passarNivell;
    private float posX, posY, pes;
    private int vides, punts;

    private World world;                                        // Referència al mon on està definit el personatge
    private Body cos, cosAtac;                                  // per definir les propietats del cos
    private Sprite spritePersonatge, spritePersonatgeAtac;      // sprite associat al personatge
    private AnimatedSprite spriteAnimat, spriteAtac;            // animació de l'sprite
    private Sound soSalt, soAplastar, soEspasa;                 // el so que reprodueix en saltar
    private Texture animatedTexture, animatedTextureAtac, stoppedTexture, stoppedTextureE;     // la seva textura

    private FixtureDef propietats = new FixtureDef();

    private String pathTextura, pathImatge, pathImatgeE, pathImatgeAtac;


    public String getPathImatgeAtac() {
        return pathImatgeAtac;
    }


    public Personatge(World world, int vides, int punts, String pathTextura, String pathImatge, String pathImatgeE, String pathImatgeAtac, float posX, float posY, float pes) {
        moureEsquerra = moureDreta = ferSalt = ferAtac = atac = false;
        this.world = world;
        this.vides = vides;
        this.punts = punts;
        this.pathTextura = pathTextura;
        this.pathImatge = pathImatge;
        this.pathImatgeE = pathImatgeE;
        this.personatgeCaraDreta = true;
        this.pathImatgeAtac = pathImatgeAtac;
        this.passarNivell = false;
        this.posX = posX;
        this.posY = posY;
        this.pes = pes;
        carregarTextures();
        carregarSons();
        crearProtagonista();
    }

    public Personatge(int vides, int punts, String pathTextura, String pathImatge, String pathImatgeE, String pathImatgeAtac, float posX, float posY, float pes) {
        moureEsquerra = moureDreta = ferSalt = ferAtac = atac = false;
        this.vides = vides;
        this.punts = punts;
        this.pathTextura = pathTextura;
        this.pathImatge = pathImatge;
        this.pathImatgeE = pathImatgeE;
        this.personatgeCaraDreta = true;
        this.pathImatgeAtac = pathImatgeAtac;
        this.passarNivell = false;
        this.posX = posX;
        this.posY = posY;
        this.pes = pes;
    }

    private void carregarTextures() {
        animatedTexture = new Texture(Gdx.files.internal(this.pathTextura));
        animatedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        animatedTextureAtac = new Texture(Gdx.files.internal(this.pathImatgeAtac));
        animatedTextureAtac.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        stoppedTexture = new Texture(Gdx.files.internal(this.pathImatge));
        stoppedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        stoppedTextureE = new Texture(Gdx.files.internal(pathImatgeE));
        stoppedTextureE.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    /**
     * Carregar els arxius de so
     */
    private void carregarSons() {
        soSalt = Gdx.audio.newSound(Gdx.files.internal("sons/salt.mp3"));
        soAplastar = Gdx.audio.newSound(Gdx.files.internal("sons/aplastar.mp3"));
        soEspasa = Gdx.audio.newSound(Gdx.files.internal("sons/espases.mp3"));
    }

    public void crearProtagonista() {
        spritePersonatge = new Sprite(animatedTexture);
        spriteAnimat = new AnimatedSprite(spritePersonatge, FRAME_COLS, FRAME_ROWS,stoppedTexture, stoppedTextureE);

        spritePersonatgeAtac = new Sprite(animatedTextureAtac);
        spriteAtac = new AnimatedSprite(spritePersonatgeAtac, 4, FRAME_ROWS);

        cos = novesColisions(null);
    }

    public void inicialitzarMoviments() {
        setMoureDreta(false);
        setMoureEsquerra(false);
        setFerSalt(false);
        setFerAtac(false);
        spriteAnimat.setDirection(AnimatedSprite.Direction.STOPPED);
    }

    public void updatePosition() {

        if (!isFerAtac()) {

            if (atac) {
                cos = novesColisions(cosAtac);
                atac = false;
            }
            spritePersonatge.setPosition(
                    JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                            - spritePersonatge.getWidth() / FRAME_COLS / 2,
                    JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                            - spritePersonatge.getHeight() / FRAME_ROWS / 2);
            spriteAnimat.setPosition(spritePersonatge.getX(), spritePersonatge.getY());
        }else {
            if (!atac){
                atac = true;
                cosAtac = novesColisions(cos);
            }
            spritePersonatgeAtac.setPosition(
                    JocDeTrons.PIXELS_PER_METRE * cosAtac.getPosition().x
                            - spritePersonatgeAtac.getWidth() / FRAME_COLS / 2,
                    JocDeTrons.PIXELS_PER_METRE * cosAtac.getPosition().y
                            - spritePersonatgeAtac.getHeight() / FRAME_ROWS / 2);
            spriteAtac.setPosition(spritePersonatgeAtac.getX(), spritePersonatgeAtac.getY());
        }
    }

    /**
     * Mètode que crea les colisions dels personatges segons estiguin en posició
     * d'atacar o en posició normal.
     */
    private Body novesColisions(Body cos) {
        if (cos != null){
            world.destroyBody(cos);
        }

        BodyDef defCos = new BodyDef();
        defCos.type = BodyDef.BodyType.DynamicBody;

        if (cos != null) {
            defCos.position.set(this.getPositionBody().x, this.getPositionBody().y);
        }else{
            defCos.position.set(posX, posY);
        }
        cos = world.createBody(defCos);
        cos.setUserData("personatge");

        /**
         * La densitat i fricció del protagonista. Si es modifiquen aquests
         * valor anirà més ràpid o més lent.
         */

        if (isFerAtac()) {
            PolygonShape requadre2 = new PolygonShape();
            requadre2.setAsBox((spritePersonatgeAtac.getWidth() / 4) / (2 * JocDeTrons.PIXELS_PER_METRE),
                    (spritePersonatgeAtac.getHeight() / FRAME_ROWS) / (2 * JocDeTrons.PIXELS_PER_METRE));
            propietats.shape = requadre2;
        }else{
            PolygonShape requadre = new PolygonShape();
            requadre.setAsBox((spritePersonatge.getWidth() / FRAME_COLS) / (2 * JocDeTrons.PIXELS_PER_METRE),
                    (spritePersonatge.getHeight() / FRAME_ROWS) / (2 * JocDeTrons.PIXELS_PER_METRE));
            propietats.shape = requadre;
        }
        propietats.density = pes;
        propietats.friction = 3.0f;

        cos.setFixedRotation(true);
        cos.createFixture(propietats);
        return cos;
    }

    public void dibuixar(SpriteBatch batch) {
        if (this.isFerAtac()) {
            this.soEspasa.stop();
            this.soEspasa.play();
            spriteAtac.draw(batch, this.isCaraDreta(), isFerAtac());
        }else{
            spriteAnimat.draw(batch, this.isCaraDreta(), isFerAtac());
        }
    }

    /**
     * Fer que el personatge es mogui
     * Canvia la posició del protagonista
     * Es tracta de forma separada el salt perquè es vol que es pugui moure si salta
     * al mateix temps..
     * Els impulsos s'apliquen des del centre del protagonista
     */
    public void moure() {

        if (moureDreta && cos.getLinearVelocity().x < 3.0f) {
            cos.applyLinearImpulse(new Vector2(0.1f, 0.0f),  // linearVelocity
                    cos.getWorldCenter(), true);

        } else if (moureEsquerra) {
            if (cos.getLinearVelocity().x > -3.0f) {
                cos.applyLinearImpulse(new Vector2(-0.1f, 0.0f),
                        cos.getWorldCenter(), true);
            }
        }

        if (ferSalt && Math.abs(cos.getLinearVelocity().y) < 1e-9) {
            cos.applyLinearImpulse(new Vector2(0.0f, 1.5f),
                    cos.getWorldCenter(), true);

            long id = soSalt.play();
        }

        if (moureDreta) {
            spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);

            if (!personatgeCaraDreta) {
                spritePersonatge.flip(true, false);
            }
            personatgeCaraDreta = true;
        } else if (moureEsquerra) {
            spriteAnimat.setDirection(AnimatedSprite.Direction.LEFT);
            if (personatgeCaraDreta) {
                spritePersonatge.flip(true, false);
            }
            personatgeCaraDreta = false;
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

    public boolean isFerSalt() {
        return ferSalt;
    }

    public void setFerSalt(boolean ferSalt) {
        this.ferSalt = ferSalt;
    }

    public boolean isCaraDreta() {
        return this.personatgeCaraDreta;
    }

    public void setCaraDreta(boolean caraDreta) {
        this.personatgeCaraDreta = caraDreta;

    }

    public String getPathTextura() {
        return pathTextura;
    }

    public void setPathTextura(String pathTextura) {
        this.pathTextura = pathTextura;
    }

    public String getPathImatge() {
        return pathImatge;
    }

    public void setPathImatge(String pathImatge) {
        this.pathImatge = pathImatge;
    }
    public Sound getSoSalt() {
        return soSalt;
    }

    public void setSoSalt(Sound soSalt) {
        this.soSalt = soSalt;
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

    public int getVides() {
        return vides;
    }

    public void setVides(int vides) {
        this.vides = vides;
    }

    public int getPunts() {
        return punts;
    }

    public void setPunts(int punts) {
        this.punts = punts;
    }

    public String getPathImatgeE() {
        return pathImatgeE;
    }

    public void setPathImatgeE(String pathImatgeE) {
        this.pathImatgeE = pathImatgeE;
    }

    public boolean isFerAtac() {
        return ferAtac;
    }

    public void setFerAtac(boolean ferAtac) {
        this.ferAtac = ferAtac;
    }

    public boolean isPassarNivell() {
        return passarNivell;
    }

    public void setPassarNivell(boolean passarNivell) {
        this.passarNivell = passarNivell;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getPes() {
        return pes;
    }

    public Sound getSoAplastar() {
        return soAplastar;
    }

    public void dispose() {
        animatedTexture.dispose();
        stoppedTexture.dispose();
        soSalt.dispose();
    }
}
