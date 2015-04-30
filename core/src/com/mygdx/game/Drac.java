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


public class Drac {

    public  int frameCols, frameRows;
    private final int PUNTS = 100;
    /**
     * Detectar el moviment
     */
    private boolean moureEsquerra;
    private boolean moureDreta;
    private boolean personatgeCaraDreta;

    public boolean isAtacant() {
        return atacant;
    }

    public void setAtacant(boolean atacant) {
        this.atacant = atacant;
    }

    private boolean atacant;
    private float posX,posY,posMax,posMin, velocitat;

    private String nom, pathTextura, pathImg;

    private World world;                // Refer?ncia al mon on est? definit el personatge
    private Body cos;                   // per definir les propietats del cos
    private Sprite spritePersonatge;    // sprite associat al personatge
    private AnimatedSprite spriteAnimat;// animaci? de l'sprite
    private Texture stoppedTexture,animatedTexture;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Drac(World world,String nom, float posX, float posY, float posMax, float posMin, float velocitat, String pathTextura, String pathImg, int frameCols, int frameRows){
        moureEsquerra = false;
        moureDreta = true;
        this.nom = nom;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.posMax = posMax;
        this.posMin = posMin;
        this.velocitat = velocitat;
        this.pathTextura = pathTextura;
        this.pathImg = pathImg;
        this.frameCols = frameCols;
        this.frameRows = frameRows;
        this.atacant = false;
        carregarTextures();
        crearProtagonista();
        inicialitzarMoviments();
    }

    private void carregarTextures() {
        animatedTexture = new Texture(Gdx.files.internal(this.pathTextura));
        animatedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        stoppedTexture = new Texture(Gdx.files.internal(this.pathImg));
        stoppedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }



    private void crearProtagonista() {
        spritePersonatge = new Sprite(animatedTexture);
        spriteAnimat = new AnimatedSprite(spritePersonatge, frameCols, frameRows, stoppedTexture);

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
        requadre.setAsBox((spritePersonatge.getWidth() / frameCols) / (2 * JocDeTrons.PIXELS_PER_METRE),
                (spritePersonatge.getHeight() / frameRows) / (2 * JocDeTrons.PIXELS_PER_METRE));

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

    public void inicialitzarMoviments() {
        setMoureDreta(false);
        setMoureEsquerra(false);
        spriteAnimat.setDirection(AnimatedSprite.Direction.STOPPED);
    }

    /**
     * Actualitza la posici? de l'sprite
     */
    public void updatePosition() {
        spritePersonatge.setPosition(
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                        - spritePersonatge.getWidth() / frameCols / 2,
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                        - spritePersonatge.getHeight() / frameRows / 2);
        spriteAnimat.setPosition(spritePersonatge.getX(), spritePersonatge.getY());
    }

    public void dibuixar(SpriteBatch batch) {
        spriteAnimat.draw(batch);
    }

    /**
     * Fer que el personatge es mogui
     * <p/>
     * Canvia la posici? del protagonista
     * Es tracta de forma separada el salt perqu? es vol que es pugui moure si salta
     * al mateix temps..
     * <p/>
     * Els impulsos s'apliquen des del centre del protagonista
     */

    public void moure(Personatge personatge) {
        System.out.println(personatge.getPositionBody().x + " " + this.getPositionBody().x);
        if (personatge.getPositionBody().x > 301.02f) {
            if (atacant) {
                spriteAnimat.setDirection(AnimatedSprite.Direction.STOPPED);
            } else if (this.getPositionBody().x <= personatge.getPositionBody().x) {


                if (this.getPositionBody().x < personatge.getPositionBody().x  && this.getPositionBody().x > personatge.getPositionBody().x - 1.5f){
                    atacant = true;
                    System.out.println("prova");
                }else{
                    cos.setLinearVelocity(this.velocitat, 0.0f);
                    spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);
                }
            }

            /*else if (this.getPositionBody().x < personatge.getPositionBody().x - 1.0f && this.getPositionBody().x > personatge.getPositionBody().x - 2.5f) {
                atacant = true;
                System.out.println("prova");
            }
            else if (this.getPositionBody().x <= personatge.getPositionBody().x - 2.6f && !atacant) {
                cos.setLinearVelocity(this.velocitat, 0.0f);
                spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);

            } else if (this.getPositionBody().x + 1.5f > personatge.getPositionBody().x) {
                cos.setLinearVelocity(-this.velocitat, 0.0f);
                spriteAnimat.setDirection(AnimatedSprite.Direction.LEFT);
            }else{
                System.out.println("ffff");
                //spriteAnimat.setDirection(AnimatedSprite.Direction.STOPPED);
            }*/
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

    public int getPUNTS() {
        return PUNTS;
    }
}

