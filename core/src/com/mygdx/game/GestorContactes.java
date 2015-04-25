package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
/**
 * Classe que implementa la interface de gesti� de contactes
 * 
 * @author Marc
 *
 */
public class GestorContactes implements ContactListener {
	// de moment, no implementat
	private ArrayList<Body> bodyDestroyList;
	private Personatge personatge;

	public GestorContactes() {
		
	}
	
	public GestorContactes(ArrayList<Body> bodyDestroyList) {
		this.bodyDestroyList = bodyDestroyList;
	}

	public GestorContactes(ArrayList<Body> bodyDestroyList,Personatge personatge) {
		this.bodyDestroyList = bodyDestroyList;
		this.personatge = personatge;
	}

	private World world = new World(new Vector2(0.0f, -9.8f), true);

	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		Gdx.app.log("beginContact", "entre " + fixtureA.toString() + " i "
				+ fixtureB.toString());

		if (fixtureA.getBody().getUserData() == null
				|| fixtureB.getBody().getUserData() == null) {
			return;
		}

		if (fixtureA.getBody().getUserData().equals("personatge")&& fixtureB.getBody().getUserData().equals("monstre1")
				|| fixtureA.getBody().getUserData().equals("monstre1")&& fixtureB.getBody().getUserData().equals("personatge")
				|| fixtureA.getBody().getUserData().equals("personatge")&& fixtureB.getBody().getUserData().equals("monstre2")
				|| fixtureA.getBody().getUserData().equals("monstre2")&& fixtureB.getBody().getUserData().equals("personatge")) {
			Gdx.app.log("HIT", "stark ha topat amb el primer objecte");

			/*
			 * Afegir cos a destruir
			 * 
			 */
			if(!fixtureA.getBody().getUserData().equals("personatge")) {
				if (fixtureB.getBody().getPosition().y > (fixtureA.getBody().getPosition().y + 0.7f)) {
					bodyDestroyList.add(fixtureA.getBody());
				}else{
					personatge.setVides(personatge.getVides() - 1);
				}
			} else {
				if (fixtureA.getBody().getPosition().y > (fixtureB.getBody().getPosition().y + 0.7f)) {
					bodyDestroyList.add(fixtureB.getBody());
				}else{
					personatge.setVides(personatge.getVides() - 1);
				}
			}
		}


	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
