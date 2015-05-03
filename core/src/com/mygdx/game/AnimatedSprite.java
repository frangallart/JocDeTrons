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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Class que implementa un Sprite animat
 */
public class AnimatedSprite {
    /**
     * Enumeració per les direccions
     */
    public enum Direction {LEFT, RIGHT, STOPPED};

    private Sprite sprite;
    private Animation animation;
    private TextureRegion[] framesLeft, framesRight;
    private Texture frame, frameE;
    private int textureCols, textureRows;
    private Direction direction;

    private float stateTime;

    /**
     * Constructor que utilitza el drac
     *
     * @param sprite        sprite associat al personatge
     * @param textureCols   columnes de la textura
     * @param textureRows   files de la textura
     * @param stoppedTexture textura a utilitzar quan el personatge està aturat
     */

    public AnimatedSprite(Sprite sprite, int textureCols, int textureRows, Texture stoppedTexture) {
        Texture framesTexture = sprite.getTexture();
        TextureRegion[][] tmp = TextureRegion.split(framesTexture,
                                                    framesTexture.getWidth() / textureCols,
                                                    framesTexture.getHeight() / textureRows);

        this.sprite = sprite;
        this.textureCols = textureCols;
        this.textureRows = textureRows;
        frame = stoppedTexture;
        direction = Direction.STOPPED;

        framesLeft = new TextureRegion[textureCols];
        for (int j = 0; j < textureCols; j++) {
            framesLeft[j] = tmp[1][j];
        }

        framesRight = new TextureRegion[textureCols];
        for (int j = 0; j < textureCols; j++) {
            framesRight[j] = tmp[0][j];
        }

        animation = new Animation(0.25f, framesRight);
        stateTime = 0f;
    }

    /**
     * Constructor que utilitza el personatge
     *
     * @param sprite            sprite associat al personatge
     * @param textureCols       columnes de la textura
     * @param textureRows       files de la textura
     * @param stoppedTexture    textura a utilitzar quan el personatge està aturat mirant cap a la dreta
     * @param stoppedTextureE   textura a utilitzar quan el personatge està aturat mirant cap a l'esquerra
     */
    public AnimatedSprite(Sprite sprite, int textureCols, int textureRows, Texture stoppedTexture, Texture stoppedTextureE) {
        Texture framesTexture = sprite.getTexture();
        TextureRegion[][] tmp = TextureRegion.split(framesTexture,
                framesTexture.getWidth() / textureCols,
                framesTexture.getHeight() / textureRows);

        this.sprite = sprite;
        this.textureCols = textureCols;
        this.textureRows = textureRows;
        frame = stoppedTexture;
        frameE = stoppedTextureE;

        direction = Direction.STOPPED;

        framesLeft = new TextureRegion[textureCols];
        for (int j = 0; j < textureCols; j++) {
            framesLeft[j] = tmp[1][j];
        }

        framesRight = new TextureRegion[textureCols];
        for (int j = 0; j < textureCols; j++) {
            framesRight[j] = tmp[0][j];
        }

        animation = new Animation(0.25f, framesRight);
        stateTime = 0f;
    }

    /**
     * Constructor que l'utilitzn els monstres, les vides, les claus, boles de foc
     * @param sprite
     * @param textureCols
     * @param textureRows
     */
    public AnimatedSprite(Sprite sprite, int textureCols, int textureRows) {
        Texture framesTexture = sprite.getTexture();
        TextureRegion[][] tmp = TextureRegion.split(framesTexture,
                framesTexture.getWidth() / textureCols,
                framesTexture.getHeight() / textureRows);

        this.sprite = sprite;
        this.textureCols = textureCols;
        this.textureRows = textureRows;

        direction = Direction.STOPPED;

        framesLeft = new TextureRegion[textureCols];
        for (int j = 0; j < textureCols; j++) {
            framesLeft[j] = tmp[1][j];
        }

        framesRight = new TextureRegion[textureCols];
        for (int j = 0; j < textureCols; j++) {
            framesRight[j] = tmp[0][j];
        }

        animation = new Animation(0.25f, framesRight);

        stateTime = 0f;
    }

    /**
     * Dibuixar l'sprite de les boles, els monstres, les vides
     *
     * @param spriteBatch
     */
    public void draw(SpriteBatch spriteBatch) {
        if (direction == Direction.STOPPED) {
            if (frame != null) {
                spriteBatch.draw(frame, sprite.getX(), sprite.getY());
            }
        } else {
            stateTime += Gdx.graphics.getDeltaTime() * 2;
            spriteBatch.draw(animation.getKeyFrame(stateTime, true), sprite.getX(), sprite.getY());
        }
    }

    /**
     * Dibuixar l'sprite del personatge i el drac
     *
     * @param spriteBatch
     * @param cara          s'utilitza per saber l'orientació que s'ha de pintar l'animació
     * @param atac          s'utilitza per saber si el personatge o el drac està en mode atac per dibuixar l'animació corresponent
     */
    public void draw(SpriteBatch spriteBatch, boolean cara, boolean atac) {
        if (atac) {
            stateTime += Gdx.graphics.getDeltaTime() * 2;

            if (cara) {
                animation = new Animation(0.25f, framesRight);
            }else{
                animation = new Animation(0.25f, framesLeft);
            }
            spriteBatch.draw(animation.getKeyFrame(stateTime, true), sprite.getX(), sprite.getY());

        }else {
            if (direction == Direction.STOPPED) {
                if (cara) {
                    spriteBatch.draw(frame, sprite.getX(), sprite.getY());
                } else {
                    spriteBatch.draw(frameE, sprite.getX(), sprite.getY());
                }
            } else {
                stateTime += Gdx.graphics.getDeltaTime() * 2;
                spriteBatch.draw(animation.getKeyFrame(stateTime, true), sprite.getX(), sprite.getY());
            }
        }
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        if (direction == Direction.LEFT) {
            animation = new Animation(0.25f, framesLeft);
        } else {
            animation = new Animation(0.25f, framesRight);
        }
    }

    public void setStoppedTexture(Texture texture) {
        this.frame = texture;
    }
}