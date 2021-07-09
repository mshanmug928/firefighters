package com.appian.firefighters;

import java.util.ArrayList;
import java.util.List;

import com.appian.api.City;
import com.appian.api.CityNode;
import com.appian.api.FireDispatch;
import com.appian.api.Firefighter;
import com.appian.api.Building;
import com.appian.api.exceptions.InvalidFirefighterException;
import com.appian.api.exceptions.NoFirefighterFoundException;
import com.appian.api.exceptions.OutOfCityBoundsException;
import com.appian.api.exceptions.NoFireFoundException;
import com.appian.firefighters.FirefighterImpl;

public class FireDispatchImpl implements FireDispatch {
  private final City city;
  private List<Firefighter> firefighters;

  public FireDispatchImpl(City city) {
    this.city = city;
  }

  private void addFirefighters(int numFirefightersToAdd) {
    if (firefighters == null) {
      throw new RuntimeException("Firefighters unexpectedly null");
    }

    while (numFirefightersToAdd > 0) {
      Firefighter firefighter = new FirefighterImpl(city.getFireStation().getLocation());
      firefighters.add(firefighter);
      numFirefightersToAdd--;
    }
  }

  @Override
  public void setFirefighters(int numFirefighters) throws InvalidFirefighterException {
    if (numFirefighters < 1) {
      throw new InvalidFirefighterException(numFirefighters);
    }

    // Initial case
    if (firefighters == null) {
      firefighters = new ArrayList<>(numFirefighters);
      addFirefighters(numFirefighters);
      return;
    }

    int currentNumFirefighters = firefighters.size();
    if (currentNumFirefighters == numFirefighters) {
      // No changes in number of fire fighters
      return;
    }

    // Handle increase in number of firefighters
    if (numFirefighters > currentNumFirefighters) {
      numFirefighters -= currentNumFirefighters;
      addFirefighters(numFirefighters);
      return;
    }

    // Handle reduction in number of firefighters
    if (currentNumFirefighters > numFirefighters) {
      int startIdx = currentNumFirefighters - numFirefighters;
      firefighters.subList(startIdx, currentNumFirefighters).clear();
      return;
    }

    return;
  }

  @Override
  public List<Firefighter> getFirefighters() {
    return firefighters;
  }

  @Override
  public void dispatchFirefighers(CityNode... burningBuildings) {
    if (firefighters == null) {
      throw new NoFirefighterFoundException();
    }

    int firefighterIndex = 0;
    for (CityNode node : burningBuildings) {
      Building burningBuilding;
      try {
         burningBuilding = city.getBuilding(node);
         if (burningBuilding.isFireproof()) {
           continue;
         }

         // There is no requirement to use the closest firefighter to reach the fire.
         // So, dispatch the first available firefighter to extinguish the fire.
         if (firefighterIndex == firefighters.size()) {
           // Reset the index to start from begining
           firefighterIndex = 0;
         }

         Firefighter fighterToExtinguish = firefighters.get(firefighterIndex);
         burningBuilding.extinguishFire();
         // Update the location and distance traveled
         fighterToExtinguish.updateLocation(burningBuilding.getLocation());
         firefighterIndex++;
      }
      catch (OutOfCityBoundsException outOfCityException) {
        System.out.println("Building:" + node + " is not inside the city.");
      }
      catch (NoFireFoundException noFireFoundException) {
        System.out.println("Building:" + node + " is not in fire.");
      }
    }
  }
}
