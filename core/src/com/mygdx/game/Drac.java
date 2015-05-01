package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;


public class Drac {

    public  int frameCols, frameRows;
    /**
     * Detectar el moviment
     */
    private boolean atac;

    private boolean atacant, tornar;
    private float posX,posY,posMax,posMin, velocitat;

    private String nom, pathTextura, pathImg;

    private World world;                // Refer?ncia al mon on est? definit el personatge
    private Body cos, cosAtac;                   // per definir les propietats del cos
    private Sprite spritePersonatge, spritePersonatgeAtac;    // sprite associat al personatge
    private AnimatedSprite spriteAnimat, spriteAtac;// animaci? de l'sprite
    private Texture stoppedTexture,animatedTexture, animatedTextureAtac;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Drac(World world,String nom, float posX, float posY, float posMax, float posMin, float velocitat, String pathTextura, String pathImg, int frameCols, int frameRows){
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
        this.atac = false;
        this.tornar = false;
        carregarTextures();
        crearProtagonista();
        inicialitzarMoviments();
    }

    private void carregarTextures() {
        animatedTexture = new Texture(Gdx.files.internal(this.pathTextura));
        animatedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        animatedTextureAtac = new Texture(Gdx.files.internal("imatges/dracAtacantDalt.png"));
        animatedTextureAtac.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        stoppedTexture = new Texture(Gdx.files.internal(this.pathImg));
        stoppedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }



    private void crearProtagonista() {
        spritePersonatge = new Sprite(animatedTexture);
        spritePersonatgeAtac = new Sprite(animatedTextureAtac);

        spriteAtac = new AnimatedSprite(spritePersonatgeAtac, 4, frameRows);
        spriteAnimat = new AnimatedSprite(spritePersonatge, frameCols, frameRows, stoppedTexture);

        cos = novesColisions(null);
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
        defCos.type = BodyDef.BodyType.KinematicBody;

        if (cos != null) {
            defCos.position.set(this.getPositionBody().x, this.getPositionBody().y);
        }else{
            defCos.position.set(posX, posY);
        }
        cos = world.createBody(defCos);
        cos.setUserData("drac");

        return cos;
    }

    public void inicialitzarMoviments() {
        spriteAnimat.setDirection(AnimatedSprite.Direction.STOPPED);
    }

    /**
     * Actualitza la posici? de l'sprite
     */
    public void updatePosition() {

        if (!isAtacant()) {
            if (atac) {
                cos = novesColisions(cosAtac);
                atac = false;
            }

            spritePersonatge.setPosition(
                    JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                            - spritePersonatge.getWidth() / frameCols / 2,
                    JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                            - spritePersonatge.getHeight() / frameRows / 2);
            spriteAnimat.setPosition(spritePersonatge.getX(), spritePersonatge.getY());
        }else{
            if (!atac){
                atac = true;
                cosAtac = novesColisions(cos);
            }

            spritePersonatgeAtac.setPosition(
                    JocDeTrons.PIXELS_PER_METRE * cosAtac.getPosition().x
                            - spritePersonatgeAtac.getWidth() / frameCols / 2,
                    JocDeTrons.PIXELS_PER_METRE * cosAtac.getPosition().y
                            - spritePersonatgeAtac.getHeight() / frameRows / 2);
            spriteAtac.setPosition(spritePersonatgeAtac.getX(), spritePersonatgeAtac.getY());
        }
    }

    public void dibuixar(SpriteBatch batch) {
        if (atac) {
            spriteAtac.draw(batch, true, true);
        }else{
            spriteAnimat.draw(batch, true, false);
        }
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
        if (personatge.getPositionBody().x < 321) {
            if (personatge.getPositionBody().x > 301.62f && this.getPositionBody().y < 4f) {
                this.atacant = false;
                cos.setLinearVelocity(2.0f, 3.6f);
                spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);
            } else if (this.getPositionBody().y > 4f) {
                this.atacant = true;
                cos.setLinearVelocity(0f, 0f);
            }

            if ((personatge.getPositionBody().x > 305f && this.getPositionBody().x < 306f)
                    || (personatge.getPositionBody().x > 308.5f && this.getPositionBody().x < 309f)
                    || (personatge.getPositionBody().x > 311.5f && this.getPositionBody().x < 312f)
                    || (personatge.getPositionBody().x > 314.5f && this.getPositionBody().x < 315f)
                    || (personatge.getPositionBody().x > 317.5f && this.getPositionBody().x < 318f)) {
                this.atacant = false;
                cos.setLinearVelocity(4.0f, 0f);
                spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);
            } else if (this.getPositionBody().x > 306f) {
                this.atacant = true;
                cos.setLinearVelocity(0f, 0f);
            }
        }else{
            if (this.getPositionBody().x < 320 && !tornar){
                this.atacant = false;
                spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);
                cos.setLinearVelocity(2.4f, 0f);
            }else{
                tornar = true;
                spriteAnimat.setDirection(AnimatedSprite.Direction.LEFT);
                cos.setLinearVelocity(-2.4f, 0f);
            }
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

    public boolean isAtacant() {
        return atacant;
    }

    public void dispose() {
        animatedTexture.dispose();
        stoppedTexture.dispose();
    }
}

