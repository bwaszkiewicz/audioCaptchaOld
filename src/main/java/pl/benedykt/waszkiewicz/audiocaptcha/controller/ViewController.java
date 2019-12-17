package pl.benedykt.waszkiewicz.audiocaptcha.controller;

public interface ViewController {
    void init();
    Boolean submitCheck();

    void play();
    Boolean submit();
    void refresh();

    Boolean isChacked();
}
