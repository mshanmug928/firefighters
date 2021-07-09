package com.appian.firefighters;

import com.appian.api.CityNode;
import com.appian.api.Firefighter;

public class FirefighterImpl implements Firefighter {
  private CityNode currentLocation;
  private int distanceTraveled;

  public FirefighterImpl(CityNode firestationLocation) {
    this.currentLocation = firestationLocation;
  }

  @Override
  public CityNode getLocation() {
    return this.currentLocation;
  }

  @Override
  public int distanceTraveled() {
    return this.distanceTraveled;
  }

  @Override
  public void updateLocation(CityNode node) {
    if (node == null) {
      System.out.println("Invalid city node");
      return;
    }

    int distanceTraveled = findDistanceTraveled(node);
    this.currentLocation = node;
    this.distanceTraveled += distanceTraveled;
  }

  private int findDistanceTraveled(CityNode node) {
    int xDiff = Math.abs(node.getX() - this.currentLocation.getX());
    int yDiff = Math.abs(node.getY() - this.currentLocation.getY());
    return xDiff + yDiff;
  }
}
