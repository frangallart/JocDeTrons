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
public class PersonatgeBackup {
    /**
     * Detectar el moviment
     */
    private boolean moureEsquerra;
    private boolean moureDreta;
    private boolean ferSalt;
    private boolean personatgeCaraDreta;

    // Refer�ncia al mon on est� definit el personatge
    private World world;

    private Body cos;            // per definir les propietats del cos
    private Sprite spritePersonatge;    // sprite associat al personatge
    private Texture textura;    // la seva textura
    private Sound soSalt;        // el so que reprodueix en saltar

    public PersonatgeBackup() {
        moureEsquerra = moureDreta = ferSalt = false;
    }

    public PersonatgeBackup(World world) {
        this();
        this.world = world;
        carregarTexturaPersonantage();
        carregarSons();
        crearProtagonista();
    }


    private void carregarTexturaPersonantage() {
        textura = new Texture(
                Gdx.files.internal("imatges/personatge.png"));
        textura.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    /**
     * Carregar els arxius de so
     */
    public void carregarSons() {
        soSalt = Gdx.audio.newSound(Gdx.files.internal("sons/salt.mp3"));
    }

    private void crearProtagonista() {

        spritePersonatge = new Sprite(textura, 0, 0, 32, 32);

        // Definir el tipus de cos i la seva posici�
        BodyDef defCos = new BodyDef();
        defCos.type = BodyDef.BodyType.DynamicBody;
        defCos.position.set(1.0f, 3.0f);

        cos = world.createBody(defCos);
        cos.setUserData("Personatge");
        /**
         * Definir les vores de l'sprite
         */
        PolygonShape requadre = new PolygonShape();
        requadre.setAsBox(spritePersonatge.getWidth()
                / (2 * JocDeTrons.PIXELS_PER_METRE), spritePersonatge.getHeight()
                / (2 * JocDeTrons.PIXELS_PER_METRE));

        /**
         * La densitat i fricci� del protagonista. Si es modifiquen aquests
         * valor anir� m�s r�pid o m�s lent.
         */
        FixtureDef propietats = new FixtureDef();
        propietats.shape = requadre;
        propietats.density = 1.0f;
        propietats.friction = 3.0f;

        cos.setFixedRotation(true);

        cos.createFixture(propietats);
        requadre.dispose();

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
        return textura;
    }

    public void setTextura(Texture textura) {
        this.textura = textura;
    }

    public void inicialitzarMoviments() {
        setMoureDreta(false);
        setMoureEsquerra(false);
        setFerSalt(false);
    }

    /**
     * Actualitza la posici� de l'sprite
     */
    public void updatePosition() {
        spritePersonatge.setPosition(
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                        - spritePersonatge.getWidth() / 2,
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                        - spritePersonatge.getHeight() / 2);

        /*animationPersonatge.setPosition(
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                        - animationPersonatge.getWidth() / 2,
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                        - animationPersonatge.getHeight() / 2);*/
    }

    public void dibuixar(SpriteBatch batch) {
        spritePersonatge.draw(batch);
    }

    /**
     * Fer que el personatge es mogui
     * <p/>
     * Canvia la posició del protagonista
     * Es tracta de forma separada el salt perquè es vol que es pugui moure i salta
     * al mateix temps..
     * <p/>
     * Els impulsos s'apliquen des del centre del protagonista
     */
    public void moure() {
        if (moureDreta) {
            cos.applyLinearImpulse(new Vector2(0.05f, 0.0f),
                    cos.getWorldCenter(), true);
            if (personatgeCaraDreta == false) {
                spritePersonatge.flip(true, false);
                //animationPersonatge.flip(true,false);
            }

            personatgeCaraDreta = true;
        } else if (moureEsquerra) {
            cos.applyLinearImpulse(new Vector2(-0.05f, 0.0f),
                    cos.getWorldCenter(), true);
            if (personatgeCaraDreta == true) {
                spritePersonatge.flip(true, false);
                //animationPersonatge.flip(true,false);
            }
            personatgeCaraDreta = false;
        }

        if (ferSalt && Math.abs(cos.getLinearVelocity().y) < 1e-9) {
            cos.applyLinearImpulse(new Vector2(0.0f, 0.8f),
                    cos.getWorldCenter(), true);
            long id = soSalt.play();
            //soSalt.setPitch(id, 2f);
        }
    }

    public void dispose() {
        textura.dispose();
        soSalt.dispose();
    }


}
