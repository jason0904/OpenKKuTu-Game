package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.example.game.Names;

public class Game {

  private static Game instance = null;

  private String currentWord = null;

  private char lastChar = ' ';

  private final ArrayList<String> words = new ArrayList<>();

  private final DataBase db = new DataBase("jdbc:postgresql://localhost:5432/kkutudb", "postgres",
      Passwd.getPasswd());

  private final ClientQueue queue = ClientQueue.getInstance();

  private final Names names = Names.getInstance();
  private static Map<String, Integer> score = new HashMap<>(); //점수 기록용

  private int round;

  private String startWord;

  private boolean injeong = false;
  private boolean manner = false;

  private String language;

  private int time = 0; //친 시간을 기억해놓고 점수계산하는데에 사용하기 위해서.

  private int chain = 0; //chain까지 정해놔야 점수계산할때 사용할 수 있네?

  public static synchronized Game getInstance() {
    if (instance == null) {
      instance = new Game();
    }
    return instance;
  }

  private Game() {
    db.connect();
    for(String name : names.getNames()) {
      score.put(name, 0);
    }
  }

  public synchronized String getCurrentWord() {
    return this.currentWord;
  }

  public synchronized void setCurrentWord(String currentWord) {
    this.currentWord = currentWord;
  }

  public synchronized char getLastChar() {
    return this.lastChar;
  }

  public synchronized void setLastChar(char lastChar) {
    this.lastChar = lastChar;
  }

  public synchronized boolean check(String tmp) {
    if(tmp == null || tmp.isEmpty()) {
      return false;
    }
    //어인정일때 어인정부터 검사
    if(injeong) {
      if(tmp.charAt(0) == getLastChar() && db.select(tmp, getLanguage(), true) && !words.contains(tmp)) {
        words.add(tmp);
        setCurrentWord(tmp);
        setLastChar(tmp.charAt(tmp.length() - 1));
        queue.getNextClient();
        return true;
      }
      else {
        return false;
      }
    }
    if(tmp.charAt(0) == getLastChar() && db.select(tmp, getLanguage(), false) && !words.contains(tmp)) {
      words.add(tmp);
      setCurrentWord(tmp);
      setLastChar(tmp.charAt(tmp.length() - 1));
      queue.getNextClient();
      return true;
    }
    else {
      return false;
    }
  }

  public synchronized void updateRound() {
    //점수 처리.
    String loser = queue.getCurrentClientName();
    //score.replace(loser, );
    setRound(getRound() + 1);
    setLastChar(startWord.charAt(getRound() - 1));
    setCurrentWord(null);
    words.clear();
  }

  public synchronized void updateScore(String name, int length) {
    //점수 처리.
    //score.replace(name,);
  }


  public synchronized int getRound() {
    return this.round;
  }

  public synchronized void setRound(int round) {
    this.round = round;
  }

  public synchronized void setStartWord(String startWord) {
    this.startWord = startWord;
  }

  public synchronized String getStartWord() {
    return this.startWord;
  }

public synchronized void setInjeong(boolean injeong) {
    this.injeong = injeong;
  }

  public synchronized boolean getInjeong() {
    return this.injeong;
  }

  public synchronized void setManner(boolean manner) {
    this.manner = manner;
  }

  public synchronized boolean getManner() {
    return this.manner;
  }

  public synchronized void setLanguage(String language) {
    this.language = language;
  }

  public synchronized String getLanguage() {
    return this.language;
  }

  public synchronized int getScore(String name) {
    return score.get(name);
  }

  public synchronized void setChain(int chain) {
    this.chain = chain;
  }

  public synchronized int getChain() {
    return this.chain;
  }

  public synchronized void setTime(int time) {
    this.time = time;
  }

  public synchronized int getTime() {
    return this.time;
  }

}
