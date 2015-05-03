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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Classe TiledMapHelper. Aquesta classe construeix el mapa
 */

public class TiledMapHelper {
	private FileHandle packFileDirectory;

	private OrthographicCamera camera;

	private TextureAtlas tileAtlas;
	private TiledMapRenderer tileMapRenderer;

	private TiledMap map;

	/**
	 * Renders the part of the map that should be visible to the user.
	 */
	public void render() {
		tileMapRenderer.setView(camera);
		tileMapRenderer.render();
	}

	
	public int getMapHeight() {		
		return map.getProperties().get("height", Integer.class);
	}
	
	public int getMapWidth() {
		return map.getProperties().get("width", Integer.class);
	}

	public int getTileHeight() {
		return map.getProperties().get("tileheight", Integer.class);
	}
	
	public int getTileWidth() {
		return map.getProperties().get("tilewidth", Integer.class);
	}
	
	/**
	 * Get the height of the map in pixels
	 * 
	 * @return y
	 */
	public int getHeight() {
		return getMapHeight() * getTileHeight();
	}

	/**
	 * Get the width of the map in pixels
	 * 
	 * @return x
	 */
	public int getWidth() {
		return getMapWidth() * getTileWidth();
	}

	/**
	 * Get the map, useful for iterating over the set of tiles found within
	 * 
	 * @return TiledMap
	 */
	public TiledMap getMap() {
		return map;
	}

	/**
	 * Calls dispose on all disposable resources held by this object.
	 */
	public void dispose() {
		tileAtlas.dispose();
	}

	/**
	 * Sets the directory that holds the game's pack files and tile sets.
	 * 
	 * @param packDirectory
	 */
	public void setPackerDirectory(String packDirectory) {
		packFileDirectory = Gdx.files.internal(packDirectory);
	}

	/**
	 * Loads the requested tmx map file in to the helper.
	 * 
	 * @param tmxFile
	 */
	public void loadMap(String tmxFile) {
		if (packFileDirectory == null) {
			throw new IllegalStateException("loadMap() called out of sequence");
		}

		map = new TmxMapLoader().load(tmxFile);
		tileMapRenderer = new OrthogonalTiledMapRenderer(map);
	}


	/**
	 * Prepares the helper's camera object for use.
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 */
	public void prepareCamera(int screenWidth, int screenHeight) {
		camera = new OrthographicCamera(screenWidth, screenHeight);

		camera.position.set(screenWidth/2, screenHeight/2, 0);

	}

	/**
	 * Returns the camera object created for viewing the loaded map.
	 * 
	 * @return OrthographicCamera
	 */
	public OrthographicCamera getCamera() {
		if (camera == null) {
			throw new IllegalStateException(
					"getCamera() called out of sequence");
		}
		return camera;
	}

}
