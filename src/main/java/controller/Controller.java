package controller;

import view.View;

public class Controller {

    private View view;

    public Controller(View view) {
        this.view = view;
        System.out.println("itt történik a varázslat");
    }
}
