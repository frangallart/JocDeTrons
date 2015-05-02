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
    private ArrayList<MonstreEstatic> monstreEstatic;
    private ArrayList<Troncs> troncs;
    private Personatge personatge;

    public GestorContactes() {

    }

    public GestorContactes(ArrayList<Body> bodyDestroyList) {
        this.bodyDestroyList = bodyDestroyList;
    }

    public GestorContactes(ArrayList<Body> bodyDestroyList, Personatge personatge, ArrayList<Monstre> monstres,
                           ArrayList<MonstreEstatic> monstresLava, ArrayList<BolesFocMonstre> boles, ArrayList<Troncs> troncs) {
        this.bodyDestroyList = bodyDestroyList;
        this.personatge = personatge;
        this.monstres = monstres;
        this.monstreEstatic = monstresLava;
        this.boles = boles;
        this.troncs = troncs;
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

        if (boles != null && troncs == null) {
            for (int i = boles.size() - 1; i >= 0; i--) {
                if (fixtureA.getBody().getUserData().equals("terraMorir") && fixtureB.getBody().getUserData().equals(boles.get(i).getNom())) {
                    bodyDestroyList.add(fixtureB.getBody());
                    break;
                } else if (fixtureB.getBody().getUserData().equals("terraMorir") && fixtureA.getBody().getUserData().equals(boles.get(i).getNom())) {
                    bodyDestroyList.add(fixtureA.getBody());
                    break;
                } else if (fixtureA.getBody().getUserData().equals("personatge") && fixtureB.getBody().getUserData().equals(boles.get(i).getNom())) {
                    personatge.setVides(personatge.getVides() - 1);
                    break;
                } else if (fixtureB.getBody().getUserData().equals("personatge") && fixtureA.getBody().getUserData().equals(boles.get(i).getNom())) {
                    personatge.setVides(personatge.getVides() - 1);
                    break;
                } else if (fixtureB.getBody().getUserData().equals(boles.get(0).getNom()) && fixtureA.getBody().getUserData().equals(boles.get(1).getNom())) {
                    bodyDestroyList.add(fixtureA.getBody());
                    bodyDestroyList.add(fixtureB.getBody());
                    break;
                } else if (fixtureA.getBody().getUserData().equals(boles.get(0).getNom()) && fixtureB.getBody().getUserData().equals(boles.get(1).getNom())) {
                    bodyDestroyList.add(fixtureA.getBody());
                    bodyDestroyList.add(fixtureB.getBody());
                    break;
                } else if (fixtureA.getBody().getUserData().equals("Barra") && fixtureB.getBody().getUserData().equals(boles.get(i).getNom())) {
                    bodyDestroyList.add(fixtureB.getBody());
                    break;
                } else if (fixtureB.getBody().getUserData().equals("Barra") && fixtureA.getBody().getUserData().equals(boles.get(i).getNom())) {
                    bodyDestroyList.add(fixtureA.getBody());
                    break;
                } else if (fixtureB.getBody().getUserData().equals("monstreLava1") && fixtureA.getBody().getUserData().equals(boles.get(i).getNom())) {
                    bodyDestroyList.add(fixtureA.getBody());
                    break;
                } else if (fixtureA.getBody().getUserData().equals("monstreLava1") && fixtureB.getBody().getUserData().equals(boles.get(i).getNom())) {
                    bodyDestroyList.add(fixtureB.getBody());
                    break;
                } else if (fixtureB.getBody().getUserData().equals("monstreLava2") && fixtureA.getBody().getUserData().equals(boles.get(i).getNom())) {
                    bodyDestroyList.add(fixtureA.getBody());
                    break;
                } else if (fixtureA.getBody().getUserData().equals("monstreLava2") && fixtureB.getBody().getUserData().equals(boles.get(i).getNom())) {
                    bodyDestroyList.add(fixtureB.getBody());
                    break;
                }
            }
        }

        for (int i = monstres.size() - 1; i >= 0; i--) {

            if (fixtureA.getBody().getUserData().equals("personatge") && fixtureB.getBody().getUserData().equals(monstres.get(i).getNom())) {
                if ((fixtureA.getBody().getPosition().y > (fixtureB.getBody().getPosition().y + 0.5f)) || personatge.isFerAtac()) {
                    bodyDestroyList.add(fixtureB.getBody());
                } else {
                    personatge.setVides(personatge.getVides() - 1);
                }

            } else if (fixtureB.getBody().getUserData().equals("personatge") && fixtureA.getBody().getUserData().equals(monstres.get(i).getNom())) {
                if ((fixtureB.getBody().getPosition().y > (fixtureA.getBody().getPosition().y + 0.5f)) || personatge.isFerAtac()) {
                    bodyDestroyList.add(fixtureA.getBody());
                } else {
                    personatge.setVides(personatge.getVides() - 1);
                }
            }
        }

        if (troncs != null && boles != null) {
            for (int i = boles.size() - 1; i >= 0; i--) {
                for (int numTroncs = troncs.size() - 1; numTroncs >= 0; numTroncs--) {
                    if (fixtureA.getBody().getUserData().equals(troncs.get(numTroncs).getNom()) && fixtureB.getBody().getUserData().equals(boles.get(i).getNom())) {
                        if (troncs.get(numTroncs).getDestruir() < 2){
                            troncs.get(numTroncs).setDestruir(troncs.get(numTroncs).getDestruir() + 1);
                            bodyDestroyList.add(fixtureB.getBody());
                        }else{
                            bodyDestroyList.add(fixtureA.getBody());
                            bodyDestroyList.add(fixtureB.getBody());
                        }
                    }else if (fixtureB.getBody().getUserData().equals(troncs.get(numTroncs).getNom()) && fixtureA.getBody().getUserData().equals(boles.get(i).getNom())) {
                        if (troncs.get(numTroncs).getDestruir() < 2) {
                            troncs.get(numTroncs).setDestruir(troncs.get(numTroncs).getDestruir() + 1);
                            bodyDestroyList.add(fixtureA.getBody());
                        } else {
                            bodyDestroyList.add(fixtureB.getBody());
                            bodyDestroyList.add(fixtureA.getBody());
                        }
                    }else if (fixtureA.getBody().getUserData().equals("personatge") && fixtureB.getBody().getUserData().equals(boles.get(i).getNom())) {
                        personatge.setVides(personatge.getVides() - 1);
                        break;
                    }
                    else if (fixtureB.getBody().getUserData().equals("personatge") && fixtureA.getBody().getUserData().equals(boles.get(i).getNom())) {
                        personatge.setVides(personatge.getVides() - 1);
                        break;
                    }
                }

                if (fixtureA.getBody().getUserData().equals("terraMorir") && fixtureB.getBody().getUserData().equals(boles.get(i).getNom())) {
                    bodyDestroyList.add(fixtureB.getBody());
                }else if (fixtureB.getBody().getUserData().equals("terraMorir") && fixtureA.getBody().getUserData().equals(boles.get(i).getNom())) {
                    bodyDestroyList.add(fixtureA.getBody());
                }
            }
        }

        if (fixtureA.getBody().getUserData().equals("drac") && fixtureB.getBody().getUserData().equals("tronc1")) {
            bodyDestroyList.add(fixtureB.getBody());
        }

        if (fixtureA.getBody().getUserData().equals("drac") && fixtureB.getBody().getUserData().equals("tronc2")) {
            bodyDestroyList.add(fixtureB.getBody());
        }

        if (fixtureA.getBody().getUserData().equals("drac") && fixtureB.getBody().getUserData().equals("tronc3")) {
            bodyDestroyList.add(fixtureB.getBody());
        }
        if (fixtureA.getBody().getUserData().equals("drac") && fixtureB.getBody().getUserData().equals("tronc4")) {
            bodyDestroyList.add(fixtureB.getBody());
        }

        if (monstreEstatic != null) {
            for (int monstresLava = monstreEstatic.size() - 1; monstresLava >= 0; monstresLava--) {
                if (fixtureB.getBody().getUserData().equals("personatge") && fixtureA.getBody().getUserData().equals(monstreEstatic.get(monstresLava).getNom())) {
                    personatge.setVides(personatge.getVides() - 1);
                } else if (fixtureA.getBody().getUserData().equals("personatge") && fixtureB.getBody().getUserData().equals(monstreEstatic.get(monstresLava).getNom())) {
                    personatge.setVides(personatge.getVides() - 1);
                }
            }
        }

        if (fixtureA.getBody().getUserData().equals("Vida") && fixtureB.getBody().getUserData().equals("personatge")) {
            bodyDestroyList.add(fixtureA.getBody());
        }else if (fixtureB.getBody().getUserData().equals("Vida") && fixtureA.getBody().getUserData().equals("personatge")){
            bodyDestroyList.add(fixtureB.getBody());
        }

        if (fixtureA.getBody().getUserData().equals("clau") && fixtureB.getBody().getUserData().equals("personatge")) {
            personatge.setPassarNivell(true);
            bodyDestroyList.add(fixtureA.getBody());
        }else if (fixtureB.getBody().getUserData().equals("clau") && fixtureA.getBody().getUserData().equals("personatge")){
            bodyDestroyList.add(fixtureB.getBody());
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
