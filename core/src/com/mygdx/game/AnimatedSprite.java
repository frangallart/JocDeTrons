package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Class que implementa un Sprite animat
 *
 * Created by Marc
 */
public class AnimatedSprite {
    /**
     * Enumeració per les direccions
     */
    public enum Direction {LEFT, RIGHT, STOPPED};

    private Sprite sprite;
    private Animation animation, animationAtac;
    private TextureRegion[] framesLeft, framesRight, framesAtac, framesAtacLeft, framesAtacRight;
    private Texture frame, frameE;
    private int textureCols, textureRows;
    private Direction direction;

    private float stateTime;

    /**
     * Constructor
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

        animationAtac = new Animation(0.25f, framesRight);
        stateTime = 0f;
    }

    public AnimatedSprite(Sprite sprite, int textureCols, int textureRows) {
        Texture framesTexture = sprite.getTexture();
        TextureRegion[][] tmp = TextureRegion.split(framesTexture,
                framesTexture.getWidth() / textureCols,
                framesTexture.getHeight() / textureRows);

        this.sprite = sprite;
        this.textureCols = textureCols;
        this.textureRows = textureRows;

        direction = Direction.STOPPED;

        framesAtacLeft = new TextureRegion[textureCols];
        for (int j = 0; j < textureCols; j++) {
            framesAtacLeft[j] = tmp[1][j];
        }

        framesAtacRight = new TextureRegion[textureCols];
        for (int j = 0; j < textureCols; j++) {
            framesAtacRight[j] = tmp[0][j];
        }

        animationAtac = new Animation(0.25f, framesAtacRight);

        stateTime = 0f;
    }

    /**
     * Dibuixar l'sprite
     *
     * @param spriteBatch
     */
    public void draw(SpriteBatch spriteBatch) {
        if (direction == Direction.STOPPED) {
            spriteBatch.draw(frame, sprite.getX(), sprite.getY());
        } else {
            stateTime += Gdx.graphics.getDeltaTime() * 2;
            spriteBatch.draw(animation.getKeyFrame(stateTime, true), sprite.getX(), sprite.getY());
        }
    }

    public void draw(SpriteBatch spriteBatch, boolean cara, boolean atac) {
        if (atac) {
            stateTime += Gdx.graphics.getDeltaTime() * 2;

            if (cara) {
                animationAtac = new Animation(0.25f, framesAtacRight);
            }else{
                animationAtac = new Animation(0.25f, framesAtacLeft);
            }
            spriteBatch.draw(animationAtac.getKeyFrame(stateTime, true), sprite.getX(), sprite.getY());

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