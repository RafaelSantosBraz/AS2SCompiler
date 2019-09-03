class Principal {
  
  void something() {
    Goal g = new Goal();
    g.sum();
  }

}

class Goal {

  int x;
  int y;

  int sum() {
    return x + y;
  }

}