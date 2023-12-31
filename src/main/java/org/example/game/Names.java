package org.example.game;

import java.util.ArrayList;

public class Names {

  private static Names instance = null;
  final ArrayList<String> names = new ArrayList<>();

  private Names() {
  }

  public static Names getInstance() {
    if (instance == null) {
      instance = new Names();
    }
    return instance;
  }

  public synchronized void add(String name) {
    names.add(name);
  }

  public synchronized ArrayList<String> getNames() {
    return names;
  }

  public void reset() {
    instance = null;
  }
}
