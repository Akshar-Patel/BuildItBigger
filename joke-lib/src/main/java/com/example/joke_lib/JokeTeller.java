package com.example.joke_lib;

import java.util.Random;

public class JokeTeller {
  static String[] jokes = {
      "The box said 'Requires Windows 95 or better'. So I installed Linux.",
      "I would love to change the world, but they won't give me the source code.",
      "Bugs come in through open Windows.",
      "If you think patience is a virtue, try surfing the net on a 14.4k dial up connection.",
      "A computer once beat me at chess, but it was no match for me at kick boxing."
  };

  public static String tell(){
    Random random = new Random();
    return jokes[random.nextInt(5)];
  }

  public static void main(String[] args) {
    System.out.println(JokeTeller.tell());
  }
}
