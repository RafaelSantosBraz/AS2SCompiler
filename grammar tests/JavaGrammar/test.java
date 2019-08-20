class Principal {
  void something() {
    new Goal().sum(40, 2);   
  }
}

class Goal {
  int sum(int x, int y) {
    return x + y;
  }
}