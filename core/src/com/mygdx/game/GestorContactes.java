package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.ArrayList;

/**
 * Classe que implementa la interface de gesti� de contactes
 *
 * @author Marc
 */
public class GestorContactes implements ContactListener {
    // de moment, no implementat
    private ArrayList<Body> bodyDestroyList;
    private ArrayList<BolesFocMonstre> boles;
    private ArrayList<Monstre> monstres;
    private ArrayList<MonstreLava> monstreLava;
    private Personatge personatge;

    public GestorContactes() {

    }

    public GestorContactes(ArrayList<Body> bodyDestroyList) {
        this.bodyDestroyList = bodyDestroyList;
    }

    public GestorContactes(ArrayList<Body> bodyDestroyList, Personatge personatge, ArrayList<Monstre> monstres,
                           ArrayList<MonstreLava> monstresLava, ArrayList<BolesFocMonstre> boles) {
        this.bodyDestroyList = bodyDestroyList;
        this.personatge = personatge;
        this.monstres = monstres;
        this.monstreLava = monstresLava;
        this.boles = boles;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Gdx.app.log("beginContact", "entre " + fixtureA.getBody().getUserData().toString() + " i "
                + fixtureB.getBody().getUserData().toString());

        if (fixtureA.getBody().getUserData() == null
                || fixtureB.getBody().getUserData() == null) {
            return;
        }

        for (int i = boles.size() - 1; i >= 0; i--) {
            if (fixtureA.getBody().getUserData().equals("terraMorir") && fixtureB.getBody().getUserData().equals(boles.get(i).getNom())) {
                bodyDestroyList.add(fixtureB.getBody());
            } else if (fixtureB.getBody().getUserData().equals("terraMorir") && fixtureA.getBody().getUserData().equals(boles.get(i).getNom())) {
                bodyDestroyList.add(fixtureA.getBody());
            } else if (fixtureA.getBody().getUserData().equals("personatge") && fixtureB.getBody().getUserData().equals(boles.get(i).getNom())) {
                personatge.setVides(personatge.getVides() - 1);
            } else if (fixtureB.getBody().getUserData().equals("personatge") && fixtureA.getBody().getUserData().equals(boles.get(i).getNom())) {
                personatge.setVides(personatge.getVides() - 1);
            } else if (fixtureB.getBody().getUserData().equals(boles.get(0).getNom()) && fixtureA.getBody().getUserData().equals(boles.get(1).getNom())) {
                bodyDestroyList.add(fixtureA.getBody());
                bodyDestroyList.add(fixtureB.getBody());
            }else if (fixtureA.getBody().getUserData().equals(boles.get(0).getNom()) && fixtureB.getBody().getUserData().equals(boles.get(1).getNom())) {
                bodyDestroyList.add(fixtureA.getBody());
                bodyDestroyList.add(fixtureB.getBody());
            } else if (fixtureA.getBody().getUserData().equals("Barra") && fixtureB.getBody().getUserData().equals(boles.get(i).getNom())) {
                bodyDestroyList.add(fixtureB.getBody());
            }else if (fixtureB.getBody().getUserData().equals("Barra") && fixtureA.getBody().getUserData().equals(boles.get(i).getNom())) {
                bodyDestroyList.add(fixtureA.getBody());
            }
        }
            /*
			 * Afegir cos a destruir
			 * 
			 */
        for (int i = monstres.size() - 1; i >= 0; i--) {

            if (fixtureA.getBody().getUserData().equals("personatge") && fixtureB.getBody().getUserData().equals(monstres.get(i).getNom())) {
                if (fixtureA.getBody().getPosition().y > (fixtureB.getBody().getPosition().y + 0.7f)) {
                    bodyDestroyList.add(fixtureB.getBody());
                } else {
                    personatge.setVides(personatge.getVides() - 1);
                }

            } else if (fixtureB.getBody().getUserData().equals("personatge") && fixtureA.getBody().getUserData().equals(monstres.get(i).getNom())) {
                if (fixtureB.getBody().getPosition().y > (fixtureA.getBody().getPosition().y + 0.7f)) {
                    bodyDestroyList.add(fixtureA.getBody());
                } else {
                    personatge.setVides(personatge.getVides() - 1);
                }
            }
        }

        for (int monstresLava = monstreLava.size() - 1; monstresLava >= 0; monstresLava--) {
            if (fixtureB.getBody().getUserData().equals("personatge") && fixtureA.getBody().getUserData().equals(monstreLava.get(monstresLava).getNom())) {
                personatge.setVides(personatge.getVides() - 1);
            }else if (fixtureA.getBody().getUserData().equals("personatge") && fixtureB.getBody().getUserData().equals(monstreLava.get(monstresLava).getNom())) {
                personatge.setVides(personatge.getVides() - 1);
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
